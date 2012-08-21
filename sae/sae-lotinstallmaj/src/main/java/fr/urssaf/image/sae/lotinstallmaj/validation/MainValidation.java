package fr.urssaf.image.sae.lotinstallmaj.validation;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.service.impl.MajLotServiceImpl;

/**
 * Classe de validation des arguments en entrée de la classe principale du JAR
 * executable
 * 
 */
@Aspect
public class MainValidation {

   private static final Logger LOG = LoggerFactory
         .getLogger(MainValidation.class);

   private static final String MAIN_METHOD = "execution(void fr.urssaf.image.sae.lotinstallmaj.Main.main(*)) && args(args)";

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode main de classe principale MAIN sont bien corrects.
    * 
    * @param args
    *           chemin complet du fichier de configuration SAE operations a
    *           effectuée
    * @throws MajLotGeneralException
    *            en cas d'échec de validation de args
    * 
    */
   @Before(MAIN_METHOD)
   public final void main(String[] args) throws MajLotGeneralException {

      if (args != null && args.length > 0) {

         // Extrait le 1er argument de la ligne de commande
         String pathFile = args[0];

         // Vérifie que ce 1er argument soit bien le chemin complet du
         // fichier de configuration SAE
         checkPathConfig(pathFile);

         // nom de l'opération
         checkOperationName(args);

      } else {

         // Aucun argument n'a été passé à la ligne de commande
         // Ajout d'un log, et levée d'une exception

         String message = "Erreur : Il faut indiquer, en premier argument de la ligne de commande, le chemin complet du fichier de configuration du SAE.";

         LOG.warn(message);

         throw new MajLotGeneralException(message);

      }
   }

   /**
    * Vérification du chemin du fichier de configuration
    */
   private void checkPathConfig(String pathFile) throws MajLotGeneralException {

      File file = new File(pathFile);

      if (StringUtils.isBlank(pathFile) || !file.exists() || !file.isFile()) {

         StringBuffer strBuff = new StringBuffer();
         strBuff
               .append("Erreur : Il faut indiquer, en premier argument de la ligne de commande, le chemin complet du fichier de configuration du SAE");
         strBuff.append(String.format(" (argument fourni : %s).", pathFile));
         String message = strBuff.toString();

         LOG.warn(message);

         throw new MajLotGeneralException(message);

      }
   }

   /**
    * Vérification du nom de l'opération<br>
    * <br>
    * Ce nom doit être transmis comme 2ème argument de la ligne de commande
    */
   private void checkOperationName(String[] args) throws MajLotGeneralException {

      // Vérifie qu'il y a un 2ème argument dans la ligne de commande
      if (args.length < 2 || StringUtils.isBlank(args[1])) {

         String message = "Erreur : Il faut indiquer, en deuxième argument de la ligne de commande, le nom de l'opération à réaliser.";

         LOG.warn(message);

         throw new MajLotGeneralException(message);
      }

      // Extrait le nom de l'opération de la ligne de commande
      String nomOperation = args[1];

      // Vérifie que l'opération est connue
      checkOperationName(nomOperation);

   }

   private void checkOperationName(String nomOperation)
         throws MajLotGeneralException {

      if (MajLotServiceImpl.CODE_ACTIVITE.equalsIgnoreCase(nomOperation))
         return;
      if (MajLotServiceImpl.CASSANDRA_120510.equalsIgnoreCase(nomOperation))
         return;
      if (MajLotServiceImpl.CASSANDRA_120512.equalsIgnoreCase(nomOperation))
         return;
      if (MajLotServiceImpl.CASSANDRA_120910.equalsIgnoreCase(nomOperation))
         return;
      if (MajLotServiceImpl.DFCE_110_CASSANDRA.equalsIgnoreCase(nomOperation))
         return;
      if (MajLotServiceImpl.DFCE_110_INDEX_DATES.equalsIgnoreCase(nomOperation))
         return;
      if (MajLotServiceImpl.META_SEPA.equalsIgnoreCase(nomOperation))
         return;
      // TODO : Traiter le cas de la mise à jour de la durée de conservation de
      // 3.1.3.1.1 (en attente du JIRA CRTL-81)

      // Opération non trouvée
      String message = String.format("Erreur : Opération inconnue : %s",
            nomOperation);
      LOG.warn(message);
      throw new MajLotGeneralException(message);
   }

}
