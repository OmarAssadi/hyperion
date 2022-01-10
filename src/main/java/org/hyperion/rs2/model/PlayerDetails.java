package org.hyperion.rs2.model;

import org.apache.mina.core.session.IoSession;
import org.hyperion.rs2.net.ISAACCipher;

/**
 * Contains details about a player (but not the actual <code>Player</code>
 * object itself) that has not logged in yet.
 *
 * @author Graham Edgecombe
 */
public class PlayerDetails {

    /**
     * The session.
     */
    private final IoSession session;

    /**
     * The player name.
     */
    private final String name;

    /**
     * The player password.
     */
    private final String pass;

    /**
     * The player's UID.
     */
    private final int uid;

    /**
     * The incoming ISAAC cipher.
     */
    private final ISAACCipher inCipher;

    /**
     * The outgoing ISAAC cipher.
     */
    private final ISAACCipher outCipher;

    /**
     * Creates the player details class.
     *
     * @param session   The session.
     * @param name      The name.
     * @param pass      The password.
     * @param uid       The unique id.
     * @param inCipher  The incoming cipher.
     * @param outCipher The outgoing cipher.
     */
    public PlayerDetails(final IoSession session, final String name, final String pass, final int uid, final ISAACCipher inCipher, final ISAACCipher outCipher) {
        this.session = session;
        this.name = name;
        this.pass = pass;
        this.uid = uid;
        this.inCipher = inCipher;
        this.outCipher = outCipher;
    }

    /**
     * Gets the <code>IoSession</code>.
     *
     * @return The <code>IoSession</code>.
     */
    public IoSession getSession() {
        return session;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return pass;
    }

    /**
     * Gets the unique id.
     *
     * @return The unique id.
     */
    public int getUID() {
        return uid;
    }

    /**
     * Gets the incoming ISAAC cipher.
     *
     * @return The incoming ISAAC cipher.
     */
    public ISAACCipher getInCipher() {
        return inCipher;
    }

    /**
     * Gets the outgoing ISAAC cipher.
     *
     * @return The outgoing ISAAC cipher.
     */
    public ISAACCipher getOutCipher() {
        return outCipher;
    }

}
