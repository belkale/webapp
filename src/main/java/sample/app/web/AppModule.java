package sample.app.web;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import sample.app.internal.*;
import sample.app.service.HelloService;
import sample.app.service.impl.HelloServiceImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * User: belkale
 * Date: 8/18/13
 */
public class AppModule extends ServletModule {
    private final String propFile;
    public AppModule(String propFile){
        this.propFile = propFile;
    }
    @Override
    protected void configureServlets() {
        EventBus eventBus = new EventBus();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        bind(EventBus.class).toInstance(eventBus);
        bind(ScheduledExecutorService.class).toInstance(executorService);

        bind(String.class).annotatedWith(Names.named(ConfigLoader.PROPS_FILE)).toInstance(propFile);
        bind(Configuration.class).toProvider(ConfigLoader.class);
        bind(HelloServiceImpl.Config.class);
        bind(HelloService.class).to(HelloServiceImpl.class);

        bind(HelloServlet.class);

        bindListener(CustomMatchers.subclassesOf(DynamicConf.class), new EventTypeListener(eventBus));

        serve("/hello").with(HelloServlet.class);
    }
}
