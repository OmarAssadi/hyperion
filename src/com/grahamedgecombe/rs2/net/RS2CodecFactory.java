package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class RS2CodecFactory implements ProtocolCodecFactory {
	
	private static final RS2LoginDecoder loginDecoder = new RS2LoginDecoder();
	private static final RS2Decoder decoder = new RS2Decoder();
	private static final RS2Encoder encoder = new RS2Encoder();
	
	public static final RS2CodecFactory LOGIN = new RS2CodecFactory(false);
	public static final RS2CodecFactory GAME = new RS2CodecFactory(true);
	
	private boolean loginComplete;

	private RS2CodecFactory(boolean loginComplete) {
		this.loginComplete = loginComplete;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return loginComplete ? decoder : loginDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

}
