/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Objet représentant la liste des fichiers référence insérés dans DFCE
 * 
 */
@Component
public class SaeListCaptureMasseReferenceFile extends
      ArrayList<CaptureMasseReferenceFile> implements
      List<CaptureMasseReferenceFile> {

   private static final long serialVersionUID = 1L;

}
