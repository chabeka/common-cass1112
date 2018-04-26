package fr.urssaf.image.sae.anais.framework.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Résultat d'une authentification dans ANAIS. Contient :<br>
 * <ul>
 * <li>des informations sur l'agent (nom/prénom)</li>
 * <li>ses habilitations</li>
 * </ul>
 */
public class SaeAnaisAuth {

   private String nom;
   private String prenom;
   private List<SaeAnaisAuthHabilitation> habilitations = new ArrayList<SaeAnaisAuthHabilitation>();

   /**
    * Le nom de famille de l'agent
    * 
    * @return Le nom de famille de l'agent
    */
   public final String getNom() {
      return nom;
   }

   /**
    * Le nom de famille de l'agent
    * 
    * @param nom
    *           Le nom de famille de l'agent
    */
   public final void setNom(String nom) {
      this.nom = nom;
   }

   /**
    * Le prénom de l'agent
    * 
    * @return Le prénom de l'agent
    */
   public final String getPrenom() {
      return prenom;
   }

   /**
    * Le prénom de l'agent
    * 
    * @param prenom
    *           Le prénom de l'agent
    */
   public final void setPrenom(String prenom) {
      this.prenom = prenom;
   }

   /**
    * La liste des habilitations de l'agent
    * 
    * @return La liste des habilitations de l'agent
    */
   public final List<SaeAnaisAuthHabilitation> getHabilitations() {
      return habilitations;
   }

   /**
    * La liste des habilitations de l'agent
    * 
    * @param habilitations
    *           La liste des habilitations de l'agent
    */
   public final void setHabilitations(
         List<SaeAnaisAuthHabilitation> habilitations) {
      this.habilitations = habilitations;
   }

}
