package fr.urssaf.image.sae.integration.ihmweb.service;

import org.apache.commons.collections.CollectionUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;

/**
 * Service de manipulation des objets de type MetadonneeValeurList
 */
public final class MetadonneeValeurListService {

   
   
   private MetadonneeValeurListService() {
      
   }

   
   /**
    * Ajoute une métadonnée dans la liste
    * 
    * @param liste la liste de métadonnées
    * @param code le code de la métadonnée de la nouvelle métadonnée à ajouter à la liste
    * @param valeur la valeur de la métadonnée de la nouvelle métadonnée à ajouter à la liste
    */
   public static void ajouteMetadonnee(
         MetadonneeValeurList liste,
         String code,
         String valeur) {
      
      liste.add(new MetadonneeValeur(code,valeur));
      
   }
   
   
   /**
    * Recherche une métadonnée dans la liste, à partir de son code
    * 
    * @param liste la liste de métadonnées dans laquelle rechercher
    * @param code le code de la métadonnnée à recherche
    * @return l'objet métadonnée si elle a été retrouvée, null dans le cas contraire
    */
   public static MetadonneeValeur find(
         MetadonneeValeurList liste,
         String code) {
      
      MetadonneeValeur result = null;
      
      if (CollectionUtils.isNotEmpty(liste)) {
         for(MetadonneeValeur meta : liste) {
            if (code.equals(meta.getCode())) {
               result = meta;
               break;
            }
         }
      }
      
      return result;
      
   }
   
}
