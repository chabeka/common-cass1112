package fr.urssaf.image.sae.webdemo.modele;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import fr.urssaf.image.sae.webdemo.resource.CustomDateSerializer;

/**
 * Modèlisation des traces du service d'exploitation<br>
 * 
 * 
 */
public class Log {

   private long idseq;

   private Date horodatage;

   private int occurences;

   private String probleme;

   private String action;

   private String infos;

   /**
    * @return identifiant de séquentialité
    */
   public long getIdseq() {
      return idseq;
   }

   /**
    * @param idseq
    *           identifiant de séquentialité
    */
   public void setIdseq(long idseq) {
      this.idseq = idseq;
   }

   /**
    * @return horodatage
    */
   @JsonSerialize(using = CustomDateSerializer.class)
   public Date getHorodatage() {
      return (Date) horodatage.clone();
   }

   /**
    * @param horodatage
    *           horodatage
    */
   public void setHorodatage(Date horodatage) {
      this.horodatage = (Date) horodatage.clone();
   }

   /**
    * @return nombre d'occurrence du problème
    */
   public int getOccurences() {
      return occurences;
   }

   /**
    * @param occurences
    *           nombre d'occurrence du problème
    */
   public void setOccurences(int occurences) {
      this.occurences = occurences;
   }

   /**
    * @return libellé du problème survenu
    */
   public String getProbleme() {
      return probleme;
   }

   /**
    * @param probleme
    *           libellé du problème survenu
    */
   public void setProbleme(String probleme) {
      this.probleme = probleme;
   }

   /**
    * @return code de l'action à réaliser pour corriger le problème
    */
   public String getAction() {
      return action;
   }

   /**
    * @param action
    *           code de l'action à réaliser pour corriger le problème
    */
   public void setAction(String action) {
      this.action = action;
   }

   /**
    * @return informations complémentaires
    */
   public String getInfos() {
      return infos;
   }

   /**
    * @param infos
    *           informations complémentaires
    */
   public void setInfos(String infos) {
      this.infos = infos;
   }

}
