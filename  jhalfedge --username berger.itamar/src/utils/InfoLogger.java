package utils;

import contoller.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: Lilach
 * Date: 08/12/2010
 * Time: 14:21:47
 * To change this template use File | Settings | File Templates.
 */
public class InfoLogger{
    private   static InfoLogger infoLogger;

    private String path;

    private InfoLogger() {      
    }

    public void setModelPath(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public static InfoLogger get() {
        if (infoLogger == null){
                infoLogger = new InfoLogger();
        }
        return infoLogger;
    }

    public static void setOut(GLDisplay display) {
        display.addInfoLogListener(get());
    }
}
