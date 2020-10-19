
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Retour de l'opération
 *             de copie.
 * 
 * <p>Classe Java pour documentExistantResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="documentExistantResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isDocExist" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentExistantResponseType", propOrder = {
    "isDocExist"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class DocumentExistantResponseType {

    protected boolean isDocExist;

    /**
     * Obtient la valeur de la propriété isDocExist.
     * 
     */
    public boolean isIsDocExist() {
        return isDocExist;
    }

    /**
     * Définit la valeur de la propriété isDocExist.
     * 
     */
    public void setIsDocExist(final boolean value) {
        isDocExist = value;
    }

}
