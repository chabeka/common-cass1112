/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.modele.ecde;

/**
 * 
 * 
 */
public class EcdeTest {

   /**
    * Nom du cas de test
    */
   private String name;

   /**
    * url du fichier
    */
   private String url;

   /**
    * constructeur par défaut
    */
   public EcdeTest() {

   }

   /**
    * constructeur
    * 
    * @param name
    *           nom du cas de test
    * @param url
    *           url du cas de test sous forme ecde://
    */
   public EcdeTest(String name, String url) {
      super();
      this.name = name;
      this.url = url;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the url
    */
   public String getUrl() {
      return url;
   }

   /**
    * @param url
    *           the url to set
    */
   public void setUrl(String url) {
      this.url = url;
   }

}
