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

import com.google.actions.api.App;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles request received via HTTP POST and delegates it to your Actions app. See: [Request
 * handling in Google App
 * Engine](https://cloud.google.com/appengine/docs/standard/java/how-requests-are-handled).
 */
@WebServlet(name = "actions", value = "/")
public class ActionsServlet extends HttpServlet {
  private static final Logger LOG = LoggerFactory.getLogger(ActionsServlet.class);
  private final App actionsApp = new MyActionsApp();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String body = req.getReader().lines().collect(Collectors.joining());
    LOG.info("doPost, body = {}", body);

    try {
      String jsonResponse = actionsApp.handleRequest(body, getHeadersMap(req)).get();
      LOG.info("Generated json = {}", jsonResponse);
      res.setContentType("application/json");
      writeResponse(res, jsonResponse);
    } catch (InterruptedException e) {
      handleError(res, e);
    } catch (ExecutionException e) {
      handleError(res, e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/plain");
    response
        .getWriter()
        .println(
            "ActionsServlet is listening but requires valid POST request to respond with Action response.");
  }

  private void writeResponse(HttpServletResponse res, String asJson) {
    try {
      res.getWriter().write(asJson);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleError(HttpServletResponse res, Throwable throwable) {
    try {
      throwable.printStackTrace();
      LOG.error("Error in App.handleRequest ", throwable);
      res.getWriter().write("Error handling the intent - " + throwable.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Map<String, String> getHeadersMap(HttpServletRequest request) {
    Map<String, String> map = new HashMap();

    Enumeration headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String key = (String) headerNames.nextElement();
      String value = request.getHeader(key);
      map.put(key, value);
    }
    return map;
  }

}
