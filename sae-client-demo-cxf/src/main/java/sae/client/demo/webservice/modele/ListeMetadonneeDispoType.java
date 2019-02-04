
package sae.client.demo.webservice.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de m�tadonn�es disponible pour le
 *             client
 * 
 * <p>Java class for listeMetadonneeDispoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeMetadonneeDispoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnee" type="{http://www.cirtil.fr/saeService}metadonneeDispoType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeMetadonneeDispoType", propOrder = {
    "metadonnee"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeMetadonneeDispoType {

    protected List<MetadonneeDispoType> metadonnee;

    /**
     * Gets the value of the metadonnee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadonnee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadonnee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadonneeDispoType }
     * 
     * 
     */
    public List<MetadonneeDispoType> getMetadonnee() {
        if (metadonnee == null) {
            metadonnee = new ArrayList<MetadonneeDispoType>();
        }
        return this.metadonnee;
    }

}
