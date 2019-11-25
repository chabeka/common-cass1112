//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.06 à 10:57:31 AM CEST 
//


package sae.integration.xml.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Définition d'un fichier
 * 
 * <p>Classe Java pour fichierType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="fichierType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="cheminEtNomDuFichier"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;minLength value="1"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="UUID"&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;minLength value="1"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/element&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fichierType", propOrder = {
    "cheminEtNomDuFichier",
    "uuid"
})
public class FichierType {

    protected String cheminEtNomDuFichier;
    @XmlElement(name = "UUID")
    protected String uuid;

    /**
     * Obtient la valeur de la propriété cheminEtNomDuFichier.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminEtNomDuFichier() {
        return cheminEtNomDuFichier;
    }

    /**
     * Définit la valeur de la propriété cheminEtNomDuFichier.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminEtNomDuFichier(String value) {
        this.cheminEtNomDuFichier = value;
    }

    /**
     * Obtient la valeur de la propriété uuid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Définit la valeur de la propriété uuid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUUID(String value) {
        this.uuid = value;
    }

}
