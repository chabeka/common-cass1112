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
 * Définition d'un document non archivé
 * 
 * <p>Classe Java pour nonIntegratedDocumentType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="nonIntegratedDocumentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/>
 *         &lt;element name="erreurs" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeErreurType"/>
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nonIntegratedDocumentType", propOrder = {
                                                          "objetNumerique",
                                                          "erreurs",
                                                          "numeroPageDebut",
                                                          "nombreDePages"
})
public class NonIntegratedDocumentType {

   @XmlElement(required = true)
   protected FichierType objetNumerique;
   @XmlElement(required = true)
   protected ListeErreurType erreurs;
   protected Integer numeroPageDebut;
   protected Integer nombreDePages;

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
    * Obtient la valeur de la propriété numeroPageDebut.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getNumeroPageDebut() {
      return numeroPageDebut;
   }

   /**
    * Définit la valeur de la propriété numeroPageDebut.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setNumeroPageDebut(final Integer value) {
      numeroPageDebut = value;
   }

   /**
    * Obtient la valeur de la propriété nombreDePages.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getNombreDePages() {
      return nombreDePages;
   }

   /**
    * Définit la valeur de la propriété nombreDePages.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setNombreDePages(final Integer value) {
      nombreDePages = value;
   }

}
