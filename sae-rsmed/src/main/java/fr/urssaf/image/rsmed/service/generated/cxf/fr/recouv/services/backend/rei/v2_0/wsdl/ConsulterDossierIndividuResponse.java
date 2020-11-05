
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuCompletType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.LienCompteType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.LienComptesRadiesType;


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
 *         &lt;element name="individu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuComplet_Type" minOccurs="0"/&gt;
 *         &lt;element name="lienCompte" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}LienCompte_Type" minOccurs="0"/&gt;
 *         &lt;element name="lienComptesRadies" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}LienComptesRadies_Type" minOccurs="0"/&gt;
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
    "individu",
    "lienCompte",
    "lienComptesRadies"
})
@XmlRootElement(name = "ConsulterDossierIndividuResponse")
public class ConsulterDossierIndividuResponse {

    protected IndividuCompletType individu;
    protected LienCompteType lienCompte;
    protected LienComptesRadiesType lienComptesRadies;

    /**
     * Obtient la valeur de la propriété individu.
     * 
     * @return
     *     possible object is
     *     {@link IndividuCompletType }
     *     
     */
    public IndividuCompletType getIndividu() {
        return individu;
    }

    /**
     * Définit la valeur de la propriété individu.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividuCompletType }
     *     
     */
    public void setIndividu(IndividuCompletType value) {
        this.individu = value;
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

    /**
     * Obtient la valeur de la propriété lienComptesRadies.
     * 
     * @return
     *     possible object is
     *     {@link LienComptesRadiesType }
     *     
     */
    public LienComptesRadiesType getLienComptesRadies() {
        return lienComptesRadies;
    }

    /**
     * Définit la valeur de la propriété lienComptesRadies.
     * 
     * @param value
     *     allowed object is
     *     {@link LienComptesRadiesType }
     *     
     */
    public void setLienComptesRadies(LienComptesRadiesType value) {
        this.lienComptesRadies = value;
    }

}
