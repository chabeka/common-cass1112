package fr.urssaf.image.sae.hawai.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Compute the project version.<br/>
 * 
 * @author fkocik
 * 
 */
public class ProjectVersion {

    /**
     * Check the version string format
     * 
     * @param version The version string
     * @return The buid number of the version string (last one)
     * @throws BuildException If the version string is not valid
     */
    protected static int getBuildNumber(String version) throws MojoExecutionException {
        int lastNumber;
        if (version.lastIndexOf('.') < 0) {
            throw new MojoExecutionException("Invalid version format: expecting at least one '.' " + "in the string");
        }
        if (version.endsWith(".")) {
            throw new MojoExecutionException("Invalid version format: the string must not ends with '.'");
        }
        try {
            lastNumber = Integer.parseInt(version.substring(version.lastIndexOf('.') + 1));
        } catch (NumberFormatException ex) {
            throw new MojoExecutionException("Invalid version format: build number ["
                    + version.substring(version.lastIndexOf('.') + 1) + "] is not an integer", ex);
        }
        if (lastNumber < 0) {
            throw new MojoExecutionException(
                    "Invalid version format: build number [" + lastNumber + "] must not be negative");
        }
        return lastNumber;
    }

    /**
     * @param current the current version string
     * @param radic the version radic
     * @return true if radic is greater than current
     */
    public static boolean checkRadicGreaterThanCurrent(String current, String radic) {
        boolean result = true;
        String[] numbersCurrent = current.split("\\.");
        String[] numbersRadic = radic.split("\\.");
        for (int i = 0; i < numbersRadic.length && result; i++) {
            int radDigit = Integer.parseInt(numbersRadic[i]);
            if (i < numbersCurrent.length) {
                int currDigit = Integer.parseInt(numbersCurrent[i]);
                if (radDigit < currDigit) {
                    result = false;
                } else if (radDigit > currDigit) {
                    return true;
                }
            }
        }
        if (result && (numbersRadic.length < numbersCurrent.length)) {
            for (int i = numbersRadic.length; i < numbersCurrent.length; i++) {
                int currDigit = Integer.parseInt(numbersCurrent[i]);
                result = result && (currDigit == 0);
            }
        }
        return result;
    }

    public static String getProjectVersion(String current, String radic, ConsoleService console)
            throws MojoExecutionException {
        String result = "";
        if (current.contains("-SNAPSHOT")) {
            current = current.replace("-SNAPSHOT", "");
        }
        int buildNumber = getBuildNumber(current);
        if (StringUtils.isBlank(radic)) {
            buildNumber++;
            if (console != null) {
                console.debug("No application version: updating build number to " + buildNumber);
            }
            result = current.substring(0, current.lastIndexOf('.')) + "." + buildNumber;
        } else {
            if (radic.contains("-SNAPSHOT")) {
                radic = radic.replace("-SNAPSHOT", "");
            }
            getBuildNumber(radic); // Check radic integrity
            if (console != null) {
                console.debug("Application version: " + radic);
            }
            if (current.startsWith(radic)) {
                buildNumber++;
                if (console != null) {
                    console.debug("Same version: updating build number to " + buildNumber);
                }
            } else {
                if (checkRadicGreaterThanCurrent(current, radic)) {
                    buildNumber = 0;
                    if (console != null) {
                        console.debug("New version: " + radic + ".0");
                    }
                } else {
                    throw new MojoExecutionException("Invalid version : specified version (" + radic
                            + ") must be greater than current version (" + current + ")");
                }
            }
            result = radic + "." + buildNumber;
        }
        return result;
    }


}
