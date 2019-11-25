//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.06 à 10:57:31 AM CEST 
//


package sae.integration.xml.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour resultatsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="resultatsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="batchMode" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}batchModeType"/&gt;
 *           &lt;element name="initialDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="integratedDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="nonIntegratedDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="initialVirtualDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="integratedVirtualDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="nonIntegratedVirtualDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="nonIntegratedDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeNonIntegratedDocumentsType"/&gt;
 *           &lt;element name="nonIntegratedVirtualDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeNonIntegratedVirtualDocumentsType"/&gt;
 *           &lt;element name="integratedDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeIntegratedDocumentsType" minOccurs="0"/&gt;
 *           &lt;element name="integratedVirtualDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeIntegratedDocumentsVirtuelsType" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="erreurBloquanteTraitement" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}erreurType"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultatsType", namespace = "http://www.cirtil.fr/sae/resultatsXml", propOrder = {
      "batchMode",
      "initialDocumentsCount",
      "integratedDocumentsCount",
      "nonIntegratedDocumentsCount",
      "initialVirtualDocumentsCount",
      "integratedVirtualDocumentsCount",
      "nonIntegratedVirtualDocumentsCount",
      "nonIntegratedDocuments",
      "nonIntegratedVirtualDocuments",
      "integratedDocuments",
      "integratedVirtualDocuments",
      "erreurBloquanteTraitement"
})
public class ResultatsType {

   @XmlSchemaType(name = "string")
   protected BatchModeType batchMode;
   protected Integer initialDocumentsCount;
   protected Integer integratedDocumentsCount;
   protected Integer nonIntegratedDocumentsCount;
   protected Integer initialVirtualDocumentsCount;
   protected Integer integratedVirtualDocumentsCount;
   protected Integer nonIntegratedVirtualDocumentsCount;
   protected ListeNonIntegratedDocumentsType nonIntegratedDocuments;
   protected ListeNonIntegratedVirtualDocumentsType nonIntegratedVirtualDocuments;
   protected ListeIntegratedDocumentsType integratedDocuments;
   protected ListeIntegratedDocumentsVirtuelsType integratedVirtualDocuments;
   protected ErreurType erreurBloquanteTraitement;

   /**
    * Obtient la valeur de la propriété batchMode.
    * 
    * @return
    *     possible object is
    *     {@link BatchModeType }
    *     
    */
   public BatchModeType getBatchMode() {
      return batchMode;
   }

   /**
    * Définit la valeur de la propriété batchMode.
    * 
    * @param value
    *     allowed object is
    *     {@link BatchModeType }
    *     
    */
   public void setBatchMode(final BatchModeType value) {
      batchMode = value;
   }

   /**
    * Obtient la valeur de la propriété initialDocumentsCount.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getInitialDocumentsCount() {
      return initialDocumentsCount;
   }

   /**
    * Définit la valeur de la propriété initialDocumentsCount.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setInitialDocumentsCount(final Integer value) {
      initialDocumentsCount = value;
   }

   /**
    * Obtient la valeur de la propriété integratedDocumentsCount.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getIntegratedDocumentsCount() {
      return integratedDocumentsCount;
   }

   /**
    * Définit la valeur de la propriété integratedDocumentsCount.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setIntegratedDocumentsCount(final Integer value) {
      integratedDocumentsCount = value;
   }

   /**
    * Obtient la valeur de la propriété nonIntegratedDocumentsCount.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getNonIntegratedDocumentsCount() {
      return nonIntegratedDocumentsCount;
   }

   /**
    * Définit la valeur de la propriété nonIntegratedDocumentsCount.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setNonIntegratedDocumentsCount(final Integer value) {
      nonIntegratedDocumentsCount = value;
   }

   /**
    * Obtient la valeur de la propriété initialVirtualDocumentsCount.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getInitialVirtualDocumentsCount() {
      return initialVirtualDocumentsCount;
   }

   /**
    * Définit la valeur de la propriété initialVirtualDocumentsCount.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setInitialVirtualDocumentsCount(final Integer value) {
      initialVirtualDocumentsCount = value;
   }

   /**
    * Obtient la valeur de la propriété integratedVirtualDocumentsCount.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getIntegratedVirtualDocumentsCount() {
      return integratedVirtualDocumentsCount;
   }

   /**
    * Définit la valeur de la propriété integratedVirtualDocumentsCount.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setIntegratedVirtualDocumentsCount(final Integer value) {
      integratedVirtualDocumentsCount = value;
   }

   /**
    * Obtient la valeur de la propriété nonIntegratedVirtualDocumentsCount.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getNonIntegratedVirtualDocumentsCount() {
      return nonIntegratedVirtualDocumentsCount;
   }

   /**
    * Définit la valeur de la propriété nonIntegratedVirtualDocumentsCount.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setNonIntegratedVirtualDocumentsCount(final Integer value) {
      nonIntegratedVirtualDocumentsCount = value;
   }

   /**
    * Obtient la valeur de la propriété nonIntegratedDocuments.
    * 
    * @return
    *     possible object is
    *     {@link ListeNonIntegratedDocumentsType }
    *     
    */
   public ListeNonIntegratedDocumentsType getNonIntegratedDocuments() {
      return nonIntegratedDocuments;
   }

