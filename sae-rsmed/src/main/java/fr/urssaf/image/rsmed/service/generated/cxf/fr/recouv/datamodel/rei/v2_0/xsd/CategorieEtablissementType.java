
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Categorie de l'etablissement : code et libelle
 * 
 * <p>Classe Java pour CategorieEtablissement_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CategorieEtablissement_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeCategorie" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeCategorie_Type"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategorieEtablissement_Type", propOrder = {
    "codeCategorie",
    "libelle"
})
public class CategorieEtablissementType {

    protected int codeCategorie;
    protected String libelle;

    /**
     * Obtient la valeur de la propriété codeCategorie.
     * 
     */
    public int getCodeCategorie() {
        return codeCategorie;
    }

    /**
     * Définit la valeur de la propriété codeCategorie.
     * 
     */
    public void setCodeCategorie(int value) {
        this.codeCategorie = value;
    }

    /**
     * Obtient la valeur de la propriété libelle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit la valeur de la propriété libelle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelle(String value) {
        this.libelle = value;
    }

}
