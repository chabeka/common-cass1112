package fr.urssaf.image.sae.commons.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Classe contenant les constantes utilisées dans les différents projets
 * 
 *
 */
public final class Constantes {

  /**
   * Le code du traitement
   */
  public static final String CODE_TRAITEMENT = "codeTraitement";

  /**
   * Prefixe pour la clef cassandra.
   */
  public static final String PREFIXE_SEMAPHORE_JOB = "semaphore_";

  /**
   * Prefixe pour la clef zookeeper.
   */
  public static final String PREFIXE_SEMAPHORE = "/Semaphore/";

  /**
   * Types de traitements
   */
  public static enum TYPES_JOB {
    capture_masse, suppression_masse, restore_masse, modification_masse, transfert_masse, reprise_masse;
  }

  /**
   * Nom du job d'un traitement de reprise en masse
   */
  public static final String REPRISE_MASSE_JN = "reprise_masse";


  /**
   * Code court de la métadonnée 'identifiant de suppression de masse'
   */
  public static final String CODE_COURT_META_ID_TRANSFERT = "ifi";

  /**
   * Identifiant de l'id du traitement à reprendre
   */
  public static final String ID_TRAITEMENT_A_REPRENDRE = "uuidJobAReprendre";

  /**
   * l'Url ECDE
   */
  public static final String ECDE_URL = "ecdeUrl";

  /**
   * Hash du fichier sommaire.xml
   */
  public static final String HASH = "hash";

  /**
   * Le type de hash
   */
  public static final String TYPE_HASH = "typeHash";

  /**
   * Algo hash
   */
  public static final List<String> ALGO_HASH = Arrays.asList("SHA-1", "SHA-256");
  /**
   * Identifiant du paramètre heureTraitement
   */
  public static final String HEURE_REPRISE = "heureTraitementReprise";

  // Les ColumnFamily pour mode API
  public static final String CF_PARAMETERS = "parameters";

  public static final String CF_METADATA = "metadata";

  public static final String CF_DICTIONARY = "dictionary";

  public static final String CF_REFERENTIEL_FORMAT = "referentielformat";

  public static final String CF_RND = "rnd";

  public static final String CF_CORRESPONDANCES_RND = "correspondancesrnd";

  // Traces
  public static final String CF_TRACE_JOURNAL_EVT = "tracejournalevt";

  public static final String CF_TRACE_DESTINATAIRE = "tracedestinataire";

  public static final String CF_TRACE_REG_TECHNIQUE = "traceregtechnique";

  public static final String CF_TRACE_REG_SECURITE = "traceregsecurite";

  public static final String CF_TRACE_REG_EXPLOITATION = "traceregexploitation";

}
