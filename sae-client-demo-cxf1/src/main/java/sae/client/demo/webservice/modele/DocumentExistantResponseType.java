
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Retour de l'opération
 *             de copie.
 * 
 * <p>Java class for documentExistantResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentExistantResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isDocExist" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentExistantResponseType", propOrder = {
    "isDocExist"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class DocumentExistantResponseType {

    protected boolean isDocExist;

    /**
     * Gets the value of the isDocExist property.
     * 
     */
    public boolean isIsDocExist() {
        return isDocExist;
    }

    /**
     * Sets the value of the isDocExist property.
     * 
     */
    public void setIsDocExist(boolean value) {
        this.isDocExist = value;
    }

}
