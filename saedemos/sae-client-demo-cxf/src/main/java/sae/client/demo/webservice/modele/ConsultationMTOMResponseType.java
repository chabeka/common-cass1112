
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour consultationMTOMResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="consultationMTOMResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contenu" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultationMTOMResponseType", propOrder = {
    "contenu",
    "metadonnees"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ConsultationMTOMResponseType {

    @XmlElement(required = true)
    protected byte[] contenu;
    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;

    /**
     * Obtient la valeur de la propriété contenu.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContenu() {
        return contenu;
    }

    /**
     * Définit la valeur de la propriété contenu.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContenu(byte[] value) {
        this.contenu = value;
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

}
