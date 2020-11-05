package fr.urssaf.image.rsmed.job.service.impl;


import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.job.service.XmlReaderServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


@Service
public class XmlReaderServiceImpl implements XmlReaderServiceInterface {

    private static Logger LOGGER = LoggerFactory.getLogger(XmlReaderServiceImpl.class);


    @Autowired
    CurrentDocumentBean currentDocumentBean;

    public static XMLEventReader reader;

    public static XMLEventReader getReader() {
        return reader;
    }

    public static void setReader(XMLEventReader reader) {
        XmlReaderServiceImpl.reader = reader;
    }

    public CurrentDocumentBean getNextDocument() throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "ID_V2":
                        nextEvent = reader.nextEvent();
                        currentDocumentBean.setIdV2(nextEvent.asCharacters().getData());
                        break;
                    case "DATE_R1":
                    case "DATE_R2":
                        nextEvent = reader.nextEvent();
                        //dateCreation = nextEvent.asCharacters().getData();
                        break;
                    case "NB_PAGES":
                        nextEvent = reader.nextEvent();
                        currentDocumentBean.setNbPage(Integer.parseInt(nextEvent.asCharacters().getData()));
                        break;
                    case "MED":
                    case "RAR":
                        currentDocumentBean.reset();
                        break;
                    case "PDF_MED":
                    case "PDF_AR_PND":
                        nextEvent = reader.nextEvent();
                        currentDocumentBean.setPdfName(nextEvent.asCharacters().getData());
                        break;
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                switch (endElement.getName().getLocalPart()) {
                    case "MED":
                    case "RAR":
                        reader.nextEvent();
                        return currentDocumentBean;
                    case "PRESTATAIRE_RSI_R1":
                    case "PRESTATAIRE_RSI_R2":
                        currentDocumentBean = null;
                        return null;
                    default:
                        break;
                }
            }
        }

        return null;
    }
}
