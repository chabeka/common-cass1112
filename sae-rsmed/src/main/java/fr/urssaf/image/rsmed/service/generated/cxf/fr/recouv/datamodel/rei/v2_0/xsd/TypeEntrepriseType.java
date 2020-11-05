
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Type d'entreprise : code et libelle 
 * 
 * <p>Classe Java pour TypeEntreprise_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TypeEntreprise_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeType" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeTypeEntreprise_Type"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TypeEntreprise_Type", propOrder = {
    "codeType",
    "libelle"
})
public class TypeEntrepriseType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CodeTypeEntrepriseType codeType;
    protected String libelle;

    /**
     * Obtient la valeur de la propriété codeType.
     * 
     * @return
     *     possible object is
     *     {@link CodeTypeEntrepriseType }
     *     
     */
    public CodeTypeEntrepriseType getCodeType() {
        return codeType;
    }

    /**
     * Définit la valeur de la propriété codeType.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeTypeEntrepriseType }
     *     
     */
    public void setCodeType(CodeTypeEntrepriseType value) {
        this.codeType = value;
    }

    /**
     * Obtient la valeur de la propriété libelle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit la valeur de la propriété libelle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelle(String value) {
        this.libelle = value;
    }

}
