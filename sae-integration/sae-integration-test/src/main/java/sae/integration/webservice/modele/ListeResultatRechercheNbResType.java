
package sae.integration.webservice.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Liste de résultat d'une recherche de
 *             documents
 *          
 * 
 * <p>Classe Java pour listeResultatRechercheNbResType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeResultatRechercheNbResType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultat" type="{http://www.cirtil.fr/saeService}resultatRechercheType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeResultatRechercheNbResType", propOrder = {
    "resultat"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeResultatRechercheNbResType {

    protected List<ResultatRechercheType> resultat;

    /**
     * Gets the value of the resultat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultatRechercheType }
     * 
     * 
     */
    public List<ResultatRechercheType> getResultat() {
        if (resultat == null) {
            resultat = new ArrayList<ResultatRechercheType>();
        }
        return this.resultat;
    }

}
