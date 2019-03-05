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
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;


/**
 * Tache Ant SVN.<br />
 * 
 * @author mcarpentier
 * 
 */
public class SVN {

    private static PeriodFormatter DURATION_FORMATTER = new PeriodFormatterBuilder().appendMinutes().appendSuffix("m")
            .appendSeconds().appendSuffix("s").toFormatter();

    /**
     * pattern for url including login AND password
     */
    private final static Pattern urlPattern = Pattern.compile("http://(.*)?:(.*)?@.*");

    private final static String CATEGORIE = "svn";

    public static void checkout(File dstPath, String url, String user, String password, ConsoleService console)
            throws MojoExecutionException {
        try {
            SVNURL svnUrl = SVNURL.parseURIEncoded(url);
            DAVRepositoryFactory.setup();
            // get and check auth
            SVNClientManager cm = getClientWithAuth(url, user, password);
            console.display("checking out " + svnUrl.getProtocol() + "://" + svnUrl.getHost() + ":" + svnUrl.getPort()
                    + svnUrl.getPath() + " ...", CATEGORIE);

            SVNUpdateClient uc = cm.getUpdateClient();
            DateTime start = new DateTime();
            uc.doCheckout(svnUrl, dstPath, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, false);
            Duration duration = new Duration(start, new DateTime());
            console.display("completed (" + DURATION_FORMATTER.print(duration.toPeriod()) + ")", CATEGORIE);

        } catch (SVNException e) {
            throw new MojoExecutionException("Impossible de se connecter au svn :" + e.getMessage(), e);
        }
    }

    public static void commit(File dstPath, String url, String message, String user, String password,
            ConsoleService console) throws MojoExecutionException {

        try {
            DAVRepositoryFactory.setup();
            // get and check auth
            final SVNClientManager cm = getClientWithAuth(url, user, password);
            console.display("sending content ...", CATEGORIE);
            SVNCommitClient sc = cm.getCommitClient();
            DateTime start = new DateTime();

            // http://stackoverflow.com/questions/12297516/svnkit-get-modifications-to-commit
            cm.getStatusClient().doStatus(dstPath, SVNRevision.HEAD, SVNDepth.INFINITY, false, false, false, false,
                    new ISVNStatusHandler() {
                        @Override
                        public void handleStatus(SVNStatus status) throws SVNException {
                            if (SVNStatusType.STATUS_MISSING.equals(status.getNodeStatus())) {
                                cm.getWCClient().doDelete(status.getFile(), true, false, false);
                            }
                        }
                    }, null);
            cm.getCommitClient().doCommit(new File[] { dstPath }, false, message, null, null, false, true,
                    SVNDepth.INFINITY);

            Duration duration = new Duration(start, new DateTime());
            console.display("completed (" + DURATION_FORMATTER.print(duration.toPeriod()) + ")", CATEGORIE);

        } catch (SVNException e) {
            throw new MojoExecutionException("Impossible de se connecter au svn :" + e.getMessage(), e);
        }
    }

    /**
     * @return
     */
    private static SVNClientManager getClientWithAuth(String url, String user, String password) {

        Matcher matcher = urlPattern.matcher(url);
        SVNClientManager result = null;
        if (matcher.find() && matcher.groupCount() == 2) {
            result = SVNClientManager.newInstance(null, matcher.group(1), matcher.group(2));
        } else {
            result = SVNClientManager.newInstance(null, user, password);
        }
        return result;
    }


}
