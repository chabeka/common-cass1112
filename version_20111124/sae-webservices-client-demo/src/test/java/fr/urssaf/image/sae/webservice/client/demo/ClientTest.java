package fr.urssaf.image.sae.webservice.client.demo;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class ClientTest {

   private static final Logger LOG = Logger.getLogger(ClientTest.class);

   
   /**
    * Test unitaire de {@link fr.urssaf.image.sae.webservice.client.demo.Client#main(String[])}<br>
    * <br>
    * Cas de test : on fournit tous les arguments nécessaires pour la consommation
    * du service ping<br>
    * <br>
    * Résultat attendu : aucune exception levée<br>
    * <br>
    * NB : ce test <b>ne vérifie pas</b> que la consommation du ping renvoie le bon résultat.
    * 
    */
   @Test
   public void main_ping_arguments_ok() {

      LOG.debug("ping");
      Client.main(new String[] { "ping" });
   }

   
   /**
    * Test unitaire de {@link fr.urssaf.image.sae.webservice.client.demo.Client#main(String[])}<br>
    * <br>
    * Cas de test : on fournit tous les arguments nécessaires pour la consommation
    * du service pingSecure<br>
    * <br>
    * Résultat attendu : aucune exception levée<br>
    * <br>
    * NB : ce test <b>ne vérifie pas</b> que la consommation du pingSecure renvoie le bon résultat.
    * 
    */
   @Test
   public void main_pingSecure_arguments_ok() {

      LOG.debug("ping secure avec ROLE TOUS");
      Client.main(new String[] { "ping_secure", "ROLE_TOUS;FULL" });
   }


   /**
    * Test unitaire de {@link fr.urssaf.image.sae.webservice.client.demo.Client#main(String[])}<br>
    * <br>
    * Cas de test : on demande la consommation du pingSecure sans fournir le droit SAE<br>
    * <br>
    * Résultat attendu : levée d'une exception de type {@link java.lang.IllegalArgumentException} 
    */
   @Test
   public void main_droit_requis_pour_pingSecure() {

      try {
         Client.main(new String[] { "ping_secure" });
      } catch (IllegalArgumentException e) {

         assertEquals("role is required", e.getMessage());
      }
   }

   
   /**
    * Test unitaire de {@link fr.urssaf.image.sae.webservice.client.demo.Client#main(String[])}<br>
    * <br>
    * Cas de test : on demande la consommation d'un service web qui n'est pas prévu par ce
    * programme de démonstration<br>
    * <br>
    * Résultat attendu : levée d'une exception de type {@link java.lang.IllegalArgumentException} 
    */
   @Test
   public void main_action_unknown() {

      String action = "unknown";

      try {
         Client.main(new String[] { action });
      } catch (IllegalArgumentException e) {

         assertEquals("Unknown action defined: " + action, e.getMessage());
      }
   }

   
   /**
    * Test unitaire de {@link fr.urssaf.image.sae.webservice.client.demo.Client#main(String[])}<br>
    * <br>
    * Cas de test : on appelle le main sans fournir d'argument à la ligne de commande<br>
    * <br>
    * Résultat attendu : levée d'une exception de type {@link java.lang.IllegalArgumentException} 
    */
   @Test
   public void main_args_empty() {

      args_empty(null);
      args_empty(ArrayUtils.EMPTY_STRING_ARRAY);

   }

   private void args_empty(String[] args) {

      try {
         Client.main(args);
      } catch (IllegalArgumentException e) {

         assertEquals("action required", e.getMessage());
      }
   }
}
