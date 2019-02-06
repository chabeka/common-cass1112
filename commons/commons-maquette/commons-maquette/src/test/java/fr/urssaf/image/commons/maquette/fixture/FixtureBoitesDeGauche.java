package fr.urssaf.image.commons.maquette.fixture;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.definition.ILeftCol;
import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;

/**
 * Implémentation de la colonne de gauche, pour les tests unitaires
 *
 */
@SuppressWarnings("PMD")
public final class FixtureBoitesDeGauche implements ILeftCol {

   
   public static final String REQUEST_HEADER_POUR_AVOIR_DES_BOITES = "avec_boites";
   

   
   private Boolean isAvecBoitesGauche(HttpServletRequest request) {
      return !StringUtils.isEmpty(request.getHeader(REQUEST_HEADER_POUR_AVOIR_DES_BOITES)) ;
   }
   
   
   @Override
   public String getLienDeconnexion(HttpServletRequest request) {
      
      if (isAvecBoitesGauche(request)) {
         return "deconnexion.do";
      }
      else {
         return null;
      }
      
   }

   @Override
   public String getNomApplication(HttpServletRequest request) {
      
      if (isAvecBoitesGauche(request)) {
         return "nom appli <é>";
      }
      else {
         return null;
      }
      
   }

   @Override
   public String getNomUtilisateur(HttpServletRequest request) {
      
      if (isAvecBoitesGauche(request)) {
         return "Utilisateur 1 <é>";
      }
      else {
         return null;
      }
      
   }

   @Override
   public String getRoleUtilisateur(HttpServletRequest request) {
      
      if (isAvecBoitesGauche(request)) {
         return "Consultation <à>";
      }
      else {
         return null;
      }
      
   }

   @Override
   public String getVersionApplication(HttpServletRequest request) {
      
      if (isAvecBoitesGauche(request)) {
         return "0.1a <à>";  
      }
      else {
         return null;
      }
      
   }
   
   
   @Override
   public List<InfoBoxItem> getInfoBox(HttpServletRequest request) {
      
      if (isAvecBoitesGauche(request)) {
         
         // Création de la liste résultat
         List<InfoBoxItem> result = new ArrayList<InfoBoxItem>();
         
         // 1 boîte
         InfoBoxItem item1 = new InfoBoxItem(
               "boiteGauchePerso1",
               "Title Boîte 1",
               "Desc Boîte 1");
         item1.setContent("contenu 1");
         result.add(item1);
         
         // 1 autre boîte
         InfoBoxItem item2 = new InfoBoxItem(
               "boiteGauchePerso2",
               "Title Boîte 2",
               "Desc Boîte 2");
         item2.setContent("contenu 2");
         result.add(item2);
         
         // Renvoie la liste des boîtes
         return result;
         
      }
      else {
         return null;
      }
      
   }

}
