package com.grahamedgecombe.rs2.model;

import org.apache.mina.core.session.IoSession;

import com.grahamedgecombe.rs2.net.ISAACCipher;

public class PlayerDetails {
	
	private IoSession session;
	private String name;
	private String pass;
	private int uid;
	private ISAACCipher inCipher;
	private ISAACCipher outCipher;

	public PlayerDetails(IoSession session, String name, String pass, int uid, ISAACCipher inCipher, ISAACCipher outCipher) {
		this.session = session;
		this.name = name;
		this.pass = pass;
		this.uid = uid;
		this.inCipher = inCipher;
		this.outCipher = outCipher;
	}
	
	public IoSession getSession() {
		return session;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return pass;
	}
	
	public int getUID() {
		return uid;
	}
	public ISAACCipher getInCipher() {
		return inCipher;
	}
	
	public ISAACCipher getOutCipher() {
		return outCipher;
	}

}
