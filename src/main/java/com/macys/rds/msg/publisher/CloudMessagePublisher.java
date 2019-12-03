package com.macys.rds.msg.publisher;


import com.google.api.core.ApiFuture;
import com.google.api.gax.retrying.RetrySettings;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.threeten.bp.Duration;

public class CloudMessagePublisher {
	
		
	static Logger logger = Logger.getLogger(CloudMessagePublisher.class);
	//private static String PROJECT_ID = "mtech-wms-oms-nonprod";
	//private static String PROJECT_ID = PropertyManager.get().getProperty("google.PROJECT_ID");
	//private static String PROJECT_ID = configproperties.getPROJECT_ID();
	
	private static HashMap<String, Publisher> publisherMap = new HashMap<String, Publisher>();	
	
	public static void publish(String PROJECT_ID, String topicId, String message,Map<String,String> headers, int retryAttempts) throws Exception {
		ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
		Publisher publisher = publisherMap.get(topicId);
		//String orderId = headers.get("orderId");
		String correlationId = headers.get("correlationId");

		//try {
		if(publisher == null) {
			synchronized(CloudMessagePublisher.class) {
				publisher = publisherMap.get(topicId);
				if(publisher == null) {
					Duration retryDelay = Duration.ofSeconds(retryAttempts); // default : 1 ms
					double retryDelayMultiplier = 1.0; // back off for repeated failures
					Duration maxRetryDelay = Duration.ofSeconds(retryAttempts * 2); // default : 10 seconds
					Duration totalTimeout = Duration.ofSeconds(retryAttempts * 10); // default: 0
					Duration initialRpcTimeout = Duration.ofSeconds(retryAttempts); // default: 0
					Duration maxRpcTimeout = Duration.ofSeconds(retryAttempts*3); // default: 0

					RetrySettings retrySettings =
							RetrySettings.newBuilder()
							.setMaxAttempts(retryAttempts)
							.setInitialRetryDelay(retryDelay)
							.setRetryDelayMultiplier(retryDelayMultiplier)
							.setMaxRetryDelay(maxRetryDelay)
							.setTotalTimeout(totalTimeout)
							.setInitialRpcTimeout(initialRpcTimeout)
							.setMaxRpcTimeout(maxRpcTimeout)
							.build();

					// Create a publisher instance with default settings bound to the topic
					publisher = Publisher.newBuilder(topicName).setRetrySettings(retrySettings).build();
					//publisher = Publisher.newBuilder(topicName).build();
					publisherMap.put(topicId, publisher);
				}
			}
		}
		// convert message to bytes
		ByteString data = ByteString.copyFromUtf8(message);
		PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
				.setData(data)
			//	.putAttributes("orderId", orderId)
				.putAttributes("correlationId",  correlationId)
				.build();


		// Schedule a message to be published. Messages are automatically batched.
		ApiFuture<String> future = publisher.publish(pubsubMessage);
		logger.info("Message Monitor: " + " correlationId :"+correlationId+ " topic :"+topicName+ " Message Id : "+ future.get());
    
  }
	
	

}
