import "./HeartBeat_pb"
import './Ack_pb'
import "./DefaultMsg_pb";
import { BinaryWriter, BinaryReader } from "google-protobuf";


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
// 消息包名前缀
const DEFAULT_MSG_PACKAGE_PREFIX = proto.com.feige.im.pojo.proto;
// 心跳消息
const PONG = 0;
const PING = 1;
const PONG_MSG = new DEFAULT_MSG_PACKAGE_PREFIX.Pong().setPong(0).serializeBinary();
// 授权消息，服务端socket绑定用户
const AUTH = 2;
// 普通消息
const MESSAGE = 3;
// 被服务端强制下线
const FORCED_OFFLINE = 4;
// ack消息
const ACK = 999;

// 非正常关闭需要重连
let normalStop = true;

//断线重连定时器
let  againTimer = null;

const XIAOFEI_IM = {};

XIAOFEI_IM.socketTask = null;

XIAOFEI_IM.createSocket = null;

XIAOFEI_IM.retryEvent = function(msg) {
    console.log(msg)
}

XIAOFEI_IM.onOpen = function (ev, getAuthMsg) {
    console.log("onOpen", ev);
    setInterval(() => {
        XIAOFEI_IM.login(getAuthMsg());
    }, 2000)
    againTimer && clearInterval(againTimer)
};

XIAOFEI_IM.onError = function (ev) {
    console.log("onError", ev);
    // this.reconnect();
};

XIAOFEI_IM.onClose = function (ev) {
    console.log("onClose", ev);
    // this.reconnect();
};

XIAOFEI_IM.onMessage = function (data) {
    const br = BinaryReader.alloc(data)
    let msgLength = undefined;
    let msgKey = undefined;
    let body = undefined;
    while (br.nextField()) {
        if (br.isEndGroup()) {
            break;
        }
        let field = br.getFieldNumber();
        switch (field) {
            case 1:
                msgLength = br.readInt32();
                break;
            case 2:
                msgKey = br.readInt32();
                break;
            case 3:
                body = br.readBytes()
                break;
            default:
                br.skipField();
                break;
        }
    }
    let msg = undefined;
    if (msgLength === PING) {
        // 回复心跳
        this.pong();
        return;
    }else if(msgKey === ACK){
        msg = DEFAULT_MSG_PACKAGE_PREFIX.AckMsg.deserializeBinary(body);
    }else if(msgKey === MESSAGE){
        msg = DEFAULT_MSG_PACKAGE_PREFIX.TransportMsg.deserializeBinary(body);
    }else if(msgKey === FORCED_OFFLINE){
        msg = DEFAULT_MSG_PACKAGE_PREFIX.Forced.deserializeBinary(body);
    }else {
        msg = body;
    }
    this.msgCallback(msgLength, msgKey, msg);
};

XIAOFEI_IM.sendMsg = function (body, key) {
    if (this.socketTask.readyState === 1) {
        const msgLength = body.length + DATA_HEADER_LENGTH + MSG_KEY_LENGTH;
        const bw = new BinaryWriter();
        bw.writeInt32(1, msgLength);
        bw.writeInt32(2,key);
        bw.writeBytes(3,body)
        this.socketTask.send(bw.getResultBuffer());
    }else {
        console.log("建立连接中...")
    }
};

XIAOFEI_IM.pong = function () {
    this.sendMsg(PONG_MSG, PONG);
};

XIAOFEI_IM.close = function() {
    normalStop = true;
    againTimer && clearInterval(againTimer);
    this.socketTask.close();
}


XIAOFEI_IM.msgCallback = function (msgLength, msgKey, body) {
    console.log("msgLength=", msgLength,"msgKey=", msgKey, "body=",body);
}

XIAOFEI_IM.login = function (authPayLoad) {
    const deviceId = generateUUID();
    const browser = getBrowser();
    authPayLoad.deviceId = deviceId;
    authPayLoad.deviceName = browser.name;
    authPayLoad.version = APP_VERSION;
    authPayLoad.osVersion = browser.version;
    authPayLoad.platform = APP_PLATFORM;
    const auth = new DEFAULT_MSG_PACKAGE_PREFIX.Auth(authPayLoad).serializeBinary();
    this.sendMsg(auth, AUTH);
    return authPayLoad;
}


XIAOFEI_IM.reconnect = function () {
    return ;
    if (normalStop){
        return;
    }
    againTimer && clearInterval(againTimer);
    //判断是否有websocket对象，有的话清空
    if(this.socketTask !== undefined && this.socketTask != null){
        // 确保已经关闭后再重新打开
        this.close();
        this.socketTask = null;
    }
    this.retryEvent("断线重连中...")
    // 连接  重新调用创建websocket方法
    againTimer = setInterval(()=>{
        this.createSocket()
    },1000 * 5)

}

export default XIAOFEI_IM;


export function getBrowser() {
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

export function generateUUID() {
    let d = new Date().getTime();
    let uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid.replace(/-/g, '');
}
