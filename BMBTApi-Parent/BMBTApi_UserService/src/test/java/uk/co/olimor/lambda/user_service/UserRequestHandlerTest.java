package uk.co.olimor.lambda.user_service;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.amazonaws.services.lambda.runtime.Context;

import uk.co.olimor.BMBTApi_Common.model.User;
import uk.co.olimor.BMBTApi_Common.requestmodel.ApiRequest;
import uk.co.olimor.BMBTApi_Common.response.ApiResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class UserRequestHandlerTest {

    private static ApiRequest input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = null;
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testUserRequestHandler() {
        UserRequestHandler handler = new UserRequestHandler();
        Context ctx = createContext();

        input = new ApiRequest();
        input.setEndpointName("getUser");
        input.setInput("15dbc342-bb37-4be2-8781-f8bbdab428ae");
        
        ResponseEntity<ApiResponse> output = handler.handleRequest(input, ctx);

        final User user = (User) output.getBody().object;
        // TODO: validate output here if needed.

        Assert.assertEquals("Eva", user.getFirstName());
    }
}
