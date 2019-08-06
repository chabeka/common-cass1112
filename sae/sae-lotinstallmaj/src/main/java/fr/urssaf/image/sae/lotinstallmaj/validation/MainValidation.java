package fr.urssaf.image.sae.lotinstallmaj.validation;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.MajLotServiceCQLImpl;
import fr.urssaf.image.sae.lotinstallmaj.service.impl.MajLotServiceImpl;

/**
 * Classe de validation des arguments en entrée de la classe principale du JAR
 * executable
 */
@Aspect
@Component
public class MainValidation {

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(MainValidation.class);

  private static final String MAIN_METHOD = "execution(void fr.urssaf.image.sae.lotinstallmaj.Main.main(*)) && args(args)";

  /**
   * Methode permettant de venir verifier si les paramétres d'entree de la
   * methode main de classe principale MAIN sont bien corrects.
   * 
   * @param args
   *          chemin complet du fichier de configuration SAE operations a
   *          effectuée
   * @throws MajLotGeneralException
   *           en cas d'échec de validation de args
   */
  @Before(MAIN_METHOD)
  public final void main(final String[] args) throws MajLotGeneralException {

    if (args != null && args.length > 0) {

      // Extrait le 1er argument de la ligne de commande
      final String pathFile = args[0];

      // Vérifie que ce 1er argument soit bien le chemin complet du
      // fichier de configuration SAE
      checkPathConfig(pathFile);

      // nom de l'opération
      checkOperationName(args);

    } else {

      // Aucun argument n'a été passé à la ligne de commande
      // Ajout d'un log, et levée d'une exception

      final String message = "Erreur : Il faut indiquer, en premier argument de la ligne de commande, le chemin complet du fichier de configuration du SAE.";

      LOG.warn(message);

      throw new MajLotGeneralException(message);

    }
  }

  /**
   * Vérification du chemin du fichier de configuration
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
   * Vérification du nom de l'opération<br>
   * <br>
   * Ce nom doit être transmis comme 2ème argument de la ligne de commande
   */
  private void checkOperationName(final String[] args) throws MajLotGeneralException {

    // Vérifie qu'il y a un 2ème argument dans la ligne de commande
    if (args.length < 2 || StringUtils.isBlank(args[1])) {

      final String message = "Erreur : Il faut indiquer, en deuxième argument de la ligne de commande, le nom de l'opération à réaliser.";

      LOG.warn(message);

      throw new MajLotGeneralException(message);
    }

    // Extrait le nom de l'opération de la ligne de commande
    final String nomOperation = args[1];

    // Vérifie que l'opération est connue
    checkOperationName(nomOperation);

  }

  private void checkOperationName(final String nomOperation)
      throws MajLotGeneralException {

    if (MajLotServiceImpl.CODE_ACTIVITE.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_120510.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_120512.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_121110.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if ("SUPPRESSION".equalsIgnoreCase(nomOperation)) {
      return;
    }
    /*
     * if
     * (MajLotServiceImpl.DFCE_110_CASSANDRA.equalsIgnoreCase(nomOperation))
     * return;
     */
    if (MajLotServiceImpl.META_SEPA.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.META_130400.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_130400.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_130700.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.DFCE_130700.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DROITS_GED.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_131100.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_140700.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_150100.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.META_150100.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_150400
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.DFCE_150400.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.DFCE_150400_P5.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNS_DISABLE_COMPOSITE_INDEX
                                                     .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNT_DISABLE_COMPOSITE_INDEX
                                                     .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_150600
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_150601
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.DFCE_151000.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_151000.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_151001
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_151200
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_151201
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_160300
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_160400
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNS_CASSANDRA_DFCE_160600
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNT_CASSANDRA_DFCE_160600
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNS_CASSANDRA_DFCE_160601
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNT_CASSANDRA_DFCE_160601
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_160900
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_160901
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_161100
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNS_CASSANDRA_DFCE_170200
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNT_CASSANDRA_DFCE_170200
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_170201.equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNS_CASSANDRA_DFCE_170202
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNT_CASSANDRA_DFCE_170202
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_170900
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_170901
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNS_CASSANDRA_DFCE_180300
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.GNT_CASSANDRA_DFCE_180300
                                                   .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_180900
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
    if (MajLotServiceImpl.CASSANDRA_DFCE_180901
                                               .equalsIgnoreCase(nomOperation)) {
      return;
    }
      if (MajLotServiceImpl.CASSANDRA_DFCE_180901
              .equalsIgnoreCase(nomOperation))
           return;
      if (MajLotServiceImpl.CASSANDRA_DFCE_190700
              .equalsIgnoreCase(nomOperation))
           return;
                 
    // CAS Nouvelle version de la DFCE 2.3.1 avec les scripts cql
    
    if (MajLotServiceCQLImpl.DFCE_192_TO_200_SCHEMA
	            .equalsIgnoreCase(nomOperation)) {
    	return;
	}
    if (MajLotServiceCQLImpl.DFCE_200_TO_210_SCHEMA
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
    if (MajLotServiceCQLImpl.DFCE_210_TO_230_SCHEMA
            .equalsIgnoreCase(nomOperation)) {
    	return;
	}
	if (MajLotServiceCQLImpl.DFCE_230_TO_192_SCHEMA
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
    
    // Cas migration cql des tables de la base SAE
    
	if (MajLotServiceCQLImpl.SAE_MODE_API
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	if (MajLotServiceCQLImpl.SAE_MIG_TRACES
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	if (MajLotServiceCQLImpl.SAE_MIG_PILE_TRAVAUX
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	if (MajLotServiceCQLImpl.SAE_MIG_JOB_SPRING
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	
	if (MajLotServiceCQLImpl.SAE_DELETE_MIG_JOB_SPRING
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	if (MajLotServiceCQLImpl.SAE_DELETE_MIG_PILE_TRAVAUX
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	if (MajLotServiceCQLImpl.SAE_DELETE_MIG_TRACES
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}
	if (MajLotServiceCQLImpl.SAE_DELETE_MODE_API
	            .equalsIgnoreCase(nomOperation)) {
		return;
	}

    // TODO : Traiter le cas de la mise à jour de la durée de conservation de
    // 3.1.3.1.1 (en attente du JIRA CRTL-81)

    // Opération non trouvée
    final String message = String.format("Erreur : Opération inconnue : %s",
                                         nomOperation);
    LOG.warn(message);
    throw new MajLotGeneralException(message);
  }

}
