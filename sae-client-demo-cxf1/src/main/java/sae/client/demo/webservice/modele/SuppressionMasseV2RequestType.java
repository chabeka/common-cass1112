
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Paramètre d'entrée de l'opération
 * 'suppressionMasseV2'
 * <p>
 * Java class for suppressionMasseV2RequestType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="suppressionMasseV2RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requete" type="{http://www.cirtil.fr/saeService}requeteRechercheType"/>
 *         &lt;element name="codeOrgaProprietaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "suppressionMasseV2RequestType", propOrder = {
                                                               "requete",
                                                               "codeOrgaProprietaire"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SuppressionMasseV2RequestType {

   @XmlElement(required = true)
   protected String requete;

   protected String codeOrgaProprietaire;

   /**
    * Gets the value of the requete property.
    *
    * @return
    *         possible object is
    *         {@link String }
    */
   public String getRequete() {
      return requete;
   }

   /**
    * Sets the value of the requete property.
    *
    * @param value
    *           allowed object is
    *           {@link String }
    */
   public void setRequete(final String value) {
      this.requete = value;
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
