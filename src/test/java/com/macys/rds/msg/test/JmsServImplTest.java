package com.macys.rds.msg.test;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.testng.Assert;

import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;

import javax.jms.BytesMessage;
//import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
//import javax.jms.Session;

//import org.testng.annotations.BeforeMethod;
import org.junit.Before;
import org.junit.Test;

import com.ibm.jms.JMSTextMessage;
//import com.ibm.mq.jms.MQQueueConnectionFactory;
//import com.macys.d2c.msp.bigTicketMQ.config.MqConfig;
import com.macys.rds.msg.publisher.CloudMessagePublisher;
import com.macys.rds.msg.service.impl.JmsServiceImpl;

import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import com.macys.rds.msg.Application;
import org.springframework.beans.factory.annotation.Value;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.mockito.stubbing.Answer;

import com.mockrunner.mock.jms.MockTextMessage;




//@PrepareForTest({JmsServImplTest.class})

@RunWith(PowerMockRunner.class)
@PrepareForTest({CloudMessagePublisher.class, JmsServImplTest.class})
//@PrepareForTest({CloudMessagePublisher.class})
//@SpringBootTest(classes = { Application.class })
public class JmsServImplTest {
   
	//private MqConfig mqconfig;
	//private MQQueueConnectionFactory mQQueueconnectionfactory;
	//private Connection connection;
	//@Mock
	//private JmsTemplate jmsTemplate;
	
	//@Mock
	private TextMessage textmessage;
	
	//@Mock
	//private Session session;
	
	private static byte[] dummyByteArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	
	MockTextMessage txtmsg = new MockTextMessage("Hello world");
	
	@Before
    public void setUp() throws JMSException  {
    	System.out.println("Test");	
    	   	
	}
		
	          
    @Test
    public void testServ() throws Exception
    {

    	JmsServiceImpl jmsServImpl = new JmsServiceImpl("mtech-wms-oms-nonprod", "btinventory_event_dev");
    	PowerMockito.spy(CloudMessagePublisher.class);
    
    	PowerMockito.doNothing().when(CloudMessagePublisher.class, "publish", Mockito.isA(String.class), Mockito.isA(String.class), Mockito.isA(String.class), Mockito.isA(HashMap.class), Mockito.isA(Integer.class));
    	System.out.println("Test do nothing");
    	
    	BytesMessage message = mockMQMessage(dummyByteArray);
    	//Message msg = (Message)message;
    	
    	Message msg = (Message)txtmsg;
    	    	
    	jmsServImpl.receiveMessage(msg);
    	
    	Assert.assertEquals(jmsServImpl.getHdrs().get("correlationId"), "18989");
    	
      }
    
    //Method to generate sample BytesMessage
    protected BytesMessage mockMQMessage(byte[] mockByteArray) throws JMSException {
        if (mockByteArray == null) {
            mockByteArray = dummyByteArray;
        }

        BytesMessage message = Mockito.mock(BytesMessage.class);

        // The Code Under Test needs these from the mocked BytesMessage:
        // byte[] data = new byte[(int) bytesMessage.getBodyLength()];
        // bytesMessage.readBytes(data);
        // Set getJMSMessage() on the message object of suffer headaches from this code base
        
        final byte[] finalMockByteArray = mockByteArray;
        
        //final byte[] destinationByteArray;
        //System.arraycopy(finalMockByteArray, 0, destinationByteArray, 0,
        //        finalMockByteArray.length);
              
        
        Answer<?> answer = invocation -> {
            final byte[] destinationByteArray = (byte[])invocation.getArguments()[0];
            System.arraycopy(finalMockByteArray, 0, destinationByteArray, 0,
                finalMockByteArray.length);
            return null;
        };

        
        Mockito.when(message.getJMSMessageID()).thenReturn("TestId");
        Mockito.when(message.getBodyLength()).thenReturn(new Long(mockByteArray.length));
        Mockito.when(message.readBytes(Mockito.any())).then(answer);
        return message;
    }


}
