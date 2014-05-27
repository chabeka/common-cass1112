package fr.urssaf.image.sae.ordonnanceur.support;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;

/**
 * Support pour les traitements sur l'ECDE
 * 
 * 
 */
@Component
public class EcdeSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EcdeSupport.class);

   private final EcdeSources ecdeSources;

   private final EcdeServices ecdeServices;

   /**
    * Constructeur
    * 
    * @param ecdeSources
    *           liste des {@link EcdeSource} configurées pour l'ordonnanceur
    * @param ecdeServices
    *           les services ECDE
    * 
    */
   @Autowired
   public EcdeSupport(EcdeSources ecdeSources, EcdeServices ecdeServices) {

      this.ecdeSources = ecdeSources;

      this.ecdeServices = ecdeServices;
   }

   /**
    * Indique si l'URL ECDE est au local au serveur courant.<br>
    * <br>
    * A chaque URL correspond une configuration de {@link EcdeSource} pour
    * l'ordonnanceur.<br>
    * <br>
    * La méthode {@link EcdeSource#isLocal()} indique si l'URL ECDE est local
    * pour le CNP courant. <br>
    * 
    * @param ecdeURL
    *           URL de l'ECDE
    * @return <code>true</code>le traitement est local, <code>false</code> sinon
    */
   public final boolean isLocal(URI ecdeURL) {

      EcdeSource ecdeSource = this.loadEcdeSource(ecdeURL);

      boolean isLocal = ecdeSource == null ? false : ecdeSource.isLocal();

      return isLocal;
   }

   /**
    * Charge l'instance {@link EcdeSource} correspondant à une URL ECDE donnée.<br>
    * 
    * 
    * @param ecdeURL
    *           URL ECDE
    * @return instance {@link EcdeSource} correspondante
    */
   public final EcdeSource loadEcdeSource(URI ecdeURL) {

      EcdeSource source = null;

      for (EcdeSource ecdeSource : ecdeSources.getSources()) {

         if (StringUtils.equals(ecdeURL.getAuthority(), ecdeSource.getHost())) {

            source = ecdeSource;
            break;

         }
      }

      return source;
   }

   /**
    * Vérifie que l'URL ECDE passée en paramètre soit disponible.
    * 
    * @param ecdeUrl
    *           l'URL ECDE dont il faut vérifier la disponibilité
    * 
    * @return true si l'URL ECDE est disponible, false dans le cas contraire
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final boolean isEcdeDisponible(URI ecdeUrl) {

      // Conversion de l'URL du sommaire.xml en chemin absolu de fichier
      // Le contrôle de l'URL a dû être réalisée lors de l'acquittement de
      // la demande par les web services, il est donc peu probable que les
      // exceptions de malformations d'URL soient levées à ce niveau
      File ecdeFile;
      try {
         ecdeFile = ecdeServices.convertSommaireToFile(ecdeUrl);
      } catch (EcdeBadURLException e) {
         throw new OrdonnanceurRuntimeException(e);
      } catch (EcdeBadURLFormatException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

      // Regarde si le fichier existe
      boolean result;
      try {
         result = ecdeFile.exists();
      } catch (Throwable t) {
         LOGGER
               .warn(
                     "Une exception s'est produite lors de la vérification de l'existence du fichier "
                           + ecdeFile.getAbsolutePath(), t);
         result = false;
      }
      return result;

   }

}
