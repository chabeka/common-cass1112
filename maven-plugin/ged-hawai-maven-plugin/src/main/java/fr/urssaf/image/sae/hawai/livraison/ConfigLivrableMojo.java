package fr.urssaf.image.sae.hawai.livraison;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import fr.urssaf.image.sae.hawai.service.VersionPropertiesService;
import fr.urssaf.image.sae.hawai.utils.ConsoleService;
import fr.urssaf.image.sae.hawai.utils.Constants;
import fr.urssaf.image.sae.hawai.utils.SVN;
import fr.urssaf.image.sae.hawai.validate.Allowed;
import fr.urssaf.image.sae.hawai.validate.ParameterValidator;

/**
 * @author mcarpentier
 */
@Mojo(name = "configuration_livraison", defaultPhase = LifecyclePhase.INITIALIZE)
public class ConfigLivrableMojo extends AbstractMojo {

  /**
   * paramètres propres à maven
   */

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession session;

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${project.basedir}", readonly = true)
  private File basedir;

  @Parameter(defaultValue = "${project.build.directory}/", readonly = true)
  private String targetDir;

  @Parameter(defaultValue = "${project.build.finalName}")
  private String applicationName;

  @Parameter(defaultValue = Constants.WORKSPACE_DIR, property = "workspaceDir", alias = "${workspaceDir}", readonly = true)
  private File workspaceDir;

  @Parameter(defaultValue = Constants.WORKSPACE_DIR + "/checkout", readonly = true)
  private File workspaceCheckoutDir;

  @Parameter(defaultValue = Constants.WORKSPACE_DIR + "/unzip", readonly = true)
  private File workspaceUnzipDir;

  @Parameter(defaultValue = "ged", required = true)
  private String projectName;

  /**
   * paramètres renseignés par l'utilisateur
   */

  @Parameter(defaultValue = "hawai6")
  @Allowed(values = {"hawai4", "hawai5", "hawai51", "hawai6"})
  private String hawaiVersion;

  @Parameter(defaultValue = "${branchName}", required = false)
  private String branchName;

  @Parameter(defaultValue = "${skip}", required = false)
  private boolean skip;

  @Parameter(defaultValue = "${sae.version}")
  private String saeVersion;

  @Parameter(defaultValue = "${dfce-webapp.version}")
  private String dfceWebAppVersion;

  @Parameter(defaultValue = "${sae-anais-portail.version}")
  private String saeAnaisPortailVersion;

  @Parameter(defaultValue = "${sae-ihm-web-exploit.version}")
  private String saeIhmWebExploitVersion;

  @Parameter(defaultValue = "${sae-ihm-web-exploit-livrable.version}", required = true)
  private String saeIhmWebExploitLivrableVersion;

  /**
   * identifiant ANAIS de l'utilisateur ayant lancé le build, récupéré sur
   * jenkins
   */
  private String anaisUserId;

  /**
   * Système de log pour que les traces affichées sur la console soient
   * potables. <br/>
   * TODO : surement un meilleur moyen de faire ça (logback ?)
   */
  private final ConsoleService console = new ConsoleService(getLog());

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    console.displayCategorie("initialisation");

    // on valide les parametres renseignés par l'utilisateur
    final ParameterValidator v = new ParameterValidator();
    v.validate(this);

    cleanDirectories();

    if (isSkip()) {
      // livraison skippée
      console.display("skip : préparation livraison desactivée pour ce projet");
    } else {
      // livraison applicative
      // String buildUrl = "";
      // try {
      // buildUrl = CommandLineUtils.getSystemEnvVars().getProperty(Constants.BUILD_URL_PROP);
      // if (StringUtils.isBlank(buildUrl)) {
      // throw new MojoExecutionException(
      // "Impossible de récupérer l'url du build en cours : lancez vous bien la livraison depuis jenkins/hudson ?");
      // }
      // }
      // catch (final IOException e) {
      // throw new MojoExecutionException(
      // "Impossible de récupérer l'url du build en cours : " + e.getMessage(),
      // e);
      // }
      // final JenkinsService jenkinsService = new JenkinsService();
      // anaisUserId = jenkinsService.getJenkinsBuildUsername(buildUrl);
      // console.display("projet de packaging " + project.getPackaging()
      // + " : livraison de type applicative (user :" + anaisUserId + ")");

      console.displayCategorie("checkout");
      // récupération du repo hawai - hannn le vieux mdp en dur
      // dans le code
      SVN.checkout(workspaceCheckoutDir, buildSvnUrl(), "intconpnr", "123456", console);

      updateVersions();
    }

  }

  /**
   * on clean l'espace de travail au cas où
   */
  private void cleanDirectories() throws MojoFailureException {
    cleanOrCreateDir(workspaceDir);
    cleanOrCreateDir(workspaceCheckoutDir);
    cleanOrCreateDir(workspaceUnzipDir);
  }

  private void cleanOrCreateDir(final File dir) throws MojoFailureException {
    try {
      if (dir.exists()) {
        FileUtils.cleanDirectory(dir);
      } else {
        FileUtils.mkdir(dir.getAbsolutePath());
      }
    }
    catch (final IOException e) {
      throw new MojoFailureException("Impossible de vider ou de créer le répertoire " + dir.getAbsolutePath());
    }
  }

  /**
   * on devine l'url du repo svn hawai du projet
   */
  private String buildSvnUrl() {

    String svnHwi = "apps-v6";
    if ("hawai5".equalsIgnoreCase(hawaiVersion)) {
      svnHwi = "apps";
    } else if ("hawai6".equalsIgnoreCase(hawaiVersion)) {
      svnHwi = "apps-v6";
    }

    final String branchOrTrunk = StringUtils.isBlank(branchName) ? "trunk" : "branches/" + branchName;

    return "http://" + Constants.HAWAI_SVN_URL + "/" + svnHwi + "/hwi_" + projectName + "/" + branchOrTrunk
        + "/bin";
  }

  public boolean isSkip() {
    // cette méthode permet à l'utilisateur de passer une valeur à la
    // propriété par la ligne de commande (avec -D)
    // ET d'avoir une valeur par défaut !
    boolean result = skip;
    if (session.getUserProperties().containsKey("skip")) {
      result = Boolean.parseBoolean(session.getUserProperties().getProperty("skip"));
    }
    return result;
  }

  /**
   * On récupére les versions des composants dans le fichier versions.properties
   */
  private void updateVersions() throws MojoExecutionException {
    console.displayCategorie("mise a jour version à partir du fichier version.properties");
    final VersionPropertiesService service = new VersionPropertiesService(projectName, console);
    final Map<String, String> propertiesVersionMap = service.updateProjectVersions(workspaceCheckoutDir,
                                                                                   workspaceUnzipDir,
                                                                                   project.getVersion());

    changeProjectProperties("target.sae.version", propertiesVersionMap.get("sae.version"));
    changeProjectProperties("target.dfce-webapp.version", propertiesVersionMap.get("dfce-webapp.version"));
    changeProjectProperties("target.sae-anais-portail.version", propertiesVersionMap.get("sae-anais-portail.version"));
    changeProjectProperties("target.sae-ihm-web-exploit.version", propertiesVersionMap.get("sae-ihm-web-exploit.version"));
  }

  /**
   * Methode permettant de modifier une propriété projet
   * 
   * @param key
   *          Clef
   * @param value
   *          Valeur
   */
  private void changeProjectProperties(final String key, final String value) {
    project.getProperties().setProperty(key, value);
  }

}