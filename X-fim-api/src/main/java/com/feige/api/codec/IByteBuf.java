package com.feige.api.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface IByteBuf extends Comparable<IByteBuf> {

    /**
     * Returns the number of bytes (octets) this buffer can contain.
     */
    int capacity();

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer to
     * {@code 0}. This method is identical to {@link #setIndex(int, int)
     * setIndex(0, 0)}.
     * <p/>
     * Please note that the behavior of this method is different from that of
     * NIO buffer, which sets the {@code limit} to the {@code capacity} of the
     * buffer.
     */
    void clear();

    /**
     * Returns a copy of this buffer's readable bytes.  Modifying the content of
     * the returned buffer or this buffer does not affect each other at all.
     * This method is identical to {@code buf.copy(buf.readerIndex(),
     * buf.readableBytes())}. This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     */
    IByteBuf copy();

    /**
     * Returns a copy of this buffer's sub-region.  Modifying the content of the
     * returned buffer or this buffer does not affect each other at all. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this
     * buffer.
     */
    IByteBuf copy(int index, int length);

    /**
     * Discards the bytes between the 0th index and {@code readerIndex}. It
     * moves the bytes between {@code readerIndex} and {@code writerIndex} to
     * the 0th index, and sets {@code readerIndex} and {@code writerIndex} to
     * {@code 0} and {@code oldWriterIndex - oldReaderIndex} respectively.
     * <p/>
     * Please refer to the class documentation for more detailed explanation.
     */
    void discardReadBytes();

    /**
     * Makes sure the number of {@linkplain #writableBytes() the writable bytes}
     * is equal to or greater than the specified value.  If there is enough
     * writable bytes in this buffer, this method returns with no side effect.
     * Otherwise: <ul> <li>a non-dynamic buffer will throw an {@link
     * IndexOutOfBoundsException}.</li> <li>a dynamic buffer will expand its
     * capacity so that the number of the {@link #writableBytes() writable
     * bytes} becomes equal to or greater than the specified value. The
     * expansion involves the reallocation of the internal buffer and
     * consequently memory copy.</li> </ul>
     *
     * @param writableBytes the expected minimum number of writable bytes
     * @throws IndexOutOfBoundsException if {@linkplain #writableBytes() the
     *                                   writable bytes} of this buffer is less
     *                                   than the specified value and if this
     *                                   buffer is not a dynamic buffer
     */
    void ensureWritableBytes(int writableBytes);

    /**
     * Determines if the content of the specified buffer is identical to the
     * content of this array.  'Identical' here means: <ul> <li>the size of the
     * contents of the two buffers are same and</li> <li>every single byte of
     * the content of the two buffers are same.</li> </ul> Please note that it
     * does not compare {@link #readerIndex()} nor {@link #writerIndex()}.  This
     * method also returns {@code false} for {@code null} and an object which is
     * not an instance of {@link IByteBuf} type.
     */
    @Override
    boolean equals(Object o);



    /**
     * Gets a byte at the specified absolute {@code index} in this buffer. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this
     * buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or {@code index + 1} is
     *                                   greater than {@code this.capacity}
     */
    byte getByte(int index);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   dst.length} is greater than {@code
     *                                   this.capacity}
     */
    void getBytes(int index, byte[] dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0}, if the specified {@code
     *                                   dstIndex} is less than {@code 0}, if
     *                                   {@code index + length} is greater than
     *                                   {@code this.capacity}, or if {@code
     *                                   dstIndex + length} is greater than
     *                                   {@code dst.length}
     */
    void getBytes(int index, byte[] dst, int dstIndex, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * specified absolute {@code index} until the destination's position reaches
     * its limit. This method does not modify {@code readerIndex} or {@code
     * writerIndex} of this buffer while the destination's {@code position} will
     * be increased.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   dst.remaining()} is greater than {@code
     *                                   this.capacity}
     */
    void getBytes(int index, ByteBuffer dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * specified absolute {@code index} until the destination becomes
     * non-writable.  This method is basically same with {@link #getBytes(int,
     * IByteBuf, int, int)}, except that this method increases the {@code
     * writerIndex} of the destination by the number of the transferred bytes
     * while {@link #getBytes(int, IByteBuf, int, int)} does not. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of the
     * source buffer (i.e. {@code this}).
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   dst.writableBytes} is greater than
     *                                   {@code this.capacity}
     */
    void getBytes(int index, IByteBuf dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * specified absolute {@code index}.  This method is basically same with
     * {@link #getBytes(int, IByteBuf, int, int)}, except that this method
     * increases the {@code writerIndex} of the destination by the number of the
     * transferred bytes while {@link #getBytes(int, IByteBuf, int, int)}
     * does not. This method does not modify {@code readerIndex} or {@code
     * writerIndex} of the source buffer (i.e. {@code this}).
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0}, if {@code index +
     *                                   length} is greater than {@code
     *                                   this.capacity}, or if {@code length} is
     *                                   greater than {@code dst.writableBytes}
     */
    void getBytes(int index, IByteBuf dst, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of both the source (i.e. {@code
     * this}) and the destination.
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0}, if the specified {@code
     *                                   dstIndex} is less than {@code 0}, if
     *                                   {@code index + length} is greater than
     *                                   {@code this.capacity}, or if {@code
     *                                   dstIndex + length} is greater than
     *                                   {@code dst.capacity}
     */
    void getBytes(int index, IByteBuf dst, int dstIndex, int length);

    /**
     * Transfers this buffer's data to the specified stream starting at the
     * specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   length} is greater than {@code
     *                                   this.capacity}
     * @throws IOException               if the specified stream threw an
     *                                   exception during I/O
     */
    void getBytes(int index, OutputStream dst, int length) throws IOException;

    /**
     * Returns {@code true} if and only if this buffer is backed by an NIO
     * direct buffer.
     */
    boolean isDirect();

    /**
     * Marks the current {@code readerIndex} in this buffer.  You can reposition
     * the current {@code readerIndex} to the marked {@code readerIndex} by
     * calling {@link #resetReaderIndex()}. The initial value of the marked
     * {@code readerIndex} is {@code 0}.
     */
    void markReaderIndex();

    /**
     * Marks the current {@code writerIndex} in this buffer.  You can reposition
     * the current {@code writerIndex} to the marked {@code writerIndex} by
     * calling {@link #resetWriterIndex()}. The initial value of the marked
     * {@code writerIndex} is {@code 0}.
     */
    void markWriterIndex();

    /**
     * Returns {@code true} if and only if {@code (this.writerIndex -
     * this.readerIndex)} is greater than {@code 0}.
     */
    boolean readable();

    /**
     * Returns the number of readable bytes which is equal to {@code
     * (this.writerIndex - this.readerIndex)}.
     */
    int readableBytes();

    /**
     * Gets a byte at the current {@code readerIndex} and increases the {@code
     * readerIndex} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less
     *                                   than {@code 1}
     */
    byte readByte();

    /**
     * Gets a 16-bit short integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 2}
     */
    short readShort();

    /**
     * Gets a 16-bit short integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 2}
     */
    short readShortLE();

    /**
     * Gets an unsigned 16-bit short integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 2}
     */
    int   readUnsignedShort();

    /**
     * Gets an unsigned 16-bit short integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 2}
     */
    int   readUnsignedShortLE();

    /**
     * Gets a 24-bit medium integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 3}
     */
    int   readMedium();

    /**
     * Gets a 24-bit medium integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the
     * {@code readerIndex} by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 3}
     */
    int   readMediumLE();

    /**
     * Gets an unsigned 24-bit medium integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 3}
     */
    int   readUnsignedMedium();

    /**
     * Gets an unsigned 24-bit medium integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 3} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 3}
     */
    int   readUnsignedMediumLE();

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    int   readInt();

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    int   readIntLE();

    /**
     * Gets an unsigned 32-bit integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    long  readUnsignedInt();

    /**
     * Gets an unsigned 32-bit integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    long  readUnsignedIntLE();

    /**
     * Gets a 64-bit integer at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 8}
     */
    long  readLong();

    /**
     * Gets a 64-bit integer at the current {@code readerIndex}
     * in the Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 8}
     */
    long  readLongLE();

    /**
     * Gets a 2-byte UTF-16 character at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 2}
     */
    char  readChar();

    /**
     * Gets a 32-bit floating point number at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    float readFloat();

    /**
     * Gets a 32-bit floating point number at the current {@code readerIndex}
     * in Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    default float readFloatLE() {
        return Float.intBitsToFloat(readIntLE());
    }

    /**
     * Gets a 64-bit floating point number at the current {@code readerIndex}
     * and increases the {@code readerIndex} by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 8}
     */
    double readDouble();

    /**
     * Gets a 64-bit floating point number at the current {@code readerIndex}
     * in Little Endian Byte Order and increases the {@code readerIndex}
     * by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 8}
     */
    default double readDoubleLE() {
        return Double.longBitsToDouble(readLongLE());
    }


    /**
     * Transfers this buffer's data to the specified destination starting at the
     * current {@code readerIndex} and increases the {@code readerIndex} by the
     * number of the transferred bytes (= {@code dst.length}).
     *
     * @throws IndexOutOfBoundsException if {@code dst.length} is greater than
     *                                   {@code this.readableBytes}
     */
    void readBytes(byte[] dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * current {@code readerIndex} and increases the {@code readerIndex} by the
     * number of the transferred bytes (= {@code length}).
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code dstIndex} is
     *                                   less than {@code 0}, if {@code length}
     *                                   is greater than {@code this.readableBytes},
     *                                   or if {@code dstIndex + length} is
     *                                   greater than {@code dst.length}
     */
    void readBytes(byte[] dst, int dstIndex, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * current {@code readerIndex} until the destination's position reaches its
     * limit, and increases the {@code readerIndex} by the number of the
     * transferred bytes.
     *
     * @throws IndexOutOfBoundsException if {@code dst.remaining()} is greater
     *                                   than {@code this.readableBytes}
     */
    void readBytes(ByteBuffer dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * current {@code readerIndex} until the destination becomes non-writable,
     * and increases the {@code readerIndex} by the number of the transferred
     * bytes.  This method is basically same with {@link
     * #readBytes(IByteBuf, int, int)}, except that this method increases
     * the {@code writerIndex} of the destination by the number of the
     * transferred bytes while {@link #readBytes(IByteBuf, int, int)} does
     * not.
     *
     * @throws IndexOutOfBoundsException if {@code dst.writableBytes} is greater
     *                                   than {@code this.readableBytes}
     */
    void readBytes(IByteBuf dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * current {@code readerIndex} and increases the {@code readerIndex} by the
     * number of the transferred bytes (= {@code length}).  This method is
     * basically same with {@link #readBytes(IByteBuf, int, int)}, except
     * that this method increases the {@code writerIndex} of the destination by
     * the number of the transferred bytes (= {@code length}) while {@link
     * #readBytes(IByteBuf, int, int)} does not.
     *
     * @throws IndexOutOfBoundsException if {@code length} is greater than
     *                                   {@code this.readableBytes} or if {@code
     *                                   length} is greater than {@code
     *                                   dst.writableBytes}
     */
    void readBytes(IByteBuf dst, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at the
     * current {@code readerIndex} and increases the {@code readerIndex} by the
     * number of the transferred bytes (= {@code length}).
     *
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code dstIndex} is
     *                                   less than {@code 0}, if {@code length}
     *                                   is greater than {@code this.readableBytes},
     *                                   or if {@code dstIndex + length} is
     *                                   greater than {@code dst.capacity}
     */
    void readBytes(IByteBuf dst, int dstIndex, int length);

    /**
     * Transfers this buffer's data to a newly created buffer starting at the
     * current {@code readerIndex} and increases the {@code readerIndex} by the
     * number of the transferred bytes (= {@code length}). The returned buffer's
     * {@code readerIndex} and {@code writerIndex} are {@code 0} and {@code
     * length} respectively.
     *
     * @param length the number of bytes to transfer
     * @return the newly created buffer which contains the transferred bytes
     * @throws IndexOutOfBoundsException if {@code length} is greater than
     *                                   {@code this.readableBytes}
     */
    IByteBuf readBytes(int length);

    /**
     * Repositions the current {@code readerIndex} to the marked {@code
     * readerIndex} in this buffer.
     *
     * @throws IndexOutOfBoundsException if the current {@code writerIndex} is
     *                                   less than the marked {@code
     *                                   readerIndex}
     */
    void resetReaderIndex();

    /**
     * Marks the current {@code writerIndex} in this buffer.  You can reposition
     * the current {@code writerIndex} to the marked {@code writerIndex} by
     * calling {@link #resetWriterIndex()}. The initial value of the marked
     * {@code writerIndex} is {@code 0}.
     */
    void resetWriterIndex();

    /**
     * Returns the {@code readerIndex} of this buffer.
     */
    int readerIndex();

    /**
     * Sets the {@code readerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code readerIndex} is
     *                                   less than {@code 0} or greater than
     *                                   {@code this.writerIndex}
     */
    void readerIndex(int readerIndex);

    /**
     * Transfers this buffer's data to the specified stream starting at the
     * current {@code readerIndex}.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if {@code length} is greater than
     *                                   {@code this.readableBytes}
     * @throws IOException               if the specified stream threw an
     *                                   exception during I/O
     */
    void readBytes(OutputStream dst, int length) throws IOException;

    /**
     * Sets the specified byte at the specified absolute {@code index} in this
     * buffer.  The 24 high-order bits of the specified value are ignored. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this
     * buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or {@code index + 1} is
     *                                   greater than {@code this.capacity}
     */
    void setByte(int index, int value);

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   src.length} is greater than {@code
     *                                   this.capacity}
     */
    void setBytes(int index, byte[] src);

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0}, if the specified {@code
     *                                   srcIndex} is less than {@code 0}, if
     *                                   {@code index + length} is greater than
     *                                   {@code this.capacity}, or if {@code
     *                                   srcIndex + length} is greater than
     *                                   {@code src.length}
     */
    void setBytes(int index, byte[] src, int srcIndex, int length);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index} until the source buffer's position
     * reaches its limit. This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   src.remaining()} is greater than {@code
     *                                   this.capacity}
     */
    void setBytes(int index, ByteBuffer src);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index} until the source buffer becomes
     * unreadable.  This method is basically same with {@link #setBytes(int,
     * IByteBuf, int, int)}, except that this method increases the {@code
     * readerIndex} of the source buffer by the number of the transferred bytes
     * while {@link #setBytes(int, IByteBuf, int, int)} does not. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of the
     * source buffer (i.e. {@code this}).
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   src.readableBytes} is greater than
     *                                   {@code this.capacity}
     */
    void setBytes(int index, IByteBuf src);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index}.  This method is basically same with
     * {@link #setBytes(int, IByteBuf, int, int)}, except that this method
     * increases the {@code readerIndex} of the source buffer by the number of
     * the transferred bytes while {@link #setBytes(int, IByteBuf, int,
     * int)} does not. This method does not modify {@code readerIndex} or {@code
     * writerIndex} of the source buffer (i.e. {@code this}).
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0}, if {@code index +
     *                                   length} is greater than {@code
     *                                   this.capacity}, or if {@code length} is
     *                                   greater than {@code src.readableBytes}
     */
    void setBytes(int index, IByteBuf src, int length);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the specified absolute {@code index}. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of both the source (i.e. {@code
     * this}) and the destination.
     *
     * @param srcIndex the first index of the source
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0}, if the specified {@code
     *                                   srcIndex} is less than {@code 0}, if
     *                                   {@code index + length} is greater than
     *                                   {@code this.capacity}, or if {@code
     *                                   srcIndex + length} is greater than
     *                                   {@code src.capacity}
     */
    void setBytes(int index, IByteBuf src, int srcIndex, int length);

    /**
     * Transfers the content of the specified source stream to this buffer
     * starting at the specified absolute {@code index}. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @param length the number of bytes to transfer
     * @return the actual number of bytes read in from the specified channel.
     * {@code -1} if the specified channel is closed.
     * @throws IndexOutOfBoundsException if the specified {@code index} is less
     *                                   than {@code 0} or if {@code index +
     *                                   length} is greater than {@code
     *                                   this.capacity}
     * @throws IOException               if the specified stream threw an
     *                                   exception during I/O
     */
    int setBytes(int index, InputStream src, int length) throws IOException;

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer in
     * one shot.  This method is useful when you have to worry about the
     * invocation order of {@link #readerIndex(int)} and {@link
     * #writerIndex(int)} methods.  For example, the following code will fail:
     * <p/>
     * <pre>
     * // Create a buffer whose readerIndex, writerIndex and capacity are
     * // 0, 0 and 8 respectively.
     * {@link IByteBuf} buf = {@link }.buffer(8);
     *
     * // IndexOutOfBoundsException is thrown because the specified
     * // readerIndex (2) cannot be greater than the current writerIndex (0).
     * buf.readerIndex(2);
     * buf.writerIndex(4);
     * </pre>
     * <p/>
     * The following code will also fail:
     * <p/>
     * <pre>
     * // Create a buffer whose readerIndex, writerIndex and capacity are
     * // 0, 8 and 8 respectively.
     * {@link IByteBuf} buf = {@link }.wrappedBuffer(new
     * byte[8]);
     *
     * // readerIndex becomes 8.
     * buf.readLong();
     *
     * // IndexOutOfBoundsException is thrown because the specified
     * // writerIndex (4) cannot be less than the current readerIndex (8).
     * buf.writerIndex(4);
     * buf.readerIndex(2);
     * </pre>
     * <p/>
     * By contrast, {@link #setIndex(int, int)} guarantees that it never throws
     * an {@link IndexOutOfBoundsException} as long as the specified indexes
     * meet basic constraints, regardless what the current index values of the
     * buffer are:
     * <p/>
     * <pre>
     * // No matter what the current state of the buffer is, the following
     * // call always succeeds as long as the capacity of the buffer is not
     * // less than 4.
     * buf.setIndex(2, 4);
     * </pre>
     *
     * @throws IndexOutOfBoundsException if the specified {@code readerIndex} is
     *                                   less than 0, if the specified {@code
     *                                   writerIndex} is less than the specified
     *                                   {@code readerIndex} or if the specified
     *                                   {@code writerIndex} is greater than
     *                                   {@code this.capacity}
     */
    void setIndex(int readerIndex, int writerIndex);

    /**
     * Increases the current {@code readerIndex} by the specified {@code length}
     * in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code length} is greater than
     *                                   {@code this.readableBytes}
     */
    void skipBytes(int length);

    /**
     * Converts this buffer's readable bytes into a NIO buffer.  The returned
     * buffer might or might not share the content with this buffer, while they
     * have separate indexes and marks.  This method is identical to {@code
     * buf.toByteBuffer(buf.readerIndex(), buf.readableBytes())}. This method
     * does not modify {@code readerIndex} or {@code writerIndex} of this
     * buffer.
     */
    ByteBuffer toByteBuffer();

    /**
     * Converts this buffer's sub-region into a NIO buffer.  The returned buffer
     * might or might not share the content with this buffer, while they have
     * separate indexes and marks. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     */
    ByteBuffer toByteBuffer(int index, int length);

    /**
     * Returns {@code true} if and only if {@code (this.capacity -
     * this.writerIndex)} is greater than {@code 0}.
     */
    boolean writable();

    /**
     * Returns the number of writable bytes which is equal to {@code
     * (this.capacity - this.writerIndex)}.
     */
    int writableBytes();

    /**
     * Sets the specified byte at the current {@code writerIndex} and increases
     * the {@code writerIndex} by {@code 1} in this buffer. The 24 high-order
     * bits of the specified value are ignored.
     *
     * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less
     *                                   than {@code 1}
     */
    void writeByte(int value);

    /**
     * Sets the specified 16-bit short integer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
     * in this buffer.  The 16 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 2}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeShort(int value);

    /**
     * Sets the specified 16-bit short integer in the Little Endian Byte
     * Order at the current {@code writerIndex} and increases the
     * {@code writerIndex} by {@code 2} in this buffer.
     * The 16 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 2}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeShortLE(int value);

    /**
     * Sets the specified 24-bit medium integer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 3}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 3}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeMedium(int value);

    /**
     * Sets the specified 24-bit medium integer at the current
     * {@code writerIndex} in the Little Endian Byte Order and
     * increases the {@code writerIndex} by {@code 3} in this
     * buffer.
     * If {@code this.writableBytes} is less than {@code 3}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeMediumLE(int value);

    /**
     * Sets the specified 32-bit integer at the current {@code writerIndex}
     * and increases the {@code writerIndex} by {@code 4} in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeInt(int value);

    /**
     * Sets the specified 32-bit integer at the current {@code writerIndex}
     * in the Little Endian Byte Order and increases the {@code writerIndex}
     * by {@code 4} in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeIntLE(int value);

    /**
     * Sets the specified 64-bit long integer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeLong(long value);

    /**
     * Sets the specified 64-bit long integer at the current
     * {@code writerIndex} in the Little Endian Byte Order and
     * increases the {@code writerIndex} by {@code 8}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeLongLE(long value);

    /**
     * Sets the specified 2-byte UTF-16 character at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
     * in this buffer.  The 16 high-order bits of the specified value are ignored.
     * If {@code this.writableBytes} is less than {@code 2}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeChar(int value);

    /**
     * Sets the specified 32-bit floating point number at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 4}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeFloat(float value);

    /**
     * Sets the specified 32-bit floating point number at the current
     * {@code writerIndex} in Little Endian Byte Order and increases
     * the {@code writerIndex} by {@code 4} in this buffer.
     * If {@code this.writableBytes} is less than {@code 4}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    default IByteBuf writeFloatLE(float value) {
        return writeIntLE(Float.floatToRawIntBits(value));
    }

    /**
     * Sets the specified 64-bit floating point number at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
     * in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    IByteBuf writeDouble(double value);

    /**
     * Sets the specified 64-bit floating point number at the current
     * {@code writerIndex} in Little Endian Byte Order and increases
     * the {@code writerIndex} by {@code 8} in this buffer.
     * If {@code this.writableBytes} is less than {@code 8}, 
     * will be called in an attempt to expand capacity to accommodate.
     */
    default IByteBuf writeDoubleLE(double value) {
        return writeLongLE(Double.doubleToRawLongBits(value));
    }


    /**
     * Transfers the specified source array's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex} by
     * the number of the transferred bytes (= {@code src.length}).
     *
     * @throws IndexOutOfBoundsException if {@code src.length} is greater than
     *                                   {@code this.writableBytes}
     */
    void writeBytes(byte[] src);

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex} by
     * the number of the transferred bytes (= {@code length}).
     *
     * @param index  the first index of the source
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code srcIndex} is
     *                                   less than {@code 0}, if {@code srcIndex
     *                                   + length} is greater than {@code
     *                                   src.length}, or if {@code length} is
     *                                   greater than {@code this.writableBytes}
     */
    void writeBytes(byte[] src, int index, int length);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} until the source buffer's position
     * reaches its limit, and increases the {@code writerIndex} by the number of
     * the transferred bytes.
     *
     * @throws IndexOutOfBoundsException if {@code src.remaining()} is greater
     *                                   than {@code this.writableBytes}
     */
    void writeBytes(ByteBuffer src);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} until the source buffer becomes
     * unreadable, and increases the {@code writerIndex} by the number of the
     * transferred bytes.  This method is basically same with {@link
     * #writeBytes(IByteBuf, int, int)}, except that this method increases
     * the {@code readerIndex} of the source buffer by the number of the
     * transferred bytes while {@link #writeBytes(IByteBuf, int, int)} does
     * not.
     *
     * @throws IndexOutOfBoundsException if {@code src.readableBytes} is greater
     *                                   than {@code this.writableBytes}
     */
    void writeBytes(IByteBuf src);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex} by
     * the number of the transferred bytes (= {@code length}).  This method is
     * basically same with {@link #writeBytes(IByteBuf, int, int)}, except
     * that this method increases the {@code readerIndex} of the source buffer
     * by the number of the transferred bytes (= {@code length}) while {@link
     * #writeBytes(IByteBuf, int, int)} does not.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if {@code length} is greater than
     *                                   {@code this.writableBytes} or if {@code
     *                                   length} is greater then {@code
     *                                   src.readableBytes}
     */
    void writeBytes(IByteBuf src, int length);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex} by
     * the number of the transferred bytes (= {@code length}).
     *
     * @param srcIndex the first index of the source
     * @param length   the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code srcIndex} is
     *                                   less than {@code 0}, if {@code srcIndex
     *                                   + length} is greater than {@code
     *                                   src.capacity}, or if {@code length} is
     *                                   greater than {@code this.writableBytes}
     */
    void writeBytes(IByteBuf src, int srcIndex, int length);

    /**
     * Transfers the content of the specified stream to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex} by
     * the number of the transferred bytes.
     *
     * @param length the number of bytes to transfer
     * @return the actual number of bytes read in from the specified stream
     * @throws IndexOutOfBoundsException if {@code length} is greater than
     *                                   {@code this.writableBytes}
     * @throws IOException               if the specified stream threw an
     *                                   exception during I/O
     */
    int writeBytes(InputStream src, int length) throws IOException;

    /**
     * Returns the {@code writerIndex} of this buffer.
     */
    int writerIndex();

    /**
     * Sets the {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code writerIndex} is
     *                                   less than {@code this.readerIndex} or
     *                                   greater than {@code this.capacity}
     */
    void writerIndex(int writerIndex);

    /**
     * Returns the backing byte array of this buffer.
     *
     * @throws UnsupportedOperationException if there is no accessible backing byte
     *                                       array
     */
    byte[] array();

    /**
     * Returns {@code true} if and only if this buffer has a backing byte array.
     * If this method returns true, you can safely call {@link #array()} and
     * {@link #arrayOffset()}.
     */
    boolean hasArray();

    /**
     * Returns the offset of the first byte within the backing byte array of
     * this buffer.
     *
     * @throws UnsupportedOperationException if there is no accessible backing byte
     *                                       array
     */
    int arrayOffset();
}
