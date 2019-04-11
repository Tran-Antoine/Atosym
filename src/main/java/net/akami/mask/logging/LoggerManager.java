package net.akami.mask.logging;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Enumeration;

/**
 * Class used to manage logging through the api. Allows logging disabling and logger selection by class type.
 */
public class LoggerManager {

    private LoggerManager(){}

    /**
     * Gets all the current loggers and sets their level to {@link Level#OFF}
     */
    public static void disableAll() {
        setLevelAll(Level.OFF);
    }

    /**
     * Sets all the current loggers' level to the level given
     * @param level the level given
     */
    public static void setLevelAll(Level level) {
        Enumeration<Logger> loggers = LogManager.getCurrentLoggers();
        while(loggers.hasMoreElements()) {
            Logger current = loggers.nextElement();
            current.setLevel(level);
        }
    }

    /**
     * Sets the given classes' logger to the level given.
     * @param level the level given
     * @param applyToSubclasses whether the subclasses' logger of the given classes are affected or not.
     *                          In other words, if you put the parameter to true and pass Object.class as the
     *                          clazz parameter, the result is the same as if you call {@link #setLevelAll(Level)}.
     * @param clazz the given classes
     */
    public static void setLevel(Level level, boolean applyToSubclasses, Class<?>... clazz) {
        for(Enumeration<Logger> loggers = LogManager.getCurrentLoggers(); loggers.hasMoreElements();) {
            Logger current = loggers.nextElement();

            for(Class<?> element : clazz) {
                try {
                    Class<?> loggerHandler = Class.forName(current.getName());
                    if(loggerHandler.equals(element) || (applyToSubclasses && element.isAssignableFrom(loggerHandler))) {
                        current.setLevel(level);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
