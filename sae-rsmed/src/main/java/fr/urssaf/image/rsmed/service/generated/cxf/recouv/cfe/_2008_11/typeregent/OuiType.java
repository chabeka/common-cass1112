
package fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Oui_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Oui_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;length value="1"/&gt;
 *     &lt;enumeration value="O"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Oui_Type", namespace = "http://cfe.recouv/2008-11/TypeRegent")
@XmlEnum
public enum OuiType {

    O;

    public String value() {
        return name();
    }

    public static OuiType fromValue(String v) {
        return valueOf(v);
    }

}
