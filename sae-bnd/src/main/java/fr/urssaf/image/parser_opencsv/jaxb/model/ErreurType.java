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
 * Une erreur, définie par un code et un libellé
 * 
 * <p>Classe Java pour erreurType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="erreurType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "erreurType", propOrder = {
                                           "code",
                                           "libelle"
})
public class ErreurType {

   @XmlElement(required = true)
   protected String code;
   @XmlElement(required = true)
   protected String libelle;

   /**
    * Obtient la valeur de la propriété code.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getCode() {
      return code;
   }

   /**
    * Définit la valeur de la propriété code.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setCode(final String value) {
      code = value;
   }

   /**
    * Obtient la valeur de la propriété libelle.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getLibelle() {
      return libelle;
   }

   /**
    * Définit la valeur de la propriété libelle.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setLibelle(final String value) {
      libelle = value;
   }

}
