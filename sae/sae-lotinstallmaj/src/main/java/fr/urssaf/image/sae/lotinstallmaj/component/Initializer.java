package fr.urssaf.image.sae.lotinstallmaj.component;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.executable.AdministrationSAEMain;
import fr.urssaf.image.sae.admin.dfce.exploit.services.AdministrationDFCEService;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcConfigService;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.impl.DFCEIndexeMetaUpdaterService;
import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.service.MajRndService;

@Component
public class Initializer {

   @Autowired
   private MajRndService majRndService;

   @Autowired
   private IgcConfigService igcConfigService;

   @Autowired
   private IgcDownloadService igcDownloadService;

   @Autowired
   private AdministrationDFCEService adminDFCEService;

   @Autowired
   private DFCEConnection dfceConnexionParameter;

   @Value("${sae.igc.cheminFichierConfig}")
   private String pathConfigFileIgc;

   @Value("${sae.base.meta}")
   private String databaseXMLFile;

   @Value("${sae.base.rnd}")
   private String documentTypeXMLFile;

   @Autowired
   private DFCEIndexeMetaUpdaterService dfceIndexeMetaUpdaterService;

   @Autowired
   private ModeApiCqlSupport modeApiCqlSupport;

   private static final String ERREUR_UPDATE_RND = "Une erreur est survenue lors de la mise à jour du reférentiel RND";

   private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);

   /**
    * Creation de la base DFCE (SAE-INT)
    */
   public void createBaseDfce() {
      // les operations de ce services peuvent etre relativement longue
      // on configure donc un timeout de 3h (plutot que quelques minutes)
      dfceConnexionParameter.setTimeout(AdministrationSAEMain.TIMEOUT_DFCE);

      // Recuperation des fichiers XML du model de la BD Dfce
      final File xmlDataBaseModel = new File(databaseXMLFile);
      final File xmlDocumentsType = new File(documentTypeXMLFile);
      if (!xmlDataBaseModel.isFile()) {
         final String message = "Erreur technique : Le modèle de la base de données SAE n'est pas valide.";
         LOGGER.error(message);
         throw new MajLotRuntimeException(message);
      }
      if (!xmlDocumentsType.isFile()) {
         final String message = "Erreur technique : Le fichier des types de documents SAE n'est pas valide.";
         LOGGER.error(message);
         throw new MajLotRuntimeException(message);
      }

      // Appel du service de création de la base de données admin DFCE.
      LOGGER.info("Lancement de la creation de la base de donnees DFCE");
      try {
         adminDFCEService.createSAEBase(dfceConnexionParameter,
                                        xmlDataBaseModel,
                                        xmlDocumentsType);
      }
      catch (final BaseAdministrationServiceEx | ConnectionServiceEx e) {
         LOGGER.info(e.getMessage());
         throw new MajLotRuntimeException(e);
      }

      LOGGER.info("Fin de la creation de la base de donnees DFCE");
   }

   /**
    * Mise à jour du referentiel des RND
    * 
    * @throws MajLotGeneralException
    */
   public void majRnd() throws MajLotGeneralException {
      // Mise à jour RND
      LOGGER.info("Lancement de la mise à jour des code RND");
      try {
         majRndService.lancer();
      }
      catch (final MajRndException e) {
         throw new MajLotGeneralException(ERREUR_UPDATE_RND, e);
      }
      LOGGER.info("Fin de la mise à jour des code RND");
   }

   /**
    * Mise à jour du referentiel des RND dans la BD SAE tables CQL
    * Bascule du mode API pour les tables Parameters et Rnd en mode DATASTAX
    * 
    * @throws MajLotGeneralException
    */
   public void majRndCQL() throws MajLotGeneralException {
      final String cfNameRND = "rnd";
      final String cfNameParameters = "parameters";
      final String modeApiDatastax = "DATASTAX";
      final String modeApiHector = "HECTOR";

      LOGGER.info("Changement du modeAPI pour les tables Parameters et Rnd");
      modeApiCqlSupport.updateModeApi(modeApiDatastax, cfNameRND);
      modeApiCqlSupport.updateModeApi(modeApiDatastax, cfNameParameters);

      // Mise à jour RND
      LOGGER.info("Lancement de la mise à jour des code RND CQL");
      try {
         majRndService.lancer();
      }
      catch (final MajRndException e) {
         throw new MajLotGeneralException(ERREUR_UPDATE_RND, e);
      }
      LOGGER.info("Fin de la mise à jour des code RND CQL");

      LOGGER.info("Rebascule du modeAPI en HECTOR");
      modeApiCqlSupport.updateModeApi(modeApiHector, cfNameRND);
      modeApiCqlSupport.updateModeApi(modeApiHector, cfNameParameters);
   }

   /**
    * Téléchargement des CRL
    * 
    * @throws IgcConfigException
    */
   public void downloadCRL() {
      try {
         final IgcConfigs igcConfigs = igcConfigService.loadConfig(pathConfigFileIgc);
         igcDownloadService.telechargeCRLs(igcConfigs);
      }
      catch (final IgcDownloadException | IgcConfigException e) {
         throw new MajLotRuntimeException(e);
      }
   }

   /**
    * initialisation des méta et index composite et des droits
    */
   public void demarreCreateMetadatasIndexesDroitsSAE() {
      dfceIndexeMetaUpdaterService.demarreCreateMetadatasIndexesDroitsSAE();
   }

}
