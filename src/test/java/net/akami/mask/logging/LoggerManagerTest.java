package net.akami.mask.logging;

import net.akami.mask.handler.Adder;
import net.akami.mask.handler.BinaryOperationHandler;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class LoggerManagerTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoggerManagerTest.class);

    @Test
    public void basicLogging() {
        LOGGER.info("Message");
    }
    @Test
    public void disableWorks() {
        LOGGER.info("First test : should be printed !");
        LOGGER.debug("Should not be printed");
        LoggerManager.setLevel(Level.OFF, true, LoggerManagerTest.class);
        LOGGER.info("Second test : should not be printed !");
        LoggerManager.setLevel(Level.DEBUG, false, LoggerManagerTest.class);
        LOGGER.debug("Should be printed");
    }
}
