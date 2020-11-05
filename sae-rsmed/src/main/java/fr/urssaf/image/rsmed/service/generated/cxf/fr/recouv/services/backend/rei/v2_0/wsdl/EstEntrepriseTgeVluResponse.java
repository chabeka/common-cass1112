
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;group ref="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}EstEntrepriseTgeVlu_Groupe"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "estVLU",
    "estTGE",
    "urssafDeLiaison"
})
@XmlRootElement(name = "EstEntrepriseTgeVluResponse")
public class EstEntrepriseTgeVluResponse {

    protected boolean estVLU;
    protected boolean estTGE;
    protected String urssafDeLiaison;

    /**
     * Obtient la valeur de la propriété estVLU.
     * 
     */
    public boolean isEstVLU() {
        return estVLU;
    }

    /**
     * Définit la valeur de la propriété estVLU.
     * 
     */
    public void setEstVLU(boolean value) {
        this.estVLU = value;
    }

    /**
     * Obtient la valeur de la propriété estTGE.
     * 
     */
    public boolean isEstTGE() {
        return estTGE;
    }

    /**
     * Définit la valeur de la propriété estTGE.
     * 
     */
    public void setEstTGE(boolean value) {
        this.estTGE = value;
    }

    /**
     * Obtient la valeur de la propriété urssafDeLiaison.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrssafDeLiaison() {
        return urssafDeLiaison;
    }

    /**
     * Définit la valeur de la propriété urssafDeLiaison.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrssafDeLiaison(String value) {
        this.urssafDeLiaison = value;
    }

}
