
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Role resume et presence personnel embarque pour la CMAF
 * 
 * <p>Classe Java pour RoleLieAEntiteCotisante_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="RoleLieAEntiteCotisante_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="role" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}RoleResume_Type" minOccurs="0"/&gt;
 *         &lt;element name="presencePersonnel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="estEmbarque" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoleLieAEntiteCotisante_Type", propOrder = {
    "role",
    "presencePersonnel",
    "estEmbarque"
})
public class RoleLieAEntiteCotisanteType {

    protected RoleResumeType role;
    protected Boolean presencePersonnel;
    protected Boolean estEmbarque;

    /**
     * Obtient la valeur de la propriété role.
     * 
     * @return
     *     possible object is
     *     {@link RoleResumeType }
     *     
     */
    public RoleResumeType getRole() {
        return role;
    }

    /**
     * Définit la valeur de la propriété role.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleResumeType }
     *     
     */
    public void setRole(RoleResumeType value) {
        this.role = value;
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
     * Obtient la valeur de la propriété estEmbarque.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstEmbarque() {
        return estEmbarque;
    }

    /**
     * Définit la valeur de la propriété estEmbarque.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstEmbarque(Boolean value) {
        this.estEmbarque = value;
    }

}
