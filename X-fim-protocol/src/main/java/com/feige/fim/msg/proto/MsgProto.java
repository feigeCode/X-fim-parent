// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

public final class MsgProto {
  private MsgProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_HandshakeReqProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_HandshakeReqProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_HandshakeRespProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_HandshakeRespProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_FastConnectReqProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_FastConnectReqProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_SuccessRespProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_SuccessRespProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_BindClientReqProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_BindClientReqProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_ErrorRespProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_ErrorRespProto_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tMsg.proto\022\021com.feige.fim.msg\"\247\001\n\021Hands" +
      "hakeReqProto\022\022\n\nclient_key\030\001 \001(\t\022\n\n\002iv\030\002" +
      " \001(\t\022\026\n\016client_version\030\003 \001(\t\022\017\n\007os_name\030" +
      "\004 \001(\t\022\022\n\nos_version\030\005 \001(\t\022\023\n\013client_type" +
      "\030\006 \001(\r\022\021\n\tclient_id\030\007 \001(\t\022\r\n\005token\030\010 \001(\t" +
      "\"Q\n\022HandshakeRespProto\022\022\n\nserver_key\030\001 \001" +
      "(\t\022\022\n\nsession_id\030\002 \001(\t\022\023\n\013expire_time\030\003 " +
      "\001(\022\"<\n\023FastConnectReqProto\022\021\n\tclient_id\030" +
      "\001 \001(\t\022\022\n\nsession_id\030\002 \001(\t\"6\n\020SuccessResp" +
      "Proto\022\023\n\013status_code\030\001 \001(\005\022\r\n\005extra\030\002 \001(" +
      "\t\"6\n\022BindClientReqProto\022\022\n\nsession_id\030\001 " +
      "\001(\t\022\014\n\004tags\030\002 \001(\t\"C\n\016ErrorRespProto\022\022\n\ne" +
      "rror_code\030\001 \001(\005\022\016\n\006reason\030\002 \001(\t\022\r\n\005extra" +
      "\030\003 \001(\tB%\n\027com.feige.fim.msg.protoB\010MsgPr" +
      "otoP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_feige_fim_msg_HandshakeReqProto_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_feige_fim_msg_HandshakeReqProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_HandshakeReqProto_descriptor,
        new java.lang.String[] { "ClientKey", "Iv", "ClientVersion", "OsName", "OsVersion", "ClientType", "ClientId", "Token", });
    internal_static_com_feige_fim_msg_HandshakeRespProto_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_feige_fim_msg_HandshakeRespProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_HandshakeRespProto_descriptor,
        new java.lang.String[] { "ServerKey", "SessionId", "ExpireTime", });
    internal_static_com_feige_fim_msg_FastConnectReqProto_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_com_feige_fim_msg_FastConnectReqProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_FastConnectReqProto_descriptor,
        new java.lang.String[] { "ClientId", "SessionId", });
    internal_static_com_feige_fim_msg_SuccessRespProto_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_com_feige_fim_msg_SuccessRespProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_SuccessRespProto_descriptor,
        new java.lang.String[] { "StatusCode", "Extra", });
    internal_static_com_feige_fim_msg_BindClientReqProto_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_com_feige_fim_msg_BindClientReqProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_BindClientReqProto_descriptor,
        new java.lang.String[] { "SessionId", "Tags", });
    internal_static_com_feige_fim_msg_ErrorRespProto_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_com_feige_fim_msg_ErrorRespProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_ErrorRespProto_descriptor,
        new java.lang.String[] { "ErrorCode", "Reason", "Extra", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
