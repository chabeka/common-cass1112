
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Role pour la CMAF
 * 
 * <p>Classe Java pour Role_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Role_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}RoleResume_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="jauge" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="longueur" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="dateArmement" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dateDesarmement" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="dateMiseLocationGerance" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="codeGenreNavigation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codePortImmatriculation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresseRole" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adresse_Type" minOccurs="0"/&gt;
 *         &lt;element name="redevabilites" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}RedevabiliteResume_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="donneesContact" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DonneesContact_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="coherences" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Coherence_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="entreprisesLieesAuRole" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntrepriseLieeAuRole_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="individusLiesAuRole" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuLieAuRole_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Role_Type", propOrder = {
    "jauge",
    "longueur",
    "dateArmement",
    "dateDesarmement",
    "dateMiseLocationGerance",
    "codeGenreNavigation",
    "codePortImmatriculation",
    "adresseRole",
    "redevabilites",
    "donneesContact",
    "coherences",
    "entreprisesLieesAuRole",
    "individusLiesAuRole"
})
public class RoleType
    extends RoleResumeType
{

    protected Float jauge;
    protected Float longueur;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateArmement;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateDesarmement;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateMiseLocationGerance;
    protected String codeGenreNavigation;
    protected String codePortImmatriculation;
    protected AdresseType adresseRole;
    protected List<RedevabiliteResumeType> redevabilites;
    protected List<DonneesContactType> donneesContact;
    protected List<CoherenceType> coherences;
    protected List<EntrepriseLieeAuRoleType> entreprisesLieesAuRole;
    protected List<IndividuLieAuRoleType> individusLiesAuRole;

    /**
     * Obtient la valeur de la propriété jauge.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getJauge() {
        return jauge;
    }

    /**
     * Définit la valeur de la propriété jauge.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setJauge(Float value) {
        this.jauge = value;
    }

    /**
     * Obtient la valeur de la propriété longueur.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getLongueur() {
        return longueur;
    }

    /**
     * Définit la valeur de la propriété longueur.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setLongueur(Float value) {
        this.longueur = value;
    }

    /**
     * Obtient la valeur de la propriété dateArmement.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateArmement() {
        return dateArmement;
    }

    /**
     * Définit la valeur de la propriété dateArmement.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateArmement(XMLGregorianCalendar value) {
        this.dateArmement = value;
    }

    /**
     * Obtient la valeur de la propriété dateDesarmement.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDesarmement() {
        return dateDesarmement;
    }

    /**
     * Définit la valeur de la propriété dateDesarmement.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDesarmement(XMLGregorianCalendar value) {
        this.dateDesarmement = value;
    }

    /**
     * Obtient la valeur de la propriété dateMiseLocationGerance.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateMiseLocationGerance() {
        return dateMiseLocationGerance;
    }

    /**
     * Définit la valeur de la propriété dateMiseLocationGerance.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateMiseLocationGerance(XMLGregorianCalendar value) {
        this.dateMiseLocationGerance = value;
    }

    /**
     * Obtient la valeur de la propriété codeGenreNavigation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeGenreNavigation() {
        return codeGenreNavigation;
    }

    /**
     * Définit la valeur de la propriété codeGenreNavigation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeGenreNavigation(String value) {
        this.codeGenreNavigation = value;
    }

    /**
     * Obtient la valeur de la propriété codePortImmatriculation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodePortImmatriculation() {
        return codePortImmatriculation;
    }

    /**
     * Définit la valeur de la propriété codePortImmatriculation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodePortImmatriculation(String value) {
        this.codePortImmatriculation = value;
    }

    /**
     * Obtient la valeur de la propriété adresseRole.
     * 
     * @return
     *     possible object is
     *     {@link AdresseType }
     *     
     */
    public AdresseType getAdresseRole() {
        return adresseRole;
    }

    /**
     * Définit la valeur de la propriété adresseRole.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseType }
     *     
     */
    public void setAdresseRole(AdresseType value) {
        this.adresseRole = value;
    }

    /**
     * Gets the value of the redevabilites property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the redevabilites property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRedevabilites().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RedevabiliteResumeType }
     * 
     * 
     */
    public List<RedevabiliteResumeType> getRedevabilites() {
        if (redevabilites == null) {
            redevabilites = new ArrayList<RedevabiliteResumeType>();
        }
        return this.redevabilites;
    }

    /**
     * Gets the value of the donneesContact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the donneesContact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDonneesContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DonneesContactType }
     * 
     * 
     */
    public List<DonneesContactType> getDonneesContact() {
        if (donneesContact == null) {
            donneesContact = new ArrayList<DonneesContactType>();
        }
        return this.donneesContact;
    }

    /**
     * Gets the value of the coherences property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coherences property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoherences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CoherenceType }
     * 
     * 
     */
    public List<CoherenceType> getCoherences() {
        if (coherences == null) {
            coherences = new ArrayList<CoherenceType>();
        }
        return this.coherences;
    }

    /**
     * Gets the value of the entreprisesLieesAuRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entreprisesLieesAuRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntreprisesLieesAuRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntrepriseLieeAuRoleType }
     * 
     * 
     */
    public List<EntrepriseLieeAuRoleType> getEntreprisesLieesAuRole() {
        if (entreprisesLieesAuRole == null) {
            entreprisesLieesAuRole = new ArrayList<EntrepriseLieeAuRoleType>();
        }
        return this.entreprisesLieesAuRole;
    }

    /**
     * Gets the value of the individusLiesAuRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the individusLiesAuRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndividusLiesAuRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndividuLieAuRoleType }
     * 
     * 
     */
    public List<IndividuLieAuRoleType> getIndividusLiesAuRole() {
        if (individusLiesAuRole == null) {
            individusLiesAuRole = new ArrayList<IndividuLieAuRoleType>();
        }
        return this.individusLiesAuRole;
    }

}
