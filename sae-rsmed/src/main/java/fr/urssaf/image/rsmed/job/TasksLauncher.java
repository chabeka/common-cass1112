package fr.urssaf.image.rsmed.job;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.job.service.DocumentConstructorServiceInterface;
import fr.urssaf.image.rsmed.job.service.SommaireWriterServiceInterface;
import fr.urssaf.image.rsmed.job.service.XmlReaderServiceInterface;
import fr.urssaf.image.rsmed.job.service.impl.XmlReaderServiceImpl;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TasksLauncher {

    @Autowired
    PropertiesBean propertiesBean;

    @Autowired
    XmlReaderServiceInterface xmlReaderService;

    @Autowired
    SommaireWriterServiceInterface sommaireWriterService;

    @Autowired
    DocumentConstructorServiceInterface documentConstructorService;

    private static Logger LOGGER = LoggerFactory.getLogger(TasksLauncher.class);

    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();


    /**
     * Validation de l'éxistence du fichier xml dans le chemin d'entrée
     * et lancement de la génération du sommaire
     */
    public void launch() throws IOException, XMLStreamException, BusinessFaultMessage, TechnicalFaultMessage {
        // Récuperation du fichier xml à partir du répertoire de travail
        File xmlInputFile = Validation.validateAndGetXmlInputFile(getXmlInputFiles());

        LOGGER.info(" => Traitement du fichier: {}", xmlInputFile.getName());

        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlInputFile));
        ((XmlReaderServiceImpl) xmlReaderService).setReader(reader);

        // initialisation du sommaire avec ecriture de l'entete
        sommaireWriterService.initSommaire();


        // lecture et ecriture document par document
        LOGGER.info("Détection des documents dans le fichier xml et écriture dans le sommaire");

        CurrentDocumentBean currentDocumentBean;
        do {
            currentDocumentBean = xmlReaderService.getNextDocument();
            if(currentDocumentBean == null)  {
                break;
            }
            documentConstructorService.addMetadatasToCurrentDocument(currentDocumentBean);
            sommaireWriterService.writeNewDocument(currentDocumentBean);

        } while (true);


        sommaireWriterService.closeSommaire();
    }

    /**
     * Lister les fichiers xml dans le répertoire en entrée du script
     *
     * @return
     */
    private List<File> getXmlInputFiles() {
        if (propertiesBean == null) {
            return null;
        }

        File[] xmlInputFiles = new File(propertiesBean.getXmlFileInputDirectory()).listFiles();
        if (xmlInputFiles == null) {
            return null;
        }

        return Arrays.stream(xmlInputFiles)
                .filter(file -> file.getName().endsWith(".xml"))
                .collect(Collectors.toList());

    }
}

