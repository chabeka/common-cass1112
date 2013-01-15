//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.14 at 03:44:42 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeIntegratedDocumentsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;


/**
 * <p>Java class for resultatsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultatsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="batchMode" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}batchModeType"/>
 *           &lt;element name="initialDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="integratedDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="nonIntegratedDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="initialVirtualDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="integratedVirtualDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="nonIntegratedVirtualDocumentsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="nonIntegratedDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeNonIntegratedDocumentsType"/>
 *           &lt;element name="nonIntegratedVirtualDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeDocumentsVirtuelsType"/>
 *           &lt;element name="integratedDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeIntegratedDocumentsType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;element name="erreurBloquanteTraitement" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}erreurType"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultatsType", propOrder = {
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
    "erreurBloquanteTraitement"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ResultatsType {

    protected BatchModeType batchMode;
    protected Integer initialDocumentsCount;
    protected Integer integratedDocumentsCount;
    protected Integer nonIntegratedDocumentsCount;
    protected Integer initialVirtualDocumentsCount;
    protected Integer integratedVirtualDocumentsCount;
    protected Integer nonIntegratedVirtualDocumentsCount;
    protected ListeNonIntegratedDocumentsType nonIntegratedDocuments;
    protected ListeDocumentsVirtuelsType nonIntegratedVirtualDocuments;
    protected ListeIntegratedDocumentsType integratedDocuments;
    protected ErreurType erreurBloquanteTraitement;

    /**
     * Gets the value of the batchMode property.
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
     * Sets the value of the batchMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatchModeType }
     *     
     */
    public void setBatchMode(BatchModeType value) {
        this.batchMode = value;
    }

    /**
     * Gets the value of the initialDocumentsCount property.
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
     * Sets the value of the initialDocumentsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInitialDocumentsCount(Integer value) {
        this.initialDocumentsCount = value;
    }

    /**
     * Gets the value of the integratedDocumentsCount property.
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
     * Sets the value of the integratedDocumentsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIntegratedDocumentsCount(Integer value) {
        this.integratedDocumentsCount = value;
    }

    /**
     * Gets the value of the nonIntegratedDocumentsCount property.
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
     * Sets the value of the nonIntegratedDocumentsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNonIntegratedDocumentsCount(Integer value) {
        this.nonIntegratedDocumentsCount = value;
    }

    /**
     * Gets the value of the initialVirtualDocumentsCount property.
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
     * Sets the value of the initialVirtualDocumentsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInitialVirtualDocumentsCount(Integer value) {
        this.initialVirtualDocumentsCount = value;
    }

    /**
     * Gets the value of the integratedVirtualDocumentsCount property.
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
     * Sets the value of the integratedVirtualDocumentsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIntegratedVirtualDocumentsCount(Integer value) {
        this.integratedVirtualDocumentsCount = value;
    }

    /**
     * Gets the value of the nonIntegratedVirtualDocumentsCount property.
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
     * Sets the value of the nonIntegratedVirtualDocumentsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNonIntegratedVirtualDocumentsCount(Integer value) {
        this.nonIntegratedVirtualDocumentsCount = value;
    }

    /**
     * Gets the value of the nonIntegratedDocuments property.
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
     * Sets the value of the nonIntegratedDocuments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeNonIntegratedDocumentsType }
     *     
     */
    public void setNonIntegratedDocuments(ListeNonIntegratedDocumentsType value) {
        this.nonIntegratedDocuments = value;
    }

    /**
     * Gets the value of the nonIntegratedVirtualDocuments property.
     * 
     * @return
     *     possible object is
     *     {@link ListeDocumentsVirtuelsType }
     *     
     */
    public ListeDocumentsVirtuelsType getNonIntegratedVirtualDocuments() {
        return nonIntegratedVirtualDocuments;
    }

    /**
     * Sets the value of the nonIntegratedVirtualDocuments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeDocumentsVirtuelsType }
     *     
     */
    public void setNonIntegratedVirtualDocuments(ListeDocumentsVirtuelsType value) {
        this.nonIntegratedVirtualDocuments = value;
    }

    /**
     * Gets the value of the integratedDocuments property.
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
     * Sets the value of the integratedDocuments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeIntegratedDocumentsType }
     *     
     */
    public void setIntegratedDocuments(ListeIntegratedDocumentsType value) {
        this.integratedDocuments = value;
    }

    /**
     * Gets the value of the erreurBloquanteTraitement property.
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
     * Sets the value of the erreurBloquanteTraitement property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErreurType }
     *     
     */
    public void setErreurBloquanteTraitement(ErreurType value) {
        this.erreurBloquanteTraitement = value;
    }

}
