//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.30 at 11:06:51 AM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.listetests;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CategorieType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CategorieType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CasTests" type="{http://www.cirtil.fr/saeIntegration/tests}ListeCasTestsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategorieType", propOrder = {
    "nom",
    "id",
    "casTests"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class CategorieType {

    @XmlElement(name = "Nom", required = true)
    protected String nom;
    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "CasTests", required = true)
    protected ListeCasTestsType casTests;

    /**
     * Gets the value of the nom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets the value of the nom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNom(String value) {
        this.nom = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the casTests property.
     * 
     * @return
     *     possible object is
     *     {@link ListeCasTestsType }
     *     
     */
    public ListeCasTestsType getCasTests() {
        return casTests;
    }

    /**
     * Sets the value of the casTests property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeCasTestsType }
     *     
     */
    public void setCasTests(ListeCasTestsType value) {
        this.casTests = value;
    }

}
