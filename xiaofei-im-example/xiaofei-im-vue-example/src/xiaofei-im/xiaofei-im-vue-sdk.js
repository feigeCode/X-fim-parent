import { getBrowser,generateUUID } from "@/xiaofei-im/xioafei-im-utils";
import proto from "@/xiaofei-im/DefaultMsg.es6";
import { BufferWriter, BufferReader } from "protobufjs"

//服务器IP
const XIAOFEI_IM_HOST = window.location.hostname;
//服务端 websocket端口
const XIAOFEI_IM_PORT = 8001;
// URL
const XIAOFEI_IM_URL = "ws://" + XIAOFEI_IM_HOST + ":" + XIAOFEI_IM_PORT;
// APP的版本
const APP_VERSION = "1.0.0";
// 平台
const APP_PLATFORM = "web";

// APP包名
const APP_PACKAGE = "com.feige.im";

// 数据头长度
const DATA_HEADER_LENGTH = 4;
// 消息key长度
const MSG_KEY_LENGTH = 4;
const MSG_PACKAGE_PREFIX = proto.com.feige.im.pojo.proto;
// 心跳消息
const PONG = 0;
const PING = 1;
const PONG_MSG = MSG_PACKAGE_PREFIX.Pong.encode(MSG_PACKAGE_PREFIX.Pong.create({pong: 0}));
// 授权消息，服务端socket绑定用户
const AUTH = 2;
// 普通消息
const MESSAGE = 3;
// 被服务端强制下线
const FORCED_OFFLINE = 4;

let bufferWriter = new BufferWriter();



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
        bufferWriter.bytes(ev.data).finish();

        var bufferReader1 = new BufferReader();
        let data = new Uint8Array(ev.data);
        if (bufferReader.length < DATA_HEADER_LENGTH){
            return;
        }
        // 获取消息长度
        let msgLength = data.subarray(0,4);

        let type = data[0];
        let body = data.subarray(DATA_HEADER_LENGTH, data.length);

        if (type === PING) {
            console.log("收到心跳")
            XIAOFEI_IM.pong();
            return;
        }

        if (type === MESSAGE) {
            let message = MSG_PACKAGE_PREFIX.Msg.decode(body);
            receiveHandler(message)
        }

        if (type === FORCED_OFFLINE){
            let forcedMsg = MSG_PACKAGE_PREFIX.Forced.decode(body);
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
    const msgPayload = {
        userId: userId,
        deviceId: deviceId,
        platform: APP_PLATFORM,
        deviceName: browser.name,
        language: "zh-cn",
        version: APP_VERSION,
        osVersion: browser.version,
        token: token
    };
    let auth = MSG_PACKAGE_PREFIX.Auth;
    let errMsg = auth.verify(msgPayload);
    if (errMsg) throw Error(errMsg);
    XIAOFEI_IM.sendMsg(auth.create(msgPayload),AUTH);
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
    let msgPayload = {
        id: id,
        content: content,
        extra: extra,
        format: format,
        contentType: contentType,
        msgType: msgType,
        receiverId: receiverId,
        senderId: senderId,
        timestamp: timestamp
    }

    let msg = MSG_PACKAGE_PREFIX.Msg;
    let errMsg = msg.verify(msgPayload);
    if (errMsg) throw Error(errMsg);
    return msg.create(msgPayload);
}



export default XIAOFEI_IM;
