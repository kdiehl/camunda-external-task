import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;

public class TweetRejectionWorker {
	public static void main(String[] args) {
		ExternalTaskClient client = ExternalTaskClient.create()
				.baseUrl("http://localhost:8080/engine-rest")
				.asyncResponseTimeout(20000)
				.lockDuration(10000)
				.maxTasks(1)
				.build();

		TopicSubscriptionBuilder subscriptionBuilder = client.subscribe("tweetRejection");
		subscriptionBuilder.handler((externalTask, externalTaskService) -> {
			String content = externalTask.getVariable("content");
			System.out.println("Sorry your tweet has been rejected! " + content);
			Map<String, Object> variables = new HashMap<>();
			variables.put("notificationTimestamp", new Date());
			externalTaskService.complete(externalTask, variables);
		});
		subscriptionBuilder.open();
	}
}
