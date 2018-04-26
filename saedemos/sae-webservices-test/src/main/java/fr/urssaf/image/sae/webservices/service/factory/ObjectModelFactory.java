package fr.urssaf.image.sae.webservices.service.factory;

import fr.urssaf.image.sae.webservices.service.model.Metadata;

/**
 * Classe d'instanciation du package model pour les services
 * 
 * 
 */
public final class ObjectModelFactory {

   private ObjectModelFactory() {

   }

   /**
    * 
    * @param code
    *           code de la métadonnée
    * @param value
    *           valeur de la métadonnée
    * @return instance d'une métadonnée
    */
   public static Metadata createMetadata(String code, String value) {

      Metadata metadata = new Metadata();
      metadata.setCode(code);
      metadata.setValue(value);

      return metadata;

   }
}
