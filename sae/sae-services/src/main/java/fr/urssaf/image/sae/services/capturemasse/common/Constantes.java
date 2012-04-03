/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.common;


/**
 * Classe contenant les constantes utilisées dans le reste de la capture de
 * masse
 * 
 */
public final class Constantes {

   /**
    * Identifiant du paramètre idTraitement
    */
   public static final String ID_TRAITEMENT = "capture.masse.idtraitement";

   /**
    * Identifiant du paramètre sommaire
    */
   public static final String SOMMAIRE = "capture.masse.sommaire";

   /**
    * PoolThread
    */
   public static final String THREAD_POOL = "INSERTION_POOL_THREAD";

   /**
    * Liste des Document en erreur
    */
   public static final String DOC_EXCEPTION = "DOCUMENT_EXCEPTION";

   /**
    * Liste des indexs des documents en erreur
    */
   public static final String INDEX_EXCEPTION = "INDEX_EXCEPTION";

   /**
    * Liste des codes erreur
    */
   public static final String CODE_EXCEPTION = "CODE_EXCEPTION";

   /**
    * Documents intégrés
    */
   public static final String INTEG_DOCS = "INTEGRATED_DOCUMENTS";

   /**
    * Chemin complet du fichier sommaire.xml
    */
   public static final String SOMMAIRE_FILE = "SOMMAIRE_FILE";

   /**
    * Index utilisé pour le contrôler le document courant
    */
   public static final String CTRL_INDEX = "CTRL_INDEX";

   /**
    * Nombre de documents total dans un fichier sommaire.xml
    */
   public static final String DOC_COUNT = "DOC_COUNT";

   
   /**
    * Code Erreur technique
    */
   public static final String ERR_BUL001 = "SAE-CA-BUL001";
   
   /**
    * Code Erreur fonctionnelle
    */
   public static final String ERR_BUL002 = "SAE-CA-BUL002";
   
   
   /**
    * Constructeur
    */
   private Constantes() {
   }
}
