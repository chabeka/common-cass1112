package fr.urssaf.image.sae.hawai.livraison;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import fr.urssaf.image.sae.hawai.service.VersionPropertiesService;
import fr.urssaf.image.sae.hawai.utils.CommonsUtils;
import fr.urssaf.image.sae.hawai.utils.ConsoleService;
import fr.urssaf.image.sae.hawai.utils.Constants;
import fr.urssaf.image.sae.hawai.utils.SVN;
import fr.urssaf.image.sae.hawai.validate.Allowed;

/**
 * Description du type
 */
@Component(role = AbstractMavenLifecycleParticipant.class, hint = "ged")
public class GedMavenLifecycleParticipant extends AbstractMavenLifecycleParticipant {

  @Parameter(defaultValue = Constants.WORKSPACE_CHECKOUT, readonly = true)
  private File workspaceCheckoutDir;

  @Parameter(defaultValue = "hawai6")
  @Allowed(values = {"hawai4", "hawai5", "hawai51", "hawai6"})
  private String hawaiVersion;

  @Parameter(defaultValue = "${branchName}", required = false)
  private String branchName;

  @Parameter(defaultValue = "ged", required = true)
  private String projectName;

  @Parameter(defaultValue = "${skip}", required = false)
  private boolean skip;

  @Requirement
  private Logger logger;

  /**
   * Système de log pour que les traces affichées sur la console soient
   * potables. <br/>
   * TODO : surement un meilleur moyen de faire ça (logback ?)
   */
  private final ConsoleService console = new ConsoleService(new DefaultLog(logger));

  @Override
  public void afterSessionStart(final MavenSession session)
      throws MavenExecutionException {

    super.afterSessionStart(session);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterSessionEnd(final MavenSession session) throws MavenExecutionException {
    super.afterSessionEnd(session);
  }

  @Override
  public void afterProjectsRead(final MavenSession session)
      throws MavenExecutionException {
    super.afterProjectsRead(session);

    if (workspaceCheckoutDir == null) {
      workspaceCheckoutDir = Paths.get(session.getRequest().getBaseDirectory() + "/target/checkout/").toFile();
    }

    if (hawaiVersion == null) {
      hawaiVersion = "hawai6";
    }

    if (branchName == null) {
      branchName = session.getUserProperties().getProperty("branchName");
    }

    if (projectName == null) {
      projectName = "ged";
    }

    skip = Boolean.parseBoolean(session.getUserProperties().getProperty("skip"));

    if (!isSkip(session)) {
      try {
        cleanDirectories();
      }
      catch (final MojoFailureException e) {
        throw new MavenExecutionException("Erreur clean dossier checkout : ", e);
      }

      // récupération du repo hawai - hannn le vieux mdp en dur
      // dans le code
      try {
        console.displayCategorie("Checkout du projet /bin/");
        SVN.checkout(workspaceCheckoutDir, CommonsUtils.buildSvnUrl(hawaiVersion, branchName, projectName), "intconpnr", "123456", console);
      }
      catch (final MojoExecutionException e) {
        throw new MavenExecutionException("Erreur checkout SVN : ", e);
      }

      try {
        updateVersions(session);
      }
      catch (final MojoExecutionException e) {
        throw new MavenExecutionException("Erreur update version : ", e);
      }
    }
  }

  /**
   * On récupére les versions des composants dans le fichier versions.properties
   * 
   * @param session
   */
  private void updateVersions(final MavenSession session) throws MojoExecutionException {
    console.displayCategorie("mise a jour version à partir du fichier version.properties");
    final VersionPropertiesService service = new VersionPropertiesService(projectName, console);
    final Map<String, String> propertiesVersionMap = service.updateProjectVersions(workspaceCheckoutDir);

    changeProjectProperties("sae-livrable.version", propertiesVersionMap.get("sae.version"), session);
  }

  /**
   * Methode permettant de modifier une propriété projet
   * 
   * @param key
   *          Clef
   * @param value
   *          Valeur
   * @param session
   */
  private void changeProjectProperties(final String key, final String value, final MavenSession session) {
    session.getUserProperties().setProperty(key, value);
  }

  public boolean isSkip(final MavenSession session) {
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
   * on clean l'espace de travail au cas où
   */
  private void cleanDirectories() throws MojoFailureException {
    cleanOrCreateDir(workspaceCheckoutDir);
  }

  private void cleanOrCreateDir(final File dir) throws MojoFailureException {
    try {
      if (dir.exists()) {
        console.displayCategorie("Suppression du dossier " + dir.getAbsolutePath());
        final Path pathToBeDeleted = dir.toPath();

        Files.walkFileTree(pathToBeDeleted,
                           new SimpleFileVisitor<Path>() {
                             @Override
                             public FileVisitResult postVisitDirectory(
                                                                       final Path dir, final IOException exc)
                                 throws IOException {
                               Files.delete(dir);
                               return FileVisitResult.CONTINUE;
                             }

                             @Override
                             public FileVisitResult visitFile(
                                                              final Path file, final BasicFileAttributes attrs)
                                 throws IOException {
                               Files.delete(file);
                               return FileVisitResult.CONTINUE;
                             }
                           });
      }
    }
    catch (final IOException e) {
      throw new MojoFailureException("Impossible de vider ou de créer le répertoire " + dir.getAbsolutePath());
    }
  }

}