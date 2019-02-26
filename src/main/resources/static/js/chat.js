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

function handleNotification(message) {
	//$('#output').append("<b>Recived:" + message.body + "</b><br/>");
	myVue.session.messages.push({
        text: message.body,
        date: new Date,
        self: false
    })
}

function sendSplittle(text) {
	stomp.send("/chat", {}, text);
}

// $('#stop').click(function(){sock.close()});
