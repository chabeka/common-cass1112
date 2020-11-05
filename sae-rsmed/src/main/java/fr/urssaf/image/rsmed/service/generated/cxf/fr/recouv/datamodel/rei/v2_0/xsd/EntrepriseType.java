
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Au sens INSEE du terme une entreprise est une unite legale identifiee par son numero SIREN.
 * 
 * <p>Classe Java pour Entreprise_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Entreprise_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntrepriseResume_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="activitePrincipaleDeclaree" type="{http://cfe.recouv/2008-11/TypeRegent}Activite420_Type" minOccurs="0"/&gt;
 *         &lt;element name="urResponsable" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME"/&gt;
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dateDebutActivite" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="natureGerance" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NatureGerance_Type" minOccurs="0"/&gt;
 *         &lt;element name="nomCommercial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateCessation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dateDisparition" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="codeGreffeImmatRCS" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeGreffeImmatRCSCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeDptImmatRM" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeDptImmatRMCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeSigle" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeSigle_Type" minOccurs="0"/&gt;
 *         &lt;element name="noRCS" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRCS_Type" minOccurs="0"/&gt;
 *         &lt;element name="noRM" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRM_Type" minOccurs="0"/&gt;
 *         &lt;element name="idAgessa" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoAGESSA_Type" minOccurs="0"/&gt;
 *         &lt;element name="idMdArtiste" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoMDArtiste_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeSRC" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeSRC_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Entreprise_Type", propOrder = {
    "activitePrincipaleDeclaree",
    "urResponsable",
    "dateCreation",
    "dateDebutActivite",
    "natureGerance",
    "nomCommercial",
    "dateCessation",
    "dateDisparition",
    "codeGreffeImmatRCS",
    "codeDptImmatRM",
    "codeSigle",
    "noRCS",
    "noRM",
    "idAgessa",
    "idMdArtiste",
    "codeSRC"
})
@XmlSeeAlso({
    EntrepriseCompleteType.class
})
public class EntrepriseType
    extends EntrepriseResumeType
{

    protected String activitePrincipaleDeclaree;
    @XmlElement(required = true)
    protected String urResponsable;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCreation;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateDebutActivite;
    protected NatureGeranceType natureGerance;
    protected String nomCommercial;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCessation;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateDisparition;
    protected CodeGreffeImmatRCSCertificationType codeGreffeImmatRCS;
    protected CodeDptImmatRMCertificationType codeDptImmatRM;
    protected String codeSigle;
    protected String noRCS;
    protected String noRM;
    protected String idAgessa;
    protected String idMdArtiste;
    protected String codeSRC;

    /**
     * Obtient la valeur de la propriété activitePrincipaleDeclaree.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivitePrincipaleDeclaree() {
        return activitePrincipaleDeclaree;
    }

    /**
     * Définit la valeur de la propriété activitePrincipaleDeclaree.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivitePrincipaleDeclaree(String value) {
        this.activitePrincipaleDeclaree = value;
    }

    /**
     * Obtient la valeur de la propriété urResponsable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrResponsable() {
        return urResponsable;
    }

    /**
     * Définit la valeur de la propriété urResponsable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrResponsable(String value) {
        this.urResponsable = value;
    }

    /**
     * Obtient la valeur de la propriété dateCreation.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCreation() {
        return dateCreation;
    }

    /**
     * Définit la valeur de la propriété dateCreation.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreation(XMLGregorianCalendar value) {
        this.dateCreation = value;
    }

    /**
     * Obtient la valeur de la propriété dateDebutActivite.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebutActivite() {
        return dateDebutActivite;
    }

    /**
     * Définit la valeur de la propriété dateDebutActivite.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebutActivite(XMLGregorianCalendar value) {
        this.dateDebutActivite = value;
    }

    /**
     * Obtient la valeur de la propriété natureGerance.
     * 
     * @return
     *     possible object is
     *     {@link NatureGeranceType }
     *     
     */
    public NatureGeranceType getNatureGerance() {
        return natureGerance;
    }

    /**
     * Définit la valeur de la propriété natureGerance.
     * 
     * @param value
     *     allowed object is
     *     {@link NatureGeranceType }
     *     
     */
    public void setNatureGerance(NatureGeranceType value) {
        this.natureGerance = value;
    }

    /**
     * Obtient la valeur de la propriété nomCommercial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomCommercial() {
        return nomCommercial;
    }

    /**
     * Définit la valeur de la propriété nomCommercial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomCommercial(String value) {
        this.nomCommercial = value;
    }

    /**
     * Obtient la valeur de la propriété dateCessation.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCessation() {
        return dateCessation;
    }

    /**
     * Définit la valeur de la propriété dateCessation.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCessation(XMLGregorianCalendar value) {
        this.dateCessation = value;
    }

    /**
     * Obtient la valeur de la propriété dateDisparition.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDisparition() {
        return dateDisparition;
    }

    /**
     * Définit la valeur de la propriété dateDisparition.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDisparition(XMLGregorianCalendar value) {
        this.dateDisparition = value;
    }

    /**
     * Obtient la valeur de la propriété codeGreffeImmatRCS.
     * 
     * @return
     *     possible object is
     *     {@link CodeGreffeImmatRCSCertificationType }
     *     
     */
    public CodeGreffeImmatRCSCertificationType getCodeGreffeImmatRCS() {
        return codeGreffeImmatRCS;
    }

    /**
     * Définit la valeur de la propriété codeGreffeImmatRCS.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeGreffeImmatRCSCertificationType }
     *     
     */
    public void setCodeGreffeImmatRCS(CodeGreffeImmatRCSCertificationType value) {
        this.codeGreffeImmatRCS = value;
    }

    /**
     * Obtient la valeur de la propriété codeDptImmatRM.
     * 
     * @return
     *     possible object is
     *     {@link CodeDptImmatRMCertificationType }
     *     
     */
    public CodeDptImmatRMCertificationType getCodeDptImmatRM() {
        return codeDptImmatRM;
    }

    /**
     * Définit la valeur de la propriété codeDptImmatRM.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeDptImmatRMCertificationType }
     *     
     */
    public void setCodeDptImmatRM(CodeDptImmatRMCertificationType value) {
        this.codeDptImmatRM = value;
    }

    /**
     * Obtient la valeur de la propriété codeSigle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSigle() {
        return codeSigle;
    }

    /**
     * Définit la valeur de la propriété codeSigle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSigle(String value) {
        this.codeSigle = value;
    }

    /**
     * Obtient la valeur de la propriété noRCS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRCS() {
        return noRCS;
    }

    /**
     * Définit la valeur de la propriété noRCS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRCS(String value) {
        this.noRCS = value;
    }

    /**
     * Obtient la valeur de la propriété noRM.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRM() {
        return noRM;
    }

    /**
     * Définit la valeur de la propriété noRM.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRM(String value) {
        this.noRM = value;
    }

    /**
     * Obtient la valeur de la propriété idAgessa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAgessa() {
        return idAgessa;
    }

    /**
     * Définit la valeur de la propriété idAgessa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAgessa(String value) {
        this.idAgessa = value;
    }

    /**
     * Obtient la valeur de la propriété idMdArtiste.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMdArtiste() {
        return idMdArtiste;
    }

    /**
     * Définit la valeur de la propriété idMdArtiste.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMdArtiste(String value) {
        this.idMdArtiste = value;
    }

    /**
     * Obtient la valeur de la propriété codeSRC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSRC() {
        return codeSRC;
    }

    /**
     * Définit la valeur de la propriété codeSRC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSRC(String value) {
        this.codeSRC = value;
    }

}
