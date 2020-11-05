
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour OptionsRechercheEtablissement_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="OptionsRechercheEtablissement_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="activeUniquement" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptionsRechercheEtablissement_Type", propOrder = {
    "activeUniquement"
})
public class OptionsRechercheEtablissementType {

    protected boolean activeUniquement;

    /**
     * Obtient la valeur de la propriété activeUniquement.
     * 
     */
    public boolean isActiveUniquement() {
        return activeUniquement;
    }

    /**
     * Définit la valeur de la propriété activeUniquement.
     * 
     */
    public void setActiveUniquement(boolean value) {
        this.activeUniquement = value;
    }

}
