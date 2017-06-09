//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.09.17 à 04:50:06 PM CEST 
//


package fr.urssaf.image.sae.integration.droits.modele.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour DroitType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DroitType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listePrmd" type="{http://www.cirtil.fr/saeIntegration/droit}ListePrmdType"/>
 *         &lt;element name="listeCs" type="{http://www.cirtil.fr/saeIntegration/droit}ListeCsType"/>
 *         &lt;element name="listeAjoutPagm" type="{http://www.cirtil.fr/saeIntegration/droit}ListeAjoutPagmType" minOccurs="0"/>
 *         &lt;element name="listeModifPrmd" type="{http://www.cirtil.fr/saeIntegration/droit}ListeModifPrmdType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DroitType", propOrder = {
    "listePrmd",
    "listeCs",
    "listeAjoutPagm",
    "listeModifPrmd"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class DroitType {

    @XmlElement(required = true)
    protected ListePrmdType listePrmd;
    @XmlElement(required = true)
    protected ListeCsType listeCs;
    protected ListeAjoutPagmType listeAjoutPagm;
    protected ListeModifPrmdType listeModifPrmd;

    /**
     * Obtient la valeur de la propriété listePrmd.
     * 
     * @return
     *     possible object is
     *     {@link ListePrmdType }
     *     
     */
    public ListePrmdType getListePrmd() {
        return listePrmd;
    }

    /**
     * Définit la valeur de la propriété listePrmd.
     * 
     * @param value
     *     allowed object is
     *     {@link ListePrmdType }
     *     
     */
    public void setListePrmd(ListePrmdType value) {
        this.listePrmd = value;
    }

    /**
     * Obtient la valeur de la propriété listeCs.
     * 
     * @return
     *     possible object is
     *     {@link ListeCsType }
     *     
     */
    public ListeCsType getListeCs() {
        return listeCs;
    }

    /**
     * Définit la valeur de la propriété listeCs.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeCsType }
     *     
     */
    public void setListeCs(ListeCsType value) {
        this.listeCs = value;
    }

    /**
     * Obtient la valeur de la propriété listeAjoutPagm.
     * 
     * @return
     *     possible object is
     *     {@link ListeAjoutPagmType }
     *     
     */
    public ListeAjoutPagmType getListeAjoutPagm() {
        return listeAjoutPagm;
    }

    /**
     * Définit la valeur de la propriété listeAjoutPagm.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeAjoutPagmType }
     *     
     */
    public void setListeAjoutPagm(ListeAjoutPagmType value) {
        this.listeAjoutPagm = value;
    }

    /**
     * Obtient la valeur de la propriété listeModifPrmd.
     * 
     * @return
     *     possible object is
     *     {@link ListeModifPrmdType }
     *     
     */
    public ListeModifPrmdType getListeModifPrmd() {
        return listeModifPrmd;
    }

    /**
     * Définit la valeur de la propriété listeModifPrmd.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeModifPrmdType }
     *     
     */
    public void setListeModifPrmd(ListeModifPrmdType value) {
        this.listeModifPrmd = value;
    }

}