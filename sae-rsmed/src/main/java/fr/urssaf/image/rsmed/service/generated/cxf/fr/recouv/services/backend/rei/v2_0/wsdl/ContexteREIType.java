
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.EvenementTransactionnelType;


/**
 * <p>Classe Java pour ContexteREI_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ContexteREI_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeOrigine" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeOrigine_Type"/&gt;
 *         &lt;element name="dNUtilisateur" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DNUtilisateur_Type" minOccurs="0"/&gt;
 *         &lt;element name="profilUtilisateur" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}ProfilUtilisateur_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeOrganisme" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *         &lt;element name="codeAgentV2" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeAgentV2_Type" minOccurs="0"/&gt;
 *         &lt;element name="noLiasse" type="{http://cfe.recouv/2008-11/TypeRegent}NumLiasse_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateEffet" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="evenementTransactionnel" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EvenementTransactionnel_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContexteREI_Type", propOrder = {
    "codeOrigine",
    "dnUtilisateur",
    "profilUtilisateur",
    "codeOrganisme",
    "codeAgentV2",
    "noLiasse",
    "dateEffet",
    "evenementTransactionnel"
})
public class ContexteREIType {

    protected int codeOrigine;
    @XmlElement(name = "dNUtilisateur")
    protected String dnUtilisateur;
    protected String profilUtilisateur;
    protected String codeOrganisme;
    protected String codeAgentV2;
    protected String noLiasse;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffet;
    protected EvenementTransactionnelType evenementTransactionnel;

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

    /**
     * Obtient la valeur de la propriété dnUtilisateur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDNUtilisateur() {
        return dnUtilisateur;
    }

    /**
     * Définit la valeur de la propriété dnUtilisateur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDNUtilisateur(String value) {
        this.dnUtilisateur = value;
    }

    /**
     * Obtient la valeur de la propriété profilUtilisateur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfilUtilisateur() {
        return profilUtilisateur;
    }

    /**
     * Définit la valeur de la propriété profilUtilisateur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfilUtilisateur(String value) {
        this.profilUtilisateur = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrganisme.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOrganisme() {
        return codeOrganisme;
    }

    /**
     * Définit la valeur de la propriété codeOrganisme.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOrganisme(String value) {
        this.codeOrganisme = value;
    }

    /**
     * Obtient la valeur de la propriété codeAgentV2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeAgentV2() {
        return codeAgentV2;
    }

    /**
     * Définit la valeur de la propriété codeAgentV2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeAgentV2(String value) {
        this.codeAgentV2 = value;
    }

    /**
     * Obtient la valeur de la propriété noLiasse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoLiasse() {
        return noLiasse;
    }

    /**
     * Définit la valeur de la propriété noLiasse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoLiasse(String value) {
        this.noLiasse = value;
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
     * Obtient la valeur de la propriété evenementTransactionnel.
     * 
     * @return
     *     possible object is
     *     {@link EvenementTransactionnelType }
     *     
     */
    public EvenementTransactionnelType getEvenementTransactionnel() {
        return evenementTransactionnel;
    }

    /**
     * Définit la valeur de la propriété evenementTransactionnel.
     * 
     * @param value
     *     allowed object is
     *     {@link EvenementTransactionnelType }
     *     
     */
    public void setEvenementTransactionnel(EvenementTransactionnelType value) {
        this.evenementTransactionnel = value;
    }

}
