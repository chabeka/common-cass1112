
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.AdresseType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.EtablissementType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="erreur" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}Erreur_Type" maxOccurs="unbounded"/&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="etablissementSiege" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Etablissement_Type"/&gt;
 *           &lt;element name="adresseImplantation" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adresse_Type"/&gt;
 *           &lt;element name="denominationEntreprise" type="{http://cfe.recouv/2008-11/TypeRegent}Denomination_Type"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="nbTotal" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "erreur",
    "etablissementSiege",
    "adresseImplantation",
    "denominationEntreprise"
})
@XmlRootElement(name = "ControlerSiretResponse")
public class ControlerSiretResponse {

    protected List<ErreurType> erreur;
    protected EtablissementType etablissementSiege;
    protected AdresseType adresseImplantation;
    protected String denominationEntreprise;
    @XmlAttribute(name = "nbTotal")
    protected Integer nbTotal;

    /**
     * Gets the value of the erreur property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the erreur property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErreur().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErreurType }
     * 
     * 
     */
    public List<ErreurType> getErreur() {
        if (erreur == null) {
            erreur = new ArrayList<ErreurType>();
        }
        return this.erreur;
    }

    /**
     * Obtient la valeur de la propriété etablissementSiege.
     * 
     * @return
     *     possible object is
     *     {@link EtablissementType }
     *     
     */
    public EtablissementType getEtablissementSiege() {
        return etablissementSiege;
    }

    /**
     * Définit la valeur de la propriété etablissementSiege.
     * 
     * @param value
     *     allowed object is
     *     {@link EtablissementType }
     *     
     */
    public void setEtablissementSiege(EtablissementType value) {
        this.etablissementSiege = value;
    }

    /**
     * Obtient la valeur de la propriété adresseImplantation.
     * 
     * @return
     *     possible object is
     *     {@link AdresseType }
     *     
     */
    public AdresseType getAdresseImplantation() {
        return adresseImplantation;
    }

    /**
     * Définit la valeur de la propriété adresseImplantation.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseType }
     *     
     */
    public void setAdresseImplantation(AdresseType value) {
        this.adresseImplantation = value;
    }

    /**
     * Obtient la valeur de la propriété denominationEntreprise.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominationEntreprise() {
        return denominationEntreprise;
    }

    /**
     * Définit la valeur de la propriété denominationEntreprise.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominationEntreprise(String value) {
        this.denominationEntreprise = value;
    }

    /**
     * Obtient la valeur de la propriété nbTotal.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbTotal() {
        return nbTotal;
    }

    /**
     * Définit la valeur de la propriété nbTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbTotal(Integer value) {
        this.nbTotal = value;
    }

}
