
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.AdresseType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.RedevabiliteType;


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
 *         &lt;element name="siret" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETAlphanumerique_Type" minOccurs="0"/&gt;
 *         &lt;element name="redevabilites" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Redevabilite_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="adresseSiret" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adresse_Type" minOccurs="0"/&gt;
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
    "siret",
    "redevabilites",
    "adresseSiret"
})
@XmlRootElement(name = "RechercherEtablissementCompteParSiretResponse")
public class RechercherEtablissementCompteParSiretResponse {

    protected String siret;
    protected List<RedevabiliteType> redevabilites;
    protected AdresseType adresseSiret;

    /**
     * Obtient la valeur de la propriété siret.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiret() {
        return siret;
    }

    /**
     * Définit la valeur de la propriété siret.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiret(String value) {
        this.siret = value;
    }

    /**
     * Gets the value of the redevabilites property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the redevabilites property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRedevabilites().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RedevabiliteType }
     * 
     * 
     */
    public List<RedevabiliteType> getRedevabilites() {
        if (redevabilites == null) {
            redevabilites = new ArrayList<RedevabiliteType>();
        }
        return this.redevabilites;
    }

    /**
     * Obtient la valeur de la propriété adresseSiret.
     * 
     * @return
     *     possible object is
     *     {@link AdresseType }
     *     
     */
    public AdresseType getAdresseSiret() {
        return adresseSiret;
    }

    /**
     * Définit la valeur de la propriété adresseSiret.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseType }
     *     
     */
    public void setAdresseSiret(AdresseType value) {
        this.adresseSiret = value;
    }

}
