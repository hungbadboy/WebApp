package com.siblinks.cache;

import java.util.concurrent.TimeUnit;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CachingSetup implements JCacheManagerCustomizer {

	@Override
	public void customize(CacheManager cacheManager) {
		cacheManager.createCache("people", new MutableConfiguration<Object, Object>()  
		        .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 10L))) 
		        .setStoreByValue(false)
		        .setStatisticsEnabled(true));
	}

}
