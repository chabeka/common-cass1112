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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Définition d'un document virtuel non archivé
 * 
 * <p>Classe Java pour nonIntegratedVirtualDocumentType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="nonIntegratedVirtualDocumentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/>
 *         &lt;element name="erreurs" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeErreurType" minOccurs="0"/>
 *         &lt;element name="composants">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="composant" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedComposantDocumentVirtuelType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "nonIntegratedVirtualDocumentType", propOrder = {
                                                                 "objetNumerique",
                                                                 "erreurs",
                                                                 "composants"
})
public class NonIntegratedVirtualDocumentType {

   @XmlElement(required = true)
   protected FichierType objetNumerique;
   protected ListeErreurType erreurs;
   @XmlElement(required = true)
   protected NonIntegratedVirtualDocumentType.Composants composants;

   /**
    * Obtient la valeur de la propriété objetNumerique.
    * 
    * @return
    *     possible object is
    *     {@link FichierType }
    *     
    */
   public FichierType getObjetNumerique() {
      return objetNumerique;
   }

   /**
    * Définit la valeur de la propriété objetNumerique.
    * 
    * @param value
    *     allowed object is
    *     {@link FichierType }
    *     
    */
   public void setObjetNumerique(final FichierType value) {
      objetNumerique = value;
   }

   /**
    * Obtient la valeur de la propriété erreurs.
    * 
    * @return
    *     possible object is
    *     {@link ListeErreurType }
    *     
    */
   public ListeErreurType getErreurs() {
      return erreurs;
   }

   /**
    * Définit la valeur de la propriété erreurs.
    * 
    * @param value
    *     allowed object is
    *     {@link ListeErreurType }
    *     
    */
   public void setErreurs(final ListeErreurType value) {
      erreurs = value;
   }

   /**
    * Obtient la valeur de la propriété composants.
    * 
    * @return
    *     possible object is
    *     {@link NonIntegratedVirtualDocumentType.Composants }
    *     
    */
   public NonIntegratedVirtualDocumentType.Composants getComposants() {
      return composants;
   }

   /**
    * Définit la valeur de la propriété composants.
    * 
    * @param value
    *     allowed object is
    *     {@link NonIntegratedVirtualDocumentType.Composants }
    *     
    */
   public void setComposants(final NonIntegratedVirtualDocumentType.Composants value) {
      composants = value;
   }


   /**
    * <p>Classe Java pour anonymous complex type.
    * 
    * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
    * 
    * <pre>
    * &lt;complexType>
    *   &lt;complexContent>
    *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
    *       &lt;sequence>
    *         &lt;element name="composant" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedComposantDocumentVirtuelType" maxOccurs="unbounded"/>
    *       &lt;/sequence>
    *     &lt;/restriction>
    *   &lt;/complexContent>
    * &lt;/complexType>
    * </pre>
    * 
    * 
    */
   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "", propOrder = {
                                    "composant"
   })
   public static class Composants {

      @XmlElement(required = true)
      protected List<NonIntegratedComposantDocumentVirtuelType> composant;

      /**
       * Gets the value of the composant property.
       * 
       * <p>
       * This accessor method returns a reference to the live list,
       * not a snapshot. Therefore any modification you make to the
       * returned list will be present inside the JAXB object.
       * This is why there is not a <CODE>set</CODE> method for the composant property.
       * 
       * <p>
       * For example, to add a new item, do as follows:
       * <pre>
       *    getComposant().add(newItem);
       * </pre>
       * 
       * 
       * <p>
       * Objects of the following type(s) are allowed in the list
       * {@link NonIntegratedComposantDocumentVirtuelType }
       * 
       * 
       */
      public List<NonIntegratedComposantDocumentVirtuelType> getComposant() {
         if (composant == null) {
            composant = new ArrayList<>();
         }
         return composant;
      }

   }

}
