var sock = new SockJS("endpointPersonal");
var stomp = Stomp.over(sock);

stomp.connect('guest', 'guest', function(frame) {
	stomp.subscribe("/user/queue/hichatPersonal", handleHichatPersonal);
});

function handleHichatPersonal(message) {
	let result = JSON.parse(message.body);
	let currentSession;
	for (x in myVue.sessionList) {
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
	},
	error : function(result) {
		alert(result)
	}
});

function sendSplittle(text) {
	stomp.send("/chat", {
		receiver : myVue.session.userName
	}, text);
}

// $('#stop').click(function(){sock.close()});
