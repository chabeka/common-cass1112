
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.EtablissementCompletType;


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
 *         &lt;element name="etablissement" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EtablissementComplet_Type" minOccurs="0"/&gt;
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
    "etablissement"
})
@XmlRootElement(name = "ConsulterDossierEtablissementResponse")
public class ConsulterDossierEtablissementResponse {

    protected EtablissementCompletType etablissement;

    /**
     * Obtient la valeur de la propriété etablissement.
     * 
     * @return
     *     possible object is
     *     {@link EtablissementCompletType }
     *     
     */
    public EtablissementCompletType getEtablissement() {
        return etablissement;
    }

    /**
     * Définit la valeur de la propriété etablissement.
     * 
     * @param value
     *     allowed object is
     *     {@link EtablissementCompletType }
     *     
     */
    public void setEtablissement(EtablissementCompletType value) {
        this.etablissement = value;
    }

}