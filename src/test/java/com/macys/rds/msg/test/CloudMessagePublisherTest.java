package com.macys.rds.msg.test;

import com.macys.rds.msg.publisher.CloudMessagePublisher;

import java.util.HashMap;
import java.util.Map;


import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Publisher.Builder;
import com.google.protobuf.ByteString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import com.macys.rds.msg.Application;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.MockitoAnnotations.Mock;
import org.mockito.Mock;
import com.google.pubsub.v1.PubsubMessage;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;

import static org.mockito.Mockito.when;

//@PrepareForTest({CloudMessagePublisherTest.class})
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({CloudMessagePublisherTest.class})
//@SpringBootTest(classes = { Application.class })
@RunWith(MockitoJUnitRunner.class)
public class CloudMessagePublisherTest {
	
	private Map<String, String> hdrs = new HashMap<String, String>();
	
	//@Mock
	//private Builder builder;
	
	//@Spy
	//private Publisher publisher = new Publisher(Builder);
	
	@Mock
	private Publisher publisher;
	
	private PubsubMessage pubsubmessage;
	private ApiFuture<String> future = ApiFutures.immediateFuture(null); 
	
	@Before
    public void setUp()   {
		
		MockitoAnnotations.initMocks(this);
		hdrs.put("correlationId","19999"); 	
		pubsubmessage = PubsubMessage.newBuilder()
				.setData(ByteString.copyFromUtf8("Hello World"))			
				.putAttributes("correlationId",  "1046")
				.build();
		System.out.println("Test One");
	}
	
	 @Test
	 public void testCloudPublish() throws Exception
	 {
		//Mockito.spy(CloudMessagePublisher.class); 
		//Mockito.doReturn(null).when(publisher).publish(pubsubmessage);
		
		 //when(publisher.publish(pubsubmessage)).thenReturn(future);
		 //when(publisher.publish(org.mockito.Matchers.any(PubsubMessage.class))).thenReturn(null);
		 System.out.println("Test two");
		 CloudMessagePublisher.publish("mtech-wms-oms-nonprod","btinventory_event_dev", "Hello world", hdrs, 5);
	 }

}
