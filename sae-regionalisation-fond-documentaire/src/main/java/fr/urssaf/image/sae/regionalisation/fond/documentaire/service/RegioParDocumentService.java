package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.io.File;

/**
 * Service des traitements de régionalisation se basant sur le document (par
 * opposition aux traitements de régionalisation se basant sur les fichiers de
 * la V2)
 */
public interface RegioParDocumentService {

   /**
    * Préparation des fichiers de la V2.<br>
    * <br>
    * Il faut construire une arborescence de ce type :<br>
    * 
    * <pre>
    * c:\datas\
    *          116\
    *                fusion_regi_COTI.lst
    *                fusion_regi_CPTE.lst
    *                fusion_regi_PERS.lst
    *          217\
    *                fusion_regi_COTI.lst
    *                fusion_regi_CPTE.lst
    *                fusion_regi_PERS.lst
    *          [...]
    * </pre>
    * 
    * En sortie du traitement, 3 fichiers seront générés :<br>
    * <br>
    * <ul>
    * <li>regionalisation_coti.csv</li>
    * <li>regionalisation_cpte.csv</li>
    * <li>regionalisation_pers.csv</li>
    * </ul>
    * <br>
    * Ces 3 fichiers pourront ensuite être importés dans PostgreSQL pour les
    * traitements.
    * 
    * @param pathFichiersV2
    *           le répertoire contenant les fichiers de la V2
    * @param pathFichiersSortie
    *           le répertoire dans lequel écrire les 3 fichiers de sortie
    */
   void prepareFichiersV2(File pathFichiersV2, File pathFichiersSortie);

   /**
    * Extraction du fonds documentaire en un fichier CSV<br>
    * Ce fichier doit ensuite être exploité en SGBDR<br>
    * Pour être ensuite de nouveau extrait du SGBDR, et traité avec la méthode
    * miseAjourDocuments
    * 
    * @param fichierSortieCsv
    *           le fichier de sortie dans lequel écrire l'extraction du fonds
    *           documentaire
    */
   void extractionFondsDocumentaire(File fichierSortieCsv);

   /**
    * Traitement de mise à jour des documents
    * 
    * @param fichierCsv
    *           le fichier CSV contenant les données de régio. Ce fichier est
    *           obtenu par des traitements Postgresql
    * @param numeroPremiereLigne
    *           le n° de la 1ère ligne du fichier CSV à traiter
    * @param fichierTraces
    *           le fichier dans lequel écrire les traces du traitement
    */
   void miseAjourDocuments(File fichierCsv, int numeroPremiereLigne,
         File fichierTraces);

}
