package fr.urssaf.image.sae.webservices.service.factory;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.EtatTraitementsMasseResponse;
import fr.cirtil.www.saeservice.EtatTraitementsMasseResponseType;
import fr.cirtil.www.saeservice.ListeTraitementsMasseType;
import fr.cirtil.www.saeservice.TraitementMasseType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link EtatTraitementsMasseResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectEtatTraitementMasseFactory {

   private ObjectEtatTraitementMasseFactory() {

   }

   /**
    * instanciation de {@link EtatTraitementsMasseResponse}.<br>
    * Implementation de {@link EtatTraitementsMasseResponse}
    * 
    * 
    * @param listJobs
    *           Liste des traitements à retourner
    * @return instance de {@link EtatTraitementsMasseResponse}
    * @throws ParseException
    */
   public static EtatTraitementsMasseResponse createEtatTraitementsMasseResponse(
         List<JobRequest> listJobs) throws ParseException {

      Assert.notNull(listJobs, "listJob is required");

      EtatTraitementsMasseResponse response = new EtatTraitementsMasseResponse();
      EtatTraitementsMasseResponseType responseType = new EtatTraitementsMasseResponseType();

      ListeTraitementsMasseType listeTraitementsMasseType = new ListeTraitementsMasseType();

      for (JobRequest job : listJobs) {

         if (job != null) {

            TraitementMasseType traitementMasseType = new TraitementMasseType();

            traitementMasseType.setDateCreation(Utils.datetimeToString(job
                  .getCreationDate()));
            traitementMasseType.setDateDebut(Utils.datetimeToString(job
                  .getStartingDate()));
            traitementMasseType.setDateFin(Utils.datetimeToString(job
                  .getEndingDate()));
            traitementMasseType.setDateReservation(Utils.datetimeToString(job
                  .getReservationDate()));
            if (job.getState() == null) {
               // Cas des UUID inexistant dans la pile, on renvoit UNKNOWN
               // (demande de Saturne)
               traitementMasseType.setEtat("UNKNOWN");
            } else {
               traitementMasseType.setEtat(job.getState().toString());
            }
            UuidType uuid = new UuidType();
            uuid.setUuidType(job.getIdJob().toString());
            traitementMasseType.setIdJob(uuid);
            if (job.getMessage() != null) {
               traitementMasseType.setMessage(job.getMessage());
            } else {
               traitementMasseType.setMessage(StringUtils.EMPTY);
            }
            if (job.getDocCount() != null) {
               traitementMasseType.setNombreDocuments(job.getDocCount()
                     .toString());
            } else {
               traitementMasseType.setNombreDocuments(StringUtils.EMPTY);
            }

            if (job.getType() == null) {
               traitementMasseType.setType(StringUtils.EMPTY);
            } else {
               traitementMasseType.setType(job.getType());
            }

            listeTraitementsMasseType.addTraitementMasse(traitementMasseType);

         }

      }

      responseType.setTraitementsMasse(listeTraitementsMasseType);

      response.setEtatTraitementsMasseResponse(responseType);

      return response;
   }

   /**
    * instanciation de {@link EtatTraitementsMasseResponse } vide<br>
    * 
    * @return instance de {@link EtatTraitementsMasseResponse }
    */
   public static EtatTraitementsMasseResponse createEtatTraitementsMasseResponse() {

      EtatTraitementsMasseResponse response = new EtatTraitementsMasseResponse();
      EtatTraitementsMasseResponseType responseType = new EtatTraitementsMasseResponseType();
      response.setEtatTraitementsMasseResponse(responseType);

      return response;
   }

}
