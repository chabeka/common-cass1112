package fr.urssaf.image.sae.lotinstallmaj.component.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.component.Initializer;
import fr.urssaf.image.sae.lotinstallmaj.constantes.Commandes;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotAlreadyInstallUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotInexistantUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotManualUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRestartTomcatException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotUnknownDFCEVersion;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotServiceVerificator;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotServiceVerificatorRouter;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAECassandraUpdaterCQL;

@Component
public class Launcher {

   private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

   @Autowired
   private Initializer initializer;

   @Autowired
   private SAECassandraUpdaterCQL saeCassandraUpdaterCQL;

   @Autowired
   @Qualifier("majLotServiceImpl")
   private MajLotService majLotService;

   @Autowired
   private MajLotServiceVerificatorRouter serviceVerificatorRouter;

   /**
    * @param args
    * @throws MajLotGeneralException
    * @throws MajLotAlreadyInstallUpdateException
    * @throws MajLotInexistantUpdateException
    * @throws MajLotRestartTomcatException
    * @throws MajLotManualUpdateException
    * @throws MajLotUnknownDFCEVersion
    */
   public void launch(final String[] args) throws MajLotGeneralException,
   MajLotManualUpdateException, MajLotRestartTomcatException,
   MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
   MajLotUnknownDFCEVersion {

      if (args.length < 2) {
         throw new MajLotRuntimeException("Tous les paramètres requis ne sont pas renseignés");
      }

      final String commande = args[1];
      if (!Commandes.getAllCommandes().contains(commande)) {
         throw new MajLotRuntimeException("La commande que vous avez entrée n'est pas reconnue!");
      }

      int numVersion = 0;

      if (Commandes.getAllCommandesWithParam().contains(commande)) {
         if (args.length < 3) {
            // Alors un paramètre numVersion est attendu pour la commande
            throw new MajLotRuntimeException(String.format("La commande [%s] attend un paramètre numVersion de type Integer", commande));
         } else {
            numVersion = Integer.valueOf(args[2]);
         }
      }
      selectOperation(commande, numVersion);

   }

   /**
    * Lancement d'une commande (update, create, info)
    * 
    * @param cmd
    * @param thirdParam
    *           ce paramètre est généralement un numéro de version
    * @throws MajLotManualUpdateException
    * @throws MajLotGeneralException
    * @throws MajLotInexistantUpdateException
    * @throws MajLotAlreadyInstallUpdateException
    * @throws MajLotUnknownDFCEVersion
    */
   public void selectOperation(final String cmd, final int numVersion)
         throws MajLotGeneralException,
         MajLotManualUpdateException,
         MajLotRestartTomcatException,
         MajLotInexistantUpdateException,
         MajLotAlreadyInstallUpdateException, MajLotUnknownDFCEVersion {
      if (cmd.equals(Commandes.CREATE.getName())) {
         create();
      }
      if (cmd.equals(Commandes.CREATE_CQL.getName())) {
         saeCassandraUpdaterCQL.createCQLSchema();
      } else if (cmd.equals(Commandes.INFO.getName())) {
         info();
      } else if (cmd.equals(Commandes.UPDATE.getName())) {
         update();
      } else if (cmd.equals(Commandes.UPDATE_TO.getName())) {
         updateToVersion(numVersion);
      } else if (cmd.equals(Commandes.REDO.getName())) {
         redo(numVersion);
      } else if (cmd.equals(Commandes.VERIFY.getName())) {
         verify(numVersion);
      } else if (cmd.equals(Commandes.DETAILS.getName())) {
         getVersionInfo(numVersion);
      } else if (cmd.equals(Commandes.CHANGE_VERSION_TO.getName())) {
         forceVersion(numVersion);
      } else if (cmd.equals(Commandes.RATTRAPAGE_CQL.getName())) {
         rattrappageCQL();
      }
   }

   private void create()
         throws MajLotManualUpdateException, MajLotRestartTomcatException,
         MajLotAlreadyInstallUpdateException, MajLotGeneralException,
         MajLotUnknownDFCEVersion {

      majLotService.demarreCreateSAE();
      initializer.majRnd();
      initializer.downloadCRL();
      initializer.demarreCreateMetadatasIndexesDroitsSAE();
   }

   private void update()
         throws MajLotGeneralException, MajLotManualUpdateException,
         MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
         MajLotUnknownDFCEVersion, MajLotRestartTomcatException {
      majLotService.update();
   }

   private void info() {
      majLotService.info();
   }

   private void updateToVersion(final int numVersion)
         throws MajLotGeneralException, MajLotManualUpdateException,
         MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
         MajLotUnknownDFCEVersion, MajLotRestartTomcatException {
      majLotService.updateToVersion(numVersion);
   }

   private void redo(final int numVersion)
         throws MajLotGeneralException, MajLotManualUpdateException,
         MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
         MajLotUnknownDFCEVersion, MajLotRestartTomcatException {
      majLotService.redo(numVersion);
   }

   private void verify(final int numVersion) {
      serviceVerificatorRouter.verify(numVersion);
   }

   private void getVersionInfo(final int numVersion) throws MajLotInexistantUpdateException {
      majLotService.getVersionInfo(numVersion);
   }

   private void forceVersion(final int numVersion) {
      majLotService.forceVersion(numVersion);
   }

   private void rattrappageCQL() throws MajLotGeneralException {
      LOG.info("Lancer le rattrapage pour le versionning CQL");
      saeCassandraUpdaterCQL.lancerRattrapage();
      initializer.demarreCreateMetadatasIndexesDroitsSAE();
      initializer.majRnd();
   }
}
