package fr.urssaf.image.sae.hawai.livraison;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.twdata.maven.mojoexecutor.MojoExecutor;

import fr.urssaf.image.sae.hawai.service.JenkinsService;
import fr.urssaf.image.sae.hawai.utils.CommonsUtils;
import fr.urssaf.image.sae.hawai.utils.ConsoleService;
import fr.urssaf.image.sae.hawai.utils.Constants;
import fr.urssaf.image.sae.hawai.utils.SVN;
import fr.urssaf.image.sae.hawai.validate.Allowed;
import fr.urssaf.image.sae.hawai.validate.ParameterValidator;

/**
 * Plugin de préparation à la création du RPM de la GED National.
 * 
 * @author nvangout
 */
@Mojo(name = "configuration_livraison", defaultPhase = LifecyclePhase.INITIALIZE)
public class ConfigLivrableMojo extends AbstractMojo {

  /*
   * paramètres propres à maven
   */

  /**
   * Session maven
   */
  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession session;

  /**
   * Projet maven
   */
  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  /**
   * Manager de plugin
   */
  @Component
  private BuildPluginManager pluginManager;

  /**
   * Base directory
   */
  @Parameter(defaultValue = "${project.basedir}", readonly = true)
  private File basedir;

  /**
   * Target directory
   */
  @Parameter(defaultValue = "${project.build.directory}/", readonly = true)
  private String targetDir;

