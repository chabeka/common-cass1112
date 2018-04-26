package fr.urssaf.image.commons.xml.exemple.seda;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import un.unece.uncefact.data.standard.unqualifieddatatype._6.TextType;
import fr.gouv.ae.archive.draft.standard_echange_v0.ArchiveObjectType;
import fr.gouv.ae.archive.draft.standard_echange_v0.DocumentType;
import fr.gouv.ae.archive.draft.standard_echange_v0.ObjectFactory;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.ArchivesBinaryObjectType;

@SuppressWarnings("PMD")
public class ArchiveObjectTypeTest {

   private static final String REPERTORY;

   static {
      REPERTORY = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
   }

   @Test
   public void marshall() throws JAXBException {

      ObjectFactory factory = new ObjectFactory();

      ArchiveObjectType archive = factory.createArchiveObjectType();

      TextType textType = new TextType();
      textType.setLanguageID("FR");
      textType.setValue("Mon archive n°1");

      archive.setName(textType);

      DocumentType documentType = new DocumentType();
      documentType.setAttachment(new ArchivesBinaryObjectType());
      documentType.getAttachment().setFilename("myDoc.tif");
      documentType.getAttachment().setUri("uri");

      archive.getDocument().add(documentType);

      JAXBContext context = JAXBContext.newInstance(ArchiveObjectType.class);

      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      m.marshal(factory.createArchiveObject(archive), System.out);

      File output = new File(FilenameUtils.concat(REPERTORY, "seda.xml"));
      m.marshal(factory.createArchiveObject(archive), output);

   }

   @SuppressWarnings("unchecked")
   @Test
   public void unmarshall() throws JAXBException {

      JAXBContext context = JAXBContext.newInstance(ArchiveObjectType.class);
      Unmarshaller u = context.createUnmarshaller();

      File input = new File("src/test/resources/seda.xml");

      JAXBElement<ArchiveObjectType> doc = (JAXBElement<ArchiveObjectType>) u
            .unmarshal(input);
      ArchiveObjectType archive = doc.getValue();

      assertEquals("Mon archive 1", archive.getName().getValue());
      assertEquals("FR", archive.getName().getLanguageID());

      DocumentType documentType = archive.getDocument().get(0);

      assertEquals("myDoc.tif", documentType.getAttachment().getFilename());
      assertEquals("uri", documentType.getAttachment().getUri());

   }

}
