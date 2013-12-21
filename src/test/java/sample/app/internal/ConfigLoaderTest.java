package sample.app.internal;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

/**
 * User: belkale
 * Date: 8/17/13
 */
public class ConfigLoaderTest {
    private static final String KEY = "name";

    private static class PropsStorer{
        private String value;
        public PropsStorer(Configuration props){
            updateConf(props);
        }
        @Subscribe
        public void updateConf(Configuration props){
            value = props.getProperty(KEY);
        }

        private String getValue() {
            return value;
        }
    }

    //@Test this test does not work
    //What I have observed is that if we make the changes externally, it works properly. For eg, setting a sleep of 20 secs
    //and modifying the file manually works. But programmatically, it fails.
    public void test() throws Exception {
        String filename = "target/test.properties";

        String value = "test1";
        Properties props = new Properties();
        props.setProperty(KEY, value);

        File propsFile = new File(filename);
        propsFile.deleteOnExit();

        try(FileWriter writer = new FileWriter(propsFile)){
            props.store(writer, "test properties");
        }

        PropsStorer propsStorer = new PropsStorer(new Configuration(props));
        Assert.assertEquals(propsStorer.getValue(), value);

        EventBus eventBus = new EventBus();
        eventBus.register(propsStorer);

        ConfigLoader configLoader = new ConfigLoader(filename, eventBus);
        Assert.assertEquals(configLoader.get().getProperty(KEY), value);

        props.setProperty(KEY, "test2");
        try(FileWriter writer = new FileWriter(propsFile)){
            props.store(writer, "test properties");
        }

        //Thread.sleep(20000);
        Assert.assertEquals(propsStorer.getValue(), "test2");
    }
}
