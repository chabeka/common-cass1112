//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.02.09 à 11:29:35 AM CET 
//


package fr.urssaf.image.sae.lotinstallmaj.modele.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour MetaReference complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="MetaReference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="longCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shortCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requiredForArchival" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requiredForStorage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consultable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="defaultConsultable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="searchable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="internal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="archivable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hasDictionary" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dictionaryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isIndexed" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="modifiable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clientAvailable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="leftTrimable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rightTrimable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transferable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aCreer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aModifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaReference", propOrder = {
    "longCode",
    "shortCode",
    "type",
    "requiredForArchival",
    "requiredForStorage",
    "length",
    "pattern",
    "consultable",
    "defaultConsultable",
    "searchable",
    "internal",
    "archivable",
    "label",
    "description",
    "hasDictionary",
    "dictionaryName",
    "isIndexed",
    "modifiable",
    "clientAvailable",
    "leftTrimable",
    "rightTrimable",
    "transferable",
    "aCreer",
    "aModifier"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class MetaReference {

    @XmlElement(required = true)
    protected String longCode;
    @XmlElement(required = true)
    protected String shortCode;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String requiredForArchival;
    @XmlElement(required = true)
    protected String requiredForStorage;
    protected String length;
    protected String pattern;
    @XmlElement(required = true)
    protected String consultable;
    @XmlElement(required = true)
    protected String defaultConsultable;
    @XmlElement(required = true)
    protected String searchable;
    @XmlElement(required = true)
    protected String internal;
    @XmlElement(required = true)
    protected String archivable;
    @XmlElement(required = true)
    protected String label;
    protected String description;
    @XmlElement(required = true)
    protected String hasDictionary;
    protected String dictionaryName;
    @XmlElement(required = true)
    protected String isIndexed;
    @XmlElement(required = true)
    protected String modifiable;
    @XmlElement(required = true)
    protected String clientAvailable;
    @XmlElement(required = true)
    protected String leftTrimable;
    @XmlElement(required = true)
    protected String rightTrimable;
    @XmlElement(required = true)
    protected String transferable;
    @XmlElement(required = true)
    protected String aCreer;
    @XmlElement(required = true)
    protected String aModifier;

    /**
     * Obtient la valeur de la propriété longCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongCode() {
        return longCode;
    }

    /**
     * Définit la valeur de la propriété longCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongCode(String value) {
        this.longCode = value;
    }

    /**
     * Obtient la valeur de la propriété shortCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * Définit la valeur de la propriété shortCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortCode(String value) {
        this.shortCode = value;
    }

    /**
     * Obtient la valeur de la propriété type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Définit la valeur de la propriété type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtient la valeur de la propriété requiredForArchival.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequiredForArchival() {
        return requiredForArchival;
    }

    /**
     * Définit la valeur de la propriété requiredForArchival.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequiredForArchival(String value) {
        this.requiredForArchival = value;
    }

    /**
     * Obtient la valeur de la propriété requiredForStorage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequiredForStorage() {
        return requiredForStorage;
    }

    /**
     * Définit la valeur de la propriété requiredForStorage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequiredForStorage(String value) {
        this.requiredForStorage = value;
    }

    /**
     * Obtient la valeur de la propriété length.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLength() {
        return length;
    }

    /**
     * Définit la valeur de la propriété length.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLength(String value) {
        this.length = value;
    }

    /**
     * Obtient la valeur de la propriété pattern.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Définit la valeur de la propriété pattern.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPattern(String value) {
        this.pattern = value;
    }

    /**
     * Obtient la valeur de la propriété consultable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsultable() {
        return consultable;
    }

    /**
     * Définit la valeur de la propriété consultable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsultable(String value) {
        this.consultable = value;
    }

    /**
     * Obtient la valeur de la propriété defaultConsultable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultConsultable() {
        return defaultConsultable;
    }

    /**
     * Définit la valeur de la propriété defaultConsultable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultConsultable(String value) {
        this.defaultConsultable = value;
    }

    /**
     * Obtient la valeur de la propriété searchable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchable() {
        return searchable;
    }

    /**
     * Définit la valeur de la propriété searchable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchable(String value) {
        this.searchable = value;
    }

    /**
     * Obtient la valeur de la propriété internal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternal() {
        return internal;
    }

    /**
     * Définit la valeur de la propriété internal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternal(String value) {
        this.internal = value;
    }

    /**
     * Obtient la valeur de la propriété archivable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArchivable() {
        return archivable;
    }

    /**
     * Définit la valeur de la propriété archivable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchivable(String value) {
        this.archivable = value;
    }

    /**
     * Obtient la valeur de la propriété label.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Définit la valeur de la propriété label.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Obtient la valeur de la propriété description.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la valeur de la propriété description.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Obtient la valeur de la propriété hasDictionary.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHasDictionary() {
        return hasDictionary;
    }

    /**
     * Définit la valeur de la propriété hasDictionary.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHasDictionary(String value) {
        this.hasDictionary = value;
    }

    /**
     * Obtient la valeur de la propriété dictionaryName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDictionaryName() {
        return dictionaryName;
    }

    /**
     * Définit la valeur de la propriété dictionaryName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDictionaryName(String value) {
        this.dictionaryName = value;
    }

    /**
     * Obtient la valeur de la propriété isIndexed.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsIndexed() {
        return isIndexed;
    }

    /**
     * Définit la valeur de la propriété isIndexed.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsIndexed(String value) {
        this.isIndexed = value;
    }

    /**
     * Obtient la valeur de la propriété modifiable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiable() {
        return modifiable;
    }

    /**
     * Définit la valeur de la propriété modifiable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiable(String value) {
        this.modifiable = value;
    }

    /**
     * Obtient la valeur de la propriété clientAvailable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientAvailable() {
        return clientAvailable;
    }

    /**
     * Définit la valeur de la propriété clientAvailable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientAvailable(String value) {
        this.clientAvailable = value;
    }

    /**
     * Obtient la valeur de la propriété leftTrimable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeftTrimable() {
        return leftTrimable;
    }

    /**
     * Définit la valeur de la propriété leftTrimable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeftTrimable(String value) {
        this.leftTrimable = value;
    }

    /**
     * Obtient la valeur de la propriété rightTrimable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRightTrimable() {
        return rightTrimable;
    }

    /**
     * Définit la valeur de la propriété rightTrimable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRightTrimable(String value) {
        this.rightTrimable = value;
    }

    /**
     * Obtient la valeur de la propriété transferable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferable() {
        return transferable;
    }

    /**
     * Définit la valeur de la propriété transferable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferable(String value) {
        this.transferable = value;
    }

    /**
     * Obtient la valeur de la propriété aCreer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACreer() {
        return aCreer;
    }

    /**
     * Définit la valeur de la propriété aCreer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACreer(String value) {
        this.aCreer = value;
    }

    /**
     * Obtient la valeur de la propriété aModifier.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAModifier() {
        return aModifier;
    }

    /**
     * Définit la valeur de la propriété aModifier.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAModifier(String value) {
        this.aModifier = value;
    }

}
