package org.hyperion.fileserver;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Represents a single update session.
 *
 * @author Graham Edgecombe
 */
public class UpdateSession {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(UpdateSession.class.getName());
    /**
     * The <code>IoSession</code> we are serving.
     */
    private final IoSession session;
    /**
     * The type of session we are.
     */
    private final Type type;
    /**
     * The request we are serving.
     */
    private Request request;

    /**
     * Creates the update session.
     *
     * @param type    The type of session.
     * @param session The <code>IoSession</code>.
     */
    public UpdateSession(final Type type, final IoSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * Reads a line of input data.
     *
     * @param line The line.
     */
    public void readLine(final String line) {
        if (request == null) {
            switch (type) {
                case JAGGRAB -> readJaggrabPath(line);
                case HTTP -> readHttpPath(line);
            }
        } else if (type == Type.HTTP) {
            if (line.length() == 0) {
                serve();
            }
        }
    }

    /**
     * Reads the path from a JAGGRAB request line.
     *
     * @param line The request line.
     */
    private void readJaggrabPath(final String line) {
        final String START = "JAGGRAB ";
        if (line.startsWith(START)) {
            request = new Request(line.substring(START.length()).trim());
        } else {
            session.close(false);
        }
        serve();
    }

    /**
     * Reads the path from a HTTP request line.
     *
     * @param line The request line.
     */
    private void readHttpPath(final String line) {
        final String[] parts = line.split(" ");
        if (parts.length == 3) {
            request = new Request(parts[1].trim());
        } else {
            session.close(false);
        }
    }

    /**
     * Servers the requested file.
     */
    private void serve() {
        if (request == null) {
            session.close(false);
            return;
        }
        logger.fine("Serving " + type + " request : " + request.getPath());
        final Response resp = RequestHandler.handle(request);
        if (resp == null) {
            session.close(false);
            return;
        }

        final StringBuilder header = new StringBuilder();
        if (type == Type.HTTP) {
            header.append("HTTP/1.0 200 OK\r\n");
            header.append("Content-Length: ").append(resp.getFileData().remaining()).append("\r\n");
            header.append("Connection: close\r\n");
            header.append("Server: JaGeX/3.1\r\n");
            header.append("Content-Type: ").append(resp.getMimeType()).append("\r\n");
            header.append("\r\n");
        }
        final byte[] headerBytes = header.toString().getBytes();

        final ByteBuffer bb = resp.getFileData();
        final IoBuffer ib = IoBuffer.allocate(bb.remaining() + headerBytes.length);
        ib.put(headerBytes).put(bb);
        ib.flip();
        session.write(ib).addListener(arg0 -> session.close(false));
    }

    /**
     * An enum which describes the type of session.
     *
     * @author Graham Edgecombe
     */
    public enum Type {

        /**
         * A plain HTTP session (which the loader will fall back to if port 443
         * cannot be used).
         */
        HTTP,

        /**
         * A JAGGRAB session (which is the primary choice of the loader).
         */
        JAGGRAB
    }

}
