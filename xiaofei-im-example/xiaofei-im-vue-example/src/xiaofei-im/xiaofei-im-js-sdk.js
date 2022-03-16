import XIAOFEI_IM, {
    APP_PLATFORM,
    APP_VERSION,
    generateUUID,
    getBrowser,
    urlParamsAssemble
} from "@/xiaofei-im/xioafei-im-utils";

// 服务器IP
const XIAOFEI_IM_HOST = window.location.hostname;
// 服务端 websocket端口
const XIAOFEI_IM_PORT = 8002;
// URL
const XIAOFEI_IM_URL = "ws://" + XIAOFEI_IM_HOST + ":" + XIAOFEI_IM_PORT + "/ws";


export const createSocket = (authPayLoad) => {
    const deviceId = generateUUID();
    const browser = getBrowser();
    const params = {
        deviceId: deviceId,
        ip: authPayLoad.ip,
        version: APP_VERSION,
        platform: APP_PLATFORM,
        token: authPayLoad.token,
        address: authPayLoad.address,
        osVersion: browser.version,
        deviceName: browser.name,
        language: authPayLoad.language
    };
    const url = window.encodeURI(urlParamsAssemble(XIAOFEI_IM_URL, params))
    const socketTask = new WebSocket(url);
    socketTask.cookieEnabled = false;
    socketTask.binaryType = 'arraybuffer';
    socketTask.onopen = XIAOFEI_IM.onOpen;
    socketTask.onclose= XIAOFEI_IM.onClose;
    socketTask.onerror = XIAOFEI_IM.onError;
    XIAOFEI_IM.createSocket = function () {
        createSocket(authPayLoad);
    };
    socketTask.onmessage = function (ev) {
        XIAOFEI_IM.onMessage(ev.data);
    };
    XIAOFEI_IM.socketTask = socketTask;
    return XIAOFEI_IM;
}

