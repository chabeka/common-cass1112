package fr.urssaf.image.sae.lotinstallmaj;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.lotinstallmaj.component.launcher.Launcher;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotAlreadyInstallUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotInexistantUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotManualUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRestartTomcatException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotUnknownDFCEVersion;

public class MainApplication {

   private static final Logger LOG = LoggerFactory.getLogger(MainApplication.class);

   /**
    * @param args
    */
   public static void main(final String[] args) {

      if (args.length < 1) {
         throw new MajLotRuntimeException("Le paramètre correspondant au fichier de configuration du SAE n'est pas renseigné!");
      }

      final String cheminFicConfSae = args[0];
      try {
         checkPathConfig(cheminFicConfSae);
      }
      catch (final MajLotGeneralException e) {
         throw new MajLotRuntimeException(e);
      }

      final ApplicationContext context = startContextSpring(cheminFicConfSae);
      final Launcher launcher = context.getBean(Launcher.class);
      try {
         launcher.launch(args);
      }
      catch (final MajLotGeneralException e) {
         throw new MajLotRuntimeException(e);
      }
      catch (final MajLotUnknownDFCEVersion | MajLotManualUpdateException | MajLotRestartTomcatException | MajLotInexistantUpdateException |
            MajLotAlreadyInstallUpdateException e) {
         LOG.info(e.getMessage());
      }
      catch (Exception e){
        throw e;
      }
      finally {
         System.exit(0);
      }

   }

   /**
    * Vérification du chemin du fichier de configuration
    */
   private static void checkPathConfig(final String pathFile) throws MajLotGeneralException {

      final File file = new File(pathFile);

      if (StringUtils.isBlank(pathFile) || !file.exists() || !file.isFile()) {

         final StringBuilder stringBuilder = new StringBuilder();
         stringBuilder
         .append("Erreur : Il faut indiquer, en premier argument de la ligne de commande, le chemin complet du fichier de configuration du SAE");
         stringBuilder.append(String.format(" (argument fourni : %s).", pathFile));
         final String message = stringBuilder.toString();

         LOG.warn(message);

         throw new MajLotGeneralException(message);
      }
   }

   /**
    * Démarrage du contexte Spring
    * 
    * @param cheminFicConfSae
    *           le chemin du fichier de configuration principal du sae
    *           (sae-config.properties)
    * @return le contexte Spring
    */
   protected static ApplicationContext startContextSpring(
         final String cheminFicConfSae) {

      final String contextConfig = "/applicationContext-sae-lotinstallmaj.xml";

      return ContextFactory.createSAEApplicationContext(contextConfig,
            cheminFicConfSae);
   }

}
