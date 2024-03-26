$(function () {
    var webSocket = null;

    /* init */
    var init = function () {
        if (typeof (WebSocket) == "undefined") {
            console.log("您的浏览器不支持websocket");
            return;
        }

        console.log("您的浏览器支持websocket!");
        let socketUrl = "ws://localhost:10020/combat/1";
        webSocket = new WebSocket(socketUrl);

        webSocket.onmessage = function (res) {
            console.log("Received.");
        }

        webSocket.onclose = function () {
            console.log("connection closed.")
        }
    }
})