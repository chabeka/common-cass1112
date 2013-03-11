package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Support pour les traitements sur l'ECDE
 * 
 * 
 */
@Component
public class EcdeSupport {

   private final EcdeSources ecdeSources;

   /**
    * 
    * @param ecdeSources
    *           liste des {@link EcdeSource} configurées pour l'ordonnanceur
    */
   @Autowired
   public EcdeSupport(EcdeSources ecdeSources) {

      this.ecdeSources = ecdeSources;
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

}
