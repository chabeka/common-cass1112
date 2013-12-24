package fr.urssaf.image.sae.droit.service;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;

/**
 * Interface pour la manipulation du profil de contrôle.
 * 
 */
public interface FormatControlProfilService {

   /**
    * Crée un nouveau profil de contrôle.
    * 
    * @param formatControlProfil
    *           le profil de contrôle à créer.
    */
   void addFormatControlProfil(FormatControlProfil formatControlProfil);

   /**
    * Supprimer un profil de contrôle.
    * 
    * @param codeFormatControlProfil
    *           le profil de contrôle à supprimer.
    * @throws FormatControlProfilNotFoundException
    *            : formatControlProfil inexistant
    */
   void deleteFormatControlProfil(String codeFormatControlProfil)
         throws FormatControlProfilNotFoundException;

   /**
    * Récupère les informations relatives à un profil de contrôle donné.
    * 
    * @param codeFormatControlProfil
    *           code correspondant à un un profil de contrôle. - paramètre
    *           obligatoire.
    * @return le e profil de contrôle correspondant.
    * @throws FormatControlProfilNotFoundException
    *            : formatControlProfil inexistant
    */
   FormatControlProfil getFormatControlProfil(String codeFormatControlProfil)
         throws FormatControlProfilNotFoundException;

   /**
    * Récupère tous les profils de contrôle de la base.
    * 
    * @return tous les profils de contrôle.
    */
   List<FormatControlProfil> getAllFormatControlProfil();

}
