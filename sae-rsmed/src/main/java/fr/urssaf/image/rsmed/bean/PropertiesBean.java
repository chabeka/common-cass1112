package fr.urssaf.image.rsmed.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "directory")

public class PropertiesBean {

    @Value("${directory.xml.input}")
    private String xmlFileInputDirectory;

    @Value("${directory.sommaire.output}")
    private String xmlSommaireOutputDirectory;

    public String getXmlFileInputDirectory() {
        return xmlFileInputDirectory;
    }

    public void setXmlFileInputDirectory(String xmlFileInputDirectory) {
        this.xmlFileInputDirectory = xmlFileInputDirectory;
    }

    public String getXmlSommaireOutputDirectory() {
        return xmlSommaireOutputDirectory;
    }

    public void setXmlSommaireOutputDirectory(String xmlSommaireOutputDirectory) {
        this.xmlSommaireOutputDirectory = xmlSommaireOutputDirectory;
    }
}