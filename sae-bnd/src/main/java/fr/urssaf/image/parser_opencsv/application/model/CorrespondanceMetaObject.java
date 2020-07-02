package fr.urssaf.image.parser_opencsv.application.model;

/**
 * Table de correspondance entre Code caisse SSTI et code Organisme Gestionnaire GED
 */
public class CorrespondanceMetaObject {

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "CorrespondanceMetaObject [key=" + key + ", value=" + value + "]";
   }

   private String key;


   private String value;

   /**
    * 
    */
   public CorrespondanceMetaObject() {
      super();
   }

   /**
    * @param codeCaisse
    * @param codeOrganismeProprietaire
    */
   public CorrespondanceMetaObject(final String key, final String value) {
      super();
      this.key = key;
      this.value = value;
   }

   /**
    * @return the codeCaisse
    */
   public String getKey() {
      return key;
   }

   /**
    * @return the codeOrganismeProprietaire
    */
   public String getValue() {
      return value;
   }

}
