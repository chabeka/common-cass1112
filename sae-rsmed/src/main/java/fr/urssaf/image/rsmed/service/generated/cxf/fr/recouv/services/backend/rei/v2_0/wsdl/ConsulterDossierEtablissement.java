
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
 *         &lt;element name="idEtablissement" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
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
    "idEtablissement"
})
@XmlRootElement(name = "ConsulterDossierEtablissement")
public class ConsulterDossierEtablissement {

    protected long idEtablissement;

    /**
     * Obtient la valeur de la propriété idEtablissement.
     * 
     */
    public long getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Définit la valeur de la propriété idEtablissement.
     * 
     */
    public void setIdEtablissement(long value) {
        this.idEtablissement = value;
    }

}
