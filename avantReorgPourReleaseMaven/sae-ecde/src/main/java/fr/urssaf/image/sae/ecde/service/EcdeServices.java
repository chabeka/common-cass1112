package fr.urssaf.image.sae.ecde.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;

/**
 * EcdeServices représente la façade regroupant les différents services de l'ECDE.
 * 
 * Elle permettra d'unifier les différents services.
 * Par conséquent pour les clients de l'ECDE celle ci representera une vision unique.
 * 
 * @author lbaadj
 */
public interface EcdeServices {

   /**
    * Conversion d'un chemin absolu de fichier dans un ECDE vers l'URL ECDE correspondante
    * <br>
    * Cette methode prend donc en parametre un Fichier (en l'occurence son chemin) et une liste 
    * d'ECDESource pour le convertir en URI suivant le format decrit en entete de classe.
    * <br>
    * Exemples :<br>
    *        <u>succes</u><ul><li>chemin = /ecde/ecde_lyon/DCL01/19991231/3/documents/attestation/1990/a.pdf<br>
    *                          et la liste ecdeSource {(ecde.hoth.recouv,/ecde/ecde_hoth/);(ecde.cer69.recouv,/ecde/ecde_lyon/);(ecde.toto.recouv,/ecde/ecde_toto)}<br>
    *                          <b>resultat</b> la conversion donnera :  ecde://ecde.cer69.recouv/DCL01/19991231/3/documents/attestation/1990/a.pdf
    *        </li>
    *        </ul><u>echec : </u>
    *        <ul><li>  chemin = /ecde/ecde_lyon/DCL01/19991231/3/documents/attestation/1990/a.pdf<br>
    *                          et la liste ecdeSource {(ecde.hoth.recouv,/ecde/ecde_hoth/);(ecde.toto.recouv,/ecde/ecde_toto)}<br>
    *                          <b>resultat</b> :  Exception levée = Le chemin de fichier n'appartient à aucun ECDE transmis en parametre du service.</li>
    *        </ul>    
    * 
    * @param ecdeFile Fichier dans l'ECDE courant à convertir en URL ECDE
    * 
    *   
    * @throws EcdeBadFileException Mauvais chemin de fichier
    * 
    * @return URL ECDE
    */
    URI convertFileToURI (File ecdeFile) throws EcdeBadFileException;
   
   /**
    * Conversion d'une URL ECDE vers un chemin absolu de fichier dans l'ECDE correspondant
    * 
    * <br>
    * Cette methode prend donc en parametre une URI (verifiant bien le format decrit en entete de classe)  et une liste 
    * d'ECDESource pour la convertir en Fichier.
    * <br>
    * Exemples :
    *        <br><u>succes :</u><ul><li> uri = ecde://ecde.cer69.recouv/DCL01/19991231/3/documents/attestation/1990/a.pdf
    *        <br>
    *                          et la liste ecdeSource {(ecde.hoth.recouv,/ecde/ecde_hoth/);(ecde.cer69.recouv,/ecde/ecde_lyon/);(ecde.toto.recouv,/ecde/ecde_toto)}<br>
    *                          <b>resultat</b> la conversion donnera :  /ecde/ecde_lyon/DCL01/19991231/3/documents/attestation/1990/a.pdf
    *        </li>
    *         </ul><u>1er echec : </u>
    *        <ul>   <li> uri = ecde://ecde.cer69.recouv/DCL01/19991231/3/documents/attestation/1990/a.pdf<br>
    *                          et la liste ecdeSource {(ecde.hoth.recouv,/ecde/ecde_hoth/);(ecde.toto.recouv,/ecde/ecde_toto)}<br>
    *                          <b>resultat</b> :  Exception levée = L'URL ECDE n'appartient à aucun ECDE transmis en parametre du service.</li>
    *            </ul><u>2eme echec : </u><ul><li> uri = <b>ecd</b>://ecde.cer69.recouv/DCL01/19991231/3/<b>document</b>/attestation/1990/a.pdf<br>
    *                          et la liste ecdeSource {(ecde.hoth.recouv,/ecde/ecde_hoth/);(ecde.toto.recouv,/ecde/ecde_toto)}<br>
    *                          <b>resultat</b> :  Exception levée = L'URL ECDE est incorrecte.</li>               
    *        </ul> 
    * 
    * 
    * 
    *  
    * @param ecdeURL URL ECDE à convertir en chemin de fichier dans son ECDE correspondant
    * 
    *   
    * @throws EcdeBadURLException mauvaise url 
    * @throws EcdeBadURLFormatException mauvais format d'url
    * 
    * @return Chemin du fichier dans ECDE correspondant
    *  
    */
    File convertURIToFile (URI ecdeURL) throws EcdeBadURLException, EcdeBadURLFormatException;
    
    /**
     * Conversion d'une URL sommaire vers un chemin absolu de fichier dans l'ECDE correspondant
     * 
     * <br>
     * Cette methode prend donc en parametre une URI (verifiant bien le format decrit en entete de classe)  et une liste 
     * d'ECDESource pour la convertir en Fichier.
     * <br>
     * Exemples :
     *        <br><u>succes :</u><ul><li> uri = ecde://ecde.cer69.recouv/DCL01/19991231/3/sommaire.xml
     *        <br>
     *                          et la liste ecdeSource {(ecde.hoth.recouv,/ecde/ecde_hoth/);(ecde.cer69.recouv,/ecde/ecde_lyon/);(ecde.toto.recouv,/ecde/ecde_toto)}<br>
     *                          <b>resultat</b> la conversion donnera :  /ecde/ecde_lyon/DCL01/19991231/3/sommaire.xml
     *        </li>
     *  
     * @param sommaireURL URL sommaire à convertir en chemin de fichier dans son ECDE correspondant
     * 
     *   
     * @throws EcdeBadURLException mauvaise url 
     * @throws EcdeBadURLFormatException mauvais format d'url
     * 
     * @return Chemin du fichier dans ECDE correspondant
     *  
     */
    File convertSommaireToFile(URI sommaireURL) throws EcdeBadURLException, EcdeBadURLFormatException;
    
    /**
     * Service permettant de recuperer un objet Sommaire à partir d'un fichier sommaire.xml
     * 
     * @param uri : à fetcher
     * @return Sommaire : objet a retourner a partir du fichier 
     * @throws EcdeGeneralException erreur avec le fichier sommaire.xml
     */
    Sommaire fetchSommaireByUri(URI uri) throws EcdeGeneralException;
    
    /**
     * Methodes permettant de persister les objets Resultats dans un fichier resultats.xml
     * 
     * @param resultats : objet a persister
     * @throws IOException erreur lors de la creation du fichier flag
     * @throws EcdeXsdException non respect du format XSD
     */
    void persistResultat(Resultats resultats) throws EcdeXsdException, IOException;
   
   
   
}
