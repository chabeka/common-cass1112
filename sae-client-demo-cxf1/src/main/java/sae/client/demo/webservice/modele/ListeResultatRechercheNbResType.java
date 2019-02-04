
package sae.client.demo.webservice.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Liste de résultat d'une recherche de
 *             documents
 * 
 * <p>Java class for listeResultatRechercheNbResType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeResultatRechercheNbResType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultat" type="{http://www.cirtil.fr/saeService}resultatRechercheType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
