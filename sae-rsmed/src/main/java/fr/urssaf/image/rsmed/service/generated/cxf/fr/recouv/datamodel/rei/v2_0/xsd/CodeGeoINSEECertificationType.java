
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Code geo soumis a certification
 * 
 * <p>Classe Java pour CodeGeoINSEECertification_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CodeGeoINSEECertification_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DonneeSoumiseACertification_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="valeur" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}REICodeGeoINSEE_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeGeoINSEECertification_Type", propOrder = {
    "valeur"
})
public class CodeGeoINSEECertificationType
    extends DonneeSoumiseACertificationType
{

    protected REICodeGeoINSEEType valeur;

    /**
     * Obtient la valeur de la propriété valeur.
     * 
     * @return
     *     possible object is
     *     {@link REICodeGeoINSEEType }
     *     
     */
    public REICodeGeoINSEEType getValeur() {
        return valeur;
    }

    /**
     * Définit la valeur de la propriété valeur.
     * 
     * @param value
     *     allowed object is
     *     {@link REICodeGeoINSEEType }
     *     
     */
    public void setValeur(REICodeGeoINSEEType value) {
        this.valeur = value;
    }

}
