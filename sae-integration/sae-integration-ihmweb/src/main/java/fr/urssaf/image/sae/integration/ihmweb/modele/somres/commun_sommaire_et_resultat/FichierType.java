//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 09:24:43 AM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

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
 * &lt;complexType name="fichierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cheminEtNomDuFichier">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fichierType", propOrder = {
    "cheminEtNomDuFichier"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class FichierType {

    @XmlElement(required = true)
    protected String cheminEtNomDuFichier;

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

}
