//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.05.13 à 03:05:24 PM CEST 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de documents virtuels
 * 
 * <p>Classe Java pour listeDocumentsVirtuelsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeDocumentsVirtuelsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentVirtuel" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}documentVirtuelType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeDocumentsVirtuelsType", propOrder = {
    "documentVirtuel"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeDocumentsVirtuelsType {

    protected List<DocumentVirtuelType> documentVirtuel;

    /**
     * Gets the value of the documentVirtuel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentVirtuel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentVirtuel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentVirtuelType }
     * 
     * 
     */
    public List<DocumentVirtuelType> getDocumentVirtuel() {
        if (documentVirtuel == null) {
            documentVirtuel = new ArrayList<DocumentVirtuelType>();
        }
        return this.documentVirtuel;
    }

}
