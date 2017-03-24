package fr.urssaf.image.commons.maquette.template;

import java.util.Map;
import java.util.TreeMap;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;

import org.apache.commons.lang.StringEscapeUtils;


/**
 * Fonctions utilitaires pour le parsing des sources HTML
 *
 */
public final class MaquetteParserHelper {

   
   private MaquetteParserHelper() {
      
   }
   
   
   /**
    * Ecrit le contenu d'une balise dans un document HTML 
    * 
    * @param document le document HTML
    * @param balise la balise dont il faut écrire le contenu
    * @param contenu le contenu à écrire
    * @param escapeHtml un flag indiquant s'il faut transformer les caractères
    *        spéciaux en leurs équivalents HTML
    */
   protected static void ecritContenuBalise(
         OutputDocument document,
         final Element balise,
         final String contenu,
         final Boolean escapeHtml) {
      
      int start = balise.getContent().getBegin() ;
      int end = balise.getContent().getEnd() ;
      
      String contenuOk = contenu;
      if (escapeHtml) {
         contenuOk = StringEscapeUtils.escapeHtml(contenuOk);
      }
      
      document.replace(start, end, contenuOk) ;
      
   }
   

   /**
    * Ecrit la valeur d'un attribut d'une balise dans un document HTML
    * 
    * @param document le document HTML
    * @param balise la balise dans laquelle se trouve l'attribut à écrire
    * @param nomAttribut le nom de l'attribut à écrire
    * @param valeurAttribut la valeur de l'attribut
    * @param escapeHtml un flag indiquant s'il faut transformer les caractères
    *        spéciaux en leurs équivalents HTML
    */
   protected static void ecritAttributBalise(
         OutputDocument document,
         final Element balise,
         final String nomAttribut,
         final String valeurAttribut,
         final Boolean escapeHtml) {
      
      Attributes attributes = balise.getAttributes() ;
      
      int start = attributes.get(nomAttribut).getValueSegment().getBegin();
      int end = attributes.get(nomAttribut).getValueSegment().getEnd() ;
      
      String valeurAttributOk = valeurAttribut;
      if (escapeHtml) {
         valeurAttributOk = StringEscapeUtils.escapeHtml(valeurAttributOk);
      }
      
      document.replace(start, end, valeurAttributOk) ;
      
   }
   
   
   /**
    * Fusionne les attributs d'une balise source avec ceux fournis
    * en paramètres.
    * 
    * @param document le document HTML
    * @param baliseSource la balise source contenant les attributs
    * @param attrSupp les attributs supplémentaires
    */
   protected static void fusionneAttributs(
         OutputDocument document,
         final Element baliseSource,
         final Attributes attrSupp) {
      
      // Récupère les attributs de la balise source
      // dans une structure du parser
      Attributes sourceAttributes = baliseSource.getAttributes();
      
      // Fusionne les attributs de la balise source
      // avec ceux fournis en paramètres de la méthode
      // dans une Map
      // Création de la Map
      // Map<String,String> mapAttributs = new HashMap<String, String>();
      Map<String,String> mapAttributs = new TreeMap<String, String>();
      // Ajout des attributs de la balise source
      for(Attribute attr : sourceAttributes) {
         mapAttributs.put(attr.getKey(),attr.getValue());
      }
      // Ajout des attributs supplémentaires
      for(Attribute attr : attrSupp) {
         mapAttributs.put(attr.getKey(),attr.getValue());
      }
      
      // Met les attributs dans le document de sortie
      document.replace(sourceAttributes, mapAttributs);
      
   }
   
}
