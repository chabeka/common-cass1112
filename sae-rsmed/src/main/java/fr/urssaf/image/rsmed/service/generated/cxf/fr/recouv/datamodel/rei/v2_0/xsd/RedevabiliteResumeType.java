
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * RedevabiliteResume contient une sous partie des informations propres a une redevabilite
 * 
 * <p>Classe Java pour RedevabiliteResume_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="RedevabiliteResume_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idREI" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="numeroCompteInterne" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-INT-CPT" minOccurs="0"/&gt;
 *         &lt;element name="urssafGestionnaire" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *         &lt;element name="categorie" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CD-CAT" minOccurs="0"/&gt;
 *         &lt;element name="sousCategorie" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CD-SS-CAT" minOccurs="0"/&gt;
 *         &lt;element name="numeroCompteExterne" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT"/&gt;
 *         &lt;element name="codeCSU" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeCSU_Type" minOccurs="0"/&gt;
 *         &lt;element name="etatCompte" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CD-ETA-CPT" minOccurs="0"/&gt;
 *         &lt;element name="codeMotifEtat" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}codeMotifEtat_Type" minOccurs="0"/&gt;
 *         &lt;element name="numeroNationalCompte" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NumeroNationalCompte_Type" minOccurs="0"/&gt;
 *         &lt;element name="pseudoSIRET" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="archiveSnv2" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="dateMaj" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RedevabiliteResume_Type", propOrder = {
    "idREI",
    "numeroCompteInterne",
    "urssafGestionnaire",
    "categorie",
    "sousCategorie",
    "numeroCompteExterne",
    "codeCSU",
    "etatCompte",
    "codeMotifEtat",
    "numeroNationalCompte",
    "pseudoSIRET",
    "archiveSnv2",
    "dateMaj"
})
public class RedevabiliteResumeType {

    protected Long idREI;
    protected String numeroCompteInterne;
    protected String urssafGestionnaire;
    protected String categorie;
    protected String sousCategorie;
    @XmlElement(required = true)
    protected String numeroCompteExterne;
    protected String codeCSU;
    protected String etatCompte;
    protected String codeMotifEtat;
    protected String numeroNationalCompte;
    protected SIRETCertificationType pseudoSIRET;
    @XmlElement(defaultValue = "false")
    protected boolean archiveSnv2;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateMaj;

    /**
     * Obtient la valeur de la propriété idREI.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdREI() {
        return idREI;
    }

    /**
     * Définit la valeur de la propriété idREI.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdREI(Long value) {
        this.idREI = value;
    }

    /**
     * Obtient la valeur de la propriété numeroCompteInterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCompteInterne() {
        return numeroCompteInterne;
    }

    /**
     * Définit la valeur de la propriété numeroCompteInterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCompteInterne(String value) {
        this.numeroCompteInterne = value;
    }

    /**
     * Obtient la valeur de la propriété urssafGestionnaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrssafGestionnaire() {
        return urssafGestionnaire;
    }

    /**
     * Définit la valeur de la propriété urssafGestionnaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrssafGestionnaire(String value) {
        this.urssafGestionnaire = value;
    }

    /**
     * Obtient la valeur de la propriété categorie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Définit la valeur de la propriété categorie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategorie(String value) {
        this.categorie = value;
    }

    /**
     * Obtient la valeur de la propriété sousCategorie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSousCategorie() {
        return sousCategorie;
    }

    /**
     * Définit la valeur de la propriété sousCategorie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSousCategorie(String value) {
        this.sousCategorie = value;
    }

    /**
     * Obtient la valeur de la propriété numeroCompteExterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCompteExterne() {
        return numeroCompteExterne;
    }

    /**
     * Définit la valeur de la propriété numeroCompteExterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCompteExterne(String value) {
        this.numeroCompteExterne = value;
    }

    /**
     * Obtient la valeur de la propriété codeCSU.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCSU() {
        return codeCSU;
    }

    /**
     * Définit la valeur de la propriété codeCSU.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCSU(String value) {
        this.codeCSU = value;
    }

    /**
     * Obtient la valeur de la propriété etatCompte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEtatCompte() {
        return etatCompte;
    }

    /**
     * Définit la valeur de la propriété etatCompte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEtatCompte(String value) {
        this.etatCompte = value;
    }

    /**
     * Obtient la valeur de la propriété codeMotifEtat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeMotifEtat() {
        return codeMotifEtat;
    }

    /**
     * Définit la valeur de la propriété codeMotifEtat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeMotifEtat(String value) {
        this.codeMotifEtat = value;
    }

    /**
     * Obtient la valeur de la propriété numeroNationalCompte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroNationalCompte() {
        return numeroNationalCompte;
    }

    /**
     * Définit la valeur de la propriété numeroNationalCompte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroNationalCompte(String value) {
        this.numeroNationalCompte = value;
    }

    /**
     * Obtient la valeur de la propriété pseudoSIRET.
     * 
     * @return
     *     possible object is
     *     {@link SIRETCertificationType }
     *     
     */
    public SIRETCertificationType getPseudoSIRET() {
        return pseudoSIRET;
    }

    /**
     * Définit la valeur de la propriété pseudoSIRET.
     * 
     * @param value
     *     allowed object is
     *     {@link SIRETCertificationType }
     *     
     */
    public void setPseudoSIRET(SIRETCertificationType value) {
        this.pseudoSIRET = value;
    }

    /**
     * Obtient la valeur de la propriété archiveSnv2.
     * 
     */
    public boolean isArchiveSnv2() {
        return archiveSnv2;
    }

    /**
     * Définit la valeur de la propriété archiveSnv2.
     * 
     */
    public void setArchiveSnv2(boolean value) {
        this.archiveSnv2 = value;
    }

    /**
     * Obtient la valeur de la propriété dateMaj.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateMaj() {
        return dateMaj;
    }

    /**
     * Définit la valeur de la propriété dateMaj.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateMaj(XMLGregorianCalendar value) {
        this.dateMaj = value;
    }

}
