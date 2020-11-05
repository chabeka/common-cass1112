
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Etablissement avec ses objects connexes (son entreprise, l'adresse, et l'ensemble des redevabilites)
 * 
 * <p>Classe Java pour EtablissementComplet_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EtablissementComplet_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Etablissement_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entreprise" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Entreprise_Type" minOccurs="0"/&gt;
 *         &lt;element name="adresse" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adresse_Type" minOccurs="0"/&gt;
 *         &lt;element name="redevabilites" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Redevabilite_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="lienCompte" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}LienCompte_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EtablissementComplet_Type", propOrder = {
    "entreprise",
    "adresse",
    "redevabilites",
    "lienCompte"
})
public class EtablissementCompletType
    extends EtablissementType
{

    protected EntrepriseType entreprise;
    protected AdresseType adresse;
    protected List<RedevabiliteType> redevabilites;
    protected LienCompteType lienCompte;

    /**
     * Obtient la valeur de la propriété entreprise.
     * 
     * @return
     *     possible object is
     *     {@link EntrepriseType }
     *     
     */
    public EntrepriseType getEntreprise() {
        return entreprise;
    }

    /**
     * Définit la valeur de la propriété entreprise.
     * 
     * @param value
     *     allowed object is
     *     {@link EntrepriseType }
     *     
     */
    public void setEntreprise(EntrepriseType value) {
        this.entreprise = value;
    }

    /**
     * Obtient la valeur de la propriété adresse.
     * 
     * @return
     *     possible object is
     *     {@link AdresseType }
     *     
     */
    public AdresseType getAdresse() {
        return adresse;
    }

    /**
     * Définit la valeur de la propriété adresse.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseType }
     *     
     */
    public void setAdresse(AdresseType value) {
        this.adresse = value;
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
     * Obtient la valeur de la propriété lienCompte.
     * 
     * @return
     *     possible object is
     *     {@link LienCompteType }
     *     
     */
    public LienCompteType getLienCompte() {
        return lienCompte;
    }

    /**
     * Définit la valeur de la propriété lienCompte.
     * 
     * @param value
     *     allowed object is
     *     {@link LienCompteType }
     *     
     */
    public void setLienCompte(LienCompteType value) {
        this.lienCompte = value;
    }

}
