
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Motif etat : code et libelle
 * 
 * <p>Classe Java pour MotifEtat_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="MotifEtat_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeMotifEtat" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeMotifEtat_Type"/&gt;
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
@XmlType(name = "MotifEtat_Type", propOrder = {
    "codeMotifEtat",
    "libelle"
})
public class MotifEtatType {

    @XmlElement(required = true)
    protected String codeMotifEtat;
    protected String libelle;

    /**
     * Obtient la valeur de la propriété codeMotifEtat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeMotifEtat() {
        return codeMotifEtat;
    }

    /**
     * Définit la valeur de la propriété codeMotifEtat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeMotifEtat(String value) {
        this.codeMotifEtat = value;
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
