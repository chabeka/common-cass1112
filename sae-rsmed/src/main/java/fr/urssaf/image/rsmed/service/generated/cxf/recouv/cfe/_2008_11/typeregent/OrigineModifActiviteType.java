
package fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour OrigineModifActivite_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="OrigineModifActivite_Type"&gt;
 *   &lt;restriction base="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"&gt;
 *     &lt;enumeration value="A"/&gt;
 *     &lt;enumeration value="D"/&gt;
 *     &lt;enumeration value="V"/&gt;
 *     &lt;enumeration value="R"/&gt;
 *     &lt;enumeration value="L"/&gt;
 *     &lt;enumeration value="C"/&gt;
 *     &lt;enumeration value="M"/&gt;
 *     &lt;enumeration value="E"/&gt;
 *     &lt;enumeration value="W"/&gt;
 *     &lt;enumeration value="T"/&gt;
 *     &lt;enumeration value="X"/&gt;
 *     &lt;enumeration value="U"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OrigineModifActivite_Type", namespace = "http://cfe.recouv/2008-11/TypeRegent")
@XmlEnum
public enum OrigineModifActiviteType {

    A,
    D,
    V,
    R,
    L,
    C,
    M,
    E,
    W,
    T,
    X,
    U;

    public String value() {
        return name();
    }

    public static OrigineModifActiviteType fromValue(String v) {
        return valueOf(v);
    }

}
