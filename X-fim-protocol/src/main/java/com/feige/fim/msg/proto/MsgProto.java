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
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_AckProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_AckProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_ChatMsgReqProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_ChatMsgReqProto_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_feige_fim_msg_ChatMsgRespProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_feige_fim_msg_ChatMsgRespProto_fieldAccessorTable;

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
      "\030\003 \001(\t\"K\n\010AckProto\022\025\n\rserver_msg_id\030\001 \001(" +
      "\t\022\025\n\rclient_msg_id\030\002 \001(\t\022\021\n\tsend_time\030\003 " +
      "\001(\003\"\275\001\n\017ChatMsgReqProto\022\025\n\rserver_msg_id" +
      "\030\001 \001(\t\022\025\n\rclient_msg_id\030\002 \001(\t\022\021\n\tsend_ti" +
      "me\030\003 \001(\003\022\021\n\tsender_id\030\004 \001(\t\022\023\n\013receiver_" +
      "id\030\005 \001(\t\022\017\n\007content\030\006 \001(\t\022\016\n\006format\030\007 \001(" +
      "\005\022\020\n\010msg_type\030\010 \001(\005\022\016\n\006status\030\t \001(\005\"S\n\020C" +
      "hatMsgRespProto\022\025\n\rserver_msg_id\030\001 \001(\t\022\025" +
      "\n\rclient_msg_id\030\002 \001(\t\022\021\n\tsend_time\030\003 \001(\003" +
      "B%\n\027com.feige.fim.msg.protoB\010MsgProtoP\001b" +
      "\006proto3"
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
    internal_static_com_feige_fim_msg_AckProto_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_com_feige_fim_msg_AckProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_AckProto_descriptor,
        new java.lang.String[] { "ServerMsgId", "ClientMsgId", "SendTime", });
    internal_static_com_feige_fim_msg_ChatMsgReqProto_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_com_feige_fim_msg_ChatMsgReqProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_ChatMsgReqProto_descriptor,
        new java.lang.String[] { "ServerMsgId", "ClientMsgId", "SendTime", "SenderId", "ReceiverId", "Content", "Format", "MsgType", "Status", });
    internal_static_com_feige_fim_msg_ChatMsgRespProto_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_com_feige_fim_msg_ChatMsgRespProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_feige_fim_msg_ChatMsgRespProto_descriptor,
        new java.lang.String[] { "ServerMsgId", "ClientMsgId", "SendTime", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
