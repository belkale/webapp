package sample.app.internal;

import com.google.common.eventbus.Subscribe;

/**
 * User: belkale
 * Date: 8/17/13
 */
public interface DynamicConf {
    @Subscribe
    public void updateProperties(Configuration conf);
}
