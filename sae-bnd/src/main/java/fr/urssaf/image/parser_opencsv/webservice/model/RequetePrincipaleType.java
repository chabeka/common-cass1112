
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Requête principale de la recherche par
 *             iterateur.
 * 
 * <p>Classe Java pour requetePrincipaleType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="requetePrincipaleType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fixedMetadatas" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/&gt;
 *         &lt;element name="varyingMetadata" type="{http://www.cirtil.fr/saeService}rangeMetadonneeType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requetePrincipaleType", propOrder = {
    "fixedMetadatas",
    "varyingMetadata"
})
public class RequetePrincipaleType {

    @XmlElement(required = true)
    protected ListeMetadonneeType fixedMetadatas;
    @XmlElement(required = true)
    protected RangeMetadonneeType varyingMetadata;

    /**
     * Obtient la valeur de la propriété fixedMetadatas.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getFixedMetadatas() {
        return fixedMetadatas;
    }

    /**
     * Définit la valeur de la propriété fixedMetadatas.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setFixedMetadatas(ListeMetadonneeType value) {
        this.fixedMetadatas = value;
    }

    /**
     * Obtient la valeur de la propriété varyingMetadata.
     * 
     * @return
     *     possible object is
     *     {@link RangeMetadonneeType }
     *     
     */
    public RangeMetadonneeType getVaryingMetadata() {
        return varyingMetadata;
    }

    /**
     * Définit la valeur de la propriété varyingMetadata.
     * 
     * @param value
     *     allowed object is
     *     {@link RangeMetadonneeType }
     *     
     */
    public void setVaryingMetadata(RangeMetadonneeType value) {
        this.varyingMetadata = value;
    }

}
