// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

public interface HandshakeReqProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.feige.fim.msg.HandshakeReqProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string client_key = 1;</code>
   * @return The clientKey.
   */
  java.lang.String getClientKey();
  /**
   * <code>string client_key = 1;</code>
   * @return The bytes for clientKey.
   */
  com.google.protobuf.ByteString
      getClientKeyBytes();

  /**
   * <code>string iv = 2;</code>
   * @return The iv.
   */
  java.lang.String getIv();
  /**
   * <code>string iv = 2;</code>
   * @return The bytes for iv.
   */
  com.google.protobuf.ByteString
      getIvBytes();

  /**
   * <code>string client_version = 3;</code>
   * @return The clientVersion.
   */
  java.lang.String getClientVersion();
  /**
   * <code>string client_version = 3;</code>
   * @return The bytes for clientVersion.
   */
  com.google.protobuf.ByteString
      getClientVersionBytes();

  /**
   * <code>string os_name = 4;</code>
   * @return The osName.
   */
  java.lang.String getOsName();
  /**
   * <code>string os_name = 4;</code>
   * @return The bytes for osName.
   */
  com.google.protobuf.ByteString
      getOsNameBytes();

  /**
   * <code>string os_version = 5;</code>
   * @return The osVersion.
   */
  java.lang.String getOsVersion();
  /**
   * <code>string os_version = 5;</code>
   * @return The bytes for osVersion.
   */
  com.google.protobuf.ByteString
      getOsVersionBytes();

  /**
   * <code>uint32 client_type = 6;</code>
   * @return The clientType.
   */
  int getClientType();

  /**
   * <code>string client_id = 7;</code>
   * @return The clientId.
   */
  java.lang.String getClientId();
  /**
   * <code>string client_id = 7;</code>
   * @return The bytes for clientId.
   */
  com.google.protobuf.ByteString
      getClientIdBytes();

  /**
   * <code>string token = 8;</code>
   * @return The token.
   */
  java.lang.String getToken();
  /**
   * <code>string token = 8;</code>
   * @return The bytes for token.
   */
  com.google.protobuf.ByteString
      getTokenBytes();
}
