
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour CodeTypeEntreprise_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="CodeTypeEntreprise_Type"&gt;
 *   &lt;restriction base="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"&gt;
 *     &lt;length value="1"/&gt;
 *     &lt;enumeration value="M"/&gt;
 *     &lt;enumeration value="P"/&gt;
 *     &lt;enumeration value="F"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CodeTypeEntreprise_Type")
@XmlEnum
public enum CodeTypeEntrepriseType {


    /**
     * Morale
     * 
     */
    M,

    /**
     * Physique
     * 
     */
    P,

    /**
     * Exploitation En Commun
     * 
     */
    F;

    public String value() {
        return name();
    }

    public static CodeTypeEntrepriseType fromValue(String v) {
        return valueOf(v);
    }

}
