
package sae.integration.webservice.modele;

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
 * 
 * <p>Classe Java pour metadonneeDispoType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="metadonneeDispoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeLong" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="format" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="formatage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="specifiableArchivage" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="obligatoireArchivage" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="tailleMax" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="critereRecherche" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="indexation" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="modifiable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Obtient la valeur de la propriété format.
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
     * Définit la valeur de la propriété format.
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
     * Obtient la valeur de la propriété tailleMax.
     * 
     */
    public int getTailleMax() {
        return tailleMax;
    }

    /**
     * Définit la valeur de la propriété tailleMax.
     * 
     */
    public void setTailleMax(int value) {
        this.tailleMax = value;
    }

    /**
     * Obtient la valeur de la propriété critereRecherche.
     * 
     */
    public boolean isCritereRecherche() {
        return critereRecherche;
    }

    /**
     * Définit la valeur de la propriété critereRecherche.
     * 
     */
    public void setCritereRecherche(boolean value) {
        this.critereRecherche = value;
    }

    /**
     * Obtient la valeur de la propriété indexation.
     * 
     */
    public boolean isIndexation() {
        return indexation;
    }

    /**
     * Définit la valeur de la propriété indexation.
     * 
     */
    public void setIndexation(boolean value) {
        this.indexation = value;
    }

    /**
     * Obtient la valeur de la propriété modifiable.
     * 
     */
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * Définit la valeur de la propriété modifiable.
     * 
     */
    public void setModifiable(boolean value) {
        this.modifiable = value;
    }

}
