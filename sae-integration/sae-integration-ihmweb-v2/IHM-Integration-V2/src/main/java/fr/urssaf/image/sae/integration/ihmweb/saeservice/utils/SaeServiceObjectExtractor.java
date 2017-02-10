package fr.urssaf.image.sae.integration.ihmweb.saeservice.utils;

import org.apache.commons.lang.ArrayUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.service.MetadonneeValeurListService;


/**
 * Méthodes qui permettent de récupérer certaines parties des réponses des opérations
 * du service web SaeService, et éventuellement de les convertir en des objets plus
 * facilement manipulables que ceux de la couche web service
 */
public final class SaeServiceObjectExtractor {

   
   private SaeServiceObjectExtractor() {
      
   }
   

   /**
    * Extrait les métadonnées d'une réponse de l'opération "consultation"
    * 
    * @param consultResponse la réponse à l'opération "consultation"
    * @return la liste des métadonnées renvoyées par l'opération
    */
   public static MetadonneeValeurList extraitMetadonnees(ConsultationResponse consultResponse) {
      
      MetadonneeValeurList result = new MetadonneeValeurList() ; 
      
      if ((consultResponse!=null) && (consultResponse.getConsultationResponse()!=null)) { 
         
         result = extraitMetadonnees(
                  consultResponse.getConsultationResponse().getMetadonnees());
            
      }
      
      return result;
      
   }
   
   
   
//   /**
//    * Convertit une liste de métadonnées (code + valeur) d'un objet de la couche web service
//    * vers une liste de la couche modèle.  
//    * 
//    * @param liste la liste des métadonnnées (code + valeur) de la couche web service
//    * @return la liste des métadonnées (code + valeur) pour la couche modèle
//    */
   public static MetadonneeValeurList extraitMetadonnees(
         ListeMetadonneeType liste) {
      
      MetadonneeValeurList result = new MetadonneeValeurList();
      
      if ((liste!=null) && (ArrayUtils.isNotEmpty(liste.getMetadonnee())))  {
         
         for (MetadonneeType meta: liste.getMetadonnee()) {
            
            MetadonneeValeurListService.ajouteMetadonnee(
                  result, 
                  meta.getCode().getMetadonneeCodeType(), 
                  meta.getValeur().getMetadonneeValeurType());
            
         }
         
      }
      
      return result;
      
   }
   
   
   /**
    * Extrait les métadonnées d'un objet "résultat de recherche" de la couche web service
    * sous la forme d'une liste de métadonnées de la couche modèle
    * 
    * @param resultatRecherche l'objet "résultat de recherche" de la couche web service
    * @return l'objet "liste de métadonnées" de la couche modèle
    */
   public static MetadonneeValeurList extraitMetadonnees(
         ResultatRechercheType resultatRecherche) {
      
      MetadonneeValeurList result ;
      
      if (resultatRecherche==null) {
         result = new MetadonneeValeurList();
      } else {
         result = extraitMetadonnees(resultatRecherche.getMetadonnees());
      }
         
      return result;
      
   }
   
}
