/**
 * 
 */
package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * @author CER6990937
 *
 */
public interface TraitementMasseSupport {

   /**
    * Vérifie que l'ECDE pointé par le job de capture de masse transmis en
    * paramètre soit disponible.<br>
    * <br>
    * La méthode lève une exception runtime si le job passé en paramètre n'est
    * pas un job de capture de masse.
    * 
    * @param jobQueue
    *           un job de capture de masse
    * @return true si l'ECDE est disponible, false dans le cas contraire
    */
   public boolean isEcdeUpJobTraitementMasse(JobQueue jobQueue);

   /**
    * Filtre les traitements de masse pour ne récupérer que ceux : <br>
    * <li>concernant les captures en masse pour l'ECDE local</li> <li>concernant
    * les suppressions de masse tous CNP</li> <li>concernant les restore de
    * masse tous CNP</li> <br>
    * Le type des traitements de masse est indiqué par la propriété
    * <code>jobName</code> de l'instance {@link JobRequest}.<br>
    * <li>Si il indique '{@value #CAPTURE_MASSE_JN}' alors il s'agit d'une
    * capture en masse.</li> <li>Si il indique '{@value #SUPPRESSION_MASSE_JN}'
    * alors il s'agit d'une suppresion en masse.</li> <li>Si il indique '
    * {@value #RESTORE_MASSE_JN}' alors il s'agit d'une restore en masse.</li>
    * <li>Si il indique '{@value #MODIFICATION_MASSE_JN}' alors il s'agit d'une
    * modification en masse.</li> <br>
    * Un traitement de capture en masse indique dans ses paramètres l'URL ECDE
    * du fichier sommaire.xml.<br>
    * on s'appuie sur {@link EcdeSupport#isLocal(URI)} pour savoir si il s'agit
    * d'une URL ECDE local ou non.
    * 
    * @param jobRequests
    *           traitements de masse
    * @return traitements de masse filtrés
    */
   public List<JobQueue> filtrerTraitementMasse(List<JobQueue> jobRequests);
   
   
   /**
    * Filtre les traitements de masse pour ne récupérer que ceux : <br>
    * <li>concernant les captures en masse</li>
    * <li>concernant les suppressions de masse tous CNP</li>
    * <li>concernant les restore de masse tous CNP</li>
    * <br>
    * Le type des traitements de masse est indiqué par la propriété
    * <code>jobName</code> de l'instance {@link JobRequest}.<br>
    * <li>Si il indique '{@value #CAPTURE_MASSE_JN}' alors il s'agit d'une capture
    * en masse.</li>
    * <li>Si il indique '{@value #SUPPRESSION_MASSE_JN}' alors il s'agit d'une suppresion
    * en masse.</li>
    * <li>Si il indique '{@value #RESTORE_MASSE_JN}' alors il s'agit d'une restore
    * en masse.</li>
    * <li>Si il indique '{@value #MODIFICATION_MASSE_JN}' alors il s'agit d'une modification
    * en masse.</li>
    * <br>
    * 
    * @param jobRequests
    *           traitements de masse
    * @return traitements de masse filtrés
    */
   public List<JobRequest> filtrerTraitementMasse(Collection<JobRequest> jobRequests);
   
   
   /**
    * Filtrer le traitement de masse en erreur.
    * 
    * @param jobsFailure
    *           Liste identifiants Jobs en erreurs
    * @param jobRequests
    *           Liste Jobs JobQueue
    * @return la liste de jobs sans les jobs en erreur.
    */
   public List<JobQueue> filtrerTraitementMasseFailure(final Set<UUID> jobsFailure, Collection<JobQueue> jobRequests);

   /**
    * Extrait l'URL ECDE pointé par un job de capture de masse
    * 
    * @param jobQueue
    *           le job de capture de masse
    * @return l'URL ECDE pointé par le job de capture de masse
    */
   public URI extractUrlEcde(JobQueue jobQueue);
}
