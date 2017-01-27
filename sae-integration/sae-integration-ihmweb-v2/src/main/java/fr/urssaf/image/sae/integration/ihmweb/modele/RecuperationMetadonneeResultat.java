package fr.urssaf.image.sae.integration.ihmweb.modele;

import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeDispoType;

public class RecuperationMetadonneeResultat {
   private ListeMetadonneeDispoType metadonnees;
   
   public RecuperationMetadonneeResultat(){    
   }
   
   public RecuperationMetadonneeResultat(ListeMetadonneeDispoType metadonnees){  
      this.metadonnees = metadonnees;
   }

   public ListeMetadonneeDispoType getMetadonnees() {
      return metadonnees;
   }

   public void setMetadonnees(ListeMetadonneeDispoType metadonnees) {
      this.metadonnees = metadonnees;
   }
   
   
}
