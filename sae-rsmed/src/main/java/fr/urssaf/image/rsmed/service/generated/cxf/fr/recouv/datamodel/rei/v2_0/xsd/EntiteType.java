
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Entite_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Entite_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Individu"/&gt;
 *     &lt;enumeration value="Etablissement"/&gt;
 *     &lt;enumeration value="Entreprise"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Entite_Type")
@XmlEnum
public enum EntiteType {

    @XmlEnumValue("Individu")
    INDIVIDU("Individu"),
    @XmlEnumValue("Etablissement")
    ETABLISSEMENT("Etablissement"),
    @XmlEnumValue("Entreprise")
    ENTREPRISE("Entreprise");
    private final String value;

    EntiteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EntiteType fromValue(String v) {
        for (EntiteType c: EntiteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
