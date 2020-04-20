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
    * PAGMf rattaché au PAGM
    */
   private SaePagmf pagmf;

   /**
    * valeurs des paramètres dynamiques du PRMD associé
    **/
   private Map<String, String> parametres;
   
   /**
    * Flag indiquant que la compression est active.
    */
   private Boolean compressionPdfActive;
   
   /**
    * Nombre d'octet à partir duqel la compression de pdf est effectuée si la
    * compression est activée.
    */
   private Integer seuilCompressionPdf;

   /**
    * @return the code
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           the code to set
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return the description
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           the description to set
    */
   public final void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return the pagma
    */
   public final SaePagma getPagma() {
      return pagma;
   }

   /**
    * @param pagma
    *           the pagma to set
    */
   public final void setPagma(SaePagma pagma) {
      this.pagma = pagma;
   }

   /**
    * @return the pagmp
    */
   public final SaePagmp getPagmp() {
      return pagmp;
   }

   /**
    * @param pagmp
    *           the pagmp to set
    */
   public final void setPagmp(SaePagmp pagmp) {
      this.pagmp = pagmp;
   }

   /**
    * @return the parametres
    */
   public final Map<String, String> getParametres() {
      return parametres;
   }

   /**
    * @param parametres
    *           the parametres to set
    */
   public final void setParametres(Map<String, String> parametres) {
      this.parametres = parametres;
   }
   
   /**
    * @return le flag indiquant si la compression est active.
    */
   public final Boolean getCompressionPdfActive() {
      return compressionPdfActive;
   }

   /**
    * @param compressionPdfActive
    *           flag indiquant si la compression de pdf doit etre activee
    */
   public final void setCompressionPdfActive(Boolean compressionPdfActive) {
      this.compressionPdfActive = compressionPdfActive;
   }

   /**
    * @return le seuil a partir duquel il faut compressé les pdf si la
    *         compression est active
    */
   public final Integer getSeuilCompressionPdf() {
      return seuilCompressionPdf;
   }

   /**
    * @param seuilCompressionPdf
    *           le seuil a partir duquel il faut compressé les pdf si la
    *           compression est active
    */
   public final void setSeuilCompressionPdf(Integer seuilCompressionPdf) {
      this.seuilCompressionPdf = seuilCompressionPdf;
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

         if (!(pagmf == null && pagm.getPagmf() == null)) {
            if (pagmf != null && pagm.getPagmf() != null) {
               areEquals = areEquals && pagmf.equals(pagm.getPagmf());
            } else {
               return false;
            }
         }

         if (!(parametres == null && pagm.getParametres() == null)) {
            if (parametres != null && pagm.getParametres() != null) {
               areEquals = areEquals
                     && parametres.keySet().size() == pagm.getParametres()
                           .keySet().size()
                     && parametres.keySet().containsAll(
                           pagm.getParametres().keySet());
            } else {
               return false;
            }
         }
         
         if (!(compressionPdfActive == null && pagm.getCompressionPdfActive() == null)) {
            if (compressionPdfActive != null && pagm.getCompressionPdfActive() != null) {
               areEquals = areEquals && compressionPdfActive.equals(pagm.getCompressionPdfActive());
            } else {
               return false;
            }
         }
         
         if (!(seuilCompressionPdf == null && pagm.getSeuilCompressionPdf() == null)) {
            if (seuilCompressionPdf != null && pagm.getSeuilCompressionPdf() != null) {
               areEquals = areEquals && seuilCompressionPdf.equals(pagm.getSeuilCompressionPdf());
            } else {
               return false;
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
      if (parametres != null) {
         for (String key : parametres.keySet()) {
            buffer.append(key + " = " + parametres.get(key) + "\n");
         }
      }

      String message = "code : " + code + "\n" + "description : " + description
            + "\n" + "pagma : " + pagma + "\n" + "pagmp : " + pagmp + "\n"
            + "liste des parametres :\n" + buffer.toString();

      if (pagmf != null) {
         message += "pagmf : " + pagmf + "\n";
      }
      
      if (compressionPdfActive != null) {
         message += "compressionPdfActive : " + compressionPdfActive + "\n";
      }
      
      if (seuilCompressionPdf != null) {
         message += "seuilCompressionPdf : " + seuilCompressionPdf + "\n";
      }

      return message;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

   /**
    * @return the pagmf
    */
   public final SaePagmf getPagmf() {
      return pagmf;
   }

   /**
    * @param pagmf
    *           the pagmf to set
    */
   public final void setPagmf(SaePagmf pagmf) {
      this.pagmf = pagmf;
   }

}
