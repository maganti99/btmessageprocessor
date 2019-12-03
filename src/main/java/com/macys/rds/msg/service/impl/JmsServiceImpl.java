package com.macys.rds.msg.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
//import javax.jms.Message;
import javax.jms.TextMessage;


import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.ibm.mq.jms.MQQueue;
import com.macys.rds.msg.publisher.CloudMessagePublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class JmsServiceImpl {	
	
	//private static final String MQUEUE = "S3D.RDS.BT06.MAIL2OES.MCY";
	private static final Logger logger = LoggerFactory.getLogger(JmsServiceImpl.class);
	private Map<String, String> hdrs = new HashMap<String, String>();	
	
	
	private String topicID;	 
	private String PROJECT_ID;
	
	@Autowired
	private  JmsTemplate jmsTemplate;
	
	public JmsServiceImpl(@Value("${google.topicID}") String topicID, @Value("${google.PROJECT_ID}") String PROJECT_ID)
	{
		this.topicID = topicID;
		this.PROJECT_ID = PROJECT_ID;
		System.out.println("***JmsServiceImpl Created***");
	}
	
	public Map<String, String> getHdrs() {
		return hdrs;
	}


	@JmsListener(destination = "${mq.name}")	
	public void receiveMessage(final Message msg) throws JMSException {
	//public void receiveMs1(final Message msg) throws JMSException {
		
		if(msg instanceof TextMessage)
		{
	       TextMessage message = (TextMessage)msg;
		   System.out.println("Received message " + message.getText());
		   logger.info("Received message " + message.getText());		
		
		   hdrs.put("correlationId","18989");
		   //publishGCP("btinventory_event_dev", message.getText(), hdrs);
		   publishGCP(message.getText(), hdrs);
		}
		else
		{
			System.out.println("Inavlid message received : " + msg.getClass());
			logger.error("Inavlid message received : " + msg.getClass());
		}
	}	
	
	private void publishGCP(String message, Map<String, String> headers)
	{
		try {			
			CloudMessagePublisher.publish(PROJECT_ID, topicID, message, headers, 5);			
		} catch (Exception e) {
			logger.error("Error publishing to cloud PUB/SUB : " + e);			
		}	
		
	}

	/*public void sendMessage() throws IOException, JMSException {
		String message = "<?xml version=\"1.0\" encoding=\"IBM-1140\"?><ORDEREVENT xmlns=\"HTTP://WWW.MST.MACYS.COM/XS/ORDER/BIGTICKET/ORDEREVENT.V1.0\">\r\n" + 
				"	<FLD1>SAMPLE</FLD1>\r\n" + 
				"	<FLD2>\r\n" + 
				"		<FLD2A>RDS-TESTA1</FLD2A>\r\n" + 
				"		<FLD2B>RDS-TESTB1</FLD2B>\r\n" + 
				"	</FLD2>\r\n" + 
				"	<FLD2>\r\n" + 
				"		<FLD2A>RDS-TESTA2</FLD2A>\r\n" + 
				"		<FLD2B>RDS-TESTB2</FLD2B>\r\n" + 
				"	</FLD2>\r\n" + 
				"	<FLD2>\r\n" + 
				"		<FLD2A>RDS-TESTA3</FLD2A>\r\n" + 
				"		<FLD2B>RDS-TESTB3</FLD2B>\r\n" + 
				"	</FLD2>\r\n" + 
				"	<FLD3>END</FLD3>\r\n" + 
				"</ORDEREVENT>\r\n"; 
				
		System.out.println("Rest point messageData send----->" + message);
		Destination destination = new MQQueue(MQUEUE) ;
		jmsTemplate.convertAndSend(destination, message);

	}*/
	

}
