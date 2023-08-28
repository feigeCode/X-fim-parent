package com.feige.framework.api.context;


import com.feige.framework.annotation.SPI;

@SPI
public interface InitializingComp {
	
	void afterPropertiesSet() throws Exception;

}
