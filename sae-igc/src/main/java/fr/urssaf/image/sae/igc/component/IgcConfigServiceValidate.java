package fr.urssaf.image.sae.igc.component;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcConfigService;
import fr.urssaf.image.sae.igc.util.FileUtils;
import fr.urssaf.image.sae.igc.util.TextUtils;

/**
 * Classe de validation des méthodes contenues dans la classe
 * <code>fr.urssaf.image.sae.igc.service.IgcConfigService</code>
 * 
 * 
 */
@Aspect
public class IgcConfigServiceValidate {

   @SuppressWarnings("PMD.LongVariable")
   public static final String IGC_CONFIG_NOTEXIST = "Le fichier de configuration IGC est introuvable (${0})";

   private static final String METHODE = "execution(* fr.urssaf.image.sae.igc.service.IgcConfigService.loadConfig(*)) && args(pathConfigFile)";

   @Pointcut(METHODE)
   protected void loadConfig(String pathConfigFile) {
      // pas d'implémentation
      // sert uniquement à factoriser les advices de l'AOP
   }

   /**
    * Vérification de la validité des arguments entrée de la méthode
    * {@link IgcConfigService#loadConfig}
    * <ul>
    * <li><code>pathConfigFile</code> doit être renseigné</li>
    * <li>le fichier <code>pathConfigFile</code> doit exister</li>
    * </ul>
    * 
    * 
    * @param pathConfig
    *           chemin de configuration
    *           {@link IgcConfigService#loadConfig(String)}
    * @throws IgcConfigException
    *            une exception est levée sur les arguments en entrée
    */
   @Before("loadConfig(pathConfig)")
   public final void loadConfigBefore(String pathConfig)
         throws IgcConfigException {

      // String pathConfig = (String) joinPoint.getArgs()[0];

      if (!StringUtils.isNotBlank(pathConfig)) {

         throw new IllegalArgumentException(TextUtils
               .getArgEmpty("pathConfigFile"));
      }

      if (!FileUtils.isFile(pathConfig)) {

         throw new IgcConfigException(TextUtils.getMessage(IGC_CONFIG_NOTEXIST,
               pathConfig));
      }
   }

   /**
    * Vérification des propriétés de l'instance de {@link IgcConfig}
    * 
    * <ul>
    * 
    * <li>{@link IgcConfig#getRepertoireACRacines()} doit être renseigné</li>
    * <li>{@link IgcConfig#getRepertoireACRacines()} le chemin du répertoire
    * doit exister</li>
    * <li>{@link IgcConfig#getRepertoireCRLs()} doit être renseigné</li>
    * <li>{@link IgcConfig#getRepertoireCRLs()} le chemin du répertoire doit
    * exister</li>
    * <li>{@link IgcConfig#getUrlsTelechargementCRLs()} doit contenir au moins
    * une URL</li>
    * 
    * </ul>
    * 
    * @param pathConfig
    *           chemin de configuration
    *           {@link IgcConfigService#loadConfig(String)}
    * @param igcConfig
    *           instance de de {@link IgcConfig} en sortie de la méthode
    * @throws IgcConfigException
    *            une exception est levée sur l'instance de {@link IgcConfig} en
    *            sortie
    */
   @AfterReturning(pointcut = "loadConfig(pathConfig)", returning = "igcConfigs")
   public final void loadConfigAfter(String pathConfig, IgcConfigs igcConfigs)
         throws IgcConfigException {

      // String pathConfig = (String) joinPoint.getArgs()[0];

      for (IgcConfig igcConfig : igcConfigs.getIgcConfigs()) {
         if (StringUtils.isBlank(igcConfig.getAcRacine())) {

            throw new IgcConfigException(TextUtils.getMessage(
                  IgcConfigService.AC_RACINES_REQUIRED, pathConfig));
         }

         if (!FileUtils.isFile(igcConfig.getAcRacine())
               || !FileUtils.isAbsolute(igcConfig.getAcRacine())) {

            throw new IgcConfigException(TextUtils.getMessage(
                  IgcConfigService.AC_RACINES_NOTEXIST,
                  igcConfig.getAcRacine(), pathConfig));
         }

         if (StringUtils.isBlank(igcConfig.getCrlsRep())) {
            throw new IgcConfigException(TextUtils.getMessage(
                  IgcConfigService.CRLS_REQUIRED, pathConfig));
         }

         if (!FileUtils.isDirectory(igcConfig.getCrlsRep())
               || !FileUtils.isAbsolute(igcConfig.getCrlsRep())) {

            throw new IgcConfigException(TextUtils.getMessage(
                  IgcConfigService.CRLS_NOTEXIST, igcConfig.getCrlsRep(),
                  pathConfig));
         }

         if (igcConfig.getUrlList() == null
               || CollectionUtils.isEmpty(igcConfig.getUrlList().getUrls())) {

            throw new IgcConfigException(TextUtils.getMessage(
                  IgcConfigService.URLS_CRL_REQUIRED, pathConfig));
         }

         if (StringUtils.isEmpty(igcConfig.getPkiIdent())) {

            throw new IgcConfigException(TextUtils.getMessage(
                  IgcConfigService.ID_PKI_REQUIRED, pathConfig));
         }
      }
   }

}
