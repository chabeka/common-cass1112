//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.02 at 04:48:49 PM CET 
//


package fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for batchModeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="batchModeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TOUT_OU_RIEN"/>
 *     &lt;enumeration value="PARTIEL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "batchModeType")
@XmlEnum
public enum BatchModeType {

    TOUT_OU_RIEN,
    PARTIEL;

    public String value() {
        return name();
    }

    public static BatchModeType fromValue(String v) {
        return valueOf(v);
    }

}
