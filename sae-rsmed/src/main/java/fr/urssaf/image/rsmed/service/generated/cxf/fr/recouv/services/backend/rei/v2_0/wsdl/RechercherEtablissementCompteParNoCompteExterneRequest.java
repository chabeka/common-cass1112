
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
 *         &lt;element name="noCompteExterneRedevabilite" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT"/&gt;
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
    "noCompteExterneRedevabilite"
})
@XmlRootElement(name = "RechercherEtablissementCompteParNoCompteExterneRequest")
public class RechercherEtablissementCompteParNoCompteExterneRequest {

    @XmlElement(required = true)
    protected String noCompteExterneRedevabilite;

    /**
     * Obtient la valeur de la propriété noCompteExterneRedevabilite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCompteExterneRedevabilite() {
        return noCompteExterneRedevabilite;
    }

    /**
     * Définit la valeur de la propriété noCompteExterneRedevabilite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCompteExterneRedevabilite(String value) {
        this.noCompteExterneRedevabilite = value;
    }

}
