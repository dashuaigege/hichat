var contextPath = "/hichat"
var myVue;
var sock = new SockJS("endpointChat");
var stomp = Stomp.over(sock);

// $('#wiselyForm').submit(function(e) {
// e.preventDefault();
// var text = $('#wiselyForm').find('textarea[name="text"]').val();
// sendSplittle(text);
// });

stomp.connect('guest', 'guest', function(frame) {
	stomp.subscribe("/user/queue/notifications", handleNotification);
});

$.ajax({
	type : "get",
	url : contextPath + '/user',
	contentType : "application/json; charset=utf-8",
	async : false,
	success : function(data) {
		userInfo = data
	},
	error : function(result) {
		alert(result)
	}
});

function handleNotification(message) {
	// $('#output').append("<b>Recived:" + message.body + "</b><br/>");
	let result = JSON.parse(message.body);
	// let currentSession = Enumerable.From(myVue.sessionList).Where(
	// "x=>x.userId==result.userId").ToArray();
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

function sendSplittle(text) {
	stomp.send("/chat", {
		receiver : myVue.session.userName
	}, text);
}

// $('#stop').click(function(){sock.close()});
