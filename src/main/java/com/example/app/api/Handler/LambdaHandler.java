package com.example.app.api.Handler;

import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.Map;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.app.App;
import com.example.app.service.ServiceTrigger;
import software.amazon.awssdk.services.s3.S3AsyncClient;


// The implements RequestHandler<Map<String, Object>, Object> is what alows us to accept the request message and then manipulate the body of the request from api gateway
public class LambdaHandler implements RequestHandler<Map<String, Object>, Object> {
    private final ApplicationContext context;
    private final S3AsyncClient s3Client;
    private ServiceTrigger serviceTrigger;

    public LambdaHandler() {
        this.context = new SpringApplicationBuilder(App.class)
                    .web(WebApplicationType.NONE)
                    .run();
        s3Client = DependencyFactory.s3Client();
        this.serviceTrigger = context.getBean(ServiceTrigger.class); // If we need to call additional methods we can add additional classes here
    }

    //final Map<String, Object> input is what allows us to Accept the request from api gateway and allows us to leverage the request details in our app by referencing input
    @Override
    public Object handleRequest(final Map<String, Object> input, final Context context) {
        try{
        return serviceTrigger.TriggerService(input);
        } catch (Exception e){
            return "Fatal Error Occured on Entrypoint: " + e.getMessage() + e;
        }
    }
}
