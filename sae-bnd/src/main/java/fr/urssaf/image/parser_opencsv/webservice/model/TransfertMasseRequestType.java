
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètres d'entrées de l'opération
 *             'transfertMasse'
 * 
 * <p>Classe Java pour transfertMasseRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="transfertMasseRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="urlSommaire" type="{http://www.cirtil.fr/saeService}ecdeUrlSommaireType"/&gt;
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="typeHash" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transfertMasseRequestType", propOrder = {
    "urlSommaire",
    "hash",
    "typeHash"
})
public class TransfertMasseRequestType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String urlSommaire;
    @XmlElement(required = true)
    protected String hash;
    @XmlElement(required = true)
    protected String typeHash;

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

    /**
     * Obtient la valeur de la propriété hash.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Définit la valeur de la propriété hash.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Obtient la valeur de la propriété typeHash.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeHash() {
        return typeHash;
    }

    /**
     * Définit la valeur de la propriété typeHash.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeHash(String value) {
        this.typeHash = value;
    }

}
