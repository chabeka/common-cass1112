package fr.urssaf.image.sae.services.enrichment.dao;

import java.util.Map;

import fr.urssaf.image.sae.services.enrichment.xml.model.TypeDocument;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Service pour la manipulation du référentiel des codes RND.
 * 
 *@author rhofir.
 */
public interface RNDReferenceDAO {
   /**
    * Récupère l’ensemble des codes RND.
    * 
    * @return La liste des métadonnées du référentiel des codes RND.
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    */
   Map<String, TypeDocument> getAllRndCodes() throws ReferentialRndException;

   /**
    * Récupère le code activité à partir du code RND.
    * 
    * @param codeRnd
    *           Code RND
    * @return Code activité
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    */
   String getActivityCodeByRnd(String codeRnd) throws ReferentialRndException,
         UnknownCodeRndEx;

   /**
    * Récupère le code fonction à partir du code RND.
    * 
    * @param codeRnd
    *           Code RND
    * @return Code fonction
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    */
   String getFonctionCodeByRnd(String codeRnd) throws ReferentialRndException,
         UnknownCodeRndEx;

   /**
    * Récupère la durée de conservation à partir du code RND.
    * 
    * @param codeRnd
    *           Code RND
    * @return Durée de conservation.
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    */
   int getStorageDurationByRnd(String codeRnd) throws ReferentialRndException,
         UnknownCodeRndEx;

   /**
    * Récupère un objet avec l'ensemble des valeurs :
    * <ul>
    * <br>
    * <li>CodeActivite</li>
    * <li>CodeFonction</li><br>
    * <li>LibelleRND</li><br>
    * <li>DureeConservation</li><br>
    * <li>VersionRND</li><br>
    * <ul>
    * <br>
    * 
    * @param codeRnd
    *           Code RND
    * @return {@link TypeDocument}
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    */
   TypeDocument getTypeDocument(String codeRnd) throws ReferentialRndException,
         UnknownCodeRndEx;

}
