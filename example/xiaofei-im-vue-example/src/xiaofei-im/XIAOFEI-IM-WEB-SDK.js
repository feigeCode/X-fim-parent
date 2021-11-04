"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.XiaoFeiIM = void 0;
var XiaoFeiIM = /** @class */ (function () {
    function XiaoFeiIM(host, port) {
        this.normalClose = true;
        if (host && port) {
            this.host = host;
            this.port = port;
        }
        else {
            this.host = window.location.host;
            this.port = 8090;
        }
        this.socket = new WebSocket("ws://" + this.host + ":" + this.port + "/ws");
    }
    XiaoFeiIM.prototype.connect = function () {
        this.socket.binaryType = 'arraybuffer';
        uni.connectSocket();
    };
    return XiaoFeiIM;
}());
exports.XiaoFeiIM = XiaoFeiIM;
