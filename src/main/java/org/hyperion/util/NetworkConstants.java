package org.hyperion.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Holds various network-related constants such as port numbers.
 *
 * @author Graham Edgecombe
 * @author Major
 * @author Omar Assadi <omar@assadi.co.il>
 */
public final class NetworkConstants {

    public static final BigInteger RSA_MODULUS;
    public static final BigInteger RSA_EXPONENT;
    public static final int LOGIN_PORT = 43596;

    static {
        try (final PemReader pemReader = new PemReader(new FileReader("data/rsa-private.pem"))) {
            final PemObject pem = pemReader.readPemObject();
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pem.getContent());

            Security.addProvider(new BouncyCastleProvider());
            final KeyFactory factory = KeyFactory.getInstance("RSA", "BC");

            final RSAPrivateKey privateKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
            RSA_MODULUS = privateKey.getModulus();
            RSA_EXPONENT = privateKey.getPrivateExponent();
        } catch (final Exception exception) {
            throw new ExceptionInInitializerError(new IOException("Error parsing rsa-private.pem", exception));
        }
    }

    private NetworkConstants() {

    }
}
