
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Valeur de retour de l'opération
 * 'suppessionArchivageMasse'. Le type est un UUID, il sert à
 * identifier
 * la tâche dans la pile des travaux
 * <p>
 * Java class for suppressionArchivageMasseResponseType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="suppressionArchivageMasseResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uuid" type="{http://www.cirtil.fr/saeService}uuidType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "suppressionArchivageMasseResponseType", propOrder = {
                                                                       "uuid"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SuppressionArchivageMasseResponseType {

   @XmlElement(required = true)
   protected String uuid;

   /**
    * Gets the value of the uuid property.
    *
    * @return
    *         possible object is
    *         {@link String }
    */
   public String getUuid() {
      return uuid;
   }

   /**
    * Sets the value of the uuid property.
    *
    * @param value
    *           allowed object is
    *           {@link String }
    */
   public void setUuid(final String value) {
      this.uuid = value;
   }

}
