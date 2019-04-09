package net.akami.mask.logging;

import org.junit.Test;
import org.slf4j.Logger;

public class LoggerManagerTest {

    @Test
    public void enableWorks() {

        Logger logger = LoggerManager.LOGGER;
        logger.info("Test");
        LoggerManager.disable();
        logger.info("Test");
    }
}
