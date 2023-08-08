// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

/**
 * Protobuf type {@code com.feige.fim.msg.HandshakeRespProto}
 */
public final class HandshakeRespProto extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.feige.fim.msg.HandshakeRespProto)
    HandshakeRespProtoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use HandshakeRespProto.newBuilder() to construct.
  private HandshakeRespProto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private HandshakeRespProto() {
    serverKey_ = "";
    sessionId_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new HandshakeRespProto();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private HandshakeRespProto(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            serverKey_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            sessionId_ = s;
            break;
          }
          case 24: {

            expireTime_ = input.readSInt64();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_HandshakeRespProto_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_HandshakeRespProto_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.feige.fim.msg.proto.HandshakeRespProto.class, com.feige.fim.msg.proto.HandshakeRespProto.Builder.class);
  }

  public static final int SERVER_KEY_FIELD_NUMBER = 1;
  private volatile java.lang.Object serverKey_;
  /**
   * <code>string server_key = 1;</code>
   * @return The serverKey.
   */
  @java.lang.Override
  public java.lang.String getServerKey() {
    java.lang.Object ref = serverKey_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      serverKey_ = s;
      return s;
    }
  }
  /**
   * <code>string server_key = 1;</code>
   * @return The bytes for serverKey.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getServerKeyBytes() {
    java.lang.Object ref = serverKey_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      serverKey_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SESSION_ID_FIELD_NUMBER = 2;
  private volatile java.lang.Object sessionId_;
  /**
   * <code>string session_id = 2;</code>
   * @return The sessionId.
   */
  @java.lang.Override
  public java.lang.String getSessionId() {
    java.lang.Object ref = sessionId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      sessionId_ = s;
      return s;
    }
  }
  /**
   * <code>string session_id = 2;</code>
   * @return The bytes for sessionId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getSessionIdBytes() {
    java.lang.Object ref = sessionId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      sessionId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int EXPIRE_TIME_FIELD_NUMBER = 3;
  private long expireTime_;
  /**
   * <code>sint64 expire_time = 3;</code>
   * @return The expireTime.
   */
  @java.lang.Override
  public long getExpireTime() {
    return expireTime_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getServerKeyBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, serverKey_);
    }
    if (!getSessionIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, sessionId_);
    }
    if (expireTime_ != 0L) {
      output.writeSInt64(3, expireTime_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getServerKeyBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, serverKey_);
    }
    if (!getSessionIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, sessionId_);
    }
    if (expireTime_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeSInt64Size(3, expireTime_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.feige.fim.msg.proto.HandshakeRespProto)) {
      return super.equals(obj);
    }
    com.feige.fim.msg.proto.HandshakeRespProto other = (com.feige.fim.msg.proto.HandshakeRespProto) obj;

    if (!getServerKey()
        .equals(other.getServerKey())) return false;
    if (!getSessionId()
        .equals(other.getSessionId())) return false;
    if (getExpireTime()
        != other.getExpireTime()) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SERVER_KEY_FIELD_NUMBER;
    hash = (53 * hash) + getServerKey().hashCode();
    hash = (37 * hash) + SESSION_ID_FIELD_NUMBER;
    hash = (53 * hash) + getSessionId().hashCode();
    hash = (37 * hash) + EXPIRE_TIME_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getExpireTime());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.feige.fim.msg.proto.HandshakeRespProto parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.feige.fim.msg.proto.HandshakeRespProto prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.feige.fim.msg.HandshakeRespProto}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.feige.fim.msg.HandshakeRespProto)
      com.feige.fim.msg.proto.HandshakeRespProtoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_HandshakeRespProto_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_HandshakeRespProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.feige.fim.msg.proto.HandshakeRespProto.class, com.feige.fim.msg.proto.HandshakeRespProto.Builder.class);
    }

    // Construct using com.feige.fim.msg.proto.HandshakeRespProto.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      serverKey_ = "";

      sessionId_ = "";

      expireTime_ = 0L;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_HandshakeRespProto_descriptor;
    }

    @java.lang.Override
    public com.feige.fim.msg.proto.HandshakeRespProto getDefaultInstanceForType() {
      return com.feige.fim.msg.proto.HandshakeRespProto.getDefaultInstance();
    }

    @java.lang.Override
    public com.feige.fim.msg.proto.HandshakeRespProto build() {
      com.feige.fim.msg.proto.HandshakeRespProto result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.feige.fim.msg.proto.HandshakeRespProto buildPartial() {
      com.feige.fim.msg.proto.HandshakeRespProto result = new com.feige.fim.msg.proto.HandshakeRespProto(this);
      result.serverKey_ = serverKey_;
      result.sessionId_ = sessionId_;
      result.expireTime_ = expireTime_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.feige.fim.msg.proto.HandshakeRespProto) {
        return mergeFrom((com.feige.fim.msg.proto.HandshakeRespProto)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.feige.fim.msg.proto.HandshakeRespProto other) {
      if (other == com.feige.fim.msg.proto.HandshakeRespProto.getDefaultInstance()) return this;
      if (!other.getServerKey().isEmpty()) {
        serverKey_ = other.serverKey_;
        onChanged();
      }
      if (!other.getSessionId().isEmpty()) {
        sessionId_ = other.sessionId_;
        onChanged();
      }
      if (other.getExpireTime() != 0L) {
        setExpireTime(other.getExpireTime());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.feige.fim.msg.proto.HandshakeRespProto parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.feige.fim.msg.proto.HandshakeRespProto) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object serverKey_ = "";
    /**
     * <code>string server_key = 1;</code>
     * @return The serverKey.
     */
    public java.lang.String getServerKey() {
      java.lang.Object ref = serverKey_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        serverKey_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string server_key = 1;</code>
     * @return The bytes for serverKey.
     */
    public com.google.protobuf.ByteString
        getServerKeyBytes() {
      java.lang.Object ref = serverKey_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        serverKey_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string server_key = 1;</code>
     * @param value The serverKey to set.
     * @return This builder for chaining.
     */
    public Builder setServerKey(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      serverKey_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string server_key = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearServerKey() {
      
      serverKey_ = getDefaultInstance().getServerKey();
      onChanged();
      return this;
    }
    /**
     * <code>string server_key = 1;</code>
     * @param value The bytes for serverKey to set.
     * @return This builder for chaining.
     */
    public Builder setServerKeyBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      serverKey_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object sessionId_ = "";
    /**
     * <code>string session_id = 2;</code>
     * @return The sessionId.
     */
    public java.lang.String getSessionId() {
      java.lang.Object ref = sessionId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        sessionId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string session_id = 2;</code>
     * @return The bytes for sessionId.
     */
    public com.google.protobuf.ByteString
        getSessionIdBytes() {
      java.lang.Object ref = sessionId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        sessionId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string session_id = 2;</code>
     * @param value The sessionId to set.
     * @return This builder for chaining.
     */
    public Builder setSessionId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      sessionId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string session_id = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearSessionId() {
      
      sessionId_ = getDefaultInstance().getSessionId();
      onChanged();
      return this;
    }
    /**
     * <code>string session_id = 2;</code>
     * @param value The bytes for sessionId to set.
     * @return This builder for chaining.
     */
    public Builder setSessionIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      sessionId_ = value;
      onChanged();
      return this;
    }

    private long expireTime_ ;
    /**
     * <code>sint64 expire_time = 3;</code>
     * @return The expireTime.
     */
    @java.lang.Override
    public long getExpireTime() {
      return expireTime_;
    }
    /**
     * <code>sint64 expire_time = 3;</code>
     * @param value The expireTime to set.
     * @return This builder for chaining.
     */
    public Builder setExpireTime(long value) {
      
      expireTime_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>sint64 expire_time = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearExpireTime() {
      
      expireTime_ = 0L;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.feige.fim.msg.HandshakeRespProto)
  }

  // @@protoc_insertion_point(class_scope:com.feige.fim.msg.HandshakeRespProto)
  private static final com.feige.fim.msg.proto.HandshakeRespProto DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.feige.fim.msg.proto.HandshakeRespProto();
  }

  public static com.feige.fim.msg.proto.HandshakeRespProto getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<HandshakeRespProto>
      PARSER = new com.google.protobuf.AbstractParser<HandshakeRespProto>() {
    @java.lang.Override
    public HandshakeRespProto parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new HandshakeRespProto(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<HandshakeRespProto> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<HandshakeRespProto> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.feige.fim.msg.proto.HandshakeRespProto getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

