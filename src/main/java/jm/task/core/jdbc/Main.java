package jm.task.core.jdbc;

import jm.task.core.jdbc.util.Util;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Util.getConnection();
        Logger logger = Util.getLogger(Main.class);
        logger.severe("This is a severe log message");
        System.out.printf("\n\nLogger logger = %s", logger.getName());
    }
}
