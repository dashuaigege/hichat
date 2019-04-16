var sock = new SockJS("endpointPersonal");
var stomp = Stomp.over(sock);
var stompBroadcast;
stomp.connect('guest', 'guest', function(frame) {
	stomp.subscribe("/user/queue/hichatPersonal", function(response) {
		getChatMessage(JSON.parse(response.body));
	});
});

function getChatMessage(result) {
	let currentSession;
	for (var x in myVue.sessionList) {
		if (myVue.sessionList[x].userId == result.sendId) {
			currentSession = myVue.sessionList[x];
			break;
		}
	}
	if (currentSession != null) {
		currentSession.messages.push({
			text : result.message,
			date : new Date,
			self : false
		})
	}
}

$.ajax({
	type : "get",
	url : contextPath + '/userContext',
	contentType : "application/json; charset=utf-8",
	async : false,
	success : function(data) {
		userContext = data
		connectBroadcast()
	},
	error : function(result) {
		alert(result)
	}
});

function connectBroadcast() {
	var socketBroadcast = new SockJS('/hichat/endpointBroadcast');
	stompBroadcast = Stomp.over(socketBroadcast);
	stompBroadcast.connect({}, function(frame) {
		console.log('stompBroadcast Connected:' + frame);
		stompBroadcast.subscribe('/topic/getNewUser', function(response) {
			getNewUser(JSON.parse(response.body));
		});
		stompBroadcast.subscribe('/topic/getOn_Offline', function(response) {
			getOn_Offline(JSON.parse(response.body));
		});
		notifyOn_Offline()
	});
}

function disconnect() {
	if (stompBroadcast != null) {
		stompBroadcast.disconnect();
	}
	setConnected(false);
	console.log("DisConnected");
}

function getNewUser(simpleUserContext) {
	myVue.userList.push(simpleUserContext.user);
	myVue.sessionList.push(simpleUserContext.chat);
}

function getOn_Offline(simpleUserContext) {
	let otherId = simpleUserContext.user.id;
	let currentId = userContext.user.id;
	if (myVue != null && otherId != currentId) {
		let result = updateImg(simpleUserContext);
		if (!result) {
			myVue.userList.push(simpleUserContext.user);
			myVue.sessionList.push(simpleUserContext.chat);
		}
	}
}

function updateImg(simpleUserContext) {
	let otherId = simpleUserContext.user.id;
	for (var x in myVue.userList) {
		if (myVue.userList[x].id == otherId) {
			myVue.userList[x].img = simpleUserContext.user.img
			return true
		}
	}
	return false
}

function notifyOn_Offline() {
	stompBroadcast.send("/notifyOn_Offline", {}, JSON
			.stringify(userContext.user));
}

function sendSplittle(text) {
	stomp.send("/chat", {
		receiver : myVue.session.userId
	}, text);
}

// $('#stop').click(function(){sock.close()});
