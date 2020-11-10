package fr.urssaf.image.rsmed.job;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.exception.FunctionalException;
import fr.urssaf.image.rsmed.job.service.DocumentConstructorServiceInterface;
import fr.urssaf.image.rsmed.job.service.SommaireWriterServiceInterface;
import fr.urssaf.image.rsmed.job.service.XmlReaderServiceInterface;
import fr.urssaf.image.rsmed.job.service.impl.XmlReaderServiceImpl;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;
import fr.urssaf.image.rsmed.utils.FileUtils;
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
import java.util.NoSuchElementException;
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

    private static final String FX3_FD30ADO1_TITRE = "Mise en demeure";
    private static final String FX3_FD40ADO1_TITRE = "Contrainte notifiée";
    private static final String FX5_FD30ADO2_TITRE = "AR mise en demeure ";
    private static final String FX5_FD40ADO2_TITRE = "AR contrainte notifiée ";


    private static String titre;
    private static String codeRND;


    /**
     * Validation de l'éxistence du fichier xml dans le chemin d'entrée
     * et lancement de la génération du sommaire
     */
    public void launch() {

        try {
            // Récuperation du fichier xml à partir du répertoire de travail
            File xmlInputFile = Validation.validateAndGetXmlInputFile(getInputFiles(FileUtils.EXTENSION_XML));

            File zipInputFile = Validation.validateAndGetXmlInputFile(getInputFiles(FileUtils.EXTENSION_ZIP));
            setMetadataTitreFromZipFileName(zipInputFile.getName());

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
                if (currentDocumentBean == null) {
                    break;
                }
                currentDocumentBean.setTitre(titre);
                currentDocumentBean.setCodeRND(codeRND);
                documentConstructorService.addMetadatasToCurrentDocument(currentDocumentBean);
                sommaireWriterService.writeNewDocument(currentDocumentBean);

            } while (true);

        } catch (FunctionalException exception) {
            LOGGER.error("Une erreur fonctionnelle est survenue: ", exception);
            throw new RuntimeException("Fichier xml en entrée introuble", exception);
        } catch (NoSuchElementException | IOException exception) {
            LOGGER.error("Une erreur de lecture de fichier est survenue: ", exception);
            throw new RuntimeException("Fichier xml en entrée introuble", exception);
        } catch (XMLStreamException exception) {
            LOGGER.error("Une erreur est survenue lors du traitement du fichier xml: ", exception);
            throw new RuntimeException("Une erreur est survenue lors du traitement du fichier xml", exception);
        } catch (TechnicalFaultMessage | BusinessFaultMessage exception) {
            LOGGER.error("Une erreur est survenue au moment de l'appel des WS rei: ", exception);
            throw new RuntimeException("Une erreur est survenue au moment de l'appel des WS rei", exception);
        } finally {
            try {
                sommaireWriterService.closeSommaire();
            } catch (XMLStreamException xmlStreamException) {
                LOGGER.error("Une erreur est survenue au moment de la fermeture du sommaire: ", xmlStreamException);
                throw new RuntimeException("Une erreur est survenue au moment de la fermeture du sommaire: ", xmlStreamException);
            }
        }

    }

    /**
     * Lister les fichiers xml dans le répertoire en entrée du script
     *
     * @return
     */
    private List<File> getInputFiles(String extension) {
        if (propertiesBean == null) {
            return null;
        }

        File[] xmlInputFiles = new File(propertiesBean.getWorkdirDirectory()).listFiles();
        if (xmlInputFiles == null) {
            return null;
        }

        return Arrays.stream(xmlInputFiles)
                .filter(file -> file.getName().endsWith(extension))
                .collect(Collectors.toList());

    }

    /**
     * Définit la métadata titre des documents
     *
     * @return
     */
    private void setMetadataTitreFromZipFileName(String zipName) {
        String case_in = (zipName.substring(0, 3) + "_" + zipName.substring(15, 23)).toUpperCase();

        switch (case_in) {
            case "FX3_FD30ADO1":
                titre = FX3_FD30ADO1_TITRE;
                codeRND = "3.1.2.2.1";
                break;
            case "FX3_FD40ADO1":
                titre = FX3_FD40ADO1_TITRE;
                codeRND = "3.1.2.2.1";

                break;
            case "FX5_FD30ADO2":
                titre = FX5_FD30ADO2_TITRE;
                codeRND = "3.1.2.2.2";
                break;
            case "FX5_FD40ADO2":
                titre = FX5_FD40ADO2_TITRE;
                codeRND = "3.1.2.2.2";
                break;

        }
    }
}

