//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.11.17 à 03:49:54 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Composant d'un document virtuel (indexation)
 * 
 * <p>Classe Java pour nonIntegratedComposantDocumentVirtuelType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="nonIntegratedComposantDocumentVirtuelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType" minOccurs="0"/>
 *         &lt;element name="erreurs" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeErreurType"/>
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
@XmlType(name = "nonIntegratedComposantDocumentVirtuelType", propOrder = {
    "metadonnees",
    "erreurs",
    "numeroPageDebut",
    "nombreDePages"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class NonIntegratedComposantDocumentVirtuelType {

    protected ListeMetadonneeType metadonnees;
    @XmlElement(required = true)
    protected ListeErreurType erreurs;
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
     * Obtient la valeur de la propriété erreurs.
     * 
     * @return
     *     possible object is
     *     {@link ListeErreurType }
     *     
     */
    public ListeErreurType getErreurs() {
        return erreurs;
    }

    /**
     * Définit la valeur de la propriété erreurs.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeErreurType }
     *     
     */
    public void setErreurs(ListeErreurType value) {
        this.erreurs = value;
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
