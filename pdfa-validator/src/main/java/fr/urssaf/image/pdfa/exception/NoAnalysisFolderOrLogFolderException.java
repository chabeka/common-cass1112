package fr.urssaf.image.pdfa.exception;


public class NoAnalysisFolderOrLogFolderException extends Exception {
   /**
    * Constructeur
    * 
    */
   public NoAnalysisFolderOrLogFolderException() {
      super("Le chemin complet vers les fichiers à analyser et le répertoire des log sont obligatoires");
   }
}
