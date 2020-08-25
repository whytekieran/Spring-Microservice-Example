package com.whyte.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class MovieCatalogServiceApplication {

	/*Instead of declaring an instance of RestTemplate inside the methods for our end points we create a bean. This way
	the object is shared instead of a new instance being created for every request. A bean can be created
	like below or via XML file. The we use @Autowired to inject where we need it. Must be method like below when
	using @bean because its a method level annotation. @Bean can then be used anywhere and is managed by application context*/
	@Bean
	/*Here we are setting up service discovery. The @LoadBalanced annotation says "Hey instead of calling the service to get the
	 * address and then send back to me so i can use it, just call the service directly each time by giving the name."*/
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		//We can set a timeout here. Very important to have timeouts for fault tolerance, we do not want slow services backing up threads.
		HttpComponentsClientHttpRequestFactory clientRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientRequestFactory.setConnectTimeout(3000);
		return new RestTemplate(clientRequestFactory);
	}
	
	//Another bean for WebClient
	/*@Bean
	public WebClient.Builder getWebclientBuilder() {
		return WebClient.builder();
	}*/
	
	//Main method runs here, starts the spring application context.
	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
