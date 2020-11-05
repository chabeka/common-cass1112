
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * EntrepriseResume contient une sous partie des informations propres a une entreprise
 * 
 * <p>Classe Java pour EntrepriseResume_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EntrepriseResume_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntiteCotisante_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="siren" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRENCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="denomination" type="{http://cfe.recouv/2008-11/TypeRegent}Denomination_Type" minOccurs="0"/&gt;
 *         &lt;element name="formeJuridiqueINSEE" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeCJCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeNAF" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeNAFCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="type" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}TypeEntreprise_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntrepriseResume_Type", propOrder = {
    "siren",
    "denomination",
    "formeJuridiqueINSEE",
    "codeNAF",
    "type"
})
@XmlSeeAlso({
    EntrepriseType.class,
    TiersDeclarantType.class
})
public class EntrepriseResumeType
    extends EntiteCotisanteType
{

    protected SIRENCertificationType siren;
    protected String denomination;
    protected CodeCJCertificationType formeJuridiqueINSEE;
    protected CodeNAFCertificationType codeNAF;
    @XmlElement(required = true)
    protected TypeEntrepriseType type;

    /**
     * Obtient la valeur de la propriété siren.
     * 
     * @return
     *     possible object is
     *     {@link SIRENCertificationType }
     *     
     */
    public SIRENCertificationType getSiren() {
        return siren;
    }

    /**
     * Définit la valeur de la propriété siren.
     * 
     * @param value
     *     allowed object is
     *     {@link SIRENCertificationType }
     *     
     */
    public void setSiren(SIRENCertificationType value) {
        this.siren = value;
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
     * Obtient la valeur de la propriété formeJuridiqueINSEE.
     * 
     * @return
     *     possible object is
     *     {@link CodeCJCertificationType }
     *     
     */
    public CodeCJCertificationType getFormeJuridiqueINSEE() {
        return formeJuridiqueINSEE;
    }

    /**
     * Définit la valeur de la propriété formeJuridiqueINSEE.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeCJCertificationType }
     *     
     */
    public void setFormeJuridiqueINSEE(CodeCJCertificationType value) {
        this.formeJuridiqueINSEE = value;
    }

    /**
     * Obtient la valeur de la propriété codeNAF.
     * 
     * @return
     *     possible object is
     *     {@link CodeNAFCertificationType }
     *     
     */
    public CodeNAFCertificationType getCodeNAF() {
        return codeNAF;
    }

    /**
     * Définit la valeur de la propriété codeNAF.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeNAFCertificationType }
     *     
     */
    public void setCodeNAF(CodeNAFCertificationType value) {
        this.codeNAF = value;
    }

    /**
     * Obtient la valeur de la propriété type.
     * 
     * @return
     *     possible object is
     *     {@link TypeEntrepriseType }
     *     
     */
    public TypeEntrepriseType getType() {
        return type;
    }

    /**
     * Définit la valeur de la propriété type.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeEntrepriseType }
     *     
     */
    public void setType(TypeEntrepriseType value) {
        this.type = value;
    }

}
