// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

public interface ChatMsgRespProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.feige.fim.msg.ChatMsgRespProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string msg_id = 1;</code>
   * @return The msgId.
   */
  java.lang.String getMsgId();
  /**
   * <code>string msg_id = 1;</code>
   * @return The bytes for msgId.
   */
  com.google.protobuf.ByteString
      getMsgIdBytes();

  /**
   * <code>string sequence_num = 2;</code>
   * @return The sequenceNum.
   */
  java.lang.String getSequenceNum();
  /**
   * <code>string sequence_num = 2;</code>
   * @return The bytes for sequenceNum.
   */
  com.google.protobuf.ByteString
      getSequenceNumBytes();

  /**
   * <code>int64 send_time = 3;</code>
   * @return The sendTime.
   */
  long getSendTime();
}
