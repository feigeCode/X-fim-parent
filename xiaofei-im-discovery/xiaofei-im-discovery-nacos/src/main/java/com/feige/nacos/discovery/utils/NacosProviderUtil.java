package com.feige.nacos.discovery.utils;

/**
 * @author feige<br />
 * @ClassName: NacosProviderUtil <br/>
 * @Description: <br/>
 * @date: 2022/5/8 10:55<br/>
 */
public class NacosProviderUtil {

    private static NacosProvider nacosProvider = new DefaultNacosProvider();

    public static void setNacosProvider(NacosProvider nacosProvider){
        if (nacosProvider == null) {
            throw new IllegalArgumentException("nacosProvider is null");
        }
        NacosProviderUtil.nacosProvider = nacosProvider;
    }

    public static NacosProvider getNacosProvider(){
        return nacosProvider;
    }

}
