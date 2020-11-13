
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="idEtablissement" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IdREI_Type" maxOccurs="50"/&gt;
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
    "idEtablissement",
    "options"
})
@XmlRootElement(name = "RechercheMultipleEtablissementsParIdRei")
public class RechercheMultipleEtablissementsParIdRei {

    @XmlElement(type = Long.class)
    protected List<Long> idEtablissement;
    protected OptionsRechercheEtablissementType options;

    /**
     * Gets the value of the idEtablissement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idEtablissement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdEtablissement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getIdEtablissement() {
        if (idEtablissement == null) {
            idEtablissement = new ArrayList<Long>();
        }
        return this.idEtablissement;
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