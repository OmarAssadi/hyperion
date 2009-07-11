package com.grahamedgecombe.rs2;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.net.Packet;
import com.grahamedgecombe.rs2.net.RS2CodecFactory;
import com.grahamedgecombe.rs2.task.SessionClosedTask;
import com.grahamedgecombe.rs2.task.SessionMessageTask;
import com.grahamedgecombe.rs2.task.SessionOpenedTask;

public class ConnectionHandler implements IoHandler {
	
	private final GameEngine engine = World.getWorld().getEngine();

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		session.close(false);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		engine.pushTask(new SessionMessageTask(session, (Packet) message));
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		engine.pushTask(new SessionClosedTask(session));
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		session.close(false);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.setAttribute("remote", session.getRemoteAddress());
		session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(RS2CodecFactory.LOGIN));
		engine.pushTask(new SessionOpenedTask(session));
	}

}
