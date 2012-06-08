package fr.urssaf.image.sae.vi.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Données extraites d'un VI de type "portail à portail"
 */
public class VIPortailContenuExtrait {

   private String issuer;

   private String nameId;

   private String audience;

   private List<String> pagmList = new ArrayList<String>();
   
   private String habAnais;

   /**
    * L'identifiant du contrat de service
    * 
    * @return L'identifiant du contrat de service
    */
   public final String getIssuer() {
      return issuer;
   }

   /**
    * L'identifiant du contrat de service
    * 
    * @param issuer
    *           L'identifiant du contrat de service
    */
   public final void setIssuer(String issuer) {
      this.issuer = issuer;
   }

   /**
    * L'idenfiant de l'utilisateur
    * 
    * @return L'idenfiant de l'utilisateur
    */
   public final String getNameId() {
      return nameId;
   }

   /**
    * L'idenfiant de l'utilisateur
    * 
    * @param nameId
    *           L'idenfiant de l'utilisateur
    */
   public final void setNameId(String nameId) {
      this.nameId = nameId;
   }

   /**
    * Le service visé pour l'utilisation du VI
    * 
    * @return Le service visé pour l'utilisation du VI
    */
   public final String getAudience() {
      return audience;
   }

   /**
    * Le service visé pour l'utilisation du VI
    * 
    * @param audience
    *           Le service visé pour l'utilisation du VI
    */
   public final void setAudience(String audience) {
      this.audience = audience;
   }

   /**
    * La liste des PAGM
    * 
    * @return La liste des PAGM
    */
   public final List<String> getPagmList() {
      return pagmList;
   }

   /**
    * La liste des PAGM
    * 
    * @param pagmList
    *           La liste des PAGM
    */
   public final void setPagmList(List<String> pagmList) {
      this.pagmList = pagmList;
   }
   
   /**
    * L'habilitation ANAIS
    * @return L'habilitation ANAIS
    */
   public final String getHabAnais() {
      return habAnais;
   }

   /**
    * L'habilitation ANAIS
    * @param habAnais L'habilitation ANAIS
    */
   public final void setHabAnais(String habAnais) {
      this.habAnais = habAnais;
   }

}
