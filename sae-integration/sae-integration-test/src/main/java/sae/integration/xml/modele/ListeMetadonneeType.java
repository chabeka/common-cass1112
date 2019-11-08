//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.06 à 10:57:31 AM CEST 
//


package sae.integration.xml.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de métadonnées
 * 
 * <p>Classe Java pour listeMetadonneeType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeMetadonneeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="metadonnee" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}metadonneeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeMetadonneeType", propOrder = {
    "metadonnee"
})
public class ListeMetadonneeType {

    protected List<MetadonneeType> metadonnee;

    /**
     * Gets the value of the metadonnee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadonnee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadonnee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadonneeType }
     * 
     * 
     */
    public List<MetadonneeType> getMetadonnee() {
        if (metadonnee == null) {
            metadonnee = new ArrayList<MetadonneeType>();
        }
        return this.metadonnee;
    }

}
