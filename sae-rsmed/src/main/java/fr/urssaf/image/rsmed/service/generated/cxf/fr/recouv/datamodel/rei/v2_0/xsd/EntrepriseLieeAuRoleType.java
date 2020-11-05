
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Entreprise pour la CMAF
 * 
 * <p>Classe Java pour EntrepriseLieeAuRole_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EntrepriseLieeAuRole_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entreprise" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntrepriseResume_Type" minOccurs="0"/&gt;
 *         &lt;element name="presencePersonnel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="armateurPrincipal" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntrepriseLieeAuRole_Type", propOrder = {
    "entreprise",
    "presencePersonnel",
    "armateurPrincipal"
})
public class EntrepriseLieeAuRoleType {

    protected EntrepriseResumeType entreprise;
    protected Boolean presencePersonnel;
    protected Boolean armateurPrincipal;

    /**
     * Obtient la valeur de la propriété entreprise.
     * 
     * @return
     *     possible object is
     *     {@link EntrepriseResumeType }
     *     
     */
    public EntrepriseResumeType getEntreprise() {
        return entreprise;
    }

    /**
     * Définit la valeur de la propriété entreprise.
     * 
     * @param value
     *     allowed object is
     *     {@link EntrepriseResumeType }
     *     
     */
    public void setEntreprise(EntrepriseResumeType value) {
        this.entreprise = value;
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
     * Obtient la valeur de la propriété armateurPrincipal.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isArmateurPrincipal() {
        return armateurPrincipal;
    }

    /**
     * Définit la valeur de la propriété armateurPrincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setArmateurPrincipal(Boolean value) {
        this.armateurPrincipal = value;
    }

}
