// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

public interface HandshakeRespProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.feige.fim.msg.HandshakeRespProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string server_key = 1;</code>
   * @return The serverKey.
   */
  java.lang.String getServerKey();
  /**
   * <code>string server_key = 1;</code>
   * @return The bytes for serverKey.
   */
  com.google.protobuf.ByteString
      getServerKeyBytes();

  /**
   * <code>string session_id = 2;</code>
   * @return The sessionId.
   */
  java.lang.String getSessionId();
  /**
   * <code>string session_id = 2;</code>
   * @return The bytes for sessionId.
   */
  com.google.protobuf.ByteString
      getSessionIdBytes();

  /**
   * <code>sint64 expire_time = 3;</code>
   * @return The expireTime.
   */
  long getExpireTime();
}