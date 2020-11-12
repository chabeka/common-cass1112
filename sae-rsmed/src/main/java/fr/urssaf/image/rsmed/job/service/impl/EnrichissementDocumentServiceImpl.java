package fr.urssaf.image.rsmed.job.service.impl;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.bean.xsd.generated.ListeMetadonneeType;
import fr.urssaf.image.rsmed.bean.xsd.generated.MetadonneeType;
import fr.urssaf.image.rsmed.job.Validation;
import fr.urssaf.image.rsmed.job.service.DocumentConstructorServiceInterface;
import fr.urssaf.image.rsmed.job.service.IndividuServiceInterface;
import fr.urssaf.image.rsmed.job.service.RedevabiliteServiceInterface;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;
import fr.urssaf.image.rsmed.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class EnrichissementDocumentServiceImpl implements DocumentConstructorServiceInterface {

    private static Logger LOGGER = LoggerFactory.getLogger(EnrichissementDocumentServiceImpl.class);

    private static final String ApplicationProductrice = "RSMED_CONOT";
    private static final String ApplicationTraitement = "SATURNE";

    @Autowired
    IndividuServiceInterface individuService;

    @Autowired
    RedevabiliteServiceInterface redevabiliteService;

    @Autowired
    PropertiesBean propertiesBean;


    @Override
    public void addMetadatasToCurrentDocument(CurrentDocumentBean currentDocumentBean) throws BusinessFaultMessage, IOException, TechnicalFaultMessage {
        // Ajout des metadonnées:
        ListeMetadonneeType listeMetadonneeType = getListeMetadonnees(currentDocumentBean);
        currentDocumentBean.setListeMetadonneeType(listeMetadonneeType);

        Validation.validateMetadonnees(listeMetadonneeType);

    }

    private void addNewMetadonnee(String code, String value, ListeMetadonneeType listeMetadonneeType) {
        MetadonneeType metadonnee = new MetadonneeType();
        metadonnee.setCode(code);
        metadonnee.setValeur(value);
        LOGGER.debug("Ajout metadonnee: code={}, value={}", code, value);
        listeMetadonneeType.getMetadonnee().add(metadonnee);
    }

    /**
     * @param currentDocumentBean
     * @throws TechnicalFaultMessage
     * @throws BusinessFaultMessage  Méthode qui appelle le WS Rei afin de compléter le sommaire
     */
    private ListeMetadonneeType getListeMetadonnees(CurrentDocumentBean currentDocumentBean) throws TechnicalFaultMessage, BusinessFaultMessage, IOException {

        ListeMetadonneeType listeMetadonneeType = new ListeMetadonneeType();

        Validation.validateNumeroCompteExterne(currentDocumentBean.getIdV2());


        Long idEntite = redevabiliteService.getIdEntiteDuCompte(currentDocumentBean.getIdV2().substring(0, 18));
        String codeUrssaf = redevabiliteService.getCodeUrssafParNumCptExterne(currentDocumentBean.getIdV2().substring(0, 18));

        IndividuType individu = individuService.getIndividuParIdRei(idEntite);

        // metadonnée IdGed
        addNewMetadonnee("IdGed", UUID.randomUUID().toString(), listeMetadonneeType);

        // metadonnée Titre
        addNewMetadonnee("Titre", currentDocumentBean.getTitre(), listeMetadonneeType);

        // metadonnée DateCreation
        addNewMetadonnee("DateCreation", currentDocumentBean.getDateSaisie(), listeMetadonneeType);

        // metadonnée ApplicationProductrice
        addNewMetadonnee("ApplicationProductrice", ApplicationProductrice, listeMetadonneeType);

        // metadonnée ApplicationTraitement
        addNewMetadonnee("ApplicationTraitement", ApplicationTraitement, listeMetadonneeType);

        // metadonnée ApplicationMetier TODO
        addNewMetadonnee("ApplicationMetier", "TO BE DEFINED", listeMetadonneeType);


        // metadonnée CodeOrganismeProprietaire
        addNewMetadonnee("CodeOrganismeProprietaire", "UR" + codeUrssaf, listeMetadonneeType);

        // metadonnée CodeOrganismeGestionnaire
        addNewMetadonnee("CodeOrganismeGestionnaire", "UR" + codeUrssaf, listeMetadonneeType);

        // metadonnée CodeRND
        addNewMetadonnee("CodeRND", currentDocumentBean.getCodeRND(), listeMetadonneeType);

        // metadonnée Hash
        String pdf = currentDocumentBean.getPdf();
        addNewMetadonnee("Hash", FileUtils.getHash(pdf), listeMetadonneeType);

        // metadonnée TypeHash
        addNewMetadonnee("TypeHash", "SHA-1", listeMetadonneeType);

        // metadonnée NbPages
        addNewMetadonnee("NbPages", String.valueOf(currentDocumentBean.getNbPage()), listeMetadonneeType);

        // metadonnée FormatFichier
        addNewMetadonnee("FormatFichier", "pdf", listeMetadonneeType);

        if (individu != null) {
            // metadonnée SIREN
            addNewMetadonnee("SIREN", individu.getSirenPersonnel().getValeur(), listeMetadonneeType);

            // metadonnée NniEmployeur
            addNewMetadonnee("NniEmployeur", individu.getNir().getValeur() + individu.getCleNir().getValeur(), listeMetadonneeType);

            // metadonnée Denomination
            addNewMetadonnee("Denomination", individu.getNomPatronymique().getValeur() + " " + individu.getPrenomsPatronymiques().getValeur(), listeMetadonneeType);

            // metadonnée DateNaissanceCotisant
            String dateNaissanceCotisant = DateTimeFormatter
                    .ofPattern("YYYY-MM-dd")
                    .format(individu.getDateNaissance().getValeur().toGregorianCalendar().toZonedDateTime());
            addNewMetadonnee("DateNaissanceCotisant", dateNaissanceCotisant, listeMetadonneeType);


            // metadonnée RIBA
            addNewMetadonnee("RIBA", individu.getNoRiba().getValeur(), listeMetadonneeType);
        }
        // metadonnée NumeroCompteExterne
        addNewMetadonnee("NumeroCompteExterne", currentDocumentBean.getIdV2().substring(0, 18), listeMetadonneeType);

        // metadonnée NumeroStructure
        addNewMetadonnee("NumeroStructure", currentDocumentBean.getIdV2().substring(18, 28), listeMetadonneeType);

        return listeMetadonneeType;
    }
}
