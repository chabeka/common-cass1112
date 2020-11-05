
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.AdresseDSIType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.CompteDSIType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuDSIType;


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
 *         &lt;element name="codeRetour" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="libelleRetour" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IndividuDSI" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuDSI_Type" minOccurs="0"/&gt;
 *         &lt;element name="CompteDSI" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CompteDSI_Type" minOccurs="0"/&gt;
 *         &lt;element name="AdresseDSI" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}AdresseDSI_Type" minOccurs="0"/&gt;
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
    "codeRetour",
    "libelleRetour",
    "individuDSI",
    "compteDSI",
    "adresseDSI"
})
@XmlRootElement(name = "RechercherIndividuEligibleDSIResponse")
public class RechercherIndividuEligibleDSIResponse {

    @XmlElement(required = true)
    protected String codeRetour;
    protected String libelleRetour;
    @XmlElement(name = "IndividuDSI")
    protected IndividuDSIType individuDSI;
    @XmlElement(name = "CompteDSI")
    protected CompteDSIType compteDSI;
    @XmlElement(name = "AdresseDSI")
    protected AdresseDSIType adresseDSI;

    /**
     * Obtient la valeur de la propriété codeRetour.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeRetour() {
        return codeRetour;
    }

    /**
     * Définit la valeur de la propriété codeRetour.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeRetour(String value) {
        this.codeRetour = value;
    }

    /**
     * Obtient la valeur de la propriété libelleRetour.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleRetour() {
        return libelleRetour;
    }

    /**
     * Définit la valeur de la propriété libelleRetour.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleRetour(String value) {
        this.libelleRetour = value;
    }

    /**
     * Obtient la valeur de la propriété individuDSI.
     * 
     * @return
     *     possible object is
     *     {@link IndividuDSIType }
     *     
     */
    public IndividuDSIType getIndividuDSI() {
        return individuDSI;
    }

    /**
     * Définit la valeur de la propriété individuDSI.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividuDSIType }
     *     
     */
    public void setIndividuDSI(IndividuDSIType value) {
        this.individuDSI = value;
    }

    /**
     * Obtient la valeur de la propriété compteDSI.
     * 
     * @return
     *     possible object is
     *     {@link CompteDSIType }
     *     
     */
    public CompteDSIType getCompteDSI() {
        return compteDSI;
    }

    /**
     * Définit la valeur de la propriété compteDSI.
     * 
     * @param value
     *     allowed object is
     *     {@link CompteDSIType }
     *     
     */
    public void setCompteDSI(CompteDSIType value) {
        this.compteDSI = value;
    }

    /**
     * Obtient la valeur de la propriété adresseDSI.
     * 
     * @return
     *     possible object is
     *     {@link AdresseDSIType }
     *     
     */
    public AdresseDSIType getAdresseDSI() {
        return adresseDSI;
    }

    /**
     * Définit la valeur de la propriété adresseDSI.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseDSIType }
     *     
     */
    public void setAdresseDSI(AdresseDSIType value) {
        this.adresseDSI = value;
    }

}
