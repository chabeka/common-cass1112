/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;

/**
 * Objet représentant la liste des fichiers référence contrôlés
 * 
 */
@Component
public class SaeListVirtualReferenceFile extends
      ArrayList<VirtualReferenceFile> implements List<VirtualReferenceFile> {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    */
   public SaeListVirtualReferenceFile() {
      // nothing to do
   }
}
