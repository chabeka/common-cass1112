
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.EntiteType;


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
 *         &lt;element name="id" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="typeEntite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Entite_Type"/&gt;
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
    "id",
    "typeEntite"
})
@XmlRootElement(name = "RechercherEntreprisesEtablissementsAdressesParIdREI")
public class RechercherEntreprisesEtablissementsAdressesParIdREI {

    protected long id;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EntiteType typeEntite;

    /**
     * Obtient la valeur de la propriété id.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété typeEntite.
     * 
     * @return
     *     possible object is
     *     {@link EntiteType }
     *     
     */
    public EntiteType getTypeEntite() {
        return typeEntite;
    }

    /**
     * Définit la valeur de la propriété typeEntite.
     * 
     * @param value
     *     allowed object is
     *     {@link EntiteType }
     *     
     */
    public void setTypeEntite(EntiteType value) {
        this.typeEntite = value;
    }

}
