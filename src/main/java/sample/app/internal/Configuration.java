package sample.app.internal;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * Wrapper class around property. Methods in this class never return NULL. If there is no value for this property, it returns
 * an empty string.
 * User: belkale
 * Date: 8/18/13
 */
public class Configuration {
    private static final String EMPTY_STRING = "";
    private final Properties props;
    public Configuration(Properties props){
        Objects.requireNonNull(props);
        this.props = props;
    }

    public String getProperty(String key){
        String val = props.getProperty(key);
        return (val == null)? EMPTY_STRING: val.trim();
    }

    public String getProperty(String key, String defaultVal){
        String val = getProperty(key);
        return val.isEmpty()? defaultVal: val;
    }

    public int getIntProperty(String key, int defaultVal){
        String val = getProperty(key);
        return val.isEmpty()? defaultVal: Integer.parseInt(val);
    }

    public boolean getBoolProperty(String key, boolean defaultVal){
        String val = getProperty(key);
        return val.isEmpty()? defaultVal: Boolean.valueOf(val);
    }

    public Set<String> getKeys(){
        return props.stringPropertyNames();
    }
}
