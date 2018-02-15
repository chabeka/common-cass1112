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
 * Nom donné à une
 *             unité ou une structure rattachée à un unique intervenant sur
 *             le plan de l'organisation du travail, et exerçant un
 *             ensemble de tâches spécifiques.
 *             La notion de service interne apparait dès lors qu'un intervenant
 *             définit explicitement son mode d'organisation ainsi que la
 *             répartition des moyens humains et matériels en plusierus
 *             unités distinctes.
 * 
 *             Cette information permet de préciser davantage l'identité des
 *             intervenants mis en jeu.
 * 
 * <p>Java class for NomService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NomService">
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
@XmlType(name = "NomService")
public class NomService
    extends TextType
{


}
