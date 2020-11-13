
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
 *         &lt;element name="adresseEtablissement" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CriteresRechercheAdresse_Type"/&gt;
 *         &lt;element name="options" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}OptionsRechercheEtablissement_Type" minOccurs="0"/&gt;
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
    "adresseEtablissement",
    "options"
})
@XmlRootElement(name = "RechercherEtablissementsParAdresse")
public class RechercherEtablissementsParAdresse {

    @XmlElement(required = true)
    protected CriteresRechercheAdresseType adresseEtablissement;
    protected OptionsRechercheEtablissementType options;

    /**
     * Obtient la valeur de la propriété adresseEtablissement.
     * 
     * @return
     *     possible object is
     *     {@link CriteresRechercheAdresseType }
     *     
     */
    public CriteresRechercheAdresseType getAdresseEtablissement() {
        return adresseEtablissement;
    }

    /**
     * Définit la valeur de la propriété adresseEtablissement.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteresRechercheAdresseType }
     *     
     */
    public void setAdresseEtablissement(CriteresRechercheAdresseType value) {
        this.adresseEtablissement = value;
    }

    /**
     * Obtient la valeur de la propriété options.
     * 
     * @return
     *     possible object is
     *     {@link OptionsRechercheEtablissementType }
     *     
     */
    public OptionsRechercheEtablissementType getOptions() {
        return options;
    }

    /**
     * Définit la valeur de la propriété options.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionsRechercheEtablissementType }
     *     
     */
    public void setOptions(OptionsRechercheEtablissementType value) {
        this.options = value;
    }

}