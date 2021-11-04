//服务器IP
const XIAOFEI_IM_HOST = window.location.hostname;
//服务端 websocket端口
const XIAOFEI_IM_PORT = 8090;
// URL
const XIAOFEI_IM_URL = "ws://" + XIAOFEI_IM_HOST + ":" + XIAOFEI_IM_PORT + "/ws";
// APP的版本
const APP_VERSION = "1.0.0";
// 平台
const APP_PLATFORM = "web";

// APP包名
const APP_PACKAGE = "com.feige.im";

import './Auth'
import './Msg'
// 被服务端强制下线
const FORCED_OFFLINE = "999";
const DATA_HEADER_LENGTH = 1;

const MESSAGE = 2;
const PING = 1;
const PONG = 0;
/**
 * PONG字符串转换后
 * @type {Uint8Array}
 */
const PONG_BODY = new Uint8Array([80,79,78,71]);


let socket;
let manualStop = false;
const CIMPushManager = {};
const onConnect = CIMPushManager.connect = function () {

    manualStop = false;
    window.localStorage.account = '';
    socket = new WebSocket(XIAOFEI_IM_URL);
    socket.cookieEnabled = false;
    socket.binaryType = 'arraybuffer';
    socket.onopen = CIMPushManager.innerOnConnectFinished;
    socket.onmessage = CIMPushManager.innerOnMessageReceived;
    socket.onclose = CIMPushManager.innerOnConnectionClosed;
};

const onBindAccount = CIMPushManager.bindAccount = function (account) {
    //account = "123"
    window.localStorage.account = account;

    let deviceId = window.localStorage.deviceId;
    if (deviceId === '' || deviceId === undefined) {
        deviceId = generateUUID();
        window.localStorage.deviceId = deviceId;
    }

    let browser = getBrowser();
    let auth = new proto.com.feige.im.pojo.proto.Auth();
    //auth.setUserid("account");
    auth.setDeviceid(deviceId);
    auth.setPlatform(APP_PLATFORM);
    auth.setDevicename(browser.name);
    auth.setLanguage("zh-cn");
    auth.setVersion(APP_VERSION);
    auth.setOsversion(browser.version);
    console.log(auth)
    CIMPushManager.sendRequest(auth);
};

CIMPushManager.stop = function () {
    manualStop = true;
    socket.close();
};

CIMPushManager.resume = function () {
    manualStop = false;
    CIMPushManager.connect();
};


CIMPushManager.innerOnConnectFinished = function () {
    let account = window.localStorage.account;
    if (account === null || account === undefined || account === ""){
        CIMPushManager.bindAccount(account);
    }

};


CIMPushManager.innerOnMessageReceived = function (e) {
    let data = new Uint8Array(e.data);
    let type = data[0];
    let body = data.subarray(DATA_HEADER_LENGTH, data.length);

    if (type === PING) {
        CIMPushManager.pong();
        return;
    }

    if (type === MESSAGE) {
        let message = proto.com.feige.im.pojo.proto.Msg.deserializeBinary(body);
        console.log(message)
        onInterceptMessageReceived(message.toObject(false));
    }
};

CIMPushManager.innerOnConnectionClosed = function (e) {
    if (!manualStop) {
        let time = Math.floor(Math.random() * (30 - 15 + 1) + 15);
        setTimeout(function () {
            CIMPushManager.connect();
        }, time);
    }
};

CIMPushManager.sendRequest = function (body) {
    let data = body.serializeBinary();
    let protobuf = new Uint8Array(data.length + 1);
    protobuf[0] = MESSAGE;
    protobuf.set(data, 1);
    socket.send(protobuf);
};

CIMPushManager.pong = function () {
    socket.send(0);
};

function onInterceptMessageReceived(message) {
    /*
     *被强制下线之后，不再继续连接服务端
     */
    if (message.action === FORCED_OFFLINE) {
        manualStop = true;
    }
    /*
     *收到消息后，将消息发送给页面
     */
    console.log(message)
}

function getBrowser() {
    let explorer = window.navigator.userAgent.toLowerCase();
    if (explorer.indexOf("msie") >= 0) {
        let ver = explorer.match(/msie ([\d.]+)/)[1];
        return {name: "IE", version: ver};
    }
    else if (explorer.indexOf("firefox") >= 0) {
        let ver = explorer.match(/firefox\/([\d.]+)/)[1];
        return {name: "Firefox", version: ver};
    }
    else if (explorer.indexOf("chrome") >= 0) {
        let ver = explorer.match(/chrome\/([\d.]+)/)[1];
        return {name: "Chrome", version: ver};
    }
    else if (explorer.indexOf("opera") >= 0) {
        let ver = explorer.match(/opera.([\d.]+)/)[1];
        return {name: "Opera", version: ver};
    }
    else if (explorer.indexOf("Safari") >= 0) {
        let ver = explorer.match(/version\/([\d.]+)/)[1];
        return {name: "Safari", version: ver};
    }

    return {name: "Other", version: "1.0.0"};
}

function generateUUID() {
    let d = new Date().getTime();
    let uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid.replace(/-/g, '');
}


export {
    onConnect,
    onBindAccount
}
