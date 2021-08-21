// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: sample.proto

package org.springframework.web.reactive.protobuf;

/**
 * Protobuf type {@code SecondMsg}
 */
public final class SecondMsg extends com.google.protobuf.GeneratedMessage implements SecondMsgOrBuilder {
    // optional int32 blah = 1;
    public static final int BLAH_FIELD_NUMBER = 1;
    private static final SecondMsg defaultInstance;
    private static final long serialVersionUID = 0L;
    public static com.google.protobuf.Parser<SecondMsg> PARSER = new com.google.protobuf.AbstractParser<SecondMsg>() {
        public SecondMsg parsePartialFrom(com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
            return new SecondMsg(input, extensionRegistry);
        }
    };

    static {
        defaultInstance = new SecondMsg(true);
        defaultInstance.initFields();
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    private int bitField0_;
    private int blah_;
    private byte memoizedIsInitialized = -1;
    private int memoizedSerializedSize = -1;

    // Use SecondMsg.newBuilder() to construct.
    private SecondMsg(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
    }

    private SecondMsg(boolean noInit) {
        this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private SecondMsg(com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        @SuppressWarnings("unused")
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
            boolean done = false;
            while (!done) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    default: {
                        if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                        break;
                    }
                    case 8: {
                        bitField0_ |= 0x00000001;
                        blah_ = input.readInt32();
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
        } finally {
            this.unknownFields = unknownFields.build();
            makeExtensionsImmutable();
        }
    }

    public static SecondMsg getDefaultInstance() {
        return defaultInstance;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return OuterSample.internal_static_SecondMsg_descriptor;
    }

    public static SecondMsg parseFrom(com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static SecondMsg parseFrom(com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static SecondMsg parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static SecondMsg parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static SecondMsg parseFrom(java.io.InputStream input) throws java.io.IOException {
        return PARSER.parseFrom(input);
    }

    public static SecondMsg parseFrom(java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
    }

    public static SecondMsg parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
    }

    public static SecondMsg parseDelimitedFrom(java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }

    public static SecondMsg parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
        return PARSER.parseFrom(input);
    }

    public static SecondMsg parseFrom(com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return Builder.create();
    }

    public static Builder newBuilder(SecondMsg prototype) {
        return newBuilder().mergeFrom(prototype);
    }

    public SecondMsg getDefaultInstanceForType() {
        return defaultInstance;
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
        return OuterSample.internal_static_SecondMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(SecondMsg.class,
            SecondMsg.Builder.class);
    }

    @Override
    public com.google.protobuf.Parser<SecondMsg> getParserForType() {
        return PARSER;
    }

    /**
     * <code>optional int32 blah = 1;</code>
     */
    public boolean hasBlah() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    /**
     * <code>optional int32 blah = 1;</code>
     */
    public int getBlah() {
        return blah_;
    }

    private void initFields() {
        blah_ = 0;
    }

    public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1)
            return isInitialized == 1;

        memoizedIsInitialized = 1;
        return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
        getSerializedSize();
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
            output.writeInt32(1, blah_);
        }
        getUnknownFields().writeTo(output);
    }

    public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1)
            return size;

        size = 0;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
            size += com.google.protobuf.CodedOutputStream.computeInt32Size(1, blah_);
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
    }

    @Override
    protected Object writeReplace() throws java.io.ObjectStreamException {
        return super.writeReplace();
    }

    public Builder newBuilderForType() {
        return newBuilder();
    }

    public Builder toBuilder() {
        return newBuilder(this);
    }

    @Override
    protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    /**
     * Protobuf type {@code SecondMsg}
     */
    public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder>
        implements SecondMsgOrBuilder {
        private int bitField0_;
        // optional int32 blah = 1;
        private int blah_;

        // Construct using org.springframework.protobuf.SecondMsg.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return OuterSample.internal_static_SecondMsg_descriptor;
        }

        private static Builder create() {
            return new Builder();
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return OuterSample.internal_static_SecondMsg_fieldAccessorTable
                .ensureFieldAccessorsInitialized(SecondMsg.class, SecondMsg.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
            }
        }

        public Builder clear() {
            super.clear();
            blah_ = 0;
            bitField0_ = (bitField0_ & ~0x00000001);
            return this;
        }

        public Builder clone() {
            return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
            return OuterSample.internal_static_SecondMsg_descriptor;
        }

        public SecondMsg getDefaultInstanceForType() {
            return SecondMsg.getDefaultInstance();
        }

        public SecondMsg build() {
            SecondMsg result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        public SecondMsg buildPartial() {
            SecondMsg result = new SecondMsg(this);
            int from_bitField0_ = bitField0_;
            int to_bitField0_ = 0;
            if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                to_bitField0_ |= 0x00000001;
            }
            result.blah_ = blah_;
            result.bitField0_ = to_bitField0_;
            onBuilt();
            return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof SecondMsg) {
                return mergeFrom((SecondMsg)other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(SecondMsg other) {
            if (other == SecondMsg.getDefaultInstance())
                return this;
            if (other.hasBlah()) {
                setBlah(other.getBlah());
            }
            this.mergeUnknownFields(other.getUnknownFields());
            return this;
        }

        public final boolean isInitialized() {
            return true;
        }

        public Builder mergeFrom(com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            SecondMsg parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (SecondMsg)e.getUnfinishedMessage();
                throw e;
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        /**
         * <code>optional int32 blah = 1;</code>
         */
        public boolean hasBlah() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>optional int32 blah = 1;</code>
         */
        public int getBlah() {
            return blah_;
        }

        /**
         * <code>optional int32 blah = 1;</code>
         */
        public Builder setBlah(int value) {
            bitField0_ |= 0x00000001;
            blah_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>optional int32 blah = 1;</code>
         */
        public Builder clearBlah() {
            bitField0_ = (bitField0_ & ~0x00000001);
            blah_ = 0;
            onChanged();
            return this;
        }

        // @@protoc_insertion_point(builder_scope:SecondMsg)
    }

    // @@protoc_insertion_point(class_scope:SecondMsg)
}
