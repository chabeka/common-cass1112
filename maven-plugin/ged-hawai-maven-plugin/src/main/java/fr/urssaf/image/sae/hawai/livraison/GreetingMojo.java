package fr.urssaf.image.sae.hawai.livraison;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * (AC75094891) Description du type
 *
 */
/**
 * Says "Hi" to the user.
 */
@Mojo(name = "sayhi")
public class GreetingMojo extends AbstractMojo {

  /**
   * The greeting to display.
   */
  @Parameter(property = "sayhi.greeting", defaultValue = "Hello World! ${project.version}")
  private String greeting;

  /**
   * My boolean.
   */
  @Parameter
  private boolean myBoolean;

  /**
   * My Integer.
   */
  @Parameter
  private Integer myInteger;

  @Override
  public void execute() throws MojoExecutionException {
    getLog().info("Hello, world." + greeting);
  }

  /**
   * @param greeting
   *          the greeting to set
   */
  public void setGreeting(final String greeting) {
    this.greeting = greeting;
  }

  /**
   * @param myBoolean
   *          the myBoolean to set
   */
  public void setMyBoolean(final boolean myBoolean) {
    this.myBoolean = myBoolean;
  }

  /**
   * @param myInteger
   *          the myInteger to set
   */
  public void setMyInteger(final Integer myInteger) {
    this.myInteger = myInteger;
  }
}
