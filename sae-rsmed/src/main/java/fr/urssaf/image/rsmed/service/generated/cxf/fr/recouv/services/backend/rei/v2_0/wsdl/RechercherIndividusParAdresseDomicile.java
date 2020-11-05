
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.CriteresRechercheAdresseType;


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
 *         &lt;element name="adresseDomicile" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CriteresRechercheAdresse_Type"/&gt;
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
    "adresseDomicile"
})
@XmlRootElement(name = "RechercherIndividusParAdresseDomicile")
public class RechercherIndividusParAdresseDomicile {

    @XmlElement(required = true)
    protected CriteresRechercheAdresseType adresseDomicile;

    /**
     * Obtient la valeur de la propriété adresseDomicile.
     * 
     * @return
     *     possible object is
     *     {@link CriteresRechercheAdresseType }
     *     
     */
    public CriteresRechercheAdresseType getAdresseDomicile() {
        return adresseDomicile;
    }

    /**
     * Définit la valeur de la propriété adresseDomicile.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteresRechercheAdresseType }
     *     
     */
    public void setAdresseDomicile(CriteresRechercheAdresseType value) {
        this.adresseDomicile = value;
    }

}
