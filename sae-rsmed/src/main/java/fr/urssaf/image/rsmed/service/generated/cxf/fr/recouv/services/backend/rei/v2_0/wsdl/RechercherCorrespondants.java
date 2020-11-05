
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
 *         &lt;element name="correspondantARechercher" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}CriteresRechercheCorrespondant_Type"/&gt;
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
    "correspondantARechercher"
})
@XmlRootElement(name = "RechercherCorrespondants")
public class RechercherCorrespondants {

    @XmlElement(required = true)
    protected CriteresRechercheCorrespondantType correspondantARechercher;

    /**
     * Obtient la valeur de la propriété correspondantARechercher.
     * 
     * @return
     *     possible object is
     *     {@link CriteresRechercheCorrespondantType }
     *     
     */
    public CriteresRechercheCorrespondantType getCorrespondantARechercher() {
        return correspondantARechercher;
    }

    /**
     * Définit la valeur de la propriété correspondantARechercher.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteresRechercheCorrespondantType }
     *     
     */
    public void setCorrespondantARechercher(CriteresRechercheCorrespondantType value) {
        this.correspondantARechercher = value;
    }

}
