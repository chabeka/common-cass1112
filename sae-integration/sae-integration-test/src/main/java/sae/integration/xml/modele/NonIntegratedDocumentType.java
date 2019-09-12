//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.04 à 10:35:02 AM CEST 
//


package sae.integration.xml.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Définition d'un document non archivé
 * 
 * <p>Classe Java pour nonIntegratedDocumentType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="nonIntegratedDocumentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType" minOccurs="0"/&gt;
 *         &lt;element name="erreurs" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeErreurType"/&gt;
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nonIntegratedDocumentType", propOrder = {
    "objetNumerique",
    "metadonnees",
    "erreurs",
    "numeroPageDebut",
    "nombreDePages"
})
public class NonIntegratedDocumentType {

    @XmlElement(required = true)
    protected FichierType objetNumerique;
    protected ListeMetadonneeType metadonnees;
    @XmlElement(required = true)
    protected ListeErreurType erreurs;
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
