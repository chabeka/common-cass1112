
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="nomFichierEntree" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nomFichierSortie" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "nomFichierEntree",
    "nomFichierSortie"
})
@XmlRootElement(name = "BatchMiseAJourActionControleRequest")
public class BatchMiseAJourActionControleRequest {

    @XmlElement(required = true)
    protected String nomFichierEntree;
    @XmlElement(required = true)
    protected String nomFichierSortie;

    /**
     * Obtient la valeur de la propriété nomFichierEntree.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomFichierEntree() {
        return nomFichierEntree;
    }

    /**
     * Définit la valeur de la propriété nomFichierEntree.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomFichierEntree(String value) {
        this.nomFichierEntree = value;
    }

    /**
     * Obtient la valeur de la propriété nomFichierSortie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomFichierSortie() {
        return nomFichierSortie;
    }

    /**
     * Définit la valeur de la propriété nomFichierSortie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomFichierSortie(String value) {
        this.nomFichierSortie = value;
    }

}
