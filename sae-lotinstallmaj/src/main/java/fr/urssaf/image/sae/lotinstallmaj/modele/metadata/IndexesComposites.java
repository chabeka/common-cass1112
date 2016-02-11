//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.02.09 à 11:29:35 AM CET 
//


package fr.urssaf.image.sae.lotinstallmaj.modele.metadata;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour IndexesComposites complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="IndexesComposites">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indexReference" type="{http://www.cirtil.fr/lotinstallmaj/metadata}IndexReference" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndexesComposites", propOrder = {
    "indexReference"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class IndexesComposites {

    protected List<IndexReference> indexReference;

    /**
     * Gets the value of the indexReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indexReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndexReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexReference }
     * 
     * 
     */
    public List<IndexReference> getIndexReference() {
        if (indexReference == null) {
            indexReference = new ArrayList<IndexReference>();
        }
        return this.indexReference;
    }

}
