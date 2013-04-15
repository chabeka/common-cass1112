//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.04.15 à 10:47:58 AM CEST 
//


package fr.urssaf.image.sae.integration.meta.modele.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour MetaType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="MetaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dictionnaireAjout" type="{http://www.cirtil.fr/saeIntegration/meta}ListeDictionnaireType"/>
 *         &lt;element name="dictionnaireSuppression" type="{http://www.cirtil.fr/saeIntegration/meta}ListeDictionnaireType"/>
 *         &lt;element name="metadonneesCreation" type="{http://www.cirtil.fr/saeIntegration/meta}ListeMetadonneesType"/>
 *         &lt;element name="metadonneesModification" type="{http://www.cirtil.fr/saeIntegration/meta}ListeMetadonneesType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaType", propOrder = {
    "dictionnaireAjout",
    "dictionnaireSuppression",
    "metadonneesCreation",
    "metadonneesModification"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class MetaType {

    @XmlElement(required = true)
    protected ListeDictionnaireType dictionnaireAjout;
    @XmlElement(required = true)
    protected ListeDictionnaireType dictionnaireSuppression;
    @XmlElement(required = true)
    protected ListeMetadonneesType metadonneesCreation;
    @XmlElement(required = true)
    protected ListeMetadonneesType metadonneesModification;

    /**
     * Obtient la valeur de la propriété dictionnaireAjout.
     * 
     * @return
     *     possible object is
     *     {@link ListeDictionnaireType }
     *     
     */
    public ListeDictionnaireType getDictionnaireAjout() {
        return dictionnaireAjout;
    }

    /**
     * Définit la valeur de la propriété dictionnaireAjout.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeDictionnaireType }
     *     
     */
    public void setDictionnaireAjout(ListeDictionnaireType value) {
        this.dictionnaireAjout = value;
    }

    /**
     * Obtient la valeur de la propriété dictionnaireSuppression.
     * 
     * @return
     *     possible object is
     *     {@link ListeDictionnaireType }
     *     
     */
    public ListeDictionnaireType getDictionnaireSuppression() {
        return dictionnaireSuppression;
    }

    /**
     * Définit la valeur de la propriété dictionnaireSuppression.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeDictionnaireType }
     *     
     */
    public void setDictionnaireSuppression(ListeDictionnaireType value) {
        this.dictionnaireSuppression = value;
    }

    /**
     * Obtient la valeur de la propriété metadonneesCreation.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneesType }
     *     
     */
    public ListeMetadonneesType getMetadonneesCreation() {
        return metadonneesCreation;
    }

    /**
     * Définit la valeur de la propriété metadonneesCreation.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneesType }
     *     
     */
    public void setMetadonneesCreation(ListeMetadonneesType value) {
        this.metadonneesCreation = value;
    }

    /**
     * Obtient la valeur de la propriété metadonneesModification.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneesType }
     *     
     */
    public ListeMetadonneesType getMetadonneesModification() {
        return metadonneesModification;
    }

    /**
     * Définit la valeur de la propriété metadonneesModification.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneesType }
     *     
     */
    public void setMetadonneesModification(ListeMetadonneesType value) {
        this.metadonneesModification = value;
    }

}
