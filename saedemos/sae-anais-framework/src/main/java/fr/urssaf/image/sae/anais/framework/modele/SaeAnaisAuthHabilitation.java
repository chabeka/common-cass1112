package fr.urssaf.image.sae.anais.framework.modele;

/**
 * Représente une habilitation dans ANAIS, ramené au modèle SAE
 */
public class SaeAnaisAuthHabilitation {

   private String code;
   private String typeHab;
   private String codeOrga;
   private String codeIr;

   /**
    * Code de l'habilitation
    * 
    * @return Code de l'habilitation
    */
   public final String getCode() {
      return code;
   }

   /**
    * Code de l'habilitation
    * 
    * @param code
    *           Code de l'habilitation
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * Type de l'habilitation
    * 
    * @return Type de l'habilitation
    */
   public final String getTypeHab() {
      return typeHab;
   }

   /**
    * Type de l'habilitation
    * 
    * @param typeHab
    *           Type de l'habilitation
    */
   public final void setTypeHab(String typeHab) {
      this.typeHab = typeHab;
   }

   /**
    * Code organisme sur lequel porte l'habilitation (peut être vide)
    * 
    * @return Code organisme sur lequel porte l'habilitation (peut être vide)
    */
   public final String getCodeOrga() {
      return codeOrga;
   }

   /**
    * Code organisme sur lequel porte l'habilitation (peut être vide)
    * 
    * @param codeOrga
    *           Code organisme sur lequel porte l'habilitation (peut être vide)
    */
   public final void setCodeOrga(String codeOrga) {
      this.codeOrga = codeOrga;
   }

   /**
    * Code inter-région sur laquelle porte l'habilitation (peut être vide)
    * 
    * @return Code inter-région sur laquelle porte l'habilitation (peut être
    *         vide)
    */
   public final String getCodeIr() {
      return codeIr;
   }

   /**
    * Code inter-région sur laquelle porte l'habilitation (peut être vide)
    * 
    * @param codeIr
    *           Code inter-région sur laquelle porte l'habilitation (peut être
    *           vide)
    */
   public final void setCodeIr(String codeIr) {
      this.codeIr = codeIr;
   }

}
