//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.14 at 03:44:42 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Définition d'un fichier
 * 
 * <p>Java class for fichierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fichierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cheminEtNomDuFichier">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
@XmlType(name = "fichierType", propOrder = {
    "cheminEtNomDuFichier"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class FichierType {

    @XmlElement(required = true)
    protected String cheminEtNomDuFichier;

    /**
     * Gets the value of the cheminEtNomDuFichier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminEtNomDuFichier() {
        return cheminEtNomDuFichier;
    }

    /**
     * Sets the value of the cheminEtNomDuFichier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminEtNomDuFichier(String value) {
        this.cheminEtNomDuFichier = value;
    }

}
