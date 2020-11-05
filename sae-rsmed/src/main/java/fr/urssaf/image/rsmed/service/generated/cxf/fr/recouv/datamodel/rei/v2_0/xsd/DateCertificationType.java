
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Date soumise a certification
 * 
 * <p>Classe Java pour DateCertification_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DateCertification_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DonneeSoumiseACertification_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="valeur" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateCertification_Type", propOrder = {
    "valeur"
})
public class DateCertificationType
    extends DonneeSoumiseACertificationType
{

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar valeur;

    /**
     * Obtient la valeur de la propriété valeur.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValeur() {
        return valeur;
    }

    /**
     * Définit la valeur de la propriété valeur.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValeur(XMLGregorianCalendar value) {
        this.valeur = value;
    }

}
