//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.09.12 à 05:03:21 PM CEST 
//


package fr.urssaf.image.sae.integration.meta.modele.xml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour MetadonneeType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="MetadonneeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeCourt" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codeLong" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="specifiableArchivage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="obligatoireArchivage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="consulteeParDefaut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="consultable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="recherchable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="estIndexee" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="formatage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tailleMax" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="gereeParDfce" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="typeDfce" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="obligatoireStockage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="aUnDico" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nomDico" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="modifiable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadonneeType", propOrder = {
    "codeCourt",
    "codeLong",
    "libelle",
    "description",
    "specifiableArchivage",
    "obligatoireArchivage",
    "consulteeParDefaut",
    "consultable",
    "recherchable",
    "estIndexee",
    "formatage",
    "tailleMax",
    "gereeParDfce",
    "typeDfce",
    "obligatoireStockage",
    "aUnDico",
    "nomDico",
    "modifiable"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class MetadonneeType {

    @XmlElement(required = true)
    protected String codeCourt;
    @XmlElement(required = true)
    protected String codeLong;
    @XmlElement(required = true)
    protected String libelle;
    @XmlElement(required = true)
    protected String description;
    protected boolean specifiableArchivage;
    protected boolean obligatoireArchivage;
    protected boolean consulteeParDefaut;
    protected boolean consultable;
    protected boolean recherchable;
    protected boolean estIndexee;
    @XmlElement(required = true)
    protected String formatage;
    @XmlElement(required = true)
    protected BigInteger tailleMax;
    protected boolean gereeParDfce;
    @XmlElement(required = true)
    protected String typeDfce;
    protected boolean obligatoireStockage;
    protected boolean aUnDico;
    @XmlElement(required = true)
    protected String nomDico;
    protected Boolean modifiable;

    /**
     * Obtient la valeur de la propriété codeCourt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCourt() {
        return codeCourt;
    }

    /**
     * Définit la valeur de la propriété codeCourt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCourt(String value) {
        this.codeCourt = value;
    }

    /**
     * Obtient la valeur de la propriété codeLong.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeLong() {
        return codeLong;
    }

    /**
     * Définit la valeur de la propriété codeLong.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeLong(String value) {
        this.codeLong = value;
    }

    /**
     * Obtient la valeur de la propriété libelle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit la valeur de la propriété libelle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelle(String value) {
        this.libelle = value;
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
     * Obtient la valeur de la propriété specifiableArchivage.
     * 
     */
    public boolean isSpecifiableArchivage() {
        return specifiableArchivage;
    }

    /**
     * Définit la valeur de la propriété specifiableArchivage.
     * 
     */
    public void setSpecifiableArchivage(boolean value) {
        this.specifiableArchivage = value;
    }

    /**
     * Obtient la valeur de la propriété obligatoireArchivage.
     * 
     */
    public boolean isObligatoireArchivage() {
        return obligatoireArchivage;
    }

    /**
     * Définit la valeur de la propriété obligatoireArchivage.
     * 
     */
    public void setObligatoireArchivage(boolean value) {
        this.obligatoireArchivage = value;
    }

    /**
     * Obtient la valeur de la propriété consulteeParDefaut.
     * 
     */
    public boolean isConsulteeParDefaut() {
        return consulteeParDefaut;
    }

    /**
     * Définit la valeur de la propriété consulteeParDefaut.
     * 
     */
    public void setConsulteeParDefaut(boolean value) {
        this.consulteeParDefaut = value;
    }

    /**
     * Obtient la valeur de la propriété consultable.
     * 
     */
    public boolean isConsultable() {
        return consultable;
    }

    /**
     * Définit la valeur de la propriété consultable.
     * 
     */
    public void setConsultable(boolean value) {
        this.consultable = value;
    }

    /**
     * Obtient la valeur de la propriété recherchable.
     * 
     */
    public boolean isRecherchable() {
        return recherchable;
    }

    /**
     * Définit la valeur de la propriété recherchable.
     * 
     */
    public void setRecherchable(boolean value) {
        this.recherchable = value;
    }

    /**
     * Obtient la valeur de la propriété estIndexee.
     * 
     */
    public boolean isEstIndexee() {
        return estIndexee;
    }

    /**
     * Définit la valeur de la propriété estIndexee.
     * 
     */
    public void setEstIndexee(boolean value) {
        this.estIndexee = value;
    }

    /**
     * Obtient la valeur de la propriété formatage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatage() {
        return formatage;
    }

    /**
     * Définit la valeur de la propriété formatage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatage(String value) {
        this.formatage = value;
    }

    /**
     * Obtient la valeur de la propriété tailleMax.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTailleMax() {
        return tailleMax;
    }

    /**
     * Définit la valeur de la propriété tailleMax.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTailleMax(BigInteger value) {
        this.tailleMax = value;
    }

    /**
     * Obtient la valeur de la propriété gereeParDfce.
     * 
     */
    public boolean isGereeParDfce() {
        return gereeParDfce;
    }

    /**
     * Définit la valeur de la propriété gereeParDfce.
     * 
     */
    public void setGereeParDfce(boolean value) {
        this.gereeParDfce = value;
    }

    /**
     * Obtient la valeur de la propriété typeDfce.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeDfce() {
        return typeDfce;
    }

    /**
     * Définit la valeur de la propriété typeDfce.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeDfce(String value) {
        this.typeDfce = value;
    }

    /**
     * Obtient la valeur de la propriété obligatoireStockage.
     * 
     */
    public boolean isObligatoireStockage() {
        return obligatoireStockage;
    }

    /**
     * Définit la valeur de la propriété obligatoireStockage.
     * 
     */
    public void setObligatoireStockage(boolean value) {
        this.obligatoireStockage = value;
    }

    /**
     * Obtient la valeur de la propriété aUnDico.
     * 
     */
    public boolean isAUnDico() {
        return aUnDico;
    }

    /**
     * Définit la valeur de la propriété aUnDico.
     * 
     */
    public void setAUnDico(boolean value) {
        this.aUnDico = value;
    }

    /**
     * Obtient la valeur de la propriété nomDico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomDico() {
        return nomDico;
    }

    /**
     * Définit la valeur de la propriété nomDico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomDico(String value) {
        this.nomDico = value;
    }

    /**
     * Obtient la valeur de la propriété modifiable.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isModifiable() {
        return modifiable;
    }

    /**
     * Définit la valeur de la propriété modifiable.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setModifiable(Boolean value) {
        this.modifiable = value;
    }

}
