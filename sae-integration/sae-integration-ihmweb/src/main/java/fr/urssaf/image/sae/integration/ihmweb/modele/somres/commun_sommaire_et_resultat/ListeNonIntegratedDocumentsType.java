//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 09:24:43 AM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

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
 * &lt;complexType name="listeNonIntegratedDocumentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nonIntegratedDocument" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedDocumentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeNonIntegratedDocumentsType", propOrder = {
    "nonIntegratedDocument"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
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
