package com.macys.rds.msg.test;

import org.mockito.MockitoAnnotations;
/*import org.mockito.MockitoAnnotations.Mock;*/
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
//import org.powermock.modules.junit4.PowerMockRunner;
/*import org.powermock.api.mockito.PowerMockito;*/
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

//import org.testng.annotations.BeforeMethod;
import org.junit.Before;
import org.junit.Test;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.macys.rds.msg.config.MqConfig;
import com.macys.rds.msg.Application;


//import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Value;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.jms.Session;
import javax.jms.TextMessage;


//@RunWith(PowerMockRunner.class)
//@PrepareForTest({MqConfigTest.class})
//@SpringBootTest(classes = { Application.class })
public class MqConfigTest {
    private MqConfig mqconfig;
	private MQQueueConnectionFactory mQQueueconnectionfactory;
	Properties prop;
	Connection connection;
		
		
	@Before
    public void setUp() throws JMSException {
    	System.out.println("Test");	
    	
       	mqconfig = new MqConfig("AMD1DVIPA", "1417", "AMD1"); 
    	mQQueueconnectionfactory = mqconfig.mqQueueConnectionFactory();    	
    	try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            prop = new Properties();
            // load a properties file
            prop.load(input);            
    	} catch (IOException ex) {
            ex.printStackTrace();
        }      	
    	
    	connection = mqconfig.mqConnection();
    //	Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    //	TextMessage textmessage = session.createTextMessage("Hello Nagesh");
    //	jmsserviceimpl = new JmsServiceImpl();
    	
    	//System.out.println("connection client : " + connection.);
    }
          
    //@Test
    public void testMqConfig()
    {
    	System.out.println("mQQueueconnectionfactory HostName1 :" + mQQueueconnectionfactory.getHostName());
    	Assert.assertTrue(mQQueueconnectionfactory.getHostName().equalsIgnoreCase(prop.getProperty("mq.hostname")));
    	Assert.assertEquals(mQQueueconnectionfactory.getPort(), Integer.parseInt(prop.getProperty("mq.port")));
    	Assert.assertTrue(mQQueueconnectionfactory.getQueueManager().equalsIgnoreCase(prop.getProperty("mq.queueManager")));
    	Assert.assertTrue(connection instanceof Connection);
    	
    }
}
