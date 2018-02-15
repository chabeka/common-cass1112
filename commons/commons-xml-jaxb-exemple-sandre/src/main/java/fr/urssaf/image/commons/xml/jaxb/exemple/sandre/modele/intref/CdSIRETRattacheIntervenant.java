//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.22 at 10:20:20 AM CET 
//


package fr.urssaf.image.commons.xml.jaxb.exemple.sandre.modele.intref;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.commons.xml.jaxb.exemple.sandre.modele.composantstypes.TextType;


/**
 * Le code SIRET de
 *             l’organisme auquel est rattaché l’intervenant est un
 *             attribut optionnel permettant de préciser, lorsque
 *             l’intervenant n’est pas une structure identifiée dans le
 *             registre national de l’INSEE, le code SIRET de l’organisme
 *             auquel il est généralement rattaché.
 * 
 *             Par exemple, les SATESE (Service d’Assistance Technique aux Exploitants
 *             des Stations d’Epuration) sont générament rattachés au
 *             Conseil Général du département.
 * 
 *             Cette information relève de la responsabilité de l’auteur de la fiche
 *             SANDRE 
 * 
 * <p>Java class for CdSIRETRattacheIntervenant complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CdSIRETRattacheIntervenant">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://xml.sandre.eaufrance.fr/Composants/1>TextType">
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CdSIRETRattacheIntervenant")
public class CdSIRETRattacheIntervenant
    extends TextType
{


}
