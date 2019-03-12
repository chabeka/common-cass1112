/**
 *  TODO (AC75094891) Description du fichier
 */
package fr.urssaf.image.sae.hawai.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * TODO (AC75094891) Description du type
 */
public class CommonsUtils {

  /**
   * on devine l'url du repo svn hawai du projet
   */
  public final static String buildSvnUrl(final String hawaiVersion, final String branchName, final String projectName) {

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

}
