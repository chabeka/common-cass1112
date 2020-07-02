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
 * Une métadonnée, définie par un code et une valeur
 * 
 * <p>Classe Java pour metadonneeType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="metadonneeType">
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
 *         &lt;element name="valeur" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadonneeType", propOrder = {
                                               "code",
                                               "valeur"
})
public class MetadonneeType {

   @XmlElement(required = true)
   protected String code;
   @XmlElement(required = true)
   protected String valeur;

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
    * Obtient la valeur de la propriété valeur.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getValeur() {
      return valeur;
   }

   /**
    * Définit la valeur de la propriété valeur.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setValeur(final String value) {
      valeur = value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (code == null ? 0 : code.hashCode());
      result = prime * result + (valeur == null ? 0 : valeur.hashCode());
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final MetadonneeType other = (MetadonneeType) obj;
      if (code == null) {
         if (other.code != null) {
            return false;
         }
      } else if (!code.equals(other.code)) {
         return false;
      }
      if (valeur == null) {
         if (other.valeur != null) {
            return false;
         }
      } else if (!valeur.equals(other.valeur)) {
         return false;
      }
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "MetadonneeType [code=" + code + ", valeur=" + valeur + "]";
   }

}
