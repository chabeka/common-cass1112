
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.GroupeProfessionnelType;


/**
 * CompteEligibleDSI contient des informations du
 * 						compte eligible a la dsi
 * 
 * <p>Classe Java pour CompteEligibleDSI_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CompteEligibleDSI_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeUR" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *         &lt;element name="denomination" type="{http://cfe.recouv/2008-11/TypeRegent}Denomination_Type" minOccurs="0"/&gt;
 *         &lt;element name="groupePro" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}GroupeProfessionnel_Type" minOccurs="0"/&gt;
 *         &lt;element name="noCompteExterne" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT" minOccurs="0"/&gt;
 *         &lt;element name="noRiba" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRIBA_Type" minOccurs="0"/&gt;
 *         &lt;element name="topEligibleDSI" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="sousCategorie" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CD-SS-CAT" minOccurs="0"/&gt;
 *         &lt;element name="statut" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CD-ETA-CPT" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompteEligibleDSI_Type", propOrder = {
    "codeUR",
    "denomination",
    "groupePro",
    "noCompteExterne",
    "noRiba",
    "topEligibleDSI",
    "sousCategorie",
    "statut"
})
public class CompteEligibleDSIType {

    protected String codeUR;
    protected String denomination;
    protected GroupeProfessionnelType groupePro;
    protected String noCompteExterne;
    protected String noRiba;
    protected Boolean topEligibleDSI;
    protected String sousCategorie;
    protected String statut;

    /**
     * Obtient la valeur de la propriété codeUR.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeUR() {
        return codeUR;
    }

    /**
     * Définit la valeur de la propriété codeUR.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeUR(String value) {
        this.codeUR = value;
    }

    /**
     * Obtient la valeur de la propriété denomination.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomination() {
        return denomination;
    }

    /**
     * Définit la valeur de la propriété denomination.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomination(String value) {
        this.denomination = value;
    }

    /**
     * Obtient la valeur de la propriété groupePro.
     * 
     * @return
     *     possible object is
     *     {@link GroupeProfessionnelType }
     *     
     */
    public GroupeProfessionnelType getGroupePro() {
        return groupePro;
    }

    /**
     * Définit la valeur de la propriété groupePro.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupeProfessionnelType }
     *     
     */
    public void setGroupePro(GroupeProfessionnelType value) {
        this.groupePro = value;
    }

    /**
     * Obtient la valeur de la propriété noCompteExterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCompteExterne() {
        return noCompteExterne;
    }

    /**
     * Définit la valeur de la propriété noCompteExterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCompteExterne(String value) {
        this.noCompteExterne = value;
    }

    /**
     * Obtient la valeur de la propriété noRiba.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRiba() {
        return noRiba;
    }

    /**
     * Définit la valeur de la propriété noRiba.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRiba(String value) {
        this.noRiba = value;
    }

    /**
     * Obtient la valeur de la propriété topEligibleDSI.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTopEligibleDSI() {
        return topEligibleDSI;
    }

    /**
     * Définit la valeur de la propriété topEligibleDSI.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTopEligibleDSI(Boolean value) {
        this.topEligibleDSI = value;
    }

    /**
     * Obtient la valeur de la propriété sousCategorie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSousCategorie() {
        return sousCategorie;
    }

    /**
     * Définit la valeur de la propriété sousCategorie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSousCategorie(String value) {
        this.sousCategorie = value;
    }

    /**
     * Obtient la valeur de la propriété statut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit la valeur de la propriété statut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatut(String value) {
        this.statut = value;
    }

}
