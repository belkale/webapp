package sample.app.internal;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Properties;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * User: belkale
 * Date: 8/17/13
 */
@Singleton
public class ConfigLoader implements Provider<Configuration> {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
    public static final String PROPS_FILE = "props.file";

    private final File propsFile;
    private final EventBus eventBus;
    private Configuration props;
    private final WatchService watchService;

    @Inject
    public ConfigLoader(@Named(PROPS_FILE) String filename, EventBus eventBus) throws IOException {
        Objects.requireNonNull(filename);
        Objects.requireNonNull(eventBus);
        this.propsFile = new File(filename);
        this.eventBus = eventBus;
        this.props = loadProperties();

        watchService = FileSystems.getDefault().newWatchService();
        final Path dir = FileSystems.getDefault().getPath(propsFile.getParentFile().getAbsolutePath());
        dir.register(watchService, ENTRY_MODIFY);

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                monitorFileAndUpdate();
            }
        });
        t.start();
    }

    private void monitorFileAndUpdate(){
        while (true) {
            WatchKey key;
            try {
                LOG.debug("Listening on changes to properties file " + propsFile.getPath());
                key = watchService.take();
            } catch (final InterruptedException e) {
                LOG.error("Received exception. Further modification events will not be propagated!", e);
                break;
            }
            for (final WatchEvent<?> event : key.pollEvents()) {
                final Path modified = (Path) event.context();
                LOG.info("Received event " + modified);
                if (modified.toString().equals(propsFile.getName())) {
                    try {
                        LOG.info("Configuration file changed on fs. Updating configuration...");
                        this.props = loadProperties();
                        eventBus.post(props);
                    } catch (final IOException e) {
                        LOG.error("Unable to update configuration, will try again, in a whule", e);
                        LOG.error("Sleeping for 5 secs");
                        try {
                            Thread.sleep(5000);
                        } catch (final InterruptedException e1) {}
                    }
                }

                if (!key.reset()) {
                    LOG.error("Could not reset watch key. Further modifications will not be propagated!");
                    break;
                }
            }
        }
    }

    private Configuration loadProperties() throws IOException {
        Properties props = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(propsFile))) {
            props.load(reader);
        }
        return new Configuration(props);
    }

    @Override
    public Configuration get() {
        return props;
    }
}
