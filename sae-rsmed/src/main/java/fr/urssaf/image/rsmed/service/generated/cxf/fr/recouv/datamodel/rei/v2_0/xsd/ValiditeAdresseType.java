
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Validite de l'adresse : code et libelle en toute lettre
 * 
 * <p>Classe Java pour ValiditeAdresse_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ValiditeAdresse_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeValidite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeValiditeAdresse_Type"/&gt;
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
@XmlType(name = "ValiditeAdresse_Type", propOrder = {
    "codeValidite",
    "libelle"
})
public class ValiditeAdresseType {

    @XmlElement(required = true)
    protected String codeValidite;
    protected String libelle;

    /**
     * Obtient la valeur de la propriété codeValidite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeValidite() {
        return codeValidite;
    }

    /**
     * Définit la valeur de la propriété codeValidite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeValidite(String value) {
        this.codeValidite = value;
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
