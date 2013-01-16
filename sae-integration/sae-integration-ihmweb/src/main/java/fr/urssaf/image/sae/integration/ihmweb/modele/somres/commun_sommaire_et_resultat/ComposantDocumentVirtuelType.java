//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 09:24:43 AM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Composant d'un document virtuel (indexation)
 * 
 * <p>Classe Java pour composantDocumentVirtuelType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="composantDocumentVirtuelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType"/>
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "composantDocumentVirtuelType", propOrder = {
    "metadonnees",
    "numeroPageDebut",
    "nombreDePages"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ComposantDocumentVirtuelType {

    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;
    protected int numeroPageDebut;
    protected int nombreDePages;

    /**
     * Obtient la valeur de la propriété metadonnees.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Définit la valeur de la propriété metadonnees.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeType value) {
        this.metadonnees = value;
    }

    /**
     * Obtient la valeur de la propriété numeroPageDebut.
     * 
     */
    public int getNumeroPageDebut() {
        return numeroPageDebut;
    }

    /**
     * Définit la valeur de la propriété numeroPageDebut.
     * 
     */
    public void setNumeroPageDebut(int value) {
        this.numeroPageDebut = value;
    }

    /**
     * Obtient la valeur de la propriété nombreDePages.
     * 
     */
    public int getNombreDePages() {
        return nombreDePages;
    }

    /**
     * Définit la valeur de la propriété nombreDePages.
     * 
     */
    public void setNombreDePages(int value) {
        this.nombreDePages = value;
    }

}
