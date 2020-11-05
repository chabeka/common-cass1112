
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour OptionsRechercheRedevabilite_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="OptionsRechercheRedevabilite_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="organismeProprietaire" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptionsRechercheRedevabilite_Type", propOrder = {
    "organismeProprietaire"
})
public class OptionsRechercheRedevabiliteType {

    protected String organismeProprietaire;

    /**
     * Obtient la valeur de la propriété organismeProprietaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganismeProprietaire() {
        return organismeProprietaire;
    }

    /**
     * Définit la valeur de la propriété organismeProprietaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganismeProprietaire(String value) {
        this.organismeProprietaire = value;
    }

}
