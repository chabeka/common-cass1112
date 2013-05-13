//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.05.13 à 03:05:24 PM CEST 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeIntegratedDocumentsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeIntegratedDocumentsVirtuelsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeNonIntegratedVirtualDocumentsType;


/**
 * <p>Classe Java pour resultatsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
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
 *           &lt;element name="nonIntegratedVirtualDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeNonIntegratedVirtualDocumentsType"/>
 *           &lt;element name="integratedDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeIntegratedDocumentsType" minOccurs="0"/>
 *           &lt;element name="integratedVirtualDocuments" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeIntegratedDocumentsVirtuelsType" minOccurs="0"/>
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
    "integratedVirtualDocuments",
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
    public void setBatchMode(BatchModeType value) {
        this.batchMode = value;
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
    public void setInitialDocumentsCount(Integer value) {
        this.initialDocumentsCount = value;
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
    public void setIntegratedDocumentsCount(Integer value) {
        this.integratedDocumentsCount = value;
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
    public void setNonIntegratedDocumentsCount(Integer value) {
        this.nonIntegratedDocumentsCount = value;
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
    public void setInitialVirtualDocumentsCount(Integer value) {
        this.initialVirtualDocumentsCount = value;
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
    public void setIntegratedVirtualDocumentsCount(Integer value) {
        this.integratedVirtualDocumentsCount = value;
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
    public void setNonIntegratedVirtualDocumentsCount(Integer value) {
        this.nonIntegratedVirtualDocumentsCount = value;
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
    public void setNonIntegratedDocuments(ListeNonIntegratedDocumentsType value) {
        this.nonIntegratedDocuments = value;
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
    public void setNonIntegratedVirtualDocuments(ListeNonIntegratedVirtualDocumentsType value) {
        this.nonIntegratedVirtualDocuments = value;
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
    public void setIntegratedDocuments(ListeIntegratedDocumentsType value) {
        this.integratedDocuments = value;
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
    public void setIntegratedVirtualDocuments(ListeIntegratedDocumentsVirtuelsType value) {
        this.integratedVirtualDocuments = value;
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
    public void setErreurBloquanteTraitement(ErreurType value) {
        this.erreurBloquanteTraitement = value;
    }

}
