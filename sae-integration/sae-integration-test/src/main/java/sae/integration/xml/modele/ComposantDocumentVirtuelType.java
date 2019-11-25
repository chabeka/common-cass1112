//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.06 à 10:57:31 AM CEST 
//


package sae.integration.xml.modele;

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
 * &lt;complexType name="composantDocumentVirtuelType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType"/&gt;
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
