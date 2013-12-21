package sample.app.service;

/**
 * User: belkale
 * Date: 8/17/13
 */
public interface Service {
    /**
     * Starts the service. This method blocks until the service has completely started.
     */
    void start() throws Exception;

    /**
     * Stops the service. This method blocks until the service has completely shut down.
     */
    void stop();

}
