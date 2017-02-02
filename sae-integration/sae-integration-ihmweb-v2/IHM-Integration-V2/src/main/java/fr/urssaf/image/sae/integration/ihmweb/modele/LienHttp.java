package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Un lien HTTP
 */
public class LienHttp {

   private String texte;
   private String url;
   
   /**
    * Texte du lien
    * 
    * @return Texte du lien
    */
   public final String getTexte() {
      return texte;
   }
   
   /**
    * Texte du lien
    * 
    * @param texte Texte du lien
    */
   public final void setTexte(String texte) {
      this.texte = texte;
   }
   
   
   /**
    * URL du lien
    * 
    * @return URL du lien
    */
   public final String getUrl() {
      return url;
   }
   
   
   /**
    * URL du lien
    * 
    * @param url URL du lien
    */
   public final void setUrl(String url) {
      this.url = url;
   }
   
   
   /**
    * Constructeur
    * 
    * @param texte texte du lien
    * @param url URL du lien
    */
   public LienHttp(String texte, String url) {
      this.texte = texte ;
      this.url = url;
   }
   
}

