//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.02.24 à 03:57:27 PM CET 
//


package fr.urssaf.image.parser_opencsv.jaxb.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de documents non archivés
 * 
 * <p>Classe Java pour listeNonIntegratedVirtualDocumentsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeNonIntegratedVirtualDocumentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nonIntegratedVirtualDocument" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedVirtualDocumentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeNonIntegratedVirtualDocumentsType", propOrder = {
                                                                       "nonIntegratedVirtualDocument"
})
public class ListeNonIntegratedVirtualDocumentsType {

   protected List<NonIntegratedVirtualDocumentType> nonIntegratedVirtualDocument;

   /**
    * Gets the value of the nonIntegratedVirtualDocument property.
    * 
    * <p>
    * This accessor method returns a reference to the live list,
    * not a snapshot. Therefore any modification you make to the
    * returned list will be present inside the JAXB object.
    * This is why there is not a <CODE>set</CODE> method for the nonIntegratedVirtualDocument property.
    * 
    * <p>
    * For example, to add a new item, do as follows:
    * <pre>
    *    getNonIntegratedVirtualDocument().add(newItem);
    * </pre>
    * 
    * 
    * <p>
    * Objects of the following type(s) are allowed in the list
    * {@link NonIntegratedVirtualDocumentType }
    * 
    * 
    */
   public List<NonIntegratedVirtualDocumentType> getNonIntegratedVirtualDocument() {
      if (nonIntegratedVirtualDocument == null) {
         nonIntegratedVirtualDocument = new ArrayList<>();
      }
      return nonIntegratedVirtualDocument;
   }

}
