package fr.urssaf.image.sae.integration.ihmweb.formulaire;


public class TraceFormulaire {

   private String titre;
   private String action;
   private String url;
   private String infoPopUpUrl;
   private String popUpAction;
   
   public String getAction() {
      return action;
   }
   public void setAction(String action) {
      this.action = action;
   }
   public String getUrl() {
      return url;
   }
   public void setUrl(String url) {
      this.url = url;
   }
   public String getTitre() {
      return titre;
   }
   public void setTitre(String titre) {
      this.titre = titre;
   }
   public String getInfoPopUpUrl() {
      return infoPopUpUrl;
   }
   public void setInfoPopUpUrl(String infoPopUpUrl) {
      this.infoPopUpUrl = infoPopUpUrl;
   }
   public String getPopUpAction() {
      return popUpAction;
   }
   public void setPopUpAction(String popUpAction) {
      this.popUpAction = popUpAction;
   }
}
