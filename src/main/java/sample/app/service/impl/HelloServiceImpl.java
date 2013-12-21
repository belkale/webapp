package sample.app.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.app.internal.Configuration;
import sample.app.internal.DynamicConf;
import sample.app.service.HelloService;
import sample.app.service.Service;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: belkale
 * Date: 8/17/13
 */
@Singleton
public class HelloServiceImpl implements HelloService, Service {
    private static final Logger LOG = LoggerFactory.getLogger(HelloServiceImpl.class);
    private final Config config;
    private final ScheduledExecutorService executorService;

    @Inject
    public HelloServiceImpl(Config config, ScheduledExecutorService executorService){
        Objects.requireNonNull(config);
        Objects.requireNonNull(executorService);
        this.config = config;
        this.executorService = executorService;
    }

    @Override
    public String sayHello(String name) {
        return "Hello " + name + ", " + config.getMessage();  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void start() throws Exception {
        LOG.info("Starting service " + HelloServiceImpl.class);
        executorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                LOG.info("Running scheduled service for HelloService");
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void stop(){
        LOG.info("Stopping service " + HelloServiceImpl.class);
    }

    public static class Config implements DynamicConf {
        public static final String PROP_MESSAGE = "message";
        public static final String DEFAULT_MESSAGE = "good morning!";
        private String message;

        @Inject
        public Config(Configuration props){
            Objects.requireNonNull(props);
            updateProperties(props);
        }

        @Override
        public void updateProperties(Configuration props){
            Objects.requireNonNull(props);
            setMessage(props.getProperty(PROP_MESSAGE, DEFAULT_MESSAGE));
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            LOG.info("Setting message:" + message);
            this.message = message;
        }
    }

}
