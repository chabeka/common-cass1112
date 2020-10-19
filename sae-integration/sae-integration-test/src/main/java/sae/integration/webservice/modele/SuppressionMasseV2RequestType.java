
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération
 *             'suppressionMasseV2'
 *          
 * 
 * <p>Classe Java pour suppressionMasseV2RequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="suppressionMasseV2RequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requete" type="{http://www.cirtil.fr/saeService}requeteRechercheType"/&gt;
 *         &lt;element name="codeOrgaProprietaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "suppressionMasseV2RequestType", propOrder = {
    "requete",
    "codeOrgaProprietaire"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SuppressionMasseV2RequestType {

    @XmlElement(required = true)
    protected String requete;
    protected String codeOrgaProprietaire;

    /**
     * Obtient la valeur de la propriété requete.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequete() {
        return requete;
    }

    /**
     * Définit la valeur de la propriété requete.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequete(String value) {
        this.requete = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrgaProprietaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOrgaProprietaire() {
        return codeOrgaProprietaire;
    }

    /**
     * Définit la valeur de la propriété codeOrgaProprietaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOrgaProprietaire(String value) {
        this.codeOrgaProprietaire = value;
    }

}
