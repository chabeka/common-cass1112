package fr.urssaf.image.commons.pdfbox.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service de validation d'un format de fichier
 */
public interface FormatValidationService {

   /**
    * Valide le format d'un fichier. Il faut penser à catcher les
    * RuntimeException de cette méthode, car la validation peut planter par
    * exemple si des données incorrectes et/ou non prévues sont présentes dans
    * le fichier. Par exemple, la méthode peut lever des NumberFormatException
    * 
    * @param file
    *           Le fichier à valider
    * @return La liste des erreurs de validation. La liste est vide si le
    *         fichier est valide.
    * @throws IOException
    *            en cas de problème d'accès au fichier
    */
   List<String> validate(File file) throws IOException;

}
