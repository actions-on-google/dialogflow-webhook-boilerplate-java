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

import org.junit.Test;

import java.io.IOException;
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
  public void testWelcome() throws Exception {
    MyActionsApp app = new MyActionsApp();
    String requestBody = fromFile("request_welcome.json");

    CompletableFuture<String> future = app.handleRequest(requestBody,
            null /* headers */);

    String responseJson = future.get();
    System.out.println("Actions response = " + responseJson);
  }
}