  /**
   * Workspace du projet
   */
  @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}-dir", readonly = true)
  private File workspaceProjectDir;

  /**
   * Dossier checkout du projet
   */
  @Parameter(defaultValue = Constants.WORKSPACE_CHECKOUT, readonly = true)
  private File workspaceCheckoutDir;

  /**
   * Dossier d'extraction des archives du projet
   */
  @Parameter(defaultValue = "${project.build.directory}/unzip", readonly = true)
  private File workspaceUnzipDir;

  /**
   * Nom projet
   */
  @Parameter(defaultValue = "ged", required = true)
  private String projectName;

  /*
   * paramètres renseignés par l'utilisateur
   */

  /**
   * Version d'Hawai
   */
  @Parameter(defaultValue = "hawai6")
  @Allowed(values = {"hawai4", "hawai5", "hawai51", "hawai6"})
  private String hawaiVersion;

  /**
   * Nom de la branche
   */
  @Parameter(defaultValue = "${branchName}", required = false)
  private String branchName;

  /**
   * Skip des traitements
   */
  @Parameter(defaultValue = "${skip}", required = false)
  private boolean skip;

  /**
   * Système de log pour que les traces affichées sur la console soient
   * potables.
   */
  private final ConsoleService console = new ConsoleService(getLog());

  /**
   * identifiant ANAIS de l'utilisateur ayant lancé le build, récupéré sur
   * jenkins
   */
  private String anaisUserId;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    console.displayCategorie("-- initialisation --");

    // on valide les parametres renseignés par l'utilisateur
    final ParameterValidator v = new ParameterValidator();
    v.validate(this);

    cleanDirectories();

    verifyActivesProfils();

    if (isSkip() || isProfilLivraison()) {
      // La livraison skippée ou profil est "livraison"
      // Profil livraison permet de refaire un RPM sans pour autant faire toute la préparation à la livraison. La livraison est donc plus rapide.
      console.display("skip : préparation livraison desactivée pour le projet " + projectName);
    } else {
      // On vérifie que l'on a bien le profile livraisonFull car pour lancer le build et la création de RPM, l'un des profile "livraison" ou "livraisonFull" est obligatoire.
      if (!isProfilLivraisonFull()) {
        console.display("La création du RPM est abandonnée. L'un des profile suivants est obligatoire : 'livraison' ou 'livraisonFull'");
        return;
      }

      // On ckeckout le projet SVN hwi_projectName afin de modifier les fichiers de configuration qui aurait pu changer durant la version.
      console.displayCategorie("-- Checkout --");
      SVN.checkout(workspaceCheckoutDir, CommonsUtils.buildSvnUrl(hawaiVersion, branchName, projectName), "intconpnr", "123456", console);

      // On extrait les fichiers des dépendances pour pouvoir les assemblés par la suite.
      console.displayCategorie("-- Unpack dependency --");
      unpackArtifact();

      // On assemble les fichiers de manière à simplifier la copy dans le dossier de checkout
      console.displayCategorie("-- Assembly : Copie target file --");
      MojoExecutor.executeMojo(
                               plugin(
                                      groupId("org.apache.maven.plugins"),
                                      artifactId("maven-assembly-plugin"),
                                      version("3.1.1")),

                               goal("single"),
                               configuration(
                                             element(name("descriptors"), element(name("descriptor"), "src/assembly/copy_assembly.xml"))),
                               executionEnvironment(
                                                    project,
                                                    session,
                                                    pluginManager));

      // On copie les fichiers assemblés dans le dossier de checkout.
      console.displayCategorie("-- Copie directory for commit --");
      copyFiles();

      // On récupére le userId Anais de l'utilisateur ayant lancé le build Jenkins.
      console.displayCategorie("-- Anais userID check --");
      if (session.getRequest().getActiveProfiles() != null && ArrayUtils.contains(session.getRequest().getActiveProfiles().toArray(), "jenkins")) {
        // Recherche de l'identifiant anais du user
        String buildUrl = "";
        try {
          buildUrl = CommandLineUtils.getSystemEnvVars().getProperty(Constants.BUILD_URL_PROP);
          if (StringUtils.isBlank(buildUrl)) {
            throw new MojoExecutionException(
                                             "Impossible de récupérer l'url du build en cours : lancez vous bien la livraison depuis jenkins/hudson ?");
          }
        }
        catch (final IOException e) {
          throw new MojoExecutionException(
                                           "Impossible de récupérer l'url du build en cours : " + e.getMessage(),
                                           e);
        }

        final JenkinsService jenkinsService = new JenkinsService();
        anaisUserId = jenkinsService.getJenkinsBuildUsername(buildUrl);
      } else {
        // Si lancement depuis poste de developpement, le userId vaut 'userLocal'
        anaisUserId = "userLocal";
      }

      console.displayCategorie("Anais userID : " + anaisUserId);

      // On commit les fichiers du projet hwi_projectName/bin
      commit();

    }

  }

  /**
   * Vérification des profils actifs
   * 
   * @throws MojoExecutionException
   * @{@link MojoExecutionException}
   */
  private void verifyActivesProfils() throws MojoExecutionException {
    console.displayCategorie("Verify actives profiles");
    final List<String> profilsOutilsValide = Arrays.asList("eclipse", "jenkins");
    final List<String> profilsLivraisonValide = Arrays.asList("livraion", "livraisonFull");
    final List<String> projectActivesProfilsString = new ArrayList<>();
    boolean isProfilOutilValide = false;
    boolean isProfilLivraisonValide = false;

    for (final String profil : session.getRequest().getActiveProfiles()) {
      projectActivesProfilsString.add(profil);
    }

    for (final String profil : projectActivesProfilsString) {
      if (profilsOutilsValide.stream().anyMatch(e -> e.equals(profil))) {
        isProfilOutilValide = true;
      }
      if (profilsLivraisonValide.stream().anyMatch(e -> e.equals(profil))) {
        isProfilLivraisonValide = true;
      }
    }

    if (isProfilLivraisonValide && isProfilOutilValide) {
      console.displayCategorie("Profiles validés : " + projectActivesProfilsString);
    } else {
      throw new MojoExecutionException(
                                       "Impossible d'executer le traitement avec les profils suivants : " + projectActivesProfilsString);
    }

  }

  /**
   * Vérifie si on a bien le profil "livraison"
   * 
   * @return true si on a le profile "livraison", false sinon
   */
  private boolean isProfilLivraison() {
    for (final Profile profil : project.getActiveProfiles()) {
      if ("livraison".equals(profil.getId())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Vérifie si on a bien le profil "livraisonFull"
   * 
   * @return true si on a le profile "livraisonFull", false sinon
   */
  private boolean isProfilLivraisonFull() {
    for (final Profile profil : project.getActiveProfiles()) {
      if ("livraisonFull".equals(profil.getId())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Extraction des dependances maven (Fichiers ZIP)
   * 
   * @throws MojoExecutionException
   * @{@link MojoExecutionException}
   */
  private void unpackArtifact() throws MojoExecutionException {
    for (final Artifact dependency : project.getDependencyArtifacts()) {

      if (dependency != null) {
        String versionDependency = session.getUserProperties().getProperty(dependency.getArtifactId() + ".version");
        if (StringUtils.isBlank(versionDependency)) {
          versionDependency = dependency.getVersion();
        }

        try {
          MojoExecutor.executeMojo(
                                   plugin(
                                          groupId("org.apache.maven.plugins"),
                                          artifactId("maven-dependency-plugin"),
                                          version("3.1.0")),

                                   goal("unpack"),
                                   configuration(
                                                 element(name("artifactItems"),
                                                         element(name("artifactItem"),
                                                                 element("groupId", dependency.getGroupId()),
                                                                 element("artifactId", dependency.getArtifactId()),
                                                                 element("version", versionDependency),
                                                                 element("classifier", dependency.getClassifier()),
                                                                 element("type", dependency.getType()),
                                                                 element("overWrite", "false"),
                                                                 element("outputDirectory", "${project.build.directory}/" + dependency.getArtifactId()),
                                                                 element("includes", "**/*.*"),
                                                                 element("excludes", "")))),
                                   executionEnvironment(
                                                        project,
                                                        session,
                                                        pluginManager));
        }
        catch (final MojoExecutionException e) {
          throw new MojoExecutionException("Une erreur est survenue lors de l'extraction de l'archive " + dependency.getGroupId() + ":"
              + dependency.getArtifactId() + ":" + versionDependency + " : " + e.getMessage(),
                                           e);
        }
      }
    }

  }

  /**
   * Clean de l'espace de travail
   */
  private void cleanDirectories() throws MojoFailureException {
    cleanOrCreateDir(workspaceProjectDir);
    cleanOrCreateDir(workspaceCheckoutDir);
    cleanOrCreateDir(workspaceUnzipDir);
  }

  /**
   * Suppression ou création d'un dossier passé en paramètre
   * 
   * @param dir
   *          Dossier à traiter
   * @throws MojoFailureException
   * @{@link MojoFailureException}
   */
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
   * Vérifie si l'on skip les traitements.
   * 
   * @return true si l'on skip les traitements, false sinon.
   */
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
   * Ancien mode de fonctionnement : on copie les fichiers dans /bin qu'on
   * commitera par la suite
   */
  private void copyFiles() throws MojoExecutionException {
    try {
      console.displayCategorie("copie des fichiers vers " + workspaceCheckoutDir);
      MojoExecutor.executeMojo(
                               plugin(
                                      groupId("org.apache.maven.plugins"),
                                      artifactId("maven-resources-plugin"),
                                      version("3.1.0")),

                               goal("copy-resources"),
                               configuration(element("outputDirectory", Constants.WORKSPACE_CHECKOUT),
                                             element("overwrite", "true"),
                                             element("resources",
                                                     element("resource",
                                                             element("directory", "target/${project.build.finalName}-dir"),
                                                             element("filtering", "false"))),
                                             element("encoding", "UTF-8")),
                               executionEnvironment(
                                                    project,
                                                    session,
                                                    pluginManager));

      console.displayCategorie("Copie pom.xml");
      MojoExecutor.executeMojo(
                               plugin(
                                      groupId("org.apache.maven.plugins"),
                                      artifactId("maven-resources-plugin"),
                                      version("3.1.0")),

                               goal("copy-resources"),
                               configuration(element("outputDirectory", "${project.build.directory}"),
                                             element("overwrite", "true"),
                                             element("resources",
                                                     element("resource",
                                                             element("directory", project.getBasedir().getAbsolutePath()),
                                                             element("filtering", "false"),
                                                             element("includes", element("include", "pom.xml")))),
                                             element("encoding", "UTF-8")),
                               executionEnvironment(
                                                    project,
                                                    session,
                                                    pluginManager));

    }
    catch (final MojoExecutionException e) {
      throw new MojoExecutionException("Une erreur est survenue lors du dossier " + workspaceUnzipDir + " vers " + workspaceCheckoutDir + e.getMessage(),
                                       e);
    }
  }

  /**
   * Commit de toutes les modifications du répertoire checkout
   */
  private void commit() throws MojoExecutionException {
    console.displayCategorie("-- Commit --");
    final String commitMessage = "Auto-commit livraion MOE GEDNAT lancé par " + anaisUserId;

    console.display("commit vers " + buildSvnUrl());
    console.display("repertoire commité : " + workspaceCheckoutDir.getAbsolutePath());
    console.display("message du commit : " + commitMessage);
    SVN.commit(workspaceCheckoutDir, buildSvnUrl(), commitMessage, "intconpnr", "123456", console);
  }

  /**
   * Définition de l'url du repo svn hawai du projet
   */
  private String buildSvnUrl() {

    String svnHwi = "hawai-v4";
    if ("hawai51".equalsIgnoreCase(hawaiVersion)) {
      svnHwi = "apps-v51";
    } else if ("hawai5".equalsIgnoreCase(hawaiVersion)) {
      svnHwi = "apps";
    } else if ("hawai6".equalsIgnoreCase(hawaiVersion)) {
      svnHwi = "apps-v6";
    }

    final String branchOrTrunk = StringUtils.isBlank(branchName) ? "trunk" : "branches/" + branchName;

    return "http://" + Constants.HAWAI_SVN_URL + "/" + svnHwi + "/hwi_" + projectName + "/" + branchOrTrunk
        + "/bin";
  }

}