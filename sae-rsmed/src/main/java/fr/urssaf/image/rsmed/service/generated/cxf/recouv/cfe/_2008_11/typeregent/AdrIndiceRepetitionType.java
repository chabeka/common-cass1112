
package fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Adr_IndiceRepetition_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Adr_IndiceRepetition_Type"&gt;
 *   &lt;restriction base="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"&gt;
 *     &lt;length value="1"/&gt;
 *     &lt;enumeration value="B"/&gt;
 *     &lt;enumeration value="T"/&gt;
 *     &lt;enumeration value="Q"/&gt;
 *     &lt;enumeration value="C"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Adr_IndiceRepetition_Type", namespace = "http://cfe.recouv/2008-11/TypeRegent")
@XmlEnum
public enum AdrIndiceRepetitionType {

    B,
    T,
    Q,
    C;

    public String value() {
        return name();
    }

    public static AdrIndiceRepetitionType fromValue(String v) {
        return valueOf(v);
    }

}
