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
 * Une liste de documents
 * 
 * <p>Classe Java pour listeDocumentsTypeMultiAction complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeDocumentsTypeMultiAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="documentMultiAction" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}documentTypeMultiAction" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeDocumentsTypeMultiAction", propOrder = {
    "documentMultiAction"
})
public class ListeDocumentsTypeMultiAction {

    protected List<DocumentTypeMultiAction> documentMultiAction;

    /**
     * Gets the value of the documentMultiAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentMultiAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentMultiAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentTypeMultiAction }
     * 
     * 
     */
    public List<DocumentTypeMultiAction> getDocumentMultiAction() {
        if (documentMultiAction == null) {
            documentMultiAction = new ArrayList<DocumentTypeMultiAction>();
        }
        return this.documentMultiAction;
    }

}
