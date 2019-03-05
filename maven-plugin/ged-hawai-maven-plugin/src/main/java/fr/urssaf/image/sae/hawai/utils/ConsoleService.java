package fr.urssaf.image.sae.hawai.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;


public class ConsoleService {

    private static final int FIRST_COLONNE_SIZE = 11;

    private Log mavenLog;

    public ConsoleService(Log log) {
        super();
        mavenLog = log;
    };

    public void displayCategorie(String msg) {
        System.out.println("");
        System.out.println(msg + ":");
    }

    public void display(String msg, String type) {
        String finalType = "[" + (StringUtils.isNotBlank(type) ? type : "echo") + "]";
        String decalage = "";
        for (int i = 1; i <= FIRST_COLONNE_SIZE - finalType.length(); i++) {
            decalage = decalage + " ";
        }
        String msgToDisplay = decalage + finalType + " " + msg;
        System.out.println(msgToDisplay);
    }

    public void display(String msg) {
        display(msg, "echo");
    }

    public void debug(String msg) {
        if (mavenLog != null && mavenLog.isDebugEnabled()) {
            mavenLog.debug(msg);
        }
    }
}
