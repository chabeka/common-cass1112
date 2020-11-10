package fr.urssaf.image.rsmed.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties

public class PropertiesBean {

    @Value("${directory.workdir}")
    private String workdirDirectory;

    public String getWorkdirDirectory() {
        return workdirDirectory;
    }

    public void setWorkdirDirectory(String workdirDirectory) {
        this.workdirDirectory = workdirDirectory;
    }

}