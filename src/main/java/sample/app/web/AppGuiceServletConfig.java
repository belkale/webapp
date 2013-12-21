package sample.app.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.app.service.Service;
import sample.app.service.impl.HelloServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * User: belkale
 * Date: 8/18/13
 */
public class AppGuiceServletConfig extends GuiceServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(AppGuiceServletConfig.class);
    private Injector injector;
    private ServletContext servletContext;
    @Override
    protected Injector getInjector() {
        String propFile = servletContext.getInitParameter("propfile");
        this.injector = Guice.createInjector(new AppModule(propFile));
        return injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
        if(injector != null){
            LOG.info("Starting up services");
            try{
                Service helloService = injector.getInstance(HelloServiceImpl.class);
                helloService.start();

            }catch(Exception e){
                throw new RuntimeException("Failed to start service." + e.getMessage(), e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        if(injector != null){
            LOG.info("Stopping services");
            Service helloService = injector.getInstance(HelloServiceImpl.class);
            helloService.stop();
        }
    }
}
