package fr.urssaf.image.sae.integrationinstall;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.BaseAdministrationServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.executable.AdministrationSAEMain;
import fr.urssaf.image.sae.admin.dfce.exploit.services.AdministrationDFCEService;
import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.integrationinstall.exception.IntegrationInstRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
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

      DFCEConnection cnxParameter = context.getBean(DFCEConnection.class);
      // les operations de ce services peuvent etre relativement longue
      // on configure donc un timeout de 3h (plutot que quelques minutes)
      cnxParameter.setTimeout(AdministrationSAEMain.TIMEOUT_DFCE);

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

      // Retire des arguments de la ligne de commande ceux que l'on a déjà
      // traités.
      // On ne laisse que les arguments spécifiques à l'opération
      String[] argsSpecifiques = (String[]) ArrayUtils.subarray(args, 3,
            args.length);

      // Démarre l'opération d'update de la base DFCE
      majLotService.demarreUpdateDFCE(argsSpecifiques);

      // Appel du service de création de la base de données admin DFCE.
      try {
         adminDFCE.createSAEBase(cnxParameter, xmlDataBaseModel,
               xmlDocumentsType);
      } catch (BaseAdministrationServiceEx e) {
         LOG.error("Error - " + e.getMessage());
         throw new IntegrationInstRuntimeException(e);
      } catch (ConnectionServiceEx e) {
         throw new IntegrationInstRuntimeException(e);
      }

      // Création de la base SAE
      majLotService.demarreCreateSAE();

      // Mise à jour RND
      try {

         // appel du service de mise à jour du RND
         MajRndService majRndService = context
               .getBean(MajRndService.class);

         majRndService.lancer();

      } catch (MajRndException e) {
         throw new IntegrationInstRuntimeException(e);
      } catch (Exception e) {
         throw new IntegrationInstRuntimeException(e);
      }

      // Création des metadatas, indexes composites et droits dans la base SAE.
      majLotService.demarreCreateMetadatasIndexesDroitsSAE(argsSpecifiques);

      // on force ici la fermeture du contexte de Spring
      // ceci a pour but de forcer la déconnexion avec Cassandra, la SGBD
      // chargé de la persistance de la pile des travaux
      LOG.debug("execute - fermeture du contexte d'application");
      ((AbstractApplicationContext) context).close();
      LOG.debug("execute - fermeture du contexte d'application effectuée");

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