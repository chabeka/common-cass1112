
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for rechercheParIterateurV2RequestType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="rechercheParIterateurV2RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requetePrincipale" type="{http://www.cirtil.fr/saeService}requetePrincipaleType"/>
 *         &lt;element name="filtres" type="{http://www.cirtil.fr/saeService}filtreType" minOccurs="0"/>
 *         &lt;element name="nbDocumentsParPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="identifiantPage" type="{http://www.cirtil.fr/saeService}identifiantPageType" minOccurs="0"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeCodeType"/>
 *         &lt;element name="delai" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codeOrgaProprietaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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
    * Gets the value of the requetePrincipale property.
    *
    * @return
    *         possible object is
    *         {@link RequetePrincipaleType }
    */
   public RequetePrincipaleType getRequetePrincipale() {
      return requetePrincipale;
   }

   /**
    * Sets the value of the requetePrincipale property.
    *
    * @param value
    *           allowed object is
    *           {@link RequetePrincipaleType }
    */
   public void setRequetePrincipale(final RequetePrincipaleType value) {
      this.requetePrincipale = value;
   }

   /**
    * Gets the value of the filtres property.
    *
    * @return
    *         possible object is
    *         {@link FiltreType }
    */
   public FiltreType getFiltres() {
      return filtres;
   }

   /**
    * Sets the value of the filtres property.
    *
    * @param value
    *           allowed object is
    *           {@link FiltreType }
    */
   public void setFiltres(final FiltreType value) {
      this.filtres = value;
   }

   /**
    * Gets the value of the nbDocumentsParPage property.
    */
   public int getNbDocumentsParPage() {
      return nbDocumentsParPage;
   }

   /**
    * Sets the value of the nbDocumentsParPage property.
    */
   public void setNbDocumentsParPage(final int value) {
      this.nbDocumentsParPage = value;
   }

   /**
    * Gets the value of the identifiantPage property.
    *
    * @return
    *         possible object is
    *         {@link IdentifiantPageType }
    */
   public IdentifiantPageType getIdentifiantPage() {
      return identifiantPage;
   }

   /**
    * Sets the value of the identifiantPage property.
    *
    * @param value
    *           allowed object is
    *           {@link IdentifiantPageType }
    */
   public void setIdentifiantPage(final IdentifiantPageType value) {
      this.identifiantPage = value;
   }

   /**
    * Gets the value of the metadonnees property.
    *
    * @return
    *         possible object is
    *         {@link ListeMetadonneeCodeType }
    */
   public ListeMetadonneeCodeType getMetadonnees() {
      return metadonnees;
   }

   /**
    * Sets the value of the metadonnees property.
    *
    * @param value
    *           allowed object is
    *           {@link ListeMetadonneeCodeType }
    */
   public void setMetadonnees(final ListeMetadonneeCodeType value) {
      this.metadonnees = value;
   }

   /**
    * Gets the value of the delai property.
    *
    * @return
    *         possible object is
    *         {@link Integer }
    */
   public Integer getDelai() {
      return delai;
   }

   /**
    * Sets the value of the delai property.
    *
    * @param value
    *           allowed object is
    *           {@link Integer }
    */
   public void setDelai(final Integer value) {
      this.delai = value;
   }

   /**
    * Gets the value of the codeOrgaProprietaire property.
    *
    * @return
    *         possible object is
    *         {@link String }
    */
   public String getCodeOrgaProprietaire() {
      return codeOrgaProprietaire;
   }

   /**
    * Sets the value of the codeOrgaProprietaire property.
    *
    * @param value
    *           allowed object is
    *           {@link String }
    */
   public void setCodeOrgaProprietaire(final String value) {
      this.codeOrgaProprietaire = value;
   }

}
