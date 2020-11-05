
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Correspond aux donnees de contact (telephone, mail, fax, mobile).	
 * 
 * <p>Classe Java pour DonneesContact_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DonneesContact_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeTypeContact" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeTypeContact_Type" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="telFixeFax" type="{http://cfe.recouv/2008-11/TypeRegent}NumTel_Type" minOccurs="0"/&gt;
 *           &lt;element name="courriel" type="{http://cfe.recouv/2008-11/TypeRegent}Mail_Type" minOccurs="0"/&gt;
 *           &lt;element name="telMobile" type="{http://cfe.recouv/2008-11/TypeRegent}NumTel_Type" minOccurs="0"/&gt;
 *           &lt;element name="telecopie" type="{http://cfe.recouv/2008-11/TypeRegent}NumTel_Type" minOccurs="0"/&gt;
 *           &lt;element name="autres" type="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="criticite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="niveauFiabilite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateEffet" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="codeOrigine" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeOrigine_Type" minOccurs="0"/&gt;
 *         &lt;element name="idREI" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="entiteCotisanteId" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="correspondantId" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DonneesContact_Type", propOrder = {
    "codeTypeContact",
    "telFixeFax",
    "courriel",
    "telMobile",
    "telecopie",
    "autres",
    "criticite",
    "niveauFiabilite",
    "dateEffet",
    "codeOrigine",
    "idREI",
    "entiteCotisanteId",
    "correspondantId"
})
public class DonneesContactType {

    protected String codeTypeContact;
    protected String telFixeFax;
    protected String courriel;
    protected String telMobile;
    protected String telecopie;
    protected String autres;
    protected String criticite;
    protected String niveauFiabilite;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffet;
    protected Integer codeOrigine;
    protected Long idREI;
    protected Long entiteCotisanteId;
    protected Long correspondantId;

    /**
     * Obtient la valeur de la propriété codeTypeContact.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTypeContact() {
        return codeTypeContact;
    }

    /**
     * Définit la valeur de la propriété codeTypeContact.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTypeContact(String value) {
        this.codeTypeContact = value;
    }

    /**
     * Obtient la valeur de la propriété telFixeFax.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelFixeFax() {
        return telFixeFax;
    }

    /**
     * Définit la valeur de la propriété telFixeFax.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelFixeFax(String value) {
        this.telFixeFax = value;
    }

    /**
     * Obtient la valeur de la propriété courriel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourriel() {
        return courriel;
    }

    /**
     * Définit la valeur de la propriété courriel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourriel(String value) {
        this.courriel = value;
    }

    /**
     * Obtient la valeur de la propriété telMobile.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelMobile() {
        return telMobile;
    }

    /**
     * Définit la valeur de la propriété telMobile.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelMobile(String value) {
        this.telMobile = value;
    }

    /**
     * Obtient la valeur de la propriété telecopie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelecopie() {
        return telecopie;
    }

    /**
     * Définit la valeur de la propriété telecopie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelecopie(String value) {
        this.telecopie = value;
    }

    /**
     * Obtient la valeur de la propriété autres.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutres() {
        return autres;
    }

    /**
     * Définit la valeur de la propriété autres.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutres(String value) {
        this.autres = value;
    }

    /**
     * Obtient la valeur de la propriété criticite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticite() {
        return criticite;
    }

    /**
     * Définit la valeur de la propriété criticite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticite(String value) {
        this.criticite = value;
    }

    /**
     * Obtient la valeur de la propriété niveauFiabilite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNiveauFiabilite() {
        return niveauFiabilite;
    }

    /**
     * Définit la valeur de la propriété niveauFiabilite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNiveauFiabilite(String value) {
        this.niveauFiabilite = value;
    }

    /**
     * Obtient la valeur de la propriété dateEffet.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateEffet() {
        return dateEffet;
    }

    /**
     * Définit la valeur de la propriété dateEffet.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateEffet(XMLGregorianCalendar value) {
        this.dateEffet = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrigine.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodeOrigine() {
        return codeOrigine;
    }

    /**
     * Définit la valeur de la propriété codeOrigine.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodeOrigine(Integer value) {
        this.codeOrigine = value;
    }

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
     * Obtient la valeur de la propriété entiteCotisanteId.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEntiteCotisanteId() {
        return entiteCotisanteId;
    }

    /**
     * Définit la valeur de la propriété entiteCotisanteId.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEntiteCotisanteId(Long value) {
        this.entiteCotisanteId = value;
    }

    /**
     * Obtient la valeur de la propriété correspondantId.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCorrespondantId() {
        return correspondantId;
    }

    /**
     * Définit la valeur de la propriété correspondantId.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCorrespondantId(Long value) {
        this.correspondantId = value;
    }

}
