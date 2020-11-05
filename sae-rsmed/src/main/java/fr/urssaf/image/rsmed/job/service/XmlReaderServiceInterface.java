package fr.urssaf.image.rsmed.job.service;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;

import javax.xml.stream.XMLStreamException;

public interface XmlReaderServiceInterface {

    CurrentDocumentBean getNextDocument() throws XMLStreamException;
}
