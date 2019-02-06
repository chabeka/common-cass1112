
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Une métadonnée définie par un ensemble
 *             de
 *             propriété (code long, libellé, description, format,
 *             formatage, spécifiable à l'archivage, obligatoire à
 *             l'archivage, taille max, critère de recherche, indexation,
 *             modifiable)
 * 
 * <p>Java class for metadonneeDispoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metadonneeDispoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeLong" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="format" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formatage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="specifiableArchivage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="obligatoireArchivage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tailleMax" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="critereRecherche" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="indexation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="modifiable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadonneeDispoType", propOrder = {
    "codeLong",
    "libelle",
    "description",
    "format",
    "formatage",
    "specifiableArchivage",
    "obligatoireArchivage",
    "tailleMax",
    "critereRecherche",
    "indexation",
    "modifiable"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class MetadonneeDispoType {

    @XmlElement(required = true)
    protected String codeLong;
    @XmlElement(required = true)
    protected String libelle;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String format;
    @XmlElement(required = true)
    protected String formatage;
    protected boolean specifiableArchivage;
    protected boolean obligatoireArchivage;
    protected int tailleMax;
    protected boolean critereRecherche;
    protected boolean indexation;
    protected boolean modifiable;

    /**
     * Gets the value of the codeLong property.
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
     * Sets the value of the codeLong property.
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
     * Gets the value of the libelle property.
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
     * Sets the value of the libelle property.
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
     * Gets the value of the description property.
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
     * Sets the value of the description property.
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
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the formatage property.
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
     * Sets the value of the formatage property.
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
     * Gets the value of the specifiableArchivage property.
     * 
     */
    public boolean isSpecifiableArchivage() {
        return specifiableArchivage;
    }

    /**
     * Sets the value of the specifiableArchivage property.
     * 
     */
    public void setSpecifiableArchivage(boolean value) {
        this.specifiableArchivage = value;
    }

    /**
     * Gets the value of the obligatoireArchivage property.
     * 
     */
    public boolean isObligatoireArchivage() {
        return obligatoireArchivage;
    }

    /**
     * Sets the value of the obligatoireArchivage property.
     * 
     */
    public void setObligatoireArchivage(boolean value) {
        this.obligatoireArchivage = value;
    }

    /**
     * Gets the value of the tailleMax property.
     * 
     */
    public int getTailleMax() {
        return tailleMax;
    }

    /**
     * Sets the value of the tailleMax property.
     * 
     */
    public void setTailleMax(int value) {
        this.tailleMax = value;
    }

    /**
     * Gets the value of the critereRecherche property.
     * 
     */
    public boolean isCritereRecherche() {
        return critereRecherche;
    }

    /**
     * Sets the value of the critereRecherche property.
     * 
     */
    public void setCritereRecherche(boolean value) {
        this.critereRecherche = value;
    }

    /**
     * Gets the value of the indexation property.
     * 
     */
    public boolean isIndexation() {
        return indexation;
    }

    /**
     * Sets the value of the indexation property.
     * 
     */
    public void setIndexation(boolean value) {
        this.indexation = value;
    }

    /**
     * Gets the value of the modifiable property.
     * 
     */
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * Sets the value of the modifiable property.
     * 
     */
    public void setModifiable(boolean value) {
        this.modifiable = value;
    }

}
