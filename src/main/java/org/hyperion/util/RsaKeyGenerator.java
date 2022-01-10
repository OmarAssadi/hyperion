package org.hyperion.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * An RSA key generator.
 *
 * @author Graham
 * @author Major
 * @author Cube
 * @author Omar Assadi <omar@assdi.co.il>
 */
public final class RsaKeyGenerator {

    /**
     * Private constructor to prevent instantiation.
     */
    private RsaKeyGenerator() {

    }

    /**
     * The bit count.
     * <strong>Note:</strong> 2048 bits and above are not compatible with the client without modifications
     */
    private static final int BIT_COUNT = 1024;

    /**
     * The path to the private key file.
     */
    private static final String PRIVATE_KEY_FILE = "data/rsa-private.pem";

    /**
     * The path to the public key file.
     */
    private static final String PUBLIC_KEY_FILE = "data/rsa-public.pem";

    /**
     * The entry point of the RsaKeyGenerator.
     *
     * @param args The application arguments.
     */
    public static void main(final String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(BIT_COUNT);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        try (final PemWriter writer = new PemWriter(new FileWriter(PRIVATE_KEY_FILE))) {
            writer.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
        } catch (final Exception e) {
            System.err.println("Failed to write private key to " + PRIVATE_KEY_FILE);
            e.printStackTrace();
        }

        try (final PemWriter writer = new PemWriter(new FileWriter(PUBLIC_KEY_FILE))) {
            writer.writeObject(new PemObject("PUBLIC KEY", publicKey.getEncoded()));
        } catch (final Exception e) {
            System.err.println("Failed to write public key to " + PUBLIC_KEY_FILE);
            e.printStackTrace();
        }

        System.out.println("Place these keys in the client:");
        System.out.println("--------------------");
        System.out.println("public key: " + publicKey.getPublicExponent());
        System.out.println("modulus: " + publicKey.getModulus());
    }
}
