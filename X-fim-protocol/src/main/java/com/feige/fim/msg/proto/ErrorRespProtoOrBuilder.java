// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

public interface ErrorRespProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.feige.fim.msg.ErrorRespProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 error_code = 1;</code>
   * @return The errorCode.
   */
  int getErrorCode();

  /**
   * <code>string reason = 2;</code>
   * @return The reason.
   */
  java.lang.String getReason();
  /**
   * <code>string reason = 2;</code>
   * @return The bytes for reason.
   */
  com.google.protobuf.ByteString
      getReasonBytes();

  /**
   * <code>string extra = 3;</code>
   * @return The extra.
   */
  java.lang.String getExtra();
  /**
   * <code>string extra = 3;</code>
   * @return The bytes for extra.
   */
  com.google.protobuf.ByteString
      getExtraBytes();
}
