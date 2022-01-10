package org.hyperion.util.login;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * A factory which produces codecs for the login server protocol.
 *
 * @author Graham Edgecombe
 */
public class LoginCodecFactory implements ProtocolCodecFactory {

    /**
     * The login encoder.
     */
    private static final LoginEncoder ENCODER = new LoginEncoder();

    /**
     * The login decoder.
     */
    private final LoginDecoder decoder = new LoginDecoder();

    @Override
    public ProtocolEncoder getEncoder(final IoSession arg0) throws Exception {
        return ENCODER;
    }

    @Override
    public ProtocolDecoder getDecoder(final IoSession arg0) throws Exception {
        return decoder;
    }

}
