package fr.urssaf.image.sae.services.batch.reprise.exception;

import org.apache.commons.lang.StringUtils;

public class RepriseException extends Exception {

   /**
    * SUID
    */
   private static final long serialVersionUID = 9061354201801431195L;

   private static final String LIBELLE_RE_BUL001 = "Une erreur interne à l'application est survenue lors de la reprise du traitement de masse {0}. Détails : {1}";

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public RepriseException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    * @param idTraitementMasse
    *           identifiant de traitement de masse
    */
   public RepriseException(String message, String idTraitementMasse) {
      super(StringUtils.replaceEach(LIBELLE_RE_BUL001, new String[]{"{0}","{1}"}, new String[]{idTraitementMasse, message}));
   }
}
