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

import java.util.concurrent.CompletableFuture;

public class MyActionsApp extends DialogflowApp {

  @ForIntent("welcome")
  public CompletableFuture<ActionResponse> welcome(ActionRequest request) {
    ResponseBuilder responseBuilder = getResponseBuilder();
    responseBuilder
            .add("Welcome to my app");
    return CompletableFuture.completedFuture(responseBuilder.build());
  }

  @ForIntent("bye")
  public CompletableFuture<ActionResponse> bye(ActionRequest request) {
    ResponseBuilder responseBuilder = getResponseBuilder();
    responseBuilder
            .add("Ok see you later.")
            .endConversation();
    return CompletableFuture.completedFuture(responseBuilder.build());
  }
}
