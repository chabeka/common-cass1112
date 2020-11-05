
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
 *         &lt;element name="sirenTiersDeclarant" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type"/&gt;
 *         &lt;element name="options" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}OptionsRechercheRedevabilite_Type" minOccurs="0"/&gt;
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
    "sirenTiersDeclarant",
    "options"
})
@XmlRootElement(name = "RechercherRedevabilitesParTiersDeclarant")
public class RechercherRedevabilitesParTiersDeclarant {

    @XmlElement(required = true)
    protected String sirenTiersDeclarant;
    protected OptionsRechercheRedevabiliteType options;

    /**
     * Obtient la valeur de la propriété sirenTiersDeclarant.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSirenTiersDeclarant() {
        return sirenTiersDeclarant;
    }

    /**
     * Définit la valeur de la propriété sirenTiersDeclarant.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSirenTiersDeclarant(String value) {
        this.sirenTiersDeclarant = value;
    }

    /**
     * Obtient la valeur de la propriété options.
     * 
     * @return
     *     possible object is
     *     {@link OptionsRechercheRedevabiliteType }
     *     
     */
    public OptionsRechercheRedevabiliteType getOptions() {
        return options;
    }

    /**
     * Définit la valeur de la propriété options.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionsRechercheRedevabiliteType }
     *     
     */
    public void setOptions(OptionsRechercheRedevabiliteType value) {
        this.options = value;
    }

}
