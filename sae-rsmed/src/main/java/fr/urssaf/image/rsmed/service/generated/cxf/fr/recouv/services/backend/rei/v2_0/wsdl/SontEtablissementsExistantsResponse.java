
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *         &lt;group ref="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}SontEtablissementsExistants_Groupe" maxOccurs="unbounded"/&gt;
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
    "sontEtablissementsExistantsGroupe"
})
@XmlRootElement(name = "SontEtablissementsExistantsResponse")
public class SontEtablissementsExistantsResponse {

    @XmlElementRefs({
        @XmlElementRef(name = "siret", type = JAXBElement.class),
        @XmlElementRef(name = "existe", type = JAXBElement.class),
        @XmlElementRef(name = "active", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends Serializable>> sontEtablissementsExistantsGroupe;

    /**
     * Gets the value of the sontEtablissementsExistantsGroupe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sontEtablissementsExistantsGroupe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSontEtablissementsExistantsGroupe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Serializable>> getSontEtablissementsExistantsGroupe() {
        if (sontEtablissementsExistantsGroupe == null) {
            sontEtablissementsExistantsGroupe = new ArrayList<JAXBElement<? extends Serializable>>();
        }
        return this.sontEtablissementsExistantsGroupe;
    }

}
