package fr.urssaf.image.rsmed.job.service;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.utils.SommaireWriterUtils;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface SommaireWriterServiceInterface {

    SommaireWriterUtils initSommaire() throws IOException, XMLStreamException;

    void writeNewDocument(CurrentDocumentBean currentDocumentBean) throws XMLStreamException;

    void closeSommaire() throws XMLStreamException;
}
