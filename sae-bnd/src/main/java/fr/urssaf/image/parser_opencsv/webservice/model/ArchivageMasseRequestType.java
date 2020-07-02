
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération
 *             'archivageMasse'
 * 
 * <p>Classe Java pour archivageMasseRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="archivageMasseRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="urlSommaire" type="{http://www.cirtil.fr/saeService}ecdeUrlSommaireType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "archivageMasseRequestType", propOrder = {
    "urlSommaire"
})
public class ArchivageMasseRequestType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String urlSommaire;

    /**
     * Obtient la valeur de la propriété urlSommaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlSommaire() {
        return urlSommaire;
    }

    /**
     * Définit la valeur de la propriété urlSommaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlSommaire(String value) {
        this.urlSommaire = value;
    }

}
