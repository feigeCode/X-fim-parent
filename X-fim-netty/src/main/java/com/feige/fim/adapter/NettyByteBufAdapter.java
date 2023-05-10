package com.feige.fim.adapter;

import com.feige.api.codec.IByteBuf;
import com.feige.fim.buffer.IByteBufUtil;
import com.feige.fim.utils.AssertUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class NettyByteBufAdapter implements IByteBuf {

    private final ByteBuf buffer;

    public NettyByteBufAdapter(ByteBuf buffer) {
        AssertUtil.notNull(buffer, "buffer");
        this.buffer = buffer;
    }


    public static IByteBuf fromByteBuf(ByteBuf byteBuf){
        return new NettyByteBufAdapter(byteBuf);
    }

    @Override
    public int capacity() {
        return buffer.capacity();
    }


    @Override
    public IByteBuf copy(int index, int length) {
        return new NettyByteBufAdapter(buffer.copy(index, length));
    }



    @Override
    public byte getByte(int index) {
        return buffer.getByte(index);
    }


    @Override
    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        buffer.getBytes(index, dst, dstIndex, length);
    }


    @Override
    public void getBytes(int index, ByteBuffer dst) {
        buffer.getBytes(index, dst);
    }


    @Override
    public void getBytes(int index, IByteBuf dst, int dstIndex, int length) {
        byte[] data = new byte[length];
        buffer.getBytes(index, data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
    }


    @Override
    public void getBytes(int index, OutputStream dst, int length) throws IOException {
        buffer.getBytes(index, dst, length);
    }


    @Override
    public boolean isDirect() {
        return buffer.isDirect();
    }


    @Override
    public void setByte(int index, int value) {
        buffer.setByte(index, value);
    }


    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        buffer.setBytes(index, src, srcIndex, length);
    }


    @Override
    public void setBytes(int index, ByteBuffer src) {
        buffer.setBytes(index, src);
    }


    @Override
    public void setBytes(int index, IByteBuf src, int srcIndex, int length) {
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = new byte[length];
        src.getBytes(srcIndex, data, 0, length);
        setBytes(index, data, 0, length);
    }


    @Override
    public int setBytes(int index, InputStream src, int length) throws IOException {
        return buffer.setBytes(index, src, length);
    }


    @Override
    public ByteBuffer toByteBuffer(int index, int length) {
        return buffer.nioBuffer(index, length);
    }


    @Override
    public byte[] array() {
        return buffer.array();
    }


    @Override
    public boolean hasArray() {
        return buffer.hasArray();
    }


    @Override
    public int arrayOffset() {
        return buffer.arrayOffset();
    }


    @Override
    public void clear() {
        buffer.clear();
    }


    @Override
    public IByteBuf copy() {
        return new NettyByteBufAdapter(buffer.copy());
    }


    @Override
    public void discardReadBytes() {
        buffer.discardReadBytes();
    }


    @Override
    public void ensureWritableBytes(int writableBytes) {
        buffer.ensureWritable(writableBytes);
    }


    @Override
    public void getBytes(int index, byte[] dst) {
        buffer.getBytes(index, dst);
    }


    @Override
    public void getBytes(int index, IByteBuf dst) {
        getBytes(index, dst, dst.writableBytes());
    }


    @Override
    public void getBytes(int index, IByteBuf dst, int length) {
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
    }


    @Override
    public void markReaderIndex() {
        buffer.markReaderIndex();
    }


    @Override
    public void markWriterIndex() {
        buffer.markWriterIndex();
    }


    @Override
    public boolean readable() {
        return buffer.isReadable();
    }


    @Override
    public int readableBytes() {
        return buffer.readableBytes();
    }


    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public short readShort() {
        return buffer.readShort();
    }

    @Override
    public short readShortLE() {
        return buffer.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return buffer.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return buffer.readMedium();
    }

    @Override
    public int readMediumLE() {
        return buffer.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return buffer.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return buffer.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return  buffer.readInt();
    }

    @Override
    public int readIntLE() {
        return buffer.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return buffer.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return buffer.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return buffer.readLong();
    }

    @Override
    public long readLongLE() {
        return buffer.readLongLE();
    }

    @Override
    public char readChar() {
        return buffer.readChar();
    }

    @Override
    public float readFloat() {
        return buffer.readFloat();
    }

    @Override
    public double readDouble() {
        return buffer.readDouble();
    }


    @Override
    public void readBytes(byte[] dst) {
        buffer.readBytes(dst);
    }


    @Override
    public void readBytes(byte[] dst, int dstIndex, int length) {
        buffer.readBytes(dst, dstIndex, length);
    }


    @Override
    public void readBytes(ByteBuffer dst) {
        buffer.readBytes(dst);
    }


    @Override
    public void readBytes(IByteBuf dst) {
        readBytes(dst, dst.writableBytes());
    }


    @Override
    public void readBytes(IByteBuf dst, int length) {
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        readBytes(dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
    }


    @Override
    public void readBytes(IByteBuf dst, int dstIndex, int length) {
        if (readableBytes() < length) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = new byte[length];
        buffer.readBytes(data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
    }


    @Override
    public IByteBuf readBytes(int length) {
        return new NettyByteBufAdapter(buffer.readBytes(length));
    }


    @Override
    public void resetReaderIndex() {
        buffer.resetReaderIndex();
    }


    @Override
    public void resetWriterIndex() {
        buffer.resetWriterIndex();
    }


    @Override
    public int readerIndex() {
        return buffer.readerIndex();
    }


    @Override
    public void readerIndex(int readerIndex) {
        buffer.readerIndex(readerIndex);
    }


    @Override
    public void readBytes(OutputStream dst, int length) throws IOException {
        buffer.readBytes(dst, length);
    }


    @Override
    public void setBytes(int index, byte[] src) {
        buffer.setBytes(index, src);
    }


    @Override
    public void setBytes(int index, IByteBuf src) {
        setBytes(index, src, src.readableBytes());
    }


    @Override
    public void setBytes(int index, IByteBuf src, int length) {
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
    }


    @Override
    public void setIndex(int readerIndex, int writerIndex) {
        buffer.setIndex(readerIndex, writerIndex);
    }


    @Override
    public void skipBytes(int length) {
        buffer.skipBytes(length);
    }


    @Override
    public ByteBuffer toByteBuffer() {
        return buffer.nioBuffer();
    }


    @Override
    public boolean writable() {
        return buffer.isWritable();
    }


    @Override
    public int writableBytes() {
        return buffer.writableBytes();
    }


    @Override
    public void writeByte(int value) {
        buffer.writeByte(value);
    }

    @Override
    public IByteBuf writeShort(int value) {
        buffer.writeShort(value);
        return this;
    }

    @Override
    public IByteBuf writeShortLE(int value) {
        buffer.writeShortLE(value);
        return this;
    }

    @Override
    public IByteBuf writeMedium(int value) {
        buffer.writeMedium(value);
        return this;
    }

    @Override
    public IByteBuf writeMediumLE(int value) {
        buffer.writeMediumLE(value);
        return this;
    }

    @Override
    public IByteBuf writeInt(int value) {
        buffer.writeInt(value);
        return this;
    }

    @Override
    public IByteBuf writeIntLE(int value) {
        buffer.writeIntLE(value);
        return this;
    }

    @Override
    public IByteBuf writeLong(long value) {
        buffer.writeLong(value);
        return this;
    }

    @Override
    public IByteBuf writeLongLE(long value) {
        buffer.writeLongLE(value);
        return this;
    }

    @Override
    public IByteBuf writeChar(int value) {
        buffer.writeChar(value);
        return this;
    }

    @Override
    public IByteBuf writeFloat(float value) {
        buffer.writeFloat(value);
        return this;
    }

    @Override
    public IByteBuf writeFloatLE(float value) {
        buffer.writeFloatLE(value);
        return this;
    }

    @Override
    public IByteBuf writeDouble(double value) {
        buffer.writeDouble(value);
        return this;
    }

    @Override
    public IByteBuf writeDoubleLE(double value) {
        buffer.writeDoubleLE(value);
        return this;
    }


    @Override
    public void writeBytes(byte[] src) {
        buffer.writeBytes(src);
    }


    @Override
    public void writeBytes(byte[] src, int index, int length) {
        buffer.writeBytes(src, index, length);
    }


    @Override
    public void writeBytes(ByteBuffer src) {
        buffer.writeBytes(src);
    }


    @Override
    public void writeBytes(IByteBuf src) {
        writeBytes(src, src.readableBytes());
    }


    @Override
    public void writeBytes(IByteBuf src, int length) {
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        writeBytes(src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
    }


    @Override
    public void writeBytes(IByteBuf src, int srcIndex, int length) {
        byte[] data = new byte[length];
        src.getBytes(srcIndex, data, 0, length);
        writeBytes(data, 0, length);
    }


    @Override
    public int writeBytes(InputStream src, int length) throws IOException {
        return buffer.writeBytes(src, length);
    }


    @Override
    public int writerIndex() {
        return buffer.writerIndex();
    }


    @Override
    public void writerIndex(int writerIndex) {
        buffer.writerIndex(writerIndex);
    }

    @Override
    public int compareTo(IByteBuf o) {
        return IByteBufUtil.compare(this, o);
    }
}
