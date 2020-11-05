
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Individu et ses objets connexes (adresse, redevabilites, collaborateurs, entreprise personne physique dirigee ou autre lien de gerance)
 * 
 * <p>Classe Java pour IndividuComplet_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="IndividuComplet_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Individu_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adresseDomicile" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adresse_Type" minOccurs="0"/&gt;
 *         &lt;element name="redevabilites" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Redevabilite_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="collaborateurs" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CollaborateurComplet_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dirigeants" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CollaborateurComplet_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="entreprisesPP" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Entreprise_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="estPAM" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividuComplet_Type", propOrder = {
    "adresseDomicile",
    "redevabilites",
    "collaborateurs",
    "dirigeants",
    "entreprisesPP",
    "estPAM"
})
public class IndividuCompletType
    extends IndividuType
{

    protected AdresseType adresseDomicile;
    protected List<RedevabiliteType> redevabilites;
    protected List<CollaborateurCompletType> collaborateurs;
    protected List<CollaborateurCompletType> dirigeants;
    protected List<EntrepriseType> entreprisesPP;
    protected Boolean estPAM;

    /**
     * Obtient la valeur de la propriété adresseDomicile.
     * 
     * @return
     *     possible object is
     *     {@link AdresseType }
     *     
     */
    public AdresseType getAdresseDomicile() {
        return adresseDomicile;
    }

    /**
     * Définit la valeur de la propriété adresseDomicile.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseType }
     *     
     */
    public void setAdresseDomicile(AdresseType value) {
        this.adresseDomicile = value;
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
     * Gets the value of the collaborateurs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collaborateurs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollaborateurs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CollaborateurCompletType }
     * 
     * 
     */
    public List<CollaborateurCompletType> getCollaborateurs() {
        if (collaborateurs == null) {
            collaborateurs = new ArrayList<CollaborateurCompletType>();
        }
        return this.collaborateurs;
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
     * {@link CollaborateurCompletType }
     * 
     * 
     */
    public List<CollaborateurCompletType> getDirigeants() {
        if (dirigeants == null) {
            dirigeants = new ArrayList<CollaborateurCompletType>();
        }
        return this.dirigeants;
    }

    /**
     * Gets the value of the entreprisesPP property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entreprisesPP property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntreprisesPP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntrepriseType }
     * 
     * 
     */
    public List<EntrepriseType> getEntreprisesPP() {
        if (entreprisesPP == null) {
            entreprisesPP = new ArrayList<EntrepriseType>();
        }
        return this.entreprisesPP;
    }

    /**
     * Obtient la valeur de la propriété estPAM.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstPAM() {
        return estPAM;
    }

    /**
     * Définit la valeur de la propriété estPAM.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstPAM(Boolean value) {
        this.estPAM = value;
    }

}
