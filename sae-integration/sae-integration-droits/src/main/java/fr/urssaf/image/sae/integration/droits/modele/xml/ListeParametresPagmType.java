//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 02:11:30 PM CET 
//


package fr.urssaf.image.sae.integration.droits.modele.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour ListeParametresPagmType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ListeParametresPagmType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parametre" type="{http://www.cirtil.fr/saeIntegration/droit}ParametrePagmType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListeParametresPagmType", propOrder = {
    "parametre"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeParametresPagmType {

    protected List<ParametrePagmType> parametre;

    /**
     * Gets the value of the parametre property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parametre property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParametre().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParametrePagmType }
     * 
     * 
     */
    public List<ParametrePagmType> getParametre() {
        if (parametre == null) {
            parametre = new ArrayList<ParametrePagmType>();
        }
        return this.parametre;
    }

}
