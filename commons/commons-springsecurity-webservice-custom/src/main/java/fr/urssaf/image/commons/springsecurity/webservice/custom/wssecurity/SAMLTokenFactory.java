package fr.urssaf.image.commons.springsecurity.webservice.custom.wssecurity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLException;
import org.opensaml.SAMLNameIdentifier;
import org.opensaml.SAMLSubject;
import org.w3c.dom.Element;

public final class SAMLTokenFactory {

   private SAMLTokenFactory() {

   }

   private static final String PAGM_NAMESPACE = "urn:iops:attributs:pagm";

   private static final String PAGM_NAME = "PAGM";

   public static Element createPAGM(String... roles) {

      try {
         SAMLSubject subject = new SAMLSubject();
         SAMLNameIdentifier identifier = new SAMLNameIdentifier();
         subject.setNameIdentifier(identifier);
         identifier.setName("id-avé-accent-source");
         identifier.setFormat(SAMLNameIdentifier.FORMAT_UNSPECIFIED);

         SAMLAttribute attribute = new SAMLAttribute();
         attribute.setName(PAGM_NAME);
         attribute.setNamespace(PAGM_NAMESPACE);

         for (String role : roles) {
            attribute.addValue(role);
         }

         Set<SAMLAttribute> attributes = new HashSet<SAMLAttribute>();
         attributes.add(attribute);

         SAMLAttributeStatement statement = new SAMLAttributeStatement();
         statement.setSubject(subject);
         statement.setAttributes(attributes);

         return (Element) statement.toDOM();

      } catch (SAMLException e) {
         throw new IllegalStateException(e);
      }

   }

   public static Element createSAMLToken(String samlFile) {

      try {

         FileInputStream inputStream = new FileInputStream(samlFile);

         SAMLAssertion samlAssertion = new SAMLAssertion(inputStream);

         return (Element) samlAssertion.toDOM();

      } catch (SAMLException e) {
         throw new IllegalStateException(e);
      } catch (FileNotFoundException e) {
         throw new IllegalArgumentException(e);
      }

   }
}
