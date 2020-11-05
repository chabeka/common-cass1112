
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * L'etablissement est la plus petite unite de production, localisee geographiquement, individualisee. Il depend juridiquement d'une entreprise. Sa production est relativement homogene. Le siege est l'etablissement principal, lorsqu'une entreprise n'exerce pas ses activites dans un seul etablissement.
 * 
 * <p>Classe Java pour Etablissement_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Etablissement_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EtablissementResume_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeNaf" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeNAFCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateDebutActivite" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateFinActivite" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateMiseEnLocGerance" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="presencePersonnel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="activitePlusImportante" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}ActivitePlusImportante_Type" minOccurs="0"/&gt;
 *         &lt;element name="ensActivitesExercees" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EnsActivitesExercees_Type" minOccurs="0"/&gt;
 *         &lt;element name="etablissementSuccesseur" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="etablissementPredecesseur" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateDebutEmbauche" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateFinEmbauche" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Etablissement_Type", propOrder = {
    "codeNaf",
    "dateCreation",
    "dateDebutActivite",
    "dateFinActivite",
    "dateMiseEnLocGerance",
    "presencePersonnel",
    "activitePlusImportante",
    "ensActivitesExercees",
    "etablissementSuccesseur",
    "etablissementPredecesseur",
    "dateDebutEmbauche",
    "dateFinEmbauche"
})
@XmlSeeAlso({
    EtablissementCompletType.class
})
public class EtablissementType
    extends EtablissementResumeType
{

    protected CodeNAFCertificationType codeNaf;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateCreation;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebutActivite;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFinActivite;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateMiseEnLocGerance;
    protected Boolean presencePersonnel;
    protected String activitePlusImportante;
    protected String ensActivitesExercees;
    protected Long etablissementSuccesseur;
    protected Long etablissementPredecesseur;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebutEmbauche;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFinEmbauche;

    /**
     * Obtient la valeur de la propriété codeNaf.
     * 
     * @return
     *     possible object is
     *     {@link CodeNAFCertificationType }
     *     
     */
    public CodeNAFCertificationType getCodeNaf() {
        return codeNaf;
    }

    /**
     * Définit la valeur de la propriété codeNaf.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeNAFCertificationType }
     *     
     */
    public void setCodeNaf(CodeNAFCertificationType value) {
        this.codeNaf = value;
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
     * Obtient la valeur de la propriété dateFinActivite.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFinActivite() {
        return dateFinActivite;
    }

    /**
     * Définit la valeur de la propriété dateFinActivite.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFinActivite(XMLGregorianCalendar value) {
        this.dateFinActivite = value;
    }

    /**
     * Obtient la valeur de la propriété dateMiseEnLocGerance.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateMiseEnLocGerance() {
        return dateMiseEnLocGerance;
    }

    /**
     * Définit la valeur de la propriété dateMiseEnLocGerance.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateMiseEnLocGerance(XMLGregorianCalendar value) {
        this.dateMiseEnLocGerance = value;
    }

    /**
     * Obtient la valeur de la propriété presencePersonnel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPresencePersonnel() {
        return presencePersonnel;
    }

    /**
     * Définit la valeur de la propriété presencePersonnel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPresencePersonnel(Boolean value) {
        this.presencePersonnel = value;
    }

    /**
     * Obtient la valeur de la propriété activitePlusImportante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivitePlusImportante() {
        return activitePlusImportante;
    }

    /**
     * Définit la valeur de la propriété activitePlusImportante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivitePlusImportante(String value) {
        this.activitePlusImportante = value;
    }

    /**
     * Obtient la valeur de la propriété ensActivitesExercees.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnsActivitesExercees() {
        return ensActivitesExercees;
    }

    /**
     * Définit la valeur de la propriété ensActivitesExercees.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnsActivitesExercees(String value) {
        this.ensActivitesExercees = value;
    }

    /**
     * Obtient la valeur de la propriété etablissementSuccesseur.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEtablissementSuccesseur() {
        return etablissementSuccesseur;
    }

    /**
     * Définit la valeur de la propriété etablissementSuccesseur.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEtablissementSuccesseur(Long value) {
        this.etablissementSuccesseur = value;
    }

    /**
     * Obtient la valeur de la propriété etablissementPredecesseur.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEtablissementPredecesseur() {
        return etablissementPredecesseur;
    }

    /**
     * Définit la valeur de la propriété etablissementPredecesseur.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEtablissementPredecesseur(Long value) {
        this.etablissementPredecesseur = value;
    }

    /**
     * Obtient la valeur de la propriété dateDebutEmbauche.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebutEmbauche() {
        return dateDebutEmbauche;
    }

    /**
     * Définit la valeur de la propriété dateDebutEmbauche.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebutEmbauche(XMLGregorianCalendar value) {
        this.dateDebutEmbauche = value;
    }

    /**
     * Obtient la valeur de la propriété dateFinEmbauche.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFinEmbauche() {
        return dateFinEmbauche;
    }

    /**
     * Définit la valeur de la propriété dateFinEmbauche.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFinEmbauche(XMLGregorianCalendar value) {
        this.dateFinEmbauche = value;
    }

}
