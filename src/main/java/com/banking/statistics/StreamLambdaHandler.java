package com.banking.statistics;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * AWS Lambda Handler for Spring Boot application
 * This class handles all incoming requests from API Gateway and routes them through Spring Boot
 */
public class StreamLambdaHandler extends FunctionInvoker implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        super.handleRequest(input, output, context);
    }

}