package fr.urssaf.image.sae.format.format.compatible.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Objet possédant les informations liées au différents formats.<br>
 * En effet pour un format on lie les formats compatibles.
 * 
 */
public class SaeFormatCompatible {

   private final Map<String, List<String>> formatsCompatibles = new HashMap<String, List<String>>();

   /***
    * Ajoute un idFormat à la MAP.
    * 
    * @param idFormat
    *           : identifiant du format
    * @param idFormatsCompatibles
    *           : liste des idFormats compatibles
    */
   public final void addFormatsCompatible(String idFormat,
         List<String> idFormatsCompatibles) {
      formatsCompatibles.put(idFormat, idFormatsCompatibles);
   }

   /**
    * Récupère les idFormats du fichier properties.
    * 
    * @param idFormat
    *           : identifiant du format
    * @return liste formatsCompatibles
    */
   public final List<String> getFormatsCompatibles(String idFormat) {
      return formatsCompatibles.get(idFormat);
   }

}
