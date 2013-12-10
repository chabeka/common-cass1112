package fr.urssaf.image.sae.droit.model;

import java.util.Map;

/**
 * Classe représentant un PAGM
 * 
 * 
 */
public class SaePagm {

   /**
    * Le nom du PAGM
    */
   private String code;

   /**
    * Description du PAGM
    */
   private String description;

   /**
    * PAGMa rattaché au PAGM
    */
   private SaePagma pagma;

   /**
    * PAGMp rattaché au PAGM
    */
   private SaePagmp pagmp;

   /**
    * valeurs des paramètres dynamiques du PRMD associé
    **/
   private Map<String, String> parametres;

   /**
    * @return the code
    */
   public String getCode() {
      return code;
   }

   /**
    * @param code
    *           the code to set
    */
   public void setCode(String code) {
      this.code = code;
   }

   /**
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param description
    *           the description to set
    */
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return the pagma
    */
   public SaePagma getPagma() {
      return pagma;
   }

   /**
    * @param pagma
    *           the pagma to set
    */
   public void setPagma(SaePagma pagma) {
      this.pagma = pagma;
   }

   /**
    * @return the pagmp
    */
   public SaePagmp getPagmp() {
      return pagmp;
   }

   /**
    * @param pagmp
    *           the pagmp to set
    */
   public void setPagmp(SaePagmp pagmp) {
      this.pagmp = pagmp;
   }

   /**
    * @return the parametres
    */
   public Map<String, String> getParametres() {
      return parametres;
   }

   /**
    * @param parametres
    *           the parametres to set
    */
   public void setParametres(Map<String, String> parametres) {
      this.parametres = parametres;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof SaePagm) {
         SaePagm pagm = (SaePagm) obj;
         areEquals = code.equals(pagm.getCode())
               && description.equals(pagm.getDescription())
               && pagma.equals(pagm.getPagma())
               && pagmp.equals(pagm.getPagmp());

         if (!(parametres == null && pagm.getParametres() == null)) {
            if (parametres != null && pagm.getParametres() != null) {
               areEquals = areEquals
                     && parametres.keySet().size() == pagm.getParametres()
                           .keySet().size()
                     && parametres.keySet().containsAll(
                           pagm.getParametres().keySet());
            }
         }
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
            + "pagma : " + pagma + "\n" + "pagmp : " + pagmp + "\n"
            + "liste des parametres :\n" + buffer.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

}
