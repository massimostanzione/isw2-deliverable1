package it.uniroma2.dicii.isw2.deliverable1.utils;

import java.util.Date;
import java.util.logging.*;

/**
 * Unique logger for the whole project.
 * Implemented with the <i>singleton</i> design pattern.
 */
public class LoggerInst {
    private static Logger instance = null;

    private LoggerInst() {
        instance = Logger.getLogger(this.getClass().getName());
        Handler handlerObj = new ConsoleHandler();

        // Set to Level.FINE to obtain more verbose log
        handlerObj.setLevel(Level.FINE);
        handlerObj.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
                        lr.getMessage());
            }
        });
        instance.addHandler(handlerObj);
        instance.setLevel(Level.ALL);
        instance.setUseParentHandlers(false);
    }

    public static Logger getSingletonInstance() {
        if (instance == null) {
            new LoggerInst();
        }
        return instance;
    }
}