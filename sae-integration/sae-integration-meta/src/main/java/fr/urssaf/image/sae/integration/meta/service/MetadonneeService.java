package fr.urssaf.image.sae.integration.meta.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.meta.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.meta.factory.ObjectFactory;
import fr.urssaf.image.sae.integration.meta.modele.xml.DictionnaireType;
import fr.urssaf.image.sae.integration.meta.modele.xml.MetaType;
import fr.urssaf.image.sae.integration.meta.modele.xml.MetadonneeType;
import fr.urssaf.image.sae.integration.meta.utils.JAXBUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;

/**
 * Service de manipulation des métadonnées et des dictionnaires à partir du
 * fichier XML
 */
@Service
public class MetadonneeService {

   private static final Logger LOG = LoggerFactory
         .getLogger(MetadonneeService.class);

   @Autowired
   private SaeMetaDataService saeMetadataService;

   @Autowired
   private DictionaryService dictionaryService;

   /**
    * Réalise les traitements
    * 
    * @param fichierXml
    *           le fichier XML décrivant les traitements à réaliser
    */
   public void traitement(File fichierXml) {

      // Init
      String cheminFichier = fichierXml.getAbsolutePath();

      // Traces
      LOG.info(
            "Traitement des métadonnées/dictionnaires à partir du fichier {}",
            cheminFichier);

      // Chargement du fichier XML
      LOG.debug("Chargement du fichier XML");
      MetaType metaType = chargeFichierXml(cheminFichier);

      // Ajout d'entrée et création des dictionnaires
      traitementDicoAjout(metaType);

      // Suppression d'entrées de dictionnaires
      traitementDicoSuppression(metaType);

      // Création de métadonnées
      traitementMetaCreation(metaType);

      // Modification de métadonnées
      traitementMetaModif(metaType);

      // Traces
      LOG.info("Traitement terminé des métadonnées/dictionnaires");

   }

   private MetaType chargeFichierXml(String cheminFichierDroitsXml) {

      try {

         return JAXBUtils.unmarshalAvecXsdDansRess(MetaType.class,
               cheminFichierDroitsXml, "/xsd/saemeta/saemeta.xsd");

      } catch (Exception ex) {
         throw new IntegrationRuntimeException(
               "Erreur lors du chargement du fichier XML contenant les métadonnées/dictionnaires",
               ex);
      }

   }

   private void traitementDicoAjout(MetaType metaType) {

      List<DictionnaireType> dicos = metaType.getDictionnaireAjout()
            .getDictionnaire();
      int nbDicoAtraiter = dicos.size();
      LOG.info("Nombre de dictionnaires contenant des éléments à ajouter : {}",
            nbDicoAtraiter);
      if (nbDicoAtraiter > 0) {

         // Trace
         LOG.info("Traitement des ajouts dans les dictionnaires");

         // Boucle
         for (DictionnaireType dicoType : dicos) {

            // Le code
            String codeDico = dicoType.getCode();

            // Trace
            LOG.info("Traitement des ajouts du dico \"{}\"", codeDico);

            // Les valeurs
            List<String> valeurs = dicoType.getValeurs().getValeur();
            LOG.info("Valeurs à ajouter : {}", valeurs.toString());

            // Appel du service adéquat
            dictionaryService.addElements(codeDico, valeurs);

         }

         // Trace
         LOG.info("Traitement terminé des ajouts dans les dictionnaires");

      }

   }

   private void traitementDicoSuppression(MetaType metaType) {

      List<DictionnaireType> dicos = metaType.getDictionnaireSuppression()
            .getDictionnaire();
      int nbDicoAtraiter = dicos.size();
      LOG.info(
            "Nombre de dictionnaires contenant des éléments à supprimer : {}",
            nbDicoAtraiter);
      if (nbDicoAtraiter > 0) {

         // Trace
         LOG.info("Traitement des suppressions dans les dictionnaires");

         // Boucle
         for (DictionnaireType dicoType : dicos) {

            // Le code
            String codeDico = dicoType.getCode();

            // Trace
            LOG.info("Traitement des suppressions du dico \"{}\"", codeDico);

            // Les valeurs
            List<String> valeurs = dicoType.getValeurs().getValeur();
            LOG.info("Valeurs à supprimer : {}", valeurs.toString());

            // Appel du service adéquat
            dictionaryService.deleteElements(codeDico, valeurs);

         }

         // Trace
         LOG.info("Traitement terminé des suppressions dans les dictionnaires");

      }

   }

   private void traitementMetaCreation(MetaType metaType) {

      List<MetadonneeType> metas = metaType.getMetadonneesCreation()
            .getMetadonnee();
      int nbMetasAtraiter = metas.size();
      LOG.info("Nombre de métadonnées à créer : {}", nbMetasAtraiter);
      if (nbMetasAtraiter > 0) {

         // Trace
         LOG.info("Traitement des créations de métadonnées");

         // Boucle
         for (MetadonneeType metadonneeType : metas) {

            // Le code court
            String codeCourt = metadonneeType.getCodeCourt();

            // Trace
            LOG.info("Création de la métadonnée \"{}\"", codeCourt);

            // Conversion d'objets
            MetadataReference meta = ObjectFactory.create(metadonneeType);
            LOG.info("Détails de la métadonnée à créer : {}",
                  metaToString(meta));

            // Appel du service adéquat
            saeMetadataService.create(meta);

         }

         // Trace
         LOG.info("Traitement terminé des créations de métadonnées");

      }

   }

   private void traitementMetaModif(MetaType metaType) {

      List<MetadonneeType> metas = metaType.getMetadonneesModification()
            .getMetadonnee();
      int nbMetasAtraiter = metas.size();
      LOG.info("Nombre de métadonnées à modifier : {}", nbMetasAtraiter);
      if (nbMetasAtraiter > 0) {

         // Trace
         LOG.info("Traitement des modifications de métadonnées");

         // Boucle
         for (MetadonneeType metadonneeType : metas) {

            // Le code court
            String codeCourt = metadonneeType.getCodeCourt();

            // Trace
            LOG.info("Modification de la métadonnée \"{}\"", codeCourt);

            // Conversion d'objets
            MetadataReference meta = ObjectFactory.create(metadonneeType);
            LOG.info("Détails de la métadonnée à modifier : {}",
                  metaToString(meta));

            // Appel du service adéquat
            // TODO : changer en modify
            saeMetadataService.moify(meta);

         }

         // Trace
         LOG.info("Traitement terminé des modifications de métadonnées");

      }

   }

   private String metaToString(MetadataReference meta) {

      StringBuilder sBuilder = new StringBuilder();

      sBuilder.append(String.format("[Code court]=[%s]", meta.getShortCode()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Code long]=[%s]", meta.getLongCode()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Libellé]=[%s]", meta.getLabel()));
      sBuilder.append(";");
      sBuilder.append(String
            .format("[Description]=[%s]", meta.getDescription()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Spécifiable lors de l'archivage]=[%s]",
            meta.isArchivable()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Obligatoire à l'archivage]=[%s]", meta
            .isRequiredForArchival()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Consultée par défaut]=[%s]", meta
            .isDefaultConsultable()));
      sBuilder.append(";");
      sBuilder
            .append(String.format("[Consultable]=[%s]", meta.isConsultable()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Critère de recherche]=[%s]", meta
            .isSearchable()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Indexée]=[%s]", meta.getIsIndexed()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Formatage]=[%s]", meta.getPattern()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Taille max]=[%s]", meta.getLength()));
      sBuilder.append(";");
      sBuilder.append(String
            .format("[A un dico]=[%s]", meta.getHasDictionary()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Nom du dico]=[%s]", meta
            .getDictionaryName()));
      sBuilder.append(";");
      sBuilder
            .append(String.format("[Gérée par DFCE]=[%s]", meta.isInternal()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Type DFCE]=[%s]", meta.getType()));
      sBuilder.append(";");
      sBuilder.append(String.format("[Obligatoire au stockage]=[%s]", meta
            .isRequiredForStorage()));
      sBuilder.append(";");

      return sBuilder.toString();

   }

}
