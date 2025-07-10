/**
 * WebSocketを使用したシンプルなチャット実装
 */
var stompClient = null;

//// 接続状態により表示を切り替え
//function setConnected(connected) {
//  $("#connect").prop("disabled", connected);
//  $("#disconnect").prop("disabled", !connected);
//  if (connected) {
//    $("#conversation").show();
//  } else {
//    $("#conversation").hide();
//  }
//  $("#message").html("");
//}

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
  // /send/messageエンドポイントにメッセージを送信する
  stompClient.send("/send/message", {}, JSON.stringify(
      {'name': $("#name").text(), 
		'statement': $("#statement").val()}));
  $("#statement").val('');
}

// メッセージの表示
function showMessage(message) {
  // 受信したメッセージを整形して表示
  $("#message").append(
      "<tr><td>" + message.name + ": " + message.statement + "</td></tr>");
}

// windows.onloadの処理
$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
  $("#send").click(function () {
    sendMessage();
  });
});

setTimeout(connect(), 3000);