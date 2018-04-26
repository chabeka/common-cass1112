package fr.urssaf.image.commons.xml.hibernate.dao;

import java.io.IOException;


/**
 * 
 * @author Bertrand BARAULT
 * 
 */

public interface DocumentXML {

   void writeAllDocument(String file) throws IOException;
   
   void writeDocument(String file,int maxResult) throws IOException;
   
}
