// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Msg.proto

package com.feige.fim.msg.proto;

/**
 * Protobuf type {@code com.feige.fim.msg.BindClientReqProto}
 */
public final class BindClientReqProto extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.feige.fim.msg.BindClientReqProto)
    BindClientReqProtoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use BindClientReqProto.newBuilder() to construct.
  private BindClientReqProto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BindClientReqProto() {
    sessionId_ = "";
    tags_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new BindClientReqProto();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private BindClientReqProto(
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

            sessionId_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            tags_ = s;
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
    return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_BindClientReqProto_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_BindClientReqProto_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.feige.fim.msg.proto.BindClientReqProto.class, com.feige.fim.msg.proto.BindClientReqProto.Builder.class);
  }

  public static final int SESSION_ID_FIELD_NUMBER = 1;
  private volatile java.lang.Object sessionId_;
  /**
   * <code>string session_id = 1;</code>
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
   * <code>string session_id = 1;</code>
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

  public static final int TAGS_FIELD_NUMBER = 2;
  private volatile java.lang.Object tags_;
  /**
   * <code>string tags = 2;</code>
   * @return The tags.
   */
  @java.lang.Override
  public java.lang.String getTags() {
    java.lang.Object ref = tags_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      tags_ = s;
      return s;
    }
  }
  /**
   * <code>string tags = 2;</code>
   * @return The bytes for tags.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTagsBytes() {
    java.lang.Object ref = tags_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      tags_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (!getSessionIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, sessionId_);
    }
    if (!getTagsBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, tags_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getSessionIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, sessionId_);
    }
    if (!getTagsBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, tags_);
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
    if (!(obj instanceof com.feige.fim.msg.proto.BindClientReqProto)) {
      return super.equals(obj);
    }
    com.feige.fim.msg.proto.BindClientReqProto other = (com.feige.fim.msg.proto.BindClientReqProto) obj;

    if (!getSessionId()
        .equals(other.getSessionId())) return false;
    if (!getTags()
        .equals(other.getTags())) return false;
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
    hash = (37 * hash) + SESSION_ID_FIELD_NUMBER;
    hash = (53 * hash) + getSessionId().hashCode();
    hash = (37 * hash) + TAGS_FIELD_NUMBER;
    hash = (53 * hash) + getTags().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.feige.fim.msg.proto.BindClientReqProto parseFrom(
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
  public static Builder newBuilder(com.feige.fim.msg.proto.BindClientReqProto prototype) {
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
   * Protobuf type {@code com.feige.fim.msg.BindClientReqProto}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.feige.fim.msg.BindClientReqProto)
      com.feige.fim.msg.proto.BindClientReqProtoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_BindClientReqProto_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_BindClientReqProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.feige.fim.msg.proto.BindClientReqProto.class, com.feige.fim.msg.proto.BindClientReqProto.Builder.class);
    }

    // Construct using com.feige.fim.msg.proto.BindClientReqProto.newBuilder()
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
      sessionId_ = "";

      tags_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.feige.fim.msg.proto.MsgProto.internal_static_com_feige_fim_msg_BindClientReqProto_descriptor;
    }

    @java.lang.Override
    public com.feige.fim.msg.proto.BindClientReqProto getDefaultInstanceForType() {
      return com.feige.fim.msg.proto.BindClientReqProto.getDefaultInstance();
    }

    @java.lang.Override
    public com.feige.fim.msg.proto.BindClientReqProto build() {
      com.feige.fim.msg.proto.BindClientReqProto result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.feige.fim.msg.proto.BindClientReqProto buildPartial() {
      com.feige.fim.msg.proto.BindClientReqProto result = new com.feige.fim.msg.proto.BindClientReqProto(this);
      result.sessionId_ = sessionId_;
      result.tags_ = tags_;
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
      if (other instanceof com.feige.fim.msg.proto.BindClientReqProto) {
        return mergeFrom((com.feige.fim.msg.proto.BindClientReqProto)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.feige.fim.msg.proto.BindClientReqProto other) {
      if (other == com.feige.fim.msg.proto.BindClientReqProto.getDefaultInstance()) return this;
      if (!other.getSessionId().isEmpty()) {
        sessionId_ = other.sessionId_;
        onChanged();
      }
      if (!other.getTags().isEmpty()) {
        tags_ = other.tags_;
        onChanged();
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
      com.feige.fim.msg.proto.BindClientReqProto parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.feige.fim.msg.proto.BindClientReqProto) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object sessionId_ = "";
    /**
     * <code>string session_id = 1;</code>
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
     * <code>string session_id = 1;</code>
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
     * <code>string session_id = 1;</code>
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
     * <code>string session_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearSessionId() {
      
      sessionId_ = getDefaultInstance().getSessionId();
      onChanged();
      return this;
    }
    /**
     * <code>string session_id = 1;</code>
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

    private java.lang.Object tags_ = "";
    /**
     * <code>string tags = 2;</code>
     * @return The tags.
     */
    public java.lang.String getTags() {
      java.lang.Object ref = tags_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        tags_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string tags = 2;</code>
     * @return The bytes for tags.
     */
    public com.google.protobuf.ByteString
        getTagsBytes() {
      java.lang.Object ref = tags_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        tags_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string tags = 2;</code>
     * @param value The tags to set.
     * @return This builder for chaining.
     */
    public Builder setTags(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      tags_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string tags = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTags() {
      
      tags_ = getDefaultInstance().getTags();
      onChanged();
      return this;
    }
    /**
     * <code>string tags = 2;</code>
     * @param value The bytes for tags to set.
     * @return This builder for chaining.
     */
    public Builder setTagsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      tags_ = value;
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


    // @@protoc_insertion_point(builder_scope:com.feige.fim.msg.BindClientReqProto)
  }

  // @@protoc_insertion_point(class_scope:com.feige.fim.msg.BindClientReqProto)
  private static final com.feige.fim.msg.proto.BindClientReqProto DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.feige.fim.msg.proto.BindClientReqProto();
  }

  public static com.feige.fim.msg.proto.BindClientReqProto getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BindClientReqProto>
      PARSER = new com.google.protobuf.AbstractParser<BindClientReqProto>() {
    @java.lang.Override
    public BindClientReqProto parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new BindClientReqProto(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BindClientReqProto> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BindClientReqProto> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.feige.fim.msg.proto.BindClientReqProto getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

