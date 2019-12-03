package com.macys.rds.msg.config;

import javax.jms.Connection;
import javax.jms.JMSException;
//import javax.jms.QueueConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;



@Configuration
@PropertySource("classpath:application.properties")
public class MqConfig {
	
	
	private String mqHostname;
	private int mqport;
	private String mqqueueManager;
	
	public MqConfig(@Value("${mq.hostname}") String mqHostname, @Value("${mq.port}") String mqport, @Value("${mq.queueManager}") String mqqueueManager )
	{
		this.mqHostname = mqHostname;
		this.mqport = Integer.parseInt(mqport);
		this.mqqueueManager = mqqueueManager;
		System.out.println("***MqConfig Created***");
	}
	
	
	@Bean
	public MQQueueConnectionFactory mqQueueConnectionFactory() {
		MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		mqQueueConnectionFactory.setHostName(mqHostname);
		//mqQueueConnectionFactory.setHostName("AMD1DVIPA");
		try {
			mqQueueConnectionFactory.setTransportType(WMQConstants.ADMIN_QUEUE_DOMAIN);
			// mqQueueConnectionFactory.setCCSID(1208);
			// mqQueueConnectionFactory.setChannel("AMD1.APP.SVRCONN"); 
			
			mqQueueConnectionFactory.setPort(mqport);
			//mqQueueConnectionFactory.setPort(1417);
			
			mqQueueConnectionFactory.setQueueManager(mqqueueManager);
			//mqQueueConnectionFactory.setQueueManager("AMD1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("***MQQueueConnectionFactory Created***");
		return mqQueueConnectionFactory;
	}
	
	@Bean
	public Connection mqConnection() {

		MQQueueConnectionFactory mqQueueConnectionFactory = mqQueueConnectionFactory();
		Connection connection = null;
		try {
			connection = mqQueueConnectionFactory.createConnection("admin", "admin");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

}
