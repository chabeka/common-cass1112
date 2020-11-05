
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="idTeledep" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdTeledep_Type"/&gt;
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
    "idTeledep"
})
@XmlRootElement(name = "RechercherEntrepriseParCorrespondantInterne")
public class RechercherEntrepriseParCorrespondantInterne {

    @XmlElement(required = true)
    protected String idTeledep;

    /**
     * Obtient la valeur de la propriété idTeledep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTeledep() {
        return idTeledep;
    }

    /**
     * Définit la valeur de la propriété idTeledep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTeledep(String value) {
        this.idTeledep = value;
    }

}
