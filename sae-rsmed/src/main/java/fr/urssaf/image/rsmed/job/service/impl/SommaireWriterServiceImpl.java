package fr.urssaf.image.rsmed.job.service.impl;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.bean.xsd.generated.DocumentType;
import fr.urssaf.image.rsmed.bean.xsd.generated.FichierType;
import fr.urssaf.image.rsmed.bean.xsd.generated.ListeMetadonneeType;
import fr.urssaf.image.rsmed.job.Validation;
import fr.urssaf.image.rsmed.job.service.SommaireWriterServiceInterface;
import fr.urssaf.image.rsmed.utils.SommaireWriterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

@Service
public class SommaireWriterServiceImpl implements SommaireWriterServiceInterface {

    private static Logger LOGGER = LoggerFactory.getLogger(SommaireWriterServiceImpl.class);

    @Autowired
    PropertiesBean propertiesBean;

    private SommaireWriterUtils sommaireWriterUtils;


    public SommaireWriterUtils initSommaire() throws IOException, XMLStreamException {
        LOGGER.info("Initialisation du sommaire et ecriture de l'entete");
        sommaireWriterUtils = new SommaireWriterUtils(propertiesBean.getXmlSommaireOutputDirectory() + File.separator);
        sommaireWriterUtils.startSommaire();
        sommaireWriterUtils.addSommaireHeaders();
        return sommaireWriterUtils;
    }

    public void closeSommaire() throws XMLStreamException {
        LOGGER.info("Ecriture de la fin du sommaire");
        sommaireWriterUtils.endSommaire();
    }

    public void writeNewDocument(CurrentDocumentBean currentDocumentBean) throws XMLStreamException {

        LOGGER.info("Ecriture d'un nouveau document dans le sommaire. (objetNumérique: {})", currentDocumentBean.getPdfName());
        DocumentType documentType = new DocumentType();
        // objet numerique:
        FichierType fichierType = new FichierType();
        fichierType.setCheminEtNomDuFichier(currentDocumentBean.getPdfName());
        documentType.setObjetNumerique(fichierType);

        documentType.setNumeroPageDebut(1);
        documentType.setNombreDePages(1);

        // Ajout des metadonnées:
        ListeMetadonneeType listeMetadonneeType = currentDocumentBean.getListeMetadonneeType();

        Validation.validateMetadonnees(listeMetadonneeType);

        documentType.setMetadonnees(listeMetadonneeType);

        sommaireWriterUtils.addDocument(documentType);

    }


}

