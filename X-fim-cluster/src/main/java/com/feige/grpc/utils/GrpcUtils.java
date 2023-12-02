package com.feige.grpc.utils;

public class GrpcUtils {

    public static final String DOMAIN_SOCKET_ADDRESS_SCHEME = "unix";
    public static final String ANY_IP_ADDRESS = "*";
    
    public static String extractDomainSocketAddressPath(final String address) {
        String domainSocketAddressPrefix = DOMAIN_SOCKET_ADDRESS_SCHEME + ":";
        if (!address.startsWith(domainSocketAddressPrefix)) {
            throw new IllegalArgumentException(address + " is not a valid domain socket address.");
        }
        String path = address.substring(domainSocketAddressPrefix.length());
        if (path.startsWith("//")) {
            path = path.substring(2);
        }
        return path;
    }
    
    
}
