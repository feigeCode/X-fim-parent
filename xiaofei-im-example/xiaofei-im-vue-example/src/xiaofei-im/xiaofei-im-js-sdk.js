import XIAOFEI_IM from "@/xiaofei-im/xioafei-im-utils";

// 服务器IP
const XIAOFEI_IM_HOST = window.location.hostname;
// 服务端 websocket端口
const XIAOFEI_IM_PORT = 8001;
// URL
const XIAOFEI_IM_URL = "ws://" + XIAOFEI_IM_HOST + ":" + XIAOFEI_IM_PORT;


export const createSocket = (getAuthMsg) => {
    const socketTask = new WebSocket(XIAOFEI_IM_URL);
    socketTask.cookieEnabled = false;
    socketTask.binaryType = 'arraybuffer';
    socketTask.onopen = function (ev) {
        console.log(ev)
        XIAOFEI_IM.onOpen(ev,getAuthMsg);
    };
    socketTask.onclose= XIAOFEI_IM.onClose;
    socketTask.onerror = XIAOFEI_IM.onError;
    XIAOFEI_IM.createSocket = function () {
        createSocket();
    };
    socketTask.onmessage = function (ev) {
        XIAOFEI_IM.onMessage(ev.data);
    };
    XIAOFEI_IM.socketTask = socketTask;
    return XIAOFEI_IM;
}

