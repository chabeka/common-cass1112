package fr.urssaf.image.parser_opencsv.application.service;

/**
 * Service de lancement de la capture de masse
 */
public interface ICaptureMasseService {

   /**
    * @param urlEcde
    */
   void lancerCaptureMasseSansHash(final String urlEcde);

   /**
    * Creation d'une capture de masse dans la pile des travaux
    * 
    * @param urlEcde
    * @param hash
    * @return
    */
   public String lancerCaptureMasseAvecHash(final String urlEcde, final String hash);

   public String lancerSuppressionMasse(String luceneRequest);

   /**
    * Reserver un job dans la pile des travaux
    * 
    * @param idJob
    * @throws UnknownHostException
    * @throws JobDejaReserveException
    * @throws JobInexistantException
    * @throws LockTimeoutException
    */
   /*
    * public void reserverJob(final UUID idJob) throws UnknownHostException, JobDejaReserveException, JobInexistantException, LockTimeoutException;
    * public void lancerJob(final UUID idJob) throws JobInexistantException;
    */
}
