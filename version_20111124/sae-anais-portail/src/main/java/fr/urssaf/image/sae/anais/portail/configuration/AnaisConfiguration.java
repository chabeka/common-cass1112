package fr.urssaf.image.sae.anais.portail.configuration;

import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;

/**
 * Classe de configuration pour la connexion à ANAIS<br>
 * <br>
 * La configuration s'effectue dans le fichier
 * <code>applicationContext.xml</code><br>
 * <br>
 * exemple:
 * 
 * <pre>
 * &lt;bean id="configuration"
 *       class="fr.urssaf.image.sae.anais.portail.configuration.AnaisConfiguration">
 *       &lt;property name="environnement" value="Production" />
 *       &lt;property name="compteApplicatif" value="Sae" />
 * &lt;/bean>
 * </pre>
 * 
 */
public class AnaisConfiguration {

   private SaeAnaisEnumCodesEnvironnement environnement;

   private SaeAnaisEnumCompteApplicatif compteApplicatif;

   /**
    * initialise le code environnement de ANAIS
    * 
    * @return code environnement de ANAIS
    */
   public final SaeAnaisEnumCodesEnvironnement getEnvironnement() {
      return environnement;
   }

   /**
    * 
    * @param environnement
    *           code environnement de ANAIS
    */
   public final void setEnvironnement(
         SaeAnaisEnumCodesEnvironnement environnement) {
      this.environnement = environnement;
   }

   /**
    * initialise le code de l'application
    * 
    * @return code de l'application
    */
   public final SaeAnaisEnumCompteApplicatif getCompteApplicatif() {
      return compteApplicatif;
   }

   /**
    * 
    * @param compteApplicatif
    *           code de l'applicatio
    */
   public final void setCompteApplicatif(
         SaeAnaisEnumCompteApplicatif compteApplicatif) {
      this.compteApplicatif = compteApplicatif;
   }

}
