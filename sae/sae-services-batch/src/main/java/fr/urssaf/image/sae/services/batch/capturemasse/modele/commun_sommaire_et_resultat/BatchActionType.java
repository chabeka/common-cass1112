//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.27 at 10:07:17 AM CEST 
//


package fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for batchActionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="batchActionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TRANSFERT"/>
 *     &lt;enumeration value="SUPPRESSION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "batchActionType")
@XmlEnum
public enum BatchActionType {

    TRANSFERT,
    SUPPRESSION;

    public String value() {
        return name();
    }

    public static BatchActionType fromValue(String v) {
        return valueOf(v);
    }

}