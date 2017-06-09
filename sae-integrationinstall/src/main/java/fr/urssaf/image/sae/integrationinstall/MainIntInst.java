package fr.urssaf.image.sae.integrationinstall;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.executable.AdministrationSAEMain;
import fr.urssaf.image.sae.admin.dfce.exploit.services.AdministrationDFCEService;
import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcConfigService;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.integrationinstall.exception.IntegrationInstRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
import fr.urssaf.image.sae.lotinstallmaj.service.impl.MajLotServiceImpl;
import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.service.MajRndService;

/**
 * Classe Main du JAR Executable
 * 
 */
public final class MainIntInst {

   /**
    * LOGGER
    */
   private static final Logger LOG = LoggerFactory.getLogger(MainIntInst.class);
   
   /**
    * Constructeur
    */
   private MainIntInst() {
   }

   /**
    * Méthode main du JAR Executable
    * 
    * @param args
    *           arguments de la ligne de commande du JAR Executable
    */
   public static void main(String[] args) {

      // Extrait les infos de la ligne de commandes
      // La vérification du tableau args est faite par la validation AOP
      String cheminFicConfSae = args[0];

      // Démarrage du contexte spring
      ApplicationContext context = startContextSpring(cheminFicConfSae);

      // Récupération du contexte Spring du bean permettant de lancer
      // l'opération
      MajLotService majLotService = context.getBean("majLotServiceImpl",
            MajLotService.class);

      // Retire des arguments de la ligne de commande ceux que l'on a pas besoin
      // pour le moment.
      // On ne laisse que les arguments spécifiques à l'application concernée
      String[] argsSpecifiques = (String[]) ArrayUtils.subarray(args, 4,
            args.length);

      String applicationConcernee = argsSpecifiques.length > 0 ? argsSpecifiques[0]
            : null;
      if (applicationConcernee == null) {
         String message = "Erreur technique : Le serveur GED ou DFCE n'est pas renseigné. Veuillez indiquer le serveur sur lequel doit avoir lieu l'opération svp (GNS, GNT ou DFCE).";
         LOG.error(message);
         throw new MajLotRuntimeException(message);
      }

      // Création de la base de données DFCE (Schema et CF).
      if (MajLotServiceImpl.APPL_CONCERNEE.DFCE.getApplName().equals(applicationConcernee)) {
         LOG.info("Lancement du telechargement des CRL");
         // Démarre l'opération d'update de la base DFCE
         majLotService.demarreUpdateDFCE(applicationConcernee);
         LOG.info("Fin du telechargement des CRL");
      }

      // Création de la base de données GED (CNS ou GNT).
      if (MajLotServiceImpl.APPL_CONCERNEE.GNS.getApplName().equals(applicationConcernee) || 
            MajLotServiceImpl.APPL_CONCERNEE.GNT.getApplName().equals(applicationConcernee)) {
         final AdministrationDFCEService adminDFCE = context
               .getBean(AdministrationDFCEService.class);

         final String dataBasePath = args[1];
         final String documentsTypePath = args[2];
         final File xmlDataBaseModel = new File(dataBasePath);
         final File xmlDocumentsType = new File(documentsTypePath);
         if (!xmlDataBaseModel.isFile()) {
            // Opération inconnue => log + exception runtime
            String message = "Erreur technique : Le modèle de la base de données SAE n'est pas valide.";
            throw new IntegrationInstRuntimeException(message);
         }
         if (!xmlDocumentsType.isFile()) {
            String message = "Erreur technique : Le fichier des types de documents SAE n'est pas valide.";
            throw new IntegrationInstRuntimeException(message);
         }
         // Création de la connexion à DFCE.
         DFCEConnection cnxParameter = context.getBean(DFCEConnection.class);
         // les operations de ce services peuvent etre relativement longue
         // on configure donc un timeout de 3h (plutot que quelques minutes)
         cnxParameter.setTimeout(AdministrationSAEMain.TIMEOUT_DFCE);

         // Appel du service de création de la base de données admin DFCE.
         LOG.info("Lancement de la creation de la base de donnees DFCE");
         try {
            adminDFCE.createSAEBase(cnxParameter, xmlDataBaseModel,
                  xmlDocumentsType);
         } catch (BaseAdministrationServiceEx e) {
            LOG.error("Error - " + e.getMessage());
            throw new IntegrationInstRuntimeException(e);
         } catch (ConnectionServiceEx e) {
            throw new IntegrationInstRuntimeException(e);
         }
         LOG.info("Fin de la creation de la base de donnees DFCE");
         LOG.info("Lancement de la creation de la base de donnees " + applicationConcernee);
         // Création de la base SAE
         majLotService.demarreCreateSAE();
         LOG.info("Lancement de la creation de la base de donnees " + applicationConcernee);

         // Mise à jour RND
         LOG.info("Lancement de la mise à jour des code RND");
         try {

            // appel du service de mise à jour du RND
            MajRndService majRndService = context.getBean(MajRndService.class);

            majRndService.lancer();

         } catch (MajRndException e) {
            throw new IntegrationInstRuntimeException(e);
         } catch (Exception e) {
            throw new IntegrationInstRuntimeException(e);
         }
         LOG.info("Fin de la mise à jour des code RND");

         // Telechargement des CRL
         LOG.info("Lancement du telechargement des CRL");
         final String pathIgcConfigFile = args[3];
         try {
            IgcConfigService igcConfigService = context
                  .getBean(IgcConfigService.class);
            IgcConfigs igcConfigs = igcConfigService.loadConfig(pathIgcConfigFile);

            IgcDownloadService igcDownloadService = context
                  .getBean(IgcDownloadService.class);
            igcDownloadService.telechargeCRLs(igcConfigs);
         } catch (IgcConfigException e) {
            LOG.error(e.getMessage(), e);
            throw new IntegrationInstRuntimeException(e);

         } catch (IgcDownloadException e) {
            LOG.error(e.getMessage(), e);
            throw new IntegrationInstRuntimeException(e);

         }
         LOG.info("Fin du telechargement des CRL");
         // Création des metadatas, indexes composites et droits dans la base SAE.
         LOG.info("Lancement de la creation des metadonnees, des droits et des indexes composites pour la base de donnees "
               + applicationConcernee);
         majLotService.demarreCreateMetadatasIndexesDroitsSAE(applicationConcernee);
         LOG.info("Fin de la creation des metadonnees, des droits et des indexes composites pour la base de donnees "
               + applicationConcernee);
      }

      // on force ici la fermeture du contexte de Spring
      // ceci a pour but de forcer la déconnexion avec Cassandra, la SGBD
      // chargé de la persistance de la pile des travaux
      //      LOG.debug("execute - fermeture du contexte d'application");
      //      ((AbstractApplicationContext) context).close();
      //      LOG.debug("execute - fermeture du contexte d'application effectuée");

      LOG.debug("execute - fin des tâches demandées");

   }

   /**
    * Démarage du contexte Spring
    * 
    * @param cheminFicConfSae
    *           le chemin du fichier de configuration principal du sae
    *           (sae-config.properties)
    * @return le contexte Spring
    */
   protected static ApplicationContext startContextSpring(
         String cheminFicConfSae) {

      String contextConfig = "/applicationContext-sae-integrationinstall.xml";

      return ContextFactory.createSAEApplicationContext(contextConfig,
            cheminFicConfSae);

   }

}
