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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Définition d'un document virtuel non archivé
 * 
 * <p>Classe Java pour nonIntegratedVirtualDocumentType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="nonIntegratedVirtualDocumentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/&gt;
 *         &lt;element name="erreurs" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeErreurType" minOccurs="0"/&gt;
 *         &lt;element name="composants"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="composant" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedComposantDocumentVirtuelType" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nonIntegratedVirtualDocumentType", propOrder = {
    "objetNumerique",
    "erreurs",
    "composants"
})
public class NonIntegratedVirtualDocumentType {

    @XmlElement(required = true)
    protected FichierType objetNumerique;
    protected ListeErreurType erreurs;
    @XmlElement(required = true)
    protected NonIntegratedVirtualDocumentType.Composants composants;

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
     * Obtient la valeur de la propriété composants.
     * 
     * @return
     *     possible object is
     *     {@link NonIntegratedVirtualDocumentType.Composants }
     *     
     */
    public NonIntegratedVirtualDocumentType.Composants getComposants() {
        return composants;
    }

    /**
     * Définit la valeur de la propriété composants.
     * 
     * @param value
     *     allowed object is
     *     {@link NonIntegratedVirtualDocumentType.Composants }
     *     
     */
    public void setComposants(NonIntegratedVirtualDocumentType.Composants value) {
        this.composants = value;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="composant" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}nonIntegratedComposantDocumentVirtuelType" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "composant"
    })
    public static class Composants {

        @XmlElement(required = true)
        protected List<NonIntegratedComposantDocumentVirtuelType> composant;

        /**
         * Gets the value of the composant property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the composant property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getComposant().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NonIntegratedComposantDocumentVirtuelType }
         * 
         * 
         */
        public List<NonIntegratedComposantDocumentVirtuelType> getComposant() {
            if (composant == null) {
                composant = new ArrayList<NonIntegratedComposantDocumentVirtuelType>();
            }
            return this.composant;
        }

    }

}
