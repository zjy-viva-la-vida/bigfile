package com.supereal.bigfile.conf;


import org.apache.commons.logging.LogFactory;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 
 * @author shangjunhong@bitmain.com
 */
@Configuration
public class FastDFSConfig {
	
    @Value("${fastdfs.connect_timeout_in_seconds}")
    private Integer connectTimeoutInSeconds;
	
	@Value("${fastdfs.network_timeout_in_seconds}")
    private Integer networkTimeoutInSeconds;
	
	@Value("${fastdfs.charset}")
	private String charset;
	
	@Value("${fastdfs.http_anti_steal_token}")
	private boolean httpAntiStealToken;
	
	@Value("${fastdfs.http_secret_key}")
	private String httpSecretKey;
	
	@Value("${fastdfs.http_tracker_http_port}")
    private Integer httpTrackerHttpPort;
	
	@Value("${fastdfs.tracker_servers}")
	private String tracker_servers;
	
	@Bean
	public TrackerClient producer() {
		Properties properties = new Properties();
		properties.put("fastdfs.connect_timeout_in_seconds", connectTimeoutInSeconds);
		properties.put("fastdfs.network_timeout_in_seconds", networkTimeoutInSeconds);
		properties.put("fastdfs.charset", charset);
		properties.put("fastdfs.http_anti_steal_token", httpAntiStealToken);
		properties.put("fastdfs.http_secret_key", httpSecretKey);
		properties.put("fastdfs.http_tracker_http_port", httpTrackerHttpPort);
		properties.put("fastdfs.tracker_servers", tracker_servers);
		try {
			ClientGlobal.initByProperties(properties);
		} catch (Exception e) {
			LogFactory.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return new TrackerClient(ClientGlobal.g_tracker_group);
	}
}
