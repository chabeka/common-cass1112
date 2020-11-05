
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
 *         &lt;element name="idEntrepriseDirigee" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IdREI_Type"/&gt;
 *         &lt;element name="options" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}OptionsRechercheEntreprise_Type" minOccurs="0"/&gt;
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
    "idEntrepriseDirigee",
    "options"
})
@XmlRootElement(name = "RechercherDirigeantsPMDeEntreprise")
public class RechercherDirigeantsPMDeEntreprise {

    protected long idEntrepriseDirigee;
    protected OptionsRechercheEntrepriseType options;

    /**
     * Obtient la valeur de la propriété idEntrepriseDirigee.
     * 
     */
    public long getIdEntrepriseDirigee() {
        return idEntrepriseDirigee;
    }

    /**
     * Définit la valeur de la propriété idEntrepriseDirigee.
     * 
     */
    public void setIdEntrepriseDirigee(long value) {
        this.idEntrepriseDirigee = value;
    }

    /**
     * Obtient la valeur de la propriété options.
     * 
     * @return
     *     possible object is
     *     {@link OptionsRechercheEntrepriseType }
     *     
     */
    public OptionsRechercheEntrepriseType getOptions() {
        return options;
    }

    /**
     * Définit la valeur de la propriété options.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionsRechercheEntrepriseType }
     *     
     */
    public void setOptions(OptionsRechercheEntrepriseType value) {
        this.options = value;
    }

}
