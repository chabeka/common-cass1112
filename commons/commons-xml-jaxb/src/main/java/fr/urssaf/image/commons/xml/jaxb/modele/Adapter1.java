//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.22 at 09:50:10 AM CET 
//


package fr.urssaf.image.commons.xml.jaxb.modele;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (fr.urssaf.image.commons.xml.jaxb.util.DateConverter.parseDate(value));
    }

    public String marshal(Date value) {
        return (fr.urssaf.image.commons.xml.jaxb.util.DateConverter.printDate(value));
    }

}
