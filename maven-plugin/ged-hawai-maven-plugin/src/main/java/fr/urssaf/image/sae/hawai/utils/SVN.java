package fr.urssaf.image.sae.hawai.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.MojoExecutionException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * Tache Ant SVN.<br />
 * 
 * @author mcarpentier
 */
public class SVN {

  /**
   * Formatter
   */
  private static PeriodFormatter DURATION_FORMATTER = new PeriodFormatterBuilder().appendMinutes()
                                                                                  .appendSuffix("m")
                                                                                  .appendSeconds()
                                                                                  .appendSuffix("s")
                                                                                  .toFormatter();

  /**
   * pattern for url including login AND password
   */
  private final static Pattern urlPattern = Pattern.compile("http://(.*)?:(.*)?@.*");

  /**
   * Catégorie
   */
  private final static String CATEGORIE = "svn";

  /**
   * Méthode de gestion du checkout SVN
   * 
   * @param dstPath
   *          Chemin de destination
   * @param url
   *          Url SVN
   * @param user
   *          Utilisateur
   * @param password
   *          Mote de passe utilisateur
   * @param console
   *          Console de log
   * @throws MojoExecutionException
   * @{@link MojoExecutionException}
   */
  public static void checkout(final File dstPath, final String url, final String user, final String password, final ConsoleService console)
      throws MojoExecutionException {
    try {
      final SVNURL svnUrl = SVNURL.parseURIEncoded(url);
      DAVRepositoryFactory.setup();
      // get and check auth
      final SVNClientManager cm = getClientWithAuth(url, user, password);
      if (console != null) {
        console.display("checking out " + svnUrl.getProtocol() + "://" + svnUrl.getHost() + ":" + svnUrl.getPort()
            + svnUrl.getPath() + " ...", CATEGORIE);
      }

      final SVNUpdateClient uc = cm.getUpdateClient();
      final DateTime start = new DateTime();
      uc.doCheckout(svnUrl, dstPath, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, false);
      final Duration duration = new Duration(start, new DateTime());
      if (console != null) {
        console.display("completed (" + DURATION_FORMATTER.print(duration.toPeriod()) + ")", CATEGORIE);
      }

    }
    catch (final SVNException e) {
      throw new MojoExecutionException("Impossible de se connecter au svn :" + e.getMessage(), e);
    }
  }

  /**
   * Méthode de gestion du commit SVN
   * 
   * @param dstPath
   *          Chemin de destination
   * @param url
   *          Url SVN
   * @param user
   *          Utilisateur
   * @param password
   *          Mote de passe utilisateur
   * @param console
   *          Console de log
   * @param message
   *          message accompagnant le commit
   * @throws MojoExecutionException
   * @{@link MojoExecutionException}
   */
  public static void commit(final File dstPath, final String url, final String message, final String user, final String password,
                            final ConsoleService console)
      throws MojoExecutionException {

    try {
      DAVRepositoryFactory.setup();
      // get and check auth
      final SVNClientManager cm = getClientWithAuth(url, user, password);
      console.display("sending content ...", CATEGORIE);
      final DateTime start = new DateTime();

      cm.getStatusClient()
        .doStatus(dstPath,
                  SVNRevision.HEAD,
                  SVNDepth.INFINITY,
                  false,
                  false,
                  false,
                  false,
                  new ISVNStatusHandler() {
                    @Override
                    public void handleStatus(final SVNStatus status) throws SVNException {
                      // Vérification des status des fichiers ou dossier à traiter
                      if (SVNStatusType.STATUS_MISSING.equals(status.getNodeStatus())) {
                        // Status manquant => fichier à supprimer de SVN
                        cm.getWCClient().doDelete(status.getFile(), true, false, false);
                      }
                      if (SVNStatusType.STATUS_UNVERSIONED.equals(status.getNodeStatus())) {
                        // Status non versionné => Fichier à ajouter à SVN
                        if (status.getFile() != null && status.getFile().isDirectory()) {
                          // Paramétrage pour nouveau dossier
                          cm.getWCClient().doAdd(status.getFile(), true, true, false, SVNDepth.INFINITY, false, true);
                        } else {
                          // Paramétrage pour nouveau fichier
                          cm.getWCClient().doAdd(status.getFile(), true, false, false, SVNDepth.FILES, false, true);
                        }

                      }
                    }
                  },
                  null);
      cm.getCommitClient()
        .doCommit(new File[] {dstPath},
                  false,
                  message,
                  null,
                  null,
                  false,
                  true,
                  SVNDepth.INFINITY);

      final Duration duration = new Duration(start, new DateTime());
      console.display("completed (" + DURATION_FORMATTER.print(duration.toPeriod()) + ")", CATEGORIE);

    }
    catch (final SVNException e) {
      throw new MojoExecutionException("Impossible de se connecter au svn :" + e.getMessage(), e);
    }
  }

  /**
   * Renvoi le client avec l'authetification
   * 
   * @return Client avec l'authetification
   */
  private static SVNClientManager getClientWithAuth(final String url, final String user, final String password) {

    final Matcher matcher = urlPattern.matcher(url);
    SVNClientManager result = null;
    if (matcher.find() && matcher.groupCount() == 2) {
      result = SVNClientManager.newInstance(null, matcher.group(1), matcher.group(2));
    } else {
      result = SVNClientManager.newInstance(null, user, password);
    }
    return result;
  }

}
