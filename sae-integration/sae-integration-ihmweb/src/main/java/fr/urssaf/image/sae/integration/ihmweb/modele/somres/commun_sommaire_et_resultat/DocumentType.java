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
 * Définition d'un document à archiver
 * 
 * <p>Classe Java pour documentType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="documentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType"/>
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentType", propOrder = {
    "objetNumerique",
    "metadonnees",
    "numeroPageDebut",
    "nombreDePages"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class DocumentType {

    @XmlElement(required = true)
    protected FichierType objetNumerique;
    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;
    protected Integer numeroPageDebut;
    protected Integer nombreDePages;

    /**
     * Obtient la valeur de la propriété objetNumerique.
     * 
     * @return
     *     possible object is
     *     {@link FichierType }
     *     
     */
    public FichierType getObjetNumerique() {
        return objetNumerique;
    }

    /**
     * Définit la valeur de la propriété objetNumerique.
     * 
     * @param value
     *     allowed object is
     *     {@link FichierType }
     *     
     */
    public void setObjetNumerique(FichierType value) {
        this.objetNumerique = value;
    }

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
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroPageDebut() {
        return numeroPageDebut;
    }

    /**
     * Définit la valeur de la propriété numeroPageDebut.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroPageDebut(Integer value) {
        this.numeroPageDebut = value;
    }

    /**
     * Obtient la valeur de la propriété nombreDePages.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNombreDePages() {
        return nombreDePages;
    }

    /**
     * Définit la valeur de la propriété nombreDePages.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNombreDePages(Integer value) {
        this.nombreDePages = value;
    }

}
