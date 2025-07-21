/**
 * WebSocketを使用したシンプルなチャット実装
 */
var stompClient = null;

// 接続状態により表示を切り替え
function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  /** $("#message").html(""); //メッセージが消えるのでコメントアウト */
}

// 接続を行う。
function connect() {
  var socket = new SockJS('/chat'); // WebSocket通信開始
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    // /receive/messageエンドポイントでメッセージを受信し、表示する
    stompClient.subscribe('/receive/message', function (response) {
      showMessage(JSON.parse(response.body));
    });
  });
}

$(document).ready(function () {
	
	setTimeout(() => {
	  connect();
	}, 3000);
	
});





// 接続の切断
function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

// メッセージの送信
function sendMessage() {
   //send/messageエンドポイントにメッセージを送信する
  stompClient.send("/send/message", {}, JSON.stringify(
   {'name': $("#name").val(), 
	'statement': $("#statement").val(),
	'itemId':$("#itemId").val(),
	'chatId':$("#chatId").val(),
	'myAccountId':$("#myAccountId").val(),

	}));
  $("#statement").val('');
  
  //stompClient.send("/send/message", {
  //  "content-type": "application/json"
  //}, JSON.stringify({
  //  name: name,
  //  statement: statement
  //}));
}

//// メッセージの表示
//function showMessage(message) {
//	console.log("受信データ:", message);
//	console.log("message.name:", message.name);
//	console.log("message.statement:", message.statement);
//  	// 受信したメッセージを整形して表示
//  	$("#message").append(
//      "<tr><td>" + message.name + ": " + message.statement + "</td></tr>");
//}


function showMessage(message) {
  console.log("受信:", message);
  $("#message").append(
    "<tr><td>" + $('<div/>').text(message.name).html() + ": " + $('<div/>').text(message.statement).html() + "</td></tr>");
}



// windows.onloadの処理
$(function () {  
	
	$(".form-inline").on('submit', function (e) {
	  e.preventDefault();
	  sendMessage();
	});

	$("#disconnect").click(function () {
	  disconnect();
	});
	
//	$("#send").click(function () {
//	  sendMessage();
//	});

});





