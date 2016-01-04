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
 * Pour chaque
 *             intervenant, il est précisé la ville où il est localisé. Le
 *             nom de la ville qui est sur 35 caractères conformément à la
 *             norme AFNOR Z 10-011 d'août 1989 (spécifications postales
 *             des objets de correspondance de petits formats) ainsi qu'à
 *             la nouvelle version de cette norme actuellement en cours de
 *             validation, reprendra, dans la mesure du possible, le nom
 *             attribué par l'INSEE sur 45 caractères.
 * 
 *             Cet attribut est inutilisé en dehors de la liste SANDRE.
 * 
 *             Cette information est fournie par le ou les organismes qui font la
 *             demande, auprès du SANDRE, d'un numéro national pour un
 *             intervenant. La liste des intervenants est administrée par
 *             le SANDRE.
 * 
 * <p>Java class for VilleIntervenant complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VilleIntervenant">
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
@XmlType(name = "VilleIntervenant")
public class VilleIntervenant
    extends TextType
{


}