//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.04 à 10:35:02 AM CEST 
//


package sae.integration.xml.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de documents non archivés
 * 
 * <p>Classe Java pour listeNonIntegratedVirtualDocumentsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeNonIntegratedVirtualDocumentsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nonIntegratedVirtualDocument" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedVirtualDocumentType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeNonIntegratedVirtualDocumentsType", propOrder = {
    "nonIntegratedVirtualDocument"
})
public class ListeNonIntegratedVirtualDocumentsType {

    protected List<NonIntegratedVirtualDocumentType> nonIntegratedVirtualDocument;

    /**
     * Gets the value of the nonIntegratedVirtualDocument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonIntegratedVirtualDocument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonIntegratedVirtualDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NonIntegratedVirtualDocumentType }
     * 
     * 
     */
    public List<NonIntegratedVirtualDocumentType> getNonIntegratedVirtualDocument() {
        if (nonIntegratedVirtualDocument == null) {
            nonIntegratedVirtualDocument = new ArrayList<NonIntegratedVirtualDocumentType>();
        }
        return this.nonIntegratedVirtualDocument;
    }

}
