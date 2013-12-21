package sample.app.service.impl;

import junit.framework.Assert;
import org.testng.annotations.Test;
import sample.app.internal.Configuration;

import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * User: belkale
 * Date: 8/17/13
 */
public class HelloServiceImplTest {
    @Test
    public void testSayHello() throws Exception {
        Properties props = new Properties();
        props.setProperty(HelloServiceImpl.Config.DEFAULT_MESSAGE, "good morning!");
        HelloServiceImpl hsi = new HelloServiceImpl(new HelloServiceImpl.Config(new Configuration(props)),
                Executors.newSingleThreadScheduledExecutor());
        hsi.start();
        String name = "user1";
        Assert.assertTrue(hsi.sayHello(name).contains(name));
        hsi.stop();
    }
}
