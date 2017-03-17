//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.27 at 03:38:22 PM CET 
//


package fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un document virtuel
 * 
 * <p>Java class for documentVirtuelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentVirtuelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/>
 *         &lt;element name="composants">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="composant" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}composantDocumentVirtuelType" maxOccurs="unbounded"/>
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
@XmlType(name = "documentVirtuelType", propOrder = {
    "objetNumerique",
    "composants"
})
public class DocumentVirtuelType {

    @XmlElement(required = true)
    protected FichierType objetNumerique;
    @XmlElement(required = true)
    protected DocumentVirtuelType.Composants composants;

    /**
     * Gets the value of the objetNumerique property.
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
     * Sets the value of the objetNumerique property.
     * 
     * @param value
     *     allowed object is
     *     {@link FichierType }
     *     
     */
    public void setObjetNumerique(FichierType value) {
        this.objetNumerique = value;
    }

    /**
     * Gets the value of the composants property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentVirtuelType.Composants }
     *     
     */
    public DocumentVirtuelType.Composants getComposants() {
        return composants;
    }

    /**
     * Sets the value of the composants property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentVirtuelType.Composants }
     *     
     */
    public void setComposants(DocumentVirtuelType.Composants value) {
        this.composants = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="composant" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}composantDocumentVirtuelType" maxOccurs="unbounded"/>
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
        protected List<ComposantDocumentVirtuelType> composant;

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
         * {@link ComposantDocumentVirtuelType }
         * 
         * 
         */
        public List<ComposantDocumentVirtuelType> getComposant() {
            if (composant == null) {
                composant = new ArrayList<ComposantDocumentVirtuelType>();
            }
            return this.composant;
        }

    }

}
