package fr.urssaf.image.sae.integration.ihmweb.service.certificats;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.certificats.Certificat;
import fr.urssaf.image.sae.integration.ihmweb.modele.certificats.CertificatList;

@Service
public class CertificatService {

   private CertificatList certificats;
   
   public CertificatService() {
      
      StaxDriver staxDriver = new StaxDriver();
      XStream xstream = new XStream(staxDriver);

      xstream.processAnnotations(CertificatList.class);

      ClassPathResource ressourceXml = new ClassPathResource("certificats/certificats_applicatifs.xml");
      
      try {
         certificats = (CertificatList) xstream.fromXML(ressourceXml.getInputStream());
      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      }
      
   }

   public final CertificatList getCertificats() {
      return certificats;
   }
   
   public Certificat findCertificat(String id) {
      
      Certificat result = null;
      
      for(Certificat cert: certificats.getCertificats()) {
         if (cert.getId().equals(id)) {
            result = cert;
            break;
         }
      }
      
      return result;
      
   }
   
   
}
