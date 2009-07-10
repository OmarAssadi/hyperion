package com.grahamedgecombe.rs2.util;

import org.apache.mina.core.buffer.IoBuffer;

public class IoBufferUtils {
	
	public static String getRS2String(IoBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(buf.hasRemaining() && (b = buf.get()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

}
