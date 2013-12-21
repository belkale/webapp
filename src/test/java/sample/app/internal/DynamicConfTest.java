package sample.app.internal;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.Assert;
import org.testng.annotations.Test;
import sample.app.service.impl.HelloServiceImpl;

import java.util.Properties;

/**
 * User: belkale
 * Date: 8/17/13
 */
public class DynamicConfTest {
    @Test
    public void test(){
        final EventBus eventBus = new EventBus();
        final Properties props = new Properties();
        String msg1 = "good morning!";
        String msg2 = "good evening!";

        props.setProperty(HelloServiceImpl.Config.PROP_MESSAGE, msg1);
        final Configuration conf = new Configuration(props);
        Injector injector = Guice.createInjector(new AbstractModule(){
            @Override
            protected void configure() {
                bind(EventBus.class).toInstance(eventBus);
                bind(Configuration.class).toInstance(conf);
                bind(HelloServiceImpl.Config.class);

                bindListener(CustomMatchers.subclassesOf(DynamicConf.class), new EventTypeListener(eventBus));
            }
        });

        HelloServiceImpl.Config config = injector.getInstance(HelloServiceImpl.Config.class);
        Assert.assertEquals(config.getMessage(), msg1);

        props.setProperty(HelloServiceImpl.Config.PROP_MESSAGE, msg2);
        eventBus.post(conf);

        Assert.assertEquals(config.getMessage(), msg2);
    }
}
