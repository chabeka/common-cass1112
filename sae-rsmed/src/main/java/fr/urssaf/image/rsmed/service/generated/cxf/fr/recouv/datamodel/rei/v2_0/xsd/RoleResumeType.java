
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * RoleResume contient une sous partie des informations propres a un role
 * 
 * <p>Classe Java pour RoleResume_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="RoleResume_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntiteCotisante_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="noRole" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRole_Type" minOccurs="0"/&gt;
 *         &lt;element name="nomRole" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NomRole_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="codeTypeNavigation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codePortArmement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoleResume_Type", propOrder = {
    "noRole",
    "nomRole",
    "dateCreation",
    "codeTypeNavigation",
    "codePortArmement"
})
@XmlSeeAlso({
    RoleType.class
})
public class RoleResumeType
    extends EntiteCotisanteType
{

    protected String noRole;
    protected String nomRole;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCreation;
    protected String codeTypeNavigation;
    protected String codePortArmement;

    /**
     * Obtient la valeur de la propriété noRole.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRole() {
        return noRole;
    }

    /**
     * Définit la valeur de la propriété noRole.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRole(String value) {
        this.noRole = value;
    }

    /**
     * Obtient la valeur de la propriété nomRole.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomRole() {
        return nomRole;
    }

    /**
     * Définit la valeur de la propriété nomRole.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomRole(String value) {
        this.nomRole = value;
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
     * Obtient la valeur de la propriété codeTypeNavigation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTypeNavigation() {
        return codeTypeNavigation;
    }

    /**
     * Définit la valeur de la propriété codeTypeNavigation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTypeNavigation(String value) {
        this.codeTypeNavigation = value;
    }

    /**
     * Obtient la valeur de la propriété codePortArmement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodePortArmement() {
        return codePortArmement;
    }

    /**
     * Définit la valeur de la propriété codePortArmement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodePortArmement(String value) {
        this.codePortArmement = value;
    }

}
