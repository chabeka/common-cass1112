
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Transaction metier legacy
 * 
 * <p>Classe Java pour EvenementTransactionnel_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EvenementTransactionnel_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nomTransaction" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="numeroEvenement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EvenementTransactionnel_Type", propOrder = {
    "nomTransaction",
    "numeroEvenement"
})
public class EvenementTransactionnelType {

    @XmlElement(required = true)
    protected String nomTransaction;
    @XmlElement(required = true)
    protected String numeroEvenement;

    /**
     * Obtient la valeur de la propriété nomTransaction.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomTransaction() {
        return nomTransaction;
    }

    /**
     * Définit la valeur de la propriété nomTransaction.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomTransaction(String value) {
        this.nomTransaction = value;
    }

    /**
     * Obtient la valeur de la propriété numeroEvenement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroEvenement() {
        return numeroEvenement;
    }

    /**
     * Définit la valeur de la propriété numeroEvenement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroEvenement(String value) {
        this.numeroEvenement = value;
    }

}
