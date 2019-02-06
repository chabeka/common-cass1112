
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rechercheParIterateurRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rechercheParIterateurRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requetePrincipale" type="{http://www.cirtil.fr/saeService}requetePrincipaleType"/>
 *         &lt;element name="filtres" type="{http://www.cirtil.fr/saeService}filtreType" minOccurs="0"/>
 *         &lt;element name="nbDocumentsParPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="identifiantPage" type="{http://www.cirtil.fr/saeService}identifiantPageType" minOccurs="0"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeCodeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheParIterateurRequestType", propOrder = {
    "requetePrincipale",
    "filtres",
    "nbDocumentsParPage",
    "identifiantPage",
    "metadonnees"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RechercheParIterateurRequestType {

    @XmlElement(required = true)
    protected RequetePrincipaleType requetePrincipale;
    protected FiltreType filtres;
    protected int nbDocumentsParPage;
    protected IdentifiantPageType identifiantPage;
    @XmlElement(required = true)
    protected ListeMetadonneeCodeType metadonnees;

    /**
     * Gets the value of the requetePrincipale property.
     * 
     * @return
     *     possible object is
     *     {@link RequetePrincipaleType }
     *     
     */
    public RequetePrincipaleType getRequetePrincipale() {
        return requetePrincipale;
    }

    /**
     * Sets the value of the requetePrincipale property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequetePrincipaleType }
     *     
     */
    public void setRequetePrincipale(RequetePrincipaleType value) {
        this.requetePrincipale = value;
    }

    /**
     * Gets the value of the filtres property.
     * 
     * @return
     *     possible object is
     *     {@link FiltreType }
     *     
     */
    public FiltreType getFiltres() {
        return filtres;
    }

    /**
     * Sets the value of the filtres property.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltreType }
     *     
     */
    public void setFiltres(FiltreType value) {
        this.filtres = value;
    }

    /**
     * Gets the value of the nbDocumentsParPage property.
     * 
     */
    public int getNbDocumentsParPage() {
        return nbDocumentsParPage;
    }

    /**
     * Sets the value of the nbDocumentsParPage property.
     * 
     */
    public void setNbDocumentsParPage(int value) {
        this.nbDocumentsParPage = value;
    }

    /**
     * Gets the value of the identifiantPage property.
     * 
     * @return
     *     possible object is
     *     {@link IdentifiantPageType }
     *     
     */
    public IdentifiantPageType getIdentifiantPage() {
        return identifiantPage;
    }

    /**
     * Sets the value of the identifiantPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentifiantPageType }
     *     
     */
    public void setIdentifiantPage(IdentifiantPageType value) {
        this.identifiantPage = value;
    }

    /**
     * Gets the value of the metadonnees property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public ListeMetadonneeCodeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Sets the value of the metadonnees property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeCodeType value) {
        this.metadonnees = value;
    }

}
