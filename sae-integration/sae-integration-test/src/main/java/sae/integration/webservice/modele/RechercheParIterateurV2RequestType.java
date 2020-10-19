
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour rechercheParIterateurV2RequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="rechercheParIterateurV2RequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requetePrincipale" type="{http://www.cirtil.fr/saeService}requetePrincipaleType"/&gt;
 *         &lt;element name="filtres" type="{http://www.cirtil.fr/saeService}filtreType" minOccurs="0"/&gt;
 *         &lt;element name="nbDocumentsParPage" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="identifiantPage" type="{http://www.cirtil.fr/saeService}identifiantPageType" minOccurs="0"/&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeCodeType"/&gt;
 *         &lt;element name="delai" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="codeOrgaProprietaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheParIterateurV2RequestType", propOrder = {
    "requetePrincipale",
    "filtres",
    "nbDocumentsParPage",
    "identifiantPage",
    "metadonnees",
    "delai",
    "codeOrgaProprietaire"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RechercheParIterateurV2RequestType {

    @XmlElement(required = true)
    protected RequetePrincipaleType requetePrincipale;
    protected FiltreType filtres;
    protected int nbDocumentsParPage;
    protected IdentifiantPageType identifiantPage;
    @XmlElement(required = true)
    protected ListeMetadonneeCodeType metadonnees;
    @XmlElement(defaultValue = "-1")
    protected Integer delai;
    protected String codeOrgaProprietaire;

    /**
     * Obtient la valeur de la propriété requetePrincipale.
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
     * Définit la valeur de la propriété requetePrincipale.
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
     * Obtient la valeur de la propriété filtres.
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
     * Définit la valeur de la propriété filtres.
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
     * Obtient la valeur de la propriété nbDocumentsParPage.
     * 
     */
    public int getNbDocumentsParPage() {
        return nbDocumentsParPage;
    }

    /**
     * Définit la valeur de la propriété nbDocumentsParPage.
     * 
     */
    public void setNbDocumentsParPage(int value) {
        this.nbDocumentsParPage = value;
    }

    /**
     * Obtient la valeur de la propriété identifiantPage.
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
     * Définit la valeur de la propriété identifiantPage.
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
     * Obtient la valeur de la propriété metadonnees.
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
     * Définit la valeur de la propriété metadonnees.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeCodeType value) {
        this.metadonnees = value;
    }

    /**
     * Obtient la valeur de la propriété delai.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDelai() {
        return delai;
    }

    /**
     * Définit la valeur de la propriété delai.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDelai(Integer value) {
        this.delai = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrgaProprietaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOrgaProprietaire() {
        return codeOrgaProprietaire;
    }

    /**
     * Définit la valeur de la propriété codeOrgaProprietaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOrgaProprietaire(String value) {
        this.codeOrgaProprietaire = value;
    }

}
