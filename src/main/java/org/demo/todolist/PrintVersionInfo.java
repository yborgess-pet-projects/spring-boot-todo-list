package org.demo.todolist;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An Application Listener to print the application version as soon as the Application Context is ready.
 */
public class PrintVersionInfo implements ApplicationListener<ApplicationPreparedEvent> {
    private static final Logger logger = Logger.getLogger(PrintVersionInfo.class.getName());

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        ConfigurableEnvironment environment = context.getEnvironment();
        String applicationVersion = environment.getProperty("org.demo.todolist.app-version", "UNKNOWN");
        logger.log(Level.INFO, "Application Version: " + applicationVersion);
    }
}
