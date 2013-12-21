package sample.app.internal;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;

/**
 * User: belkale
 * Date: 8/18/13
 */
public class ConfigurationTest {

    @Test
    public void test(){
        Properties props = new Properties();
        props.setProperty("string", "hello");
        props.setProperty("integer", "10");
        props.setProperty("boolean", "true");
        //space will be treated as non-existent
        props.setProperty("space", " ");

        Configuration conf = new Configuration(props);
        Assert.assertEquals(conf.getProperty("string"), "hello");
        Assert.assertEquals(conf.getProperty("string", "abc"), "hello");
        Assert.assertEquals(conf.getProperty("nonexistent"), "");
        Assert.assertEquals(conf.getProperty("nonexistent", "abc"), "abc");

        Assert.assertEquals(conf.getProperty("space"), "");
        Assert.assertEquals(conf.getProperty("space", "abc"), "abc");

        Assert.assertEquals(conf.getIntProperty("integer", 0), 10);
        Assert.assertEquals(conf.getIntProperty("nonexistent", 15), 15);

        Assert.assertEquals(conf.getBoolProperty("boolean", false), true);
        Assert.assertEquals(conf.getBoolProperty("nonexistent", false), false);
        Assert.assertEquals(conf.getBoolProperty("nonexistent", true), true);
    }
}
