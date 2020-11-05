
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Qualite dirigeant : code et libelle
 * 
 * <p>Classe Java pour QualiteDirigeant_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="QualiteDirigeant_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeQualiteDirigeant" type="{http://cfe.recouv/2008-11/TypeRegent}Dirigeant_Type"/&gt;
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
@XmlType(name = "QualiteDirigeant_Type", propOrder = {
    "codeQualiteDirigeant",
    "libelle"
})
public class QualiteDirigeantType {

    @XmlElement(required = true)
    protected String codeQualiteDirigeant;
    protected String libelle;

    /**
     * Obtient la valeur de la propriété codeQualiteDirigeant.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeQualiteDirigeant() {
        return codeQualiteDirigeant;
    }

    /**
     * Définit la valeur de la propriété codeQualiteDirigeant.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeQualiteDirigeant(String value) {
        this.codeQualiteDirigeant = value;
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
