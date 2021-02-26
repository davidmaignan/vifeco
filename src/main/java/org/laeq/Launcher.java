package org.laeq;

import griffon.javafx.JavaFXGriffonApplication;
import org.laeq.model.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Application main class
 */
public class Launcher {
    /**
     * Entry point
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try{
            setUp();
            JavaFXGriffonApplication.main(args);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Create the application vifeco structure in $HOME.
     * @throws Exception
     */
    private static void setUp() throws Exception{
        String[] paths = Settings.paths;
        for(int i = 0; i < paths.length; i++) {
            String pathStr = paths[i];
            Path path = Paths.get(pathStr);
            if(! Files.exists(path)){
                Files.createDirectories(path);
            }
        }
    }
}