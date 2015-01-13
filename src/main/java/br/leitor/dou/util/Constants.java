package br.leitor.dou.util;

import java.io.File;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

public class Constants {

	/**
	 * Inicializa variáveis 
	 */
	public static void initVariables() {
        String logdir;
        
        try {
            
            logdir = System.getProperty("log.dir");
            
            if (Util.isNull(logdir)) {
                logdir = System.getProperty("user.home");
            } else {
                File ldir;
                
                ldir = new File(logdir);
                
                if (!ldir.exists()) {
                    ldir.mkdirs();
                } 
            } 
        } catch (Exception ex) {
            logdir = System.getProperty("user.home");
        }
        
        @SuppressWarnings("rawtypes")
		Enumeration e = Logger.getRootLogger().getAllAppenders();
        while (e.hasMoreElements()) {
        	Appender app = (Appender)e.nextElement();
        	if (app instanceof FileAppender) {
        		logdir = ((FileAppender)app).getFile();
        	}
        }
        
        System.setProperty("log.dir", logdir);
	}
}
