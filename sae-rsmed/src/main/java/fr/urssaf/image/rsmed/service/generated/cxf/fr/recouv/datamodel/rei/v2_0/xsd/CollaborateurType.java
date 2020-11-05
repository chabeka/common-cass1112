
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * lien de collaboration entre 2 individus defini par le role
 * 
 * <p>Classe Java pour Collaborateur_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Collaborateur_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idIndividu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="idIndividuDirigeant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="roleCollaborateur" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}RoleCollaboration_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Collaborateur_Type", propOrder = {
    "idIndividu",
    "idIndividuDirigeant",
    "roleCollaborateur"
})
@XmlSeeAlso({
    CollaborateurCompletType.class
})
public class CollaborateurType {

    protected long idIndividu;
    protected long idIndividuDirigeant;
    @XmlElement(required = true)
    protected RoleCollaborationType roleCollaborateur;

    /**
     * Obtient la valeur de la propriété idIndividu.
     * 
     */
    public long getIdIndividu() {
        return idIndividu;
    }

    /**
     * Définit la valeur de la propriété idIndividu.
     * 
     */
    public void setIdIndividu(long value) {
        this.idIndividu = value;
    }

    /**
     * Obtient la valeur de la propriété idIndividuDirigeant.
     * 
     */
    public long getIdIndividuDirigeant() {
        return idIndividuDirigeant;
    }

    /**
     * Définit la valeur de la propriété idIndividuDirigeant.
     * 
     */
    public void setIdIndividuDirigeant(long value) {
        this.idIndividuDirigeant = value;
    }

    /**
     * Obtient la valeur de la propriété roleCollaborateur.
     * 
     * @return
     *     possible object is
     *     {@link RoleCollaborationType }
     *     
     */
    public RoleCollaborationType getRoleCollaborateur() {
        return roleCollaborateur;
    }

    /**
     * Définit la valeur de la propriété roleCollaborateur.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleCollaborationType }
     *     
     */
    public void setRoleCollaborateur(RoleCollaborationType value) {
        this.roleCollaborateur = value;
    }

}