   /**
    * Définit la valeur de la propriété nonIntegratedDocuments.
    * 
    * @param value
    *     allowed object is
    *     {@link ListeNonIntegratedDocumentsType }
    *     
    */
   public void setNonIntegratedDocuments(final ListeNonIntegratedDocumentsType value) {
      nonIntegratedDocuments = value;
   }

   /**
    * Obtient la valeur de la propriété nonIntegratedVirtualDocuments.
    * 
    * @return
    *     possible object is
    *     {@link ListeNonIntegratedVirtualDocumentsType }
    *     
    */
   public ListeNonIntegratedVirtualDocumentsType getNonIntegratedVirtualDocuments() {
      return nonIntegratedVirtualDocuments;
   }

   /**
    * Définit la valeur de la propriété nonIntegratedVirtualDocuments.
    * 
    * @param value
    *     allowed object is
    *     {@link ListeNonIntegratedVirtualDocumentsType }
    *     
    */
   public void setNonIntegratedVirtualDocuments(final ListeNonIntegratedVirtualDocumentsType value) {
      nonIntegratedVirtualDocuments = value;
   }

   /**
    * Obtient la valeur de la propriété integratedDocuments.
    * 
    * @return
    *     possible object is
    *     {@link ListeIntegratedDocumentsType }
    *     
    */
   public ListeIntegratedDocumentsType getIntegratedDocuments() {
      return integratedDocuments;
   }

   /**
    * Définit la valeur de la propriété integratedDocuments.
    * 
    * @param value
    *     allowed object is
    *     {@link ListeIntegratedDocumentsType }
    *     
    */
   public void setIntegratedDocuments(final ListeIntegratedDocumentsType value) {
      integratedDocuments = value;
   }

   /**
    * Obtient la valeur de la propriété integratedVirtualDocuments.
    * 
    * @return
    *     possible object is
    *     {@link ListeIntegratedDocumentsVirtuelsType }
    *     
    */
   public ListeIntegratedDocumentsVirtuelsType getIntegratedVirtualDocuments() {
      return integratedVirtualDocuments;
   }

   /**
    * Définit la valeur de la propriété integratedVirtualDocuments.
    * 
    * @param value
    *     allowed object is
    *     {@link ListeIntegratedDocumentsVirtuelsType }
    *     
    */
   public void setIntegratedVirtualDocuments(final ListeIntegratedDocumentsVirtuelsType value) {
      integratedVirtualDocuments = value;
   }

   /**
    * Obtient la valeur de la propriété erreurBloquanteTraitement.
    * 
    * @return
    *     possible object is
    *     {@link ErreurType }
    *     
    */
   public ErreurType getErreurBloquanteTraitement() {
      return erreurBloquanteTraitement;
   }

   /**
    * Définit la valeur de la propriété erreurBloquanteTraitement.
    * 
    * @param value
    *     allowed object is
    *     {@link ErreurType }
    *     
    */
   public void setErreurBloquanteTraitement(final ErreurType value) {
      erreurBloquanteTraitement = value;
   }

}
