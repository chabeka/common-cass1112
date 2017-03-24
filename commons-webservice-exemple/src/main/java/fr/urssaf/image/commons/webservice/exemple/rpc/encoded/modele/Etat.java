
package fr.urssaf.image.commons.webservice.exemple.rpc.encoded.modele;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for etat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="etat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="init"/>
 *     &lt;enumeration value="close"/>
 *     &lt;enumeration value="open"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "etat")
@XmlEnum
public enum Etat {

    @XmlEnumValue("init")
    INIT("init"),
    @XmlEnumValue("close")
    CLOSE("close"),
    @XmlEnumValue("open")
    OPEN("open");
    private final String value;

    Etat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Etat fromValue(String v) {
        for (Etat c: Etat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
