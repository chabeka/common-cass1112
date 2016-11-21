//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.11.17 à 03:49:54 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de documents virtuels archivés
 * 
 * <p>Classe Java pour listeIntegratedDocumentsVirtuelsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeIntegratedDocumentsVirtuelsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="integratedDocumentVirtuel" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}integratedDocumentVirtuelType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeIntegratedDocumentsVirtuelsType", propOrder = {
    "integratedDocumentVirtuel"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeIntegratedDocumentsVirtuelsType {

    protected List<IntegratedDocumentVirtuelType> integratedDocumentVirtuel;

    /**
     * Gets the value of the integratedDocumentVirtuel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the integratedDocumentVirtuel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntegratedDocumentVirtuel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IntegratedDocumentVirtuelType }
     * 
     * 
     */
    public List<IntegratedDocumentVirtuelType> getIntegratedDocumentVirtuel() {
        if (integratedDocumentVirtuel == null) {
            integratedDocumentVirtuel = new ArrayList<IntegratedDocumentVirtuelType>();
        }
        return this.integratedDocumentVirtuel;
    }

}
