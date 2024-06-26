package com.feige.fim.factory;

import com.feige.framework.utils.Configs;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLException;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;

public class SslContextFactory {

    public static SslContext createClientSslContext(String clientKeyCertChainPath, String clientPrivateKeyPath, String clientKeyPassword, String clientTrustCertCollectionPath) {
        SslContextBuilder builder = SslContextBuilder.forClient();
        try (
                InputStream clientTrustCertCollectionPathStream = Configs.getInputStream(clientTrustCertCollectionPath);
                InputStream clientCertChainFilePathStream = Configs.getInputStream(clientKeyCertChainPath);
                InputStream clientPrivateKeyFilePathStream = Configs.getInputStream(clientPrivateKeyPath)
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
    
    public static SslContext createSeverSslContext(String serverKeyCertChainPathKey, String serverPrivateKeyPathKey, String serverTrustCertKey, String serverKeyPasswordKey) {
        SslContextBuilder sslClientContextBuilder;
        try(
                InputStream serverKeyCertChainPathStream = Configs.getInputStream(serverPrivateKeyPathKey);
                InputStream serverPrivateKeyPathStream = Configs.getInputStream(serverPrivateKeyPathKey);
                InputStream serverTrustCertStream = Configs.getInputStream(serverTrustCertKey)
        ) {
            String serverKeyPassword = Configs.getString(serverKeyPasswordKey);
            if (serverKeyPassword != null) {
                sslClientContextBuilder = SslContextBuilder.forServer(serverKeyCertChainPathStream,
                        serverPrivateKeyPathStream, serverKeyPassword);
            } else {
                sslClientContextBuilder = SslContextBuilder.forServer(serverKeyCertChainPathStream,
                        serverPrivateKeyPathStream);
            }
            if (serverTrustCertStream != null) {
                sslClientContextBuilder.trustManager(serverTrustCertStream);
                sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not find certificate file or the certificate is invalid.", e);
        }
        try {
            return sslClientContextBuilder.sslProvider(findSslProvider()).build();
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
