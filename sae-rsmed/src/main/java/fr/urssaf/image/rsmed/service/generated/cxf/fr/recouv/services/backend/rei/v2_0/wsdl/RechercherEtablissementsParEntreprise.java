
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
 *         &lt;element name="idEntreprise" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IdREI_Type"/&gt;
 *         &lt;element name="organismeGestionnaire" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
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
    "idEntreprise",
    "organismeGestionnaire",
    "options"
})
@XmlRootElement(name = "RechercherEtablissementsParEntreprise")
public class RechercherEtablissementsParEntreprise {

    protected long idEntreprise;
    protected String organismeGestionnaire;
    protected OptionsRechercheEtablissementType options;

    /**
     * Obtient la valeur de la propriété idEntreprise.
     * 
     */
    public long getIdEntreprise() {
        return idEntreprise;
    }

    /**
     * Définit la valeur de la propriété idEntreprise.
     * 
     */
    public void setIdEntreprise(long value) {
        this.idEntreprise = value;
    }

    /**
     * Obtient la valeur de la propriété organismeGestionnaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganismeGestionnaire() {
        return organismeGestionnaire;
    }

    /**
     * Définit la valeur de la propriété organismeGestionnaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganismeGestionnaire(String value) {
        this.organismeGestionnaire = value;
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
