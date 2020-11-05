
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Lien entre compte et etablissement
 * 
 * <p>Classe Java pour LienCompte_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="LienCompte_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nbComptesActifs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeroCompteExterne" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT" minOccurs="0"/&gt;
 *         &lt;element name="idEtablissement" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LienCompte_Type", propOrder = {
    "nbComptesActifs",
    "numeroCompteExterne",
    "idEtablissement"
})
public class LienCompteType {

    protected Integer nbComptesActifs;
    protected String numeroCompteExterne;
    protected Long idEtablissement;

    /**
     * Obtient la valeur de la propriété nbComptesActifs.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbComptesActifs() {
        return nbComptesActifs;
    }

    /**
     * Définit la valeur de la propriété nbComptesActifs.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbComptesActifs(Integer value) {
        this.nbComptesActifs = value;
    }

    /**
     * Obtient la valeur de la propriété numeroCompteExterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCompteExterne() {
        return numeroCompteExterne;
    }

    /**
     * Définit la valeur de la propriété numeroCompteExterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCompteExterne(String value) {
        this.numeroCompteExterne = value;
    }

    /**
     * Obtient la valeur de la propriété idEtablissement.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Définit la valeur de la propriété idEtablissement.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdEtablissement(Long value) {
        this.idEtablissement = value;
    }

}
