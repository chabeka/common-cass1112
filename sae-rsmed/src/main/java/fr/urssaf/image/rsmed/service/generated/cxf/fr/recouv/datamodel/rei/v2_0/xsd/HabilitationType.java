
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Une habilitation concerne soit un compte, soit un cotisant (seulement une entreprise au palier 1). Une habilitation est delivree a un correspondant (habilitation interne) ou a une entreprise (habilitation tiers-declarant). Si l'habilitation est delivree a un correspondant, alors l'entite concernee doit etre l'entreprise du correspondant ou appartenir a l'entreprise du correspondant (s'il s'agit d'un compte) ...	
 * 
 * <p>Classe Java pour Habilitation_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Habilitation_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="sirenEntrepriseConcernee" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type"/&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="compteConcerne" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT"/&gt;
 *             &lt;element name="habilitationSoustractive" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Habilitation_Type", propOrder = {
    "sirenEntrepriseConcernee",
    "compteConcerne",
    "habilitationSoustractive"
})
@XmlSeeAlso({
    HabilitationInterneType.class,
    HabilitationTiersDeclarantType.class
})
public class HabilitationType {

    protected String sirenEntrepriseConcernee;
    protected String compteConcerne;
    protected Boolean habilitationSoustractive;

    /**
     * Obtient la valeur de la propriété sirenEntrepriseConcernee.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSirenEntrepriseConcernee() {
        return sirenEntrepriseConcernee;
    }

    /**
     * Définit la valeur de la propriété sirenEntrepriseConcernee.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSirenEntrepriseConcernee(String value) {
        this.sirenEntrepriseConcernee = value;
    }

    /**
     * Obtient la valeur de la propriété compteConcerne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompteConcerne() {
        return compteConcerne;
    }

    /**
     * Définit la valeur de la propriété compteConcerne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompteConcerne(String value) {
        this.compteConcerne = value;
    }

    /**
     * Obtient la valeur de la propriété habilitationSoustractive.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHabilitationSoustractive() {
        return habilitationSoustractive;
    }

    /**
     * Définit la valeur de la propriété habilitationSoustractive.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHabilitationSoustractive(Boolean value) {
        this.habilitationSoustractive = value;
    }

}
