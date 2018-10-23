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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;


public class MyActionsAppTest {

  private static String fromFile(String fileName) throws IOException {
    Path absolutePath = Paths.get("src", "test", "resources",
            fileName);
    return new String(Files.readAllBytes(absolutePath));
  }

  @Test
  public void testDefaultWelcomeIntent() throws Exception {

    // Mock a URL object that will return the contents of a file instead
    // of making an HTTP request.
    URL mockUrl = Mockito.mock(URL.class);
    URLConnection mockConnection = Mockito.mock(URLConnection.class);
    String dataString = fromFile("data.json");
    InputStream dataStream = new ByteArrayInputStream(dataString.getBytes());
    Mockito.when(mockConnection.getContent()).thenReturn(dataStream);
    Mockito.when(mockUrl.openConnection()).thenReturn(mockConnection);

    // Use our secondary constructor that allows us to specify a URL object.
    MyActionsApp app = new MyActionsApp(mockUrl);
    String requestBody = fromFile("request_welcome.json");

    CompletableFuture<String> future = app.handleRequest(requestBody,
            null /* headers */);

    String responseJson = future.get();

    // Get response, then pretty-print the JSON so we can compare it with our
    // pretty-printed sample output (in a human-readable way)
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    JsonParser parser = new JsonParser();
    JsonElement element = parser.parse(responseJson);
    String prettyJsonResponse = gson.toJson(element);

    String expectedResponse = fromFile("response_welcome.json");

    Assert.assertEquals("Response was not as expected", prettyJsonResponse, expectedResponse);
    System.out.println("Actions response = " + responseJson);
  }
}
