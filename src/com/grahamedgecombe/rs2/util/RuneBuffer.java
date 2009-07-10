package com.grahamedgecombe.rs2.util;

import org.apache.mina.core.buffer.IoBuffer;

import com.grahamedgecombe.util.IoBufferUtils;

public class RuneBuffer {
	
	private IoBuffer buffer;
	
	public RuneBuffer(IoBuffer buffer) {
		this.buffer = buffer;
	}
	
	public void putString(String string) {
		byte[] bytes = string.getBytes();
		buffer.put(bytes);
		buffer.put((byte) 10);
	}
	
	public String getString() {
		return IoBufferUtils.getRS2String(buffer);
	}

}
