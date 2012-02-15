package fr.urssaf.image.sae.ecde.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
/**
 * Classe permettant de definir les balises des fichiers XML générés.
 * Ainsi nous pouvons initialiser nous mêmes les balises. * 
 *
 */
public class JAXBNamespaceMapper extends NamespacePrefixMapper {
   
   private static final String RESULTAT_XML = "http://www.cirtil.fr/sae/resultatsXml";
   private static final String COMMUN_RES_SOM = "http://www.cirtil.fr/sae/commun_sommaire_et_resultat";
   
   /**
    * Methode permettant de definir les balises correspondant au différents url
    * 
    * @param namespaceUri uri en question
    * @param suggestion null
    * @param requirePrefix boolean toujours a true
    * 
    * @return String qui correspond a la balise en question
    */
   @Override
   public final String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
      String resultat = namespaceUri; 
      if (namespaceUri.equals(RESULTAT_XML)) {
         resultat = "res";
      } else if (namespaceUri.equals(COMMUN_RES_SOM)) {
         resultat = "somres";
      } 
      return resultat;
   }
}