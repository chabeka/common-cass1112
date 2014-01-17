/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.Map;

/**
 * Classe de modèle d'un PAGM
 * 
 */
public class Pagm {

   /** code intelligible du PAGM */
   private String code;

   /** droit d'action (PAGMa) du PAGM */
   private String pagma;

   /** domaine d'action (PAGMp) du PAGM */
   private String pagmp;

   /** droit pour les formats de fichiers (PAGMf) du PAGM */
   private String pagmf;

   /** description du PAGM */
   private String description;

   /** valeurs des paramètres dynamiques du PRMD associé */
   private Map<String, String> parametres;

   /**
    * @return le code intelligible du PAGM
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           code intelligible du PAGM
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return le droit d'action (PAGMa) du PAGM
    */
   public final String getPagma() {
      return pagma;
   }

   /**
    * @param pagma
    *           droit d'action (PAGMa) du PAGM
    */
   public final void setPagma(String pagma) {
      this.pagma = pagma;
   }

   /**
    * @return le droit des formats de fichiers (PAGMf) du PAGM
    */
   public final String getPagmf() {
      return pagmf;
   }

   /**
    * @param pagmf
    *           le droit des formats de fichiers (PAGMf) du PAGM
    */
   public final void setPagmf(String pagmf) {
      this.pagmf = pagmf;
   }

   /**
    * @return le domaine d'action (PAGMp) du PAGM
    */
   public final String getPagmp() {
      return pagmp;
   }

   /**
    * @param pagmp
    *           domaine d'action (PAGMp) du PAGM
    */
   public final void setPagmp(String pagmp) {
      this.pagmp = pagmp;
   }

   /**
    * @return la description du PAGM
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           description du PAGM
    */
   public final void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return les valeurs des paramètres dynamiques du PRMD associé
    */
   public final Map<String, String> getParametres() {
      return parametres;
   }

   /**
    * @param parametres
    *           valeurs des paramètres dynamiques du PRMD associé
    */
   public final void setParametres(Map<String, String> parametres) {
      this.parametres = parametres;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof Pagm) {
         Pagm pagm = (Pagm) obj;
         areEquals = code.equals(pagm.getCode())
               && description.equals(pagm.getDescription())
               && pagma.equals(pagm.getPagma())
               && pagmf.equals(pagm.getPagmf())
               && pagmp.equals(pagm.getPagmp())
               && parametres.keySet().size() == pagm.getParametres().keySet()
                     .size()
               && parametres.keySet()
                     .containsAll(pagm.getParametres().keySet());

      }

      return areEquals;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public final String toString() {
      StringBuffer buffer = new StringBuffer();
      for (String key : parametres.keySet()) {
         buffer.append(key + " = " + parametres.get(key) + "\n");
      }

      return "code : " + code + "\n" + "description : " + description + "\n"
            + "pagma : " + pagma + "\n" + "pagmf : " + pagmf + "\n"
            + "pagmp : " + pagmp + "\n" + "liste des parametres :\n"
            + buffer.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

}
