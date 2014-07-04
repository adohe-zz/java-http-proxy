package com.westudio.java.socket.pool.deprecated;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

//@ careless of ThreadSafe because a Socket must be held by no more than one SocketInputStream/SocketOutputStream at a time.
public class SocketInputStream extends FilterInputStream {

	private static final int defaultBufferSize = 8192;
	protected byte buf[];
	protected int count;
    protected int limit;

	public SocketInputStream(InputStream in, int size) {
		super(in);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buf = new byte[size];
	}

	public SocketInputStream(InputStream in) {
		this(in, defaultBufferSize);
	}

	public byte readByte() throws IOException {
		if (count >= limit) {
			fill();
			if (count >= limit)
				return (byte) -1;
		}
		return buf[count++];
	}

	/**
	 * only for those protocols that use '\r\n' to end/start a new line.
	 * @return
	 * @throws java.io.IOException
	 */
	public String readLine() throws IOException {
		int b;
		StringBuilder sb = new StringBuilder();
		while (true) {
			if (count == limit) {
				fill();
			}
			if (limit == -1)
				break;
			b = buf[count++];
			if (b == '\r') {
				if (count == limit) {
					fill();
				}
				if (limit == -1) {
					sb.append((char) b);
					break;
				}
				if ('\n' == buf[count++]) {
					break;
				}
				sb.append('\r').append('\n');
			} else {
				sb.append((char) b);
			}
		}
		if (sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}

	public synchronized int available() throws IOException {
		int n = limit - count;
		int avail = super.available();
		return n > (Integer.MAX_VALUE - avail) ? Integer.MAX_VALUE : n + avail;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}
		if (count == limit) {
			fill();
			if (limit == -1)
				return -1;
		}
        // In case index out of bound
		final int length = Math.min(limit - count, len);
		System.arraycopy(buf, count, b, off, length);
		count += length;
		return length;
	}

	private void fill() throws IOException {
		limit = in.read(buf);
		count = 0;
	}
}
