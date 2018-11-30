package fr.urssaf.image.sae.integration.ihmweb.modele.certificats;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Certificat {

   @XStreamAlias("id")
   private String id;
   
   @XStreamAlias("chemin")
   private String chemin;
   
   @XStreamAlias("password")
   private String password;
   
   public final String getId() {
      return id;
   }
   public final void setId(String id) {
      this.id = id;
   }
   public final String getChemin() {
      return chemin;
   }
   public final void setChemin(String chemin) {
      this.chemin = chemin;
   }
   public final String getPassword() {
      return password;
   }
   public final void setPassword(String password) {
      this.password = password;
   }
   
}
