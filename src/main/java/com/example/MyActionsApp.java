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

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;

import com.google.api.services.actions_fulfillment.v2.model.BasicCard;
import com.google.api.services.actions_fulfillment.v2.model.Image;
import com.google.api.services.actions_fulfillment.v2.model.SimpleResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.xml.internal.ws.util.CompletedFuture;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class MyActionsApp extends DialogflowApp {

  public static final String CONTENT_URL = "https://raw.githubusercontent.com/actions-on-google/dialogflow-quotes-nodejs/master/quotes.json";
  public static final String BACKGROUND_IMAGE = "https://lh3.googleusercontent.com/t53m5nzjMl2B_9Qhwc81tuwyA2dBEc7WqKPlzZJ9syPUkt9VR8lu4Kq8heMjJevW3GVv9ekRWntyqXIBKEhc5i7v-SRrTan_=s688";

  private URL url;

  protected MyActionsApp() {
    super();
  }

  // Used for unit testing with mock URL instance
  protected MyActionsApp(URL url) {
    super();
    this.url = url;
  }

  @ForIntent("Default Welcome Intent")
  public CompletableFuture<ActionResponse> defaultWelcomeIntent(ActionRequest request) {

    // Holds parsed JSON from HTTP call
    JsonElement root;
    // Used to build an AoG response
    ResponseBuilder responseBuilder = getResponseBuilder();

    try {
      // If we are unit testing, URL instance might have been provided already
      if(this.url == null) {
        this.url = new URL(this.CONTENT_URL);
      }

      // Make HTTP request and parse JSON response
      URLConnection quotesConnection = this.url.openConnection();
      quotesConnection.connect();

      JsonParser parser = new JsonParser();
      InputStreamReader reader = new InputStreamReader((InputStream) quotesConnection.getContent());

      root = parser.parse(reader);

    } catch (IOException ex) {
      System.out.println("Exception while fetching and parsing data from " + this.CONTENT_URL);
      System.out.println(ex.toString());

      // Return an empty response so that the intent's default response is used
      //TODO: test that this works
      return CompletableFuture.completedFuture(responseBuilder.build());
    }

    String info;
    String author;
    String quote;

    try {
      JsonObject rootObj = root.getAsJsonObject();
      info = rootObj.get("info").getAsString();
      JsonArray data = rootObj.getAsJsonArray("data");
      JsonObject randomAuthor = data.get((int) (Math.random() * data.size())).getAsJsonObject();
      JsonArray quotes = randomAuthor.getAsJsonArray("quotes");
      author = randomAuthor.get("author").getAsString();
      quote = quotes.get((int) (Math.random() * quotes.size())).getAsString();
    } catch (Exception ex) {
      System.out.println("Exception while accessing JSON data");
      System.out.println(ex.toString());

      // Return an empty response so that the intent's default response is used
      //TODO: test that this works
      return CompletableFuture.completedFuture(responseBuilder.build());
    }

    responseBuilder.add(new SimpleResponse()
            .setDisplayText(info)
            .setTextToSpeech(author + ", from Google Developer Relations once said... " + quote));

    if(request.hasCapability("actions.capability.SCREEN_OUTPUT")) {

      responseBuilder.add(new BasicCard()
              .setTitle(author + " once said...")
              .setFormattedText(quote)
              .setImage(new Image()
                      .setUrl(this.BACKGROUND_IMAGE)
                      .setAccessibilityText("DevRel Quote")));

    }

    return CompletableFuture.completedFuture(responseBuilder.build());
  }
}
