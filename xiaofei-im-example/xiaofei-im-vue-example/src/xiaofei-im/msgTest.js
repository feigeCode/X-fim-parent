let proto = require("./DefaultMsg.commonjs.js")
let pb = require("protobufjs")

let pong = proto.com.feige.im.pojo.proto.Pong
let payload = {pong: 0}

let errMsg = pong.verify(payload);
if (errMsg){
    throw Error(errMsg);
}

let pongObj = pong.create(payload);

console.log(pongObj)

let buffer = pong.encode(pongObj).finish();

let bufferWriter = new pb.BufferWriter();
let uint8Array = bufferWriter
    .uint32(12)
    .uint32(10)
    .bytes(buffer)
    .uint32(11)
    .finish();

let bufferReader = new pb.BufferReader(uint8Array);
console.log(bufferReader.uint32());
console.log(bufferReader.uint32());
let bytes = bufferReader.bytes();
console.log(pong.decode(bytes));
console.log(bufferReader.buf)



