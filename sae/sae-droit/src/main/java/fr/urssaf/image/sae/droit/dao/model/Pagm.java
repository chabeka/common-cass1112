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

}
