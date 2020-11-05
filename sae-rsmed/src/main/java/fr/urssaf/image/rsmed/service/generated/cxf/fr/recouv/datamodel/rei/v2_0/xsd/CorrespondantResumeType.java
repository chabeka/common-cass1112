
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Ne contient que les informations identifiantes du correspondant
 * 
 * <p>Classe Java pour CorrespondantResume_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CorrespondantResume_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeCivilite" type="{http://cfe.recouv/2008-11/TypeRegent}Titre_Type" minOccurs="0"/&gt;
 *         &lt;element name="nom" type="{http://cfe.recouv/2008-11/TypeRegent}Nom_Type"/&gt;
 *         &lt;element name="prenom" type="{http://cfe.recouv/2008-11/TypeRegent}Prenom_Type"/&gt;
 *         &lt;element name="idTeledep" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdTeledep_Type"/&gt;
 *         &lt;element name="entrepriseEmployante" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type"/&gt;
 *         &lt;element name="donneesContact" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DonneesContact_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeOrigine" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeOrigine_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrespondantResume_Type", propOrder = {
    "codeCivilite",
    "nom",
    "prenom",
    "idTeledep",
    "entrepriseEmployante",
    "donneesContact",
    "id",
    "codeOrigine"
})
@XmlSeeAlso({
    CorrespondantType.class
})
public class CorrespondantResumeType {

    protected String codeCivilite;
    @XmlElement(required = true)
    protected String nom;
    @XmlElement(required = true)
    protected String prenom;
    @XmlElement(required = true)
    protected String idTeledep;
    @XmlElement(required = true)
    protected String entrepriseEmployante;
    protected List<DonneesContactType> donneesContact;
    protected Long id;
    protected int codeOrigine;

    /**
     * Obtient la valeur de la propriété codeCivilite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCivilite() {
        return codeCivilite;
    }

    /**
     * Définit la valeur de la propriété codeCivilite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCivilite(String value) {
        this.codeCivilite = value;
    }

    /**
     * Obtient la valeur de la propriété nom.
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
     * Définit la valeur de la propriété nom.
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
     * Obtient la valeur de la propriété prenom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit la valeur de la propriété prenom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrenom(String value) {
        this.prenom = value;
    }

    /**
     * Obtient la valeur de la propriété idTeledep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTeledep() {
        return idTeledep;
    }

    /**
     * Définit la valeur de la propriété idTeledep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTeledep(String value) {
        this.idTeledep = value;
    }

    /**
     * Obtient la valeur de la propriété entrepriseEmployante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntrepriseEmployante() {
        return entrepriseEmployante;
    }

    /**
     * Définit la valeur de la propriété entrepriseEmployante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntrepriseEmployante(String value) {
        this.entrepriseEmployante = value;
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
     * Obtient la valeur de la propriété id.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrigine.
     * 
     */
    public int getCodeOrigine() {
        return codeOrigine;
    }

    /**
     * Définit la valeur de la propriété codeOrigine.
     * 
     */
    public void setCodeOrigine(int value) {
        this.codeOrigine = value;
    }

}
