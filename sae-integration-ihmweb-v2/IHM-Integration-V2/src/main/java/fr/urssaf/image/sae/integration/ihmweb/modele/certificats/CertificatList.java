package fr.urssaf.image.sae.integration.ihmweb.modele.certificats;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("certificats")
public class CertificatList {

   @XStreamImplicit(itemFieldName = "certificat")
   private List<Certificat> certificats;

   public final List<Certificat> getCertificats() {
      return certificats;
   }

   public final void setCertificats(List<Certificat> certificats) {
      this.certificats = certificats;
   }
   
}
