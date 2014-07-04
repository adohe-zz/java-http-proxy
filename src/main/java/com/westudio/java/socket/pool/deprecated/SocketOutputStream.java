package com.westudio.java.socket.pool.deprecated;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SocketOutputStream extends FilterOutputStream {

	private static final int defaultBufferSize = 8192;
	protected final byte buf[];

	protected int count;

	public SocketOutputStream(final OutputStream out) {
		this(out,defaultBufferSize);
	}

	public SocketOutputStream(final OutputStream out, final int size) {
		super(out);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buf = new byte[size];
	}

	private void flushBuffer() throws IOException {
		if (count > 0) {
			out.write(buf, 0, count);
			count = 0;
		}
	}

	public void write(final byte b) throws IOException {
		if (count == buf.length) {
			flushBuffer();
		}

        buf[count++] = b;
	}

	public void write(final byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	public void write(final byte b[], final int off, final int len)
			throws IOException {
		if (len >= buf.length) {
			flushBuffer();
			out.write(b, off, len);
		} else {
			if (len >= buf.length - count) {
				flushBuffer();
			}

			System.arraycopy(b, off, buf, count, len);
			count += len;
		}
	}

    public void writeCrlf() throws IOException {
        if (2 >= buf.length - count) {
            flushBuffer();
        }

        buf[count++] = '\r';
        buf[count++] = '\n';
    }

	public static boolean isSurrogate(final char ch) {
		return ch >= Character.MIN_SURROGATE && ch <= Character.MAX_SURROGATE;
	}

	public void flush() throws IOException {
		flushBuffer();
		out.flush();
	}
}
