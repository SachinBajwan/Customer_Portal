package terms.erp.mainapp.view.listener;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import terms.erp.mainapp.view.utils.DBConfiguration;

public class CustomContextListener implements ServletContextListener {

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    public CustomContextListener() {
        super();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // TODO Implement this method
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // TODO Implement this method
    }
}
