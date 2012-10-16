package fr.urssaf.image.commons.maquette.fixture;


/**
 * Classe utilitaire pour les tests unitaires<br>
 * <br>
 * Représente un cas de test de match de pattern d'URI
 */
@SuppressWarnings("PMD")
public final class UriPatternTestItem {
   
   /**
    * Le pattern d'URI
    */
   private String pattern;
   
   
   /**
    * L'URI à tester par rapport au pattern
    */
   private String uri;
   
   
   /**
    * Le résultat attendu du matchage de l'URI avec le pattern
    */
   private Boolean resultatAttendu;
   
   
   /**
    * Constructeur sans argument
    */
   public UriPatternTestItem() {
      // rien à faire
   }
   
   /**
    * Constructeur
    * @param pattern Le pattern d'URI
    * @param uri L'URI à tester par rapport au pattern
    * @param resultatAttendu Le résultat attendu du matchage de l'URI avec le pattern
    */
   public UriPatternTestItem(String pattern,String uri,Boolean resultatAttendu) {
      this.pattern = pattern;
      this.uri = uri;
      this.resultatAttendu = resultatAttendu;
   }

   /**
    * Renvoie le pattern d'URI
    * @return Le pattern d'URI
    */
   public String getPattern() {
      return pattern;
   }

   /**
    * Définit le pattern d'URI
    * @param pattern Le pattern d'URI
    */
   public void setPattern(String pattern) {
      this.pattern = pattern;
   }

   /**
    * Renvoie l'URI à tester par rapport au pattern
    * @return L'URI à tester par rapport au pattern
    */
   public String getUri() {
      return uri;
   }

   /**
    * Définit l'URI à tester par rapport au pattern
    * @param uri L'URI à tester par rapport au pattern
    */
   public void setUri(String uri) {
      this.uri = uri;
   }

   /**
    * Renvoie le résultat attendu du matchage de l'URI avec le pattern
    * @return Le résultat attendu du matchage de l'URI avec le pattern
    */
   public Boolean getResultatAttendu() {
      return resultatAttendu;
   }

   /**
    * Définit le résultat attendu du matchage de l'URI avec le pattern
    * @param resultatAttendu Le résultat attendu du matchage de l'URI avec le pattern
    */
   public void setResultatAttendu(Boolean resultatAttendu) {
      this.resultatAttendu = resultatAttendu;
   }
   
}
