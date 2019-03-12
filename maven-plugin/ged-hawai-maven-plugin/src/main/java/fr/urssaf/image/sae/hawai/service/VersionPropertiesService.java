package fr.urssaf.image.sae.hawai.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

import fr.urssaf.image.sae.hawai.utils.ConsoleService;

public class VersionPropertiesService {

  private final String projectName;

  private final ConsoleService console;

  public VersionPropertiesService(final String projectName, final ConsoleService console) {
    super();
    this.projectName = projectName;
    this.console = console;
  }

  public Map<String, String> updateProjectVersions(final File workspaceCheckoutDir)
      throws MojoExecutionException {
    final Map<String, String> propertiesVersionMap = new HashMap<>();
    if (console != null) {
      console.display("mise a jour du fichier version.properties...");
    }
    final String versionFile = workspaceCheckoutDir.getAbsolutePath() + File.separator + "version.properties";
    try {
      final Configuration conf = new PropertiesConfiguration(versionFile);
      final String currentProjectVersion = conf.getString("version.projet_" + projectName);
      if (console != null) {
        console.display("version.projet_ged=" + currentProjectVersion);
      }
      final String saeVersion = conf.getString("version." + projectName);
      propertiesVersionMap.put("sae.version", saeVersion);
      if (console != null) {
        console.display("version.ged=" + saeVersion);
      }
      final String dfceVersion = conf.getString("version.dfce-webapp");
      propertiesVersionMap.put("dfce-webapp.version", dfceVersion);
      if (console != null) {
        console.display("version.dfce-webapp=" + dfceVersion);
      }
      final String anaisVersion = conf.getString("version.sae-anais-portail");
      propertiesVersionMap.put("sae-anais-portail.version", anaisVersion);
      if (console != null) {
        console.display("version.sae-anais-portail=" + anaisVersion);
      }
      final String ihmWebExploitVersion = conf.getString("version.sae-ihm-web-exploit");
      propertiesVersionMap.put("sae-ihm-web-exploit.version", ihmWebExploitVersion);
      if (console != null) {
        console.display("version.sae-ihm-web-exploit=" + ihmWebExploitVersion);
      }

    }
    catch (final ConfigurationException e) {
      throw new MojoExecutionException("Impossible de parser le fichier " + versionFile + " : " + e.getMessage(),
                                       e);
    }

    return propertiesVersionMap;

  }

}
