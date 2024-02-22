package com.feige.fim.utils.ssl;

import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;

public class SslContexts {
    
    public static InputStream getInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SslContext buildClientSslContext(String clientKeyCertChainPath, String clientPrivateKeyPath, String clientKeyPassword, String clientTrustCertCollectionPath) {
        SslContextBuilder builder = SslContextBuilder.forClient();
        try (
                InputStream clientTrustCertCollectionPathStream = getInputStream(clientTrustCertCollectionPath);
                InputStream clientCertChainFilePathStream = getInputStream(clientKeyCertChainPath);
                InputStream clientPrivateKeyFilePathStream = getInputStream(clientPrivateKeyPath)
        ){
            if (clientTrustCertCollectionPathStream != null) {
                builder.trustManager(clientTrustCertCollectionPathStream);
            }

            if (clientCertChainFilePathStream != null && clientPrivateKeyFilePathStream != null) {
                if (clientKeyPassword != null) {
                    builder.keyManager(clientCertChainFilePathStream, clientPrivateKeyFilePathStream, clientKeyPassword);
                } else {
                    builder.keyManager(clientCertChainFilePathStream, clientPrivateKeyFilePathStream);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not find certificate file or find invalid certificate.", e);
        } 
        try {
            return builder.sslProvider(findSslProvider()).build();
        } catch (SSLException e) {
            throw new IllegalStateException("Build SslSession failed.", e);
        }
    }

    /**
     * Returns OpenSSL if available, otherwise returns the JDK provider.
     */
    private static SslProvider findSslProvider() {
        if (OpenSsl.isAvailable()) {
           // Using OPENSSL provider
            return SslProvider.OPENSSL;
        }
        if (checkJdkProvider()) {
            // Using JDK provider
            return SslProvider.JDK;
        }
        throw new IllegalStateException(
                "Could not find any valid TLS provider, please check your dependency or deployment environment, " +
                        "usually netty-tcnative, Conscrypt, or Jetty NPN/ALPN is needed.");
    }

    private static boolean checkJdkProvider() {
        Provider[] jdkProviders = Security.getProviders("SSLContext.TLS");
        return (jdkProviders != null && jdkProviders.length > 0);
    }
    

}
