
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
 *         &lt;group ref="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}NomPrenom_Groupe"/&gt;
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
    "nomPatronymique",
    "prenomUsuel"
})
@XmlRootElement(name = "RechercherIndividusParNomPrenom")
public class RechercherIndividusParNomPrenom {

    @XmlElement(required = true)
    protected String nomPatronymique;
    @XmlElement(required = true)
    protected String prenomUsuel;

    /**
     * Obtient la valeur de la propriété nomPatronymique.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomPatronymique() {
        return nomPatronymique;
    }

    /**
     * Définit la valeur de la propriété nomPatronymique.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomPatronymique(String value) {
        this.nomPatronymique = value;
    }

    /**
     * Obtient la valeur de la propriété prenomUsuel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrenomUsuel() {
        return prenomUsuel;
    }

    /**
     * Définit la valeur de la propriété prenomUsuel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrenomUsuel(String value) {
        this.prenomUsuel = value;
    }

}
