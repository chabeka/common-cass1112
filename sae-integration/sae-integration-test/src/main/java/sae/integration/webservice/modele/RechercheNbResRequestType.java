
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour rechercheNbResRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="rechercheNbResRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requete" type="{http://www.cirtil.fr/saeService}requeteRechercheNbResType"/&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeCodeType"/&gt;
 *         &lt;element name="delai" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheNbResRequestType", propOrder = {
    "requete",
    "metadonnees",
    "delai"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RechercheNbResRequestType {

    @XmlElement(required = true)
    protected String requete;
    @XmlElement(required = true)
    protected ListeMetadonneeCodeType metadonnees;
    @XmlElement(defaultValue = "-1")
    protected Integer delai;

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
     * Obtient la valeur de la propriété metadonnees.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public ListeMetadonneeCodeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Définit la valeur de la propriété metadonnees.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeCodeType value) {
        this.metadonnees = value;
    }

    /**
     * Obtient la valeur de la propriété delai.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDelai() {
        return delai;
    }

    /**
     * Définit la valeur de la propriété delai.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDelai(Integer value) {
        this.delai = value;
    }

}
