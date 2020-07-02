//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.02.24 à 03:57:27 PM CET 
//


package fr.urssaf.image.parser_opencsv.jaxb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Définition d'un fichier
 * 
 * <p>Classe Java pour fichierType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="fichierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cheminEtNomDuFichier">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fichierType", propOrder = {
                                            "cheminEtNomDuFichier"
})
public class FichierType {

   @XmlElement(required = true)
   protected String cheminEtNomDuFichier;

   protected String path;

   /**
    * @return the path
    */
   public String getPath() {
      return path;
   }

   /**
    * @param path
    *           the path to set
    */
   public void setPath(final String path) {
      this.path = path;
   }

   /**
    * Obtient la valeur de la propriété cheminEtNomDuFichier.
    * 
    * @return
    *         possible object is
    *         {@link String }
    */
   public String getCheminEtNomDuFichier() {
      return cheminEtNomDuFichier;
   }

   /**
    * Définit la valeur de la propriété cheminEtNomDuFichier.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setCheminEtNomDuFichier(final String value) {
      cheminEtNomDuFichier = value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "FichierType [cheminEtNomDuFichier=" + cheminEtNomDuFichier + "]";
   }

}
