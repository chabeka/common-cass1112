
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.RedevabiliteType;


/**
 * <p>Classe Java pour SiretEtRedevabilite_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SiretEtRedevabilite_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="siret" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETAlphanumerique_Type" minOccurs="0"/&gt;
 *         &lt;element name="redevabilite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Redevabilite_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiretEtRedevabilite_Type", propOrder = {
    "siret",
    "redevabilite"
})
public class SiretEtRedevabiliteType {

    protected String siret;
    @XmlElement(required = true)
    protected RedevabiliteType redevabilite;

    /**
     * Obtient la valeur de la propriété siret.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiret() {
        return siret;
    }

    /**
     * Définit la valeur de la propriété siret.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiret(String value) {
        this.siret = value;
    }

    /**
     * Obtient la valeur de la propriété redevabilite.
     * 
     * @return
     *     possible object is
     *     {@link RedevabiliteType }
     *     
     */
    public RedevabiliteType getRedevabilite() {
        return redevabilite;
    }

    /**
     * Définit la valeur de la propriété redevabilite.
     * 
     * @param value
     *     allowed object is
     *     {@link RedevabiliteType }
     *     
     */
    public void setRedevabilite(RedevabiliteType value) {
        this.redevabilite = value;
    }

}
