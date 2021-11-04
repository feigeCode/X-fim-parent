import './Auth'
import './Msg'
import './Forced'
import './Pong'
import { getBrowser,generateUUID } from "@/xiaofei-im/xioafei-im-utils";

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


const DATA_HEADER_LENGTH = 1;

// 心跳消息
const PONG = 0;
const PING = 1;
const PONG_MSG = new proto.com.feige.im.pojo.proto.Pong().setPong(0).serializeBinary();
// 授权消息，服务端socket绑定用户
const AUTH = 2;
// 普通消息
const MESSAGE = 3;
// 被服务端强制下线
const FORCED_OFFLINE = 4;




let socket;
// 非正常关闭需要重连
let normalStop = false;
const XIAOFEI_IM = {};
XIAOFEI_IM.connect = function (receiveHandler,userId,token) {

    normalStop = false;
    socket = new WebSocket(XIAOFEI_IM_URL);
    socket.cookieEnabled = false;
    socket.binaryType = 'arraybuffer';
    socket.onopen = function (ev) {
        XIAOFEI_IM.bindAccount(userId,token)
    };
    socket.onmessage = function (ev) {
        let data = new Uint8Array(ev.data);
        let type = data[0];
        let body = data.subarray(DATA_HEADER_LENGTH, data.length);

        if (type === PING) {
            console.log("收到心跳")
            XIAOFEI_IM.pong();
            return;
        }

        if (type === MESSAGE) {
            let message = proto.com.feige.im.pojo.proto.Msg.deserializeBinary(body);
            receiveHandler(message)
        }

        if (type === FORCED_OFFLINE){
            let forcedMsg = proto.com.feige.im.pojo.proto.Forced.deserializeBinary(body);
            console.log(forcedMsg)
            normalStop = true;
        }
    };
    socket.onclose = function (ev) {
        console.log(ev)
        normalStop = true;
        socket.close();
    };
};

XIAOFEI_IM.bindAccount = function (userId,token) {
    let deviceId = generateUUID();
    let browser = getBrowser();
    let auth = new proto.com.feige.im.pojo.proto.Auth();
    auth.setUserid(userId);
    auth.setDeviceid(deviceId);
    auth.setPlatform(APP_PLATFORM);
    auth.setDevicename(browser.name);
    auth.setLanguage("zh-cn");
    auth.setVersion(APP_VERSION);
    auth.setOsversion(browser.version);
    auth.setToken(token);
    XIAOFEI_IM.sendMsg(auth,AUTH);
};



XIAOFEI_IM.resume = function (receiveHandler,userId,token) {
    normalStop = false;
    XIAOFEI_IM.connect(receiveHandler,userId,token);
};

XIAOFEI_IM.reconnect = function (receiveHandler,userId,token) {
    if (!normalStop) {
        let time = Math.floor(Math.random() * (30 - 15 + 1) + 15);
        setTimeout(function () {
            XIAOFEI_IM.connect(receiveHandler,userId,token);
        }, time);
    }
};

XIAOFEI_IM.sendMsg = function (body,type) {
    let data = body.serializeBinary();
    let protobuf = new Uint8Array(data.length + 1);
    protobuf[0] = type;
    protobuf.set(data, 1);
    socket.send(protobuf);
};

XIAOFEI_IM.pong = function () {
    let protobuf = new Uint8Array(PONG_MSG.length + 1);
    protobuf[0] = PONG;
    protobuf.set(PONG_MSG, 1);
    socket.send(protobuf);
};

XIAOFEI_IM.constructMsg = function (id,content,extra,format, contentType,msgType,receiverId,senderId,timestamp) {
    let msg = new proto.com.feige.im.pojo.proto.Msg();
    msg.setId(id);
    msg.setContent(content);
    msg.setExtra(extra);
    msg.setFormat(format);
    msg.setContenttype(contentType);
    msg.setMsgtype(msgType);
    msg.setReceiverid(receiverId);
    msg.setSenderid(senderId);
    msg.setTimestamp(timestamp)
    return msg;
}



export default XIAOFEI_IM;
