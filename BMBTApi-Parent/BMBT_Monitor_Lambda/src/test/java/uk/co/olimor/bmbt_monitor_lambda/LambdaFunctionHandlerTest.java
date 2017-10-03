package uk.co.olimor.bmbt_monitor_lambda;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

    private static MonitorInput input;

    @BeforeClass
    public static void createInput() throws IOException { 
        input = new MonitorInput();
        input.setConnectionString("bmbtinstance-test.cabidcrldsms.us-east-1.rds.amazonaws.com");
        input.setDbUserName("lcmorley");
        input.setDbPassword("%lCm0rl3y50%");
        input.setAppPassword("0l1m0rPa55w0rd!");
        input.setAppUserName("bmbt0l1m0rUs3r!");
        input.setSchemaName("bmbtschema");
        input.setSecret("0l1m0rS3cr3t!");
        input.setContextRoot("http://bmbtapi-test.us-east-1.elasticbeanstalk.com");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testLambdaFunctionHandler() {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();
        Context ctx = createContext();

        String output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        Assert.assertEquals("Passed", output);
    }
}
