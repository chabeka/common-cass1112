//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.10.27 à 11:48:04 AM CET 
//


package fr.urssaf.image.rsmed.bean.xsd.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de documents non archivés
 * 
 * <p>Classe Java pour listeNonIntegratedDocumentsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeNonIntegratedDocumentsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nonIntegratedDocument" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedDocumentType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeNonIntegratedDocumentsType", propOrder = {
    "nonIntegratedDocument"
})
public class ListeNonIntegratedDocumentsType {

    protected List<NonIntegratedDocumentType> nonIntegratedDocument;

    /**
     * Gets the value of the nonIntegratedDocument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonIntegratedDocument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonIntegratedDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NonIntegratedDocumentType }
     * 
     * 
     */
    public List<NonIntegratedDocumentType> getNonIntegratedDocument() {
        if (nonIntegratedDocument == null) {
            nonIntegratedDocument = new ArrayList<NonIntegratedDocumentType>();
        }
        return this.nonIntegratedDocument;
    }

}
