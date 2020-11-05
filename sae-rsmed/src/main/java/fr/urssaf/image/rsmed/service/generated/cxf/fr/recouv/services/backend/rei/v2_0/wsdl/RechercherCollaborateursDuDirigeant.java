
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
 *         &lt;element name="idIndividuDirigeant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
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
    "idIndividuDirigeant"
})
@XmlRootElement(name = "RechercherCollaborateursDuDirigeant")
public class RechercherCollaborateursDuDirigeant {

    protected long idIndividuDirigeant;

    /**
     * Obtient la valeur de la propriété idIndividuDirigeant.
     * 
     */
    public long getIdIndividuDirigeant() {
        return idIndividuDirigeant;
    }

    /**
     * Définit la valeur de la propriété idIndividuDirigeant.
     * 
     */
    public void setIdIndividuDirigeant(long value) {
        this.idIndividuDirigeant = value;
    }

}
