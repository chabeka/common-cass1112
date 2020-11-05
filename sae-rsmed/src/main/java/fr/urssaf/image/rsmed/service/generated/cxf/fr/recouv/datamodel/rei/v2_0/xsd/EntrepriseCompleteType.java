
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Entreprise avec ses objets connexes (adresse, ensemble des etablissements, des redevabilites, des dirigeants)
 * 
 * <p>Classe Java pour EntrepriseComplete_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EntrepriseComplete_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Entreprise_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adresseSiege" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adresse_Type" minOccurs="0"/&gt;
 *         &lt;element name="etablissements" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EtablissementComplet_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="redevabilites" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Redevabilite_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dirigeants" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DirigeantPersPhysique_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="nbEtablissements" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntrepriseComplete_Type", propOrder = {
    "adresseSiege",
    "etablissements",
    "redevabilites",
    "dirigeants",
    "nbEtablissements"
})
public class EntrepriseCompleteType
    extends EntrepriseType
{

    protected AdresseType adresseSiege;
    protected List<EtablissementCompletType> etablissements;
    protected List<RedevabiliteType> redevabilites;
    protected List<DirigeantPersPhysiqueType> dirigeants;
    protected Integer nbEtablissements;

    /**
     * Obtient la valeur de la propriété adresseSiege.
     * 
     * @return
     *     possible object is
     *     {@link AdresseType }
     *     
     */
    public AdresseType getAdresseSiege() {
        return adresseSiege;
    }

    /**
     * Définit la valeur de la propriété adresseSiege.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseType }
     *     
     */
    public void setAdresseSiege(AdresseType value) {
        this.adresseSiege = value;
    }

    /**
     * Gets the value of the etablissements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etablissements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEtablissements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EtablissementCompletType }
     * 
     * 
     */
    public List<EtablissementCompletType> getEtablissements() {
        if (etablissements == null) {
            etablissements = new ArrayList<EtablissementCompletType>();
        }
        return this.etablissements;
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
     * Gets the value of the dirigeants property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dirigeants property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirigeants().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DirigeantPersPhysiqueType }
     * 
     * 
     */
    public List<DirigeantPersPhysiqueType> getDirigeants() {
        if (dirigeants == null) {
            dirigeants = new ArrayList<DirigeantPersPhysiqueType>();
        }
        return this.dirigeants;
    }

    /**
     * Obtient la valeur de la propriété nbEtablissements.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbEtablissements() {
        return nbEtablissements;
    }

    /**
     * Définit la valeur de la propriété nbEtablissements.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbEtablissements(Integer value) {
        this.nbEtablissements = value;
    }

}
