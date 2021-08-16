package com.example;

import com.google.actions.api.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/actions")
public class ActionsSpringBootController {
    private static final Logger LOG = LoggerFactory.getLogger(ActionsServlet.class);
    private final App actionsApp = new MyActionsApp();

    @PostMapping()
    protected String post(@RequestHeader Map<String, String> headers, @RequestBody String body) {

        LOG.info("doPost, body = {}", body);

        try {
            String jsonResponse = actionsApp.handleRequest(body, headers).get();
            LOG.info("Generated json = {}", jsonResponse);
            System.out.println("Generated json = {} " + jsonResponse);
            return jsonResponse;
        } catch (InterruptedException | ExecutionException e) {
            handleError(e);
        }

        return null;
    }

    private String handleError(Throwable throwable) {
        return ("Error handling the intent - " + throwable.getMessage());
    }

}
