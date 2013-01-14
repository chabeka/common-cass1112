package fr.urssaf.image.dictao.client.utils;

import org.apache.commons.ssl.HttpSecureProtocol;
import org.apache.commons.ssl.KeyMaterial;
import org.apache.commons.ssl.TrustMaterial;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;

/**
 * Basé sur AuthSSLProtocolSocketFactory, mais avec une correction.
 * Cf http://old.nabble.com/No-private-keys-found-in-keystore-td18165598.html
 */

public class PatchedAuthSSLProtocolSocketFactory extends HttpSecureProtocol {

    /**
     * Constructor for AuthSSLProtocolSocketFactory. Either a keystore or truststore file
     * must be given. Otherwise SSL context initialization error will result.
     *
     * @param keystoreUrl        URL of the keystore file. May be <tt>null</tt> if HTTPS client
     *                           authentication is not to be used.
     * @param keystorePassword   Password to unlock the keystore. IMPORTANT: this implementation
     *                           assumes that the same password is used to protect the key and the keystore itself.
     * @param truststoreUrl      URL of the truststore file. May be <tt>null</tt> if HTTPS server
     *                           authentication is not to be used.
     * @param truststorePassword Password to unlock the truststore.
     */
    public PatchedAuthSSLProtocolSocketFactory(final URL keystoreUrl,
                                        final String keystorePassword,
                                        final URL truststoreUrl,
                                        final String truststorePassword)
        throws GeneralSecurityException, IOException {

        super();

        // prepare key material
        if (keystoreUrl != null) {
            char[] ksPass = null;
            if (keystorePassword != null) {
                ksPass = keystorePassword.toCharArray();
            }
            KeyMaterial km = new KeyMaterial(keystoreUrl, ksPass);
            super.setKeyMaterial(km);
        }

        // prepare trust material1
        if (truststoreUrl != null) {
            char[] tsPass = null;
            if (truststorePassword != null) {
                tsPass = truststorePassword.toCharArray();
            }
            
            // Début correction
            //TrustMaterial tm = new KeyMaterial(truststoreUrl, tsPass);
            TrustMaterial tm = new TrustMaterial(truststoreUrl, tsPass);
            // Fin correction
            
            super.setTrustMaterial(tm);
        }
    }

}

