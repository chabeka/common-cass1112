package fr.urssaf.image.sae.webservices.factory;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeValeurType;

/**
 * Classe d'instanciation du modèle contenu dans la classe
 * {@link fr.urssaf.image.sae.webservices.modele.SaeServiceStub}
 * 
 * 
 */
public final class ObjectModeleFactory {

   private ObjectModeleFactory() {

   }

   /**
    * 
    * @param code
    *           code de la métadonnée
    * @return instance de {@link MetadonneeCodeType}
    */
   public static MetadonneeCodeType createMetadonneeCodeType(String code) {

      MetadonneeCodeType codeType = new MetadonneeCodeType();
      codeType.setMetadonneeCodeType(code);

      return codeType;
   }

   /**
    * 
    * @param valeur
    *           valeur de la metadonnée
    * @return instance de {@link MetadonneeValeurType}
    */
   public static MetadonneeValeurType createMetadonneeValeurType(String valeur) {

      MetadonneeValeurType valeurType = new MetadonneeValeurType();
      valeurType.setMetadonneeValeurType(valeur);

      return valeurType;
   }

   /**
    * 
    * @return instance de {@link MetadonneeType}
    */
   public static MetadonneeType createMetadonneeType() {

      MetadonneeType type = new MetadonneeType();

      return type;
   }
}
