
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idEntite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="typeEntite" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idEntite",
    "typeEntite"
})
@XmlRootElement(name = "RechercherEntiteDuCompteResponse")
public class RechercherEntiteDuCompteResponse {

    protected Long idEntite;
    protected Integer typeEntite;

    /**
     * Obtient la valeur de la propriété idEntite.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdEntite() {
        return idEntite;
    }

    /**
     * Définit la valeur de la propriété idEntite.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdEntite(Long value) {
        this.idEntite = value;
    }

    /**
     * Obtient la valeur de la propriété typeEntite.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTypeEntite() {
        return typeEntite;
    }

    /**
     * Définit la valeur de la propriété typeEntite.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTypeEntite(Integer value) {
        this.typeEntite = value;
    }

}
