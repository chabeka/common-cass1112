package fr.urssaf.image.sae.lotinstallmaj.validation;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;

/**
 * Validation des arguments attendus
 */
@Aspect
@Component
public class MainArgsValidation {

  private static final Logger LOG = LoggerFactory.getLogger(MainArgsValidation.class);

  private static final List<String> LIST_CMDS = Arrays.asList(
                                                              "info",
                                                              "details",
                                                              "update",
                                                              "updateTo",
                                                              "verify",
                                                              "redo",
      "changeVersionTo");

  @Before("execution(void fr.urssaf.image.sae.lotinstallmaj.MainApplication.main(*)) && args(args)")
  public void validate(final String[] args) throws MajLotGeneralException {
    if (args != null && args.length > 0) {
      final String saeConfig = args[0];

      checkPathConfig(saeConfig);
    }
  }

  /**
   * Vérification du chemin du fichier de configuration global du SAE
   */
  private void checkPathConfig(final String pathFile) throws MajLotGeneralException {

    final File file = new File(pathFile);

    if (StringUtils.isBlank(pathFile) || !file.exists() || !file.isFile()) {

      final StringBuffer strBuff = new StringBuffer();
      strBuff
      .append("Erreur : Il faut indiquer, en premier argument de la ligne de commande, le chemin complet du fichier de configuration du SAE");
      strBuff.append(String.format(" (argument fourni : %s).", pathFile));
      final String message = strBuff.toString();

      LOG.warn(message);

      throw new MajLotGeneralException(message);

    }
  }

  /**
   * Vérification du nom de la commande (update, info...)
   * <br>
   * Ce nom doit être transmis comme 2ème argument de la ligne de commande
   */
  private void checkCmd(final String[] args) throws MajLotGeneralException {

    // Vérifie qu'il y a un 2ème argument dans la ligne de commande
    if (args.length < 2 || StringUtils.isBlank(args[1])) {

      final String message = "Erreur : Il faut indiquer, en deuxième argument de la ligne de commande, le nom de l'opération à réaliser.";

      LOG.warn(message);

      throw new MajLotGeneralException(message);
    }

    // Extraction du nom de la commande
    final String nomOperation = args[1];
    checkCmdName(nomOperation);

    // Vérifie que l'opération est connue
    // checkOperationName(nomOperation);

  }

  /**
   * Vérifie que le nom de la commande spécifiée existe bien dans la liste
   * des commandes possibles
   * 
   * @param nomDeLaCommande
   * @throws MajLotGeneralException
   */
  private void checkCmdName(final String nomDeLaCommande) throws MajLotGeneralException {

    if (!LIST_CMDS.contains(nomDeLaCommande)) {
      throw new MajLotGeneralException("La commande spécifiée n'est pas reconnue. Commande Attendue (");
    }
  }
}
