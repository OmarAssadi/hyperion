package org.hyperion.ls;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.util.login.LoginCodecFactory;
import org.hyperion.util.login.LoginPacket;

/**
 * Handles the login server connections.
 *
 * @author Graham Edgecombe
 */
public class LoginConnectionHandler extends IoHandlerAdapter {

    /**
     * The login server.
     */
    private final LoginServer server;

    /**
     * Creates the connection handler.
     *
     * @param server The server.
     */
    public LoginConnectionHandler(final LoginServer server) {
        this.server = server;
    }

    @Override
    public void sessionOpened(final IoSession session) throws Exception {
        session.getFilterChain().addFirst("protocolCodecFilter", new ProtocolCodecFilter(new LoginCodecFactory()));
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        server.pushTask(() -> {
            if (session.containsAttribute("node")) {
                NodeManager.getNodeManager().unregister((Node) session.getAttribute("node"));
            }
        });
    }

    @Override
    public void exceptionCaught(final IoSession session, final Throwable throwable) throws Exception {
        session.close(false);
        throwable.printStackTrace();
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) throws Exception {
        server.pushTask(() -> {
            if (session.containsAttribute("node")) {
                ((Node) session.getAttribute("node")).handlePacket((LoginPacket) message);
            } else {
                handlePreAuthenticationPacket(session, (LoginPacket) message);
            }
        });
    }

    /**
     * Handles the authentication packet.
     *
     * @param session The session.
     * @param message
     */
    private void handlePreAuthenticationPacket(final IoSession session, final LoginPacket message) {
        if (message.getOpcode() == LoginPacket.AUTH) {
            final int node = message.getPayload().getUnsignedShort();
            final String password = IoBufferUtils.getRS2String(message.getPayload());
            final boolean valid = NodeManager.getNodeManager().isNodeAuthenticationValid(node, password);
            final Node n = new Node(server, session, node);
            session.setAttribute("node", n);
            NodeManager.getNodeManager().register(n);
            final int code = valid ? 0 : 1;
            final IoBuffer resp = IoBuffer.allocate(1);
            resp.put((byte) code);
            resp.flip();
            session.write(new LoginPacket(LoginPacket.AUTH_RESPONSE, resp));
        } else {
            session.close(false);
        }
    }

}
