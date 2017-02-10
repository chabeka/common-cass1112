package fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;


/**
 * Classe de comparaison entre deux résultats de recherche (pour les trier)
 */
public final class ResultatRechercheComparator implements Comparator<ResultatRechercheType> {

   
   /**
    * Type de comparaison
    */
   public enum TypeComparaison {
      
      /**
       * DateCreation croissante
       */
      DateCreation,
      
      /**
       * NumeroRecours croissant
       */
      NumeroRecours;
   }
   
   
   
   private final TypeComparaison typeComparaison ;
   
   
   private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.FRENCH);
      
      
   /**
    * Constructeur
    * 
    * @param typeComparaison le type de comparaison
    */
   public ResultatRechercheComparator(TypeComparaison typeComparaison) {
      this.typeComparaison = typeComparaison;
      
   }
   
   
   private String getValeurMetadonnee(
         ResultatRechercheType resultatRecherche,
         String code) {
      
      String result = null;
      
      if (  (resultatRecherche!=null) && 
            (resultatRecherche.getMetadonnees()!=null) && 
            (resultatRecherche.getMetadonnees().getMetadonnee()!=null)) {
         
         MetadonneeType[] metas = resultatRecherche.getMetadonnees().getMetadonnee();
         
         for (MetadonneeType meta:metas) {
            
            if (meta.getCode().getMetadonneeCodeType().equals(code)) {
               
               result = meta.getValeur().getMetadonneeValeurType();
               
               break;
               
            }
            
         }
         
      }
      
      return result;
      
   }
      
   
   private Date getDateCreation(ResultatRechercheType resultatRecherche) {
      
      Date result = null;
      
      String dateCreation = getValeurMetadonnee(
            resultatRecherche,
            "DateCreation");
      
      if (StringUtils.isNotBlank(dateCreation)) {
         try {
            result = dateFormat.parse(dateCreation);
         } catch (ParseException e) {
            throw new IntegrationRuntimeException(
                  "Erreur lors la création d'un objet Date à partir de la métadonnée DateCreation dont la valeur est \"" + dateCreation + "\"",
                  e);
         }
      }
      
      return result;
      
   }
   
   
   private Integer getNumeroRecours(ResultatRechercheType resultatRecherche) {
      
      Integer result = null;
      
      String numeroRecours = getValeurMetadonnee(
            resultatRecherche,
            "NumeroRecours");
      
      if (StringUtils.isNotBlank(numeroRecours)) {
         result = Integer.parseInt(numeroRecours);
      }
      
      return result;
      
   }
   
   
   @Override
   public int compare(ResultatRechercheType objet1, ResultatRechercheType objet2) {
      
      int result;
      
      if (typeComparaison.equals(TypeComparaison.DateCreation)) {
         result = compareSurDateCreation(objet1, objet2);
      } else if (typeComparaison.equals(TypeComparaison.NumeroRecours)) {
         result = compareSurNumeroRecours(objet1, objet2);
      } else {
         throw new IntegrationRuntimeException("Le type de comparaison " + typeComparaison + " n'est pas supporté.");
      }
      
      return result;
      
   }
   
   
   
   @SuppressWarnings("unchecked")
   private int compare(Comparable objet1, Comparable object2) {
      
      // Compare
      int result;
      if ((objet1==null) && (object2==null)) {
         result = 0;
      } else if (objet1==null) {
         result = 1;
      } else if (object2==null) {
         result = -1;
      } else {
         result = objet1.compareTo(object2);
      }
      
      // Renvoie le résultat
      return result;
      
   }
   
   
   
   private int compareSurDateCreation(
         ResultatRechercheType objet1, 
         ResultatRechercheType objet2) {
      
      Date date1 = getDateCreation(objet1);
      Date date2 = getDateCreation(objet2);
      
      return compare(date1,date2);
      
   }
   
   
   private int compareSurNumeroRecours(
         ResultatRechercheType objet1, 
         ResultatRechercheType objet2) {
      
      Integer num1 = getNumeroRecours(objet1);
      Integer num2 = getNumeroRecours(objet2);
      
      return compare(num1,num2);
      
   }

   
}
