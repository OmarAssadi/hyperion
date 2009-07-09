package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class RS2Encoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object in, ProtocolEncoderOutput out) throws Exception {
		Packet p = (Packet) in;
		if(p.isRaw()) {
			out.write(p.getPayload());
		} else {
			
		}
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
		
	}
	
}
