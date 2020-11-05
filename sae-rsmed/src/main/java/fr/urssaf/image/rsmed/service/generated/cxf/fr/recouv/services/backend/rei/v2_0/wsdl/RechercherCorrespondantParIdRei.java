
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
 *         &lt;element name="idCorrespondant" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IdREI_Type"/&gt;
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
    "idCorrespondant"
})
@XmlRootElement(name = "RechercherCorrespondantParIdRei")
public class RechercherCorrespondantParIdRei {

    protected long idCorrespondant;

    /**
     * Obtient la valeur de la propriété idCorrespondant.
     * 
     */
    public long getIdCorrespondant() {
        return idCorrespondant;
    }

    /**
     * Définit la valeur de la propriété idCorrespondant.
     * 
     */
    public void setIdCorrespondant(long value) {
        this.idCorrespondant = value;
    }

}
