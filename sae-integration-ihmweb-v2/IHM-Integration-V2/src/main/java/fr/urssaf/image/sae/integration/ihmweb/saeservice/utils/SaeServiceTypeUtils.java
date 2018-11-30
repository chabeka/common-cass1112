package fr.urssaf.image.sae.integration.ihmweb.saeservice.utils;

import javax.activation.DataHandler;

import org.apache.commons.lang.ArrayUtils;

import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeValeurType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ObjetNumeriqueConsultationType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.UuidType;


/**
 * Méthodes utilitaires pour les types d'objets du stub de SaeService
 */
public final class SaeServiceTypeUtils {

   private SaeServiceTypeUtils() {
      
   }
   
   
   /**
    * Ajoute une métadonnées à une liste des métadonnées de la couche WebService
    * 
    * @param metadonnees la liste de métadonnées de la couche 
    * @param code le code de la métadonnée à ajouter
    * @param valeur la valeur de la métadonnée à ajouter
    */
   public static void addMetadonnee(
         ListeMetadonneeType metadonnees,
         String code,
         String valeur) {
      
      MetadonneeType metaType = new MetadonneeType();
      
      MetadonneeCodeType metaCodeType = new MetadonneeCodeType() ;
      metaCodeType.setMetadonneeCodeType(code);
      metaType.setCode(metaCodeType);
      
      MetadonneeValeurType metaValeurType = new MetadonneeValeurType();
      metaValeurType.setMetadonneeValeurType(valeur);
      metaType.setValeur(metaValeurType);
      
      metadonnees.addMetadonnee(metaType);
      
   }
   
   
   /**
    * Récupère l'UUID au format chaîne de caractères depuis un objet de la couche WebService
    * 
    * @param uuid l'UUID de la couche WebService
    * @return l'UUID sous forme de chaîne de caractères
    */
   public static String extractUuid(UuidType uuid) {
      
      return uuid.getUuidType();
      
   }
   
   
   /**
    * Récupère l'objet numérique contenu dans un réponse de consultation
    * 
    * @param consultResponse l'objet réponse de l'opération consultation
    * @return l'objet permettant d'accèder au flux binaire
    */
   public static DataHandler getObjetNumeriqueAsContenu(
         ConsultationResponseType consultResponse) {
      
      return consultResponse.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();
      
   }
   
   
   /**
    * Renvoie un boolean indiquant si l'objet numérique possède un contenu en base 64
    * 
    * @param objNum l'objet numérique
    * @return le boolean indiquant si l'objet numérique possède un contenu en base 64
    */
   public static boolean isObjetNumeriqueContenuBase64(
         ObjetNumeriqueConsultationType objNum) {
      
      boolean result = false;
      
      if (  (objNum!=null) && 
            (objNum.getObjetNumeriqueConsultationTypeChoice_type0()!=null) && 
            (objNum.getObjetNumeriqueConsultationTypeChoice_type0().getContenu()!=null)) {
         result = true;
      }
      
      return result ;
         
   }
   
   
   /**
    * Renvoie un boolean indiquant si l'objet numérique possède une URL de consultation directe
    * 
    * @param objNum l'objet numérique
    * @return le boolean indiquant si l'objet numérique possède une URL de consultation directe
    */
   public static boolean isObjetNumeriqueUrlConsultDirecte(
         ObjetNumeriqueConsultationType objNum) {
      
      boolean result = false;
      
      if (  (objNum!=null) && 
            (objNum.getObjetNumeriqueConsultationTypeChoice_type0()!=null) && 
            (objNum.getObjetNumeriqueConsultationTypeChoice_type0().getUrl()!=null)) {
         result = true;
      }
      
      return result ;
         
   }
   
   
   
   /**
    * Recherche une métadonnée à partir de son code long, dans une liste de métadonnées
    * 
    * @param codeLong le code long à recherche
    * @param metasFournies la liste de métadonnées dans laquelle rechercher
    * @return l'objet métadonnée si elle a été trouvée, ou null dans le cas contraire
    */
   @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
   public static MetadonneeType chercheMeta(
         String codeLong,
         ListeMetadonneeType metasFournies) {
      
      // Initialise le résultat
      MetadonneeType result = null;
      
      // Boucle sur la liste des métadonnées fournies
      if (metasFournies!=null) { 
         
         MetadonneeType[] metasFournies2 = metasFournies.getMetadonnee();
         
         if (ArrayUtils.isNotEmpty(metasFournies2)) {
            
            String codeLongMetaFourni;
            
            for(MetadonneeType metaFournie: metasFournies2) {
               codeLongMetaFourni = metaFournie.getCode().getMetadonneeCodeType();
               if (codeLongMetaFourni.equals(codeLong)) {
                  result = metaFournie;
                  break;
               }
            }
            
         }
      }
      
      // Renvoie du résultat
      return result;
      
   }
   
   
}
