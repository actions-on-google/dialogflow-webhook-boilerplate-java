/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.actions.api.App;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Handles request received via AWS - API Gateway [proxy integration](https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html)
 * and delegates to your Actions App.
 */
public class ActionsAWSHandler implements RequestStreamHandler {
  // Replace this with your webhook.
  private final App actionsApp = new MyActionsApp();
  private final JSONParser parser = new JSONParser();

  @Override
  public void handleRequest(InputStream inputStream,
                            OutputStream outputStream,
                            Context context) throws IOException {
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream));
    JSONObject awsResponse = new JSONObject();
    LambdaLogger logger = context.getLogger();
    try {
      JSONObject awsRequest = (JSONObject) parser.parse(reader);
      JSONObject headers = (JSONObject) awsRequest.get("headers");
      String body = (String) awsRequest.get("body");
      logger.log("AWS request body = " + body);

      actionsApp.handleRequest(body, headers)
              .thenAccept((webhookResponseJson) -> {
                logger.log("Generated json = " + webhookResponseJson);

                JSONObject responseHeaders = new JSONObject();
                responseHeaders.put("Content-Type", "application/json");

                awsResponse.put("statusCode", "200");
                awsResponse.put("headers", responseHeaders);
                awsResponse.put("body", webhookResponseJson);
                writeResponse(outputStream, awsResponse);
              }).exceptionally((throwable -> {
        awsResponse.put("statusCode", "500");
        awsResponse.put("exception", throwable);
        writeResponse(outputStream, awsResponse);
        return null;
      }));

    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private void writeResponse(OutputStream outputStream, JSONObject responseJson) {
    try {
      OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
      writer.write(responseJson.toJSONString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
