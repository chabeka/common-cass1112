
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour CriteresRechercheCorrespondant_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CriteresRechercheCorrespondant_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="siren" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeCivilite" type="{http://cfe.recouv/2008-11/TypeRegent}Titre_Type" minOccurs="0"/&gt;
 *         &lt;element name="nom" type="{http://cfe.recouv/2008-11/TypeRegent}Nom_Type" minOccurs="0"/&gt;
 *         &lt;element name="prenom" type="{http://cfe.recouv/2008-11/TypeRegent}Prenom_Type" minOccurs="0"/&gt;
 *         &lt;element name="mail" type="{http://cfe.recouv/2008-11/TypeRegent}Mail_Type" minOccurs="0"/&gt;
 *         &lt;element name="telephone" type="{http://cfe.recouv/2008-11/TypeRegent}NumTel_Type" minOccurs="0"/&gt;
 *         &lt;element name="idTeledep" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdTeledep_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CriteresRechercheCorrespondant_Type", propOrder = {
    "siren",
    "codeCivilite",
    "nom",
    "prenom",
    "mail",
    "telephone",
    "idTeledep"
})
public class CriteresRechercheCorrespondantType {

    protected String siren;
    protected String codeCivilite;
    protected String nom;
    protected String prenom;
    protected String mail;
    protected String telephone;
    protected String idTeledep;

    /**
     * Obtient la valeur de la propriété siren.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiren() {
        return siren;
    }

    /**
     * Définit la valeur de la propriété siren.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiren(String value) {
        this.siren = value;
    }

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
     * Obtient la valeur de la propriété mail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMail() {
        return mail;
    }

    /**
     * Définit la valeur de la propriété mail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMail(String value) {
        this.mail = value;
    }

    /**
     * Obtient la valeur de la propriété telephone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit la valeur de la propriété telephone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephone(String value) {
        this.telephone = value;
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

}
