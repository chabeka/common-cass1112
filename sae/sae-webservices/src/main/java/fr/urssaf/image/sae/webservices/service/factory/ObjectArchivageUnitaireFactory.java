package fr.urssaf.image.sae.webservices.service.factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponseType;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponseType;
import fr.cirtil.www.saeservice.StockageUnitaireResponse;
import fr.cirtil.www.saeservice.StockageUnitaireResponseType;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link ArchivageUnitaireResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectArchivageUnitaireFactory {
  private static final Logger LOG = LoggerFactory
      .getLogger(ObjectArchivageUnitaireFactory.class);

  private ObjectArchivageUnitaireFactory() {

  }

  /**
   * instanciation de {@link ArchivageUnitaireResponse}.<br>
   * Implementation de {@link ArchivageUnitaireResponseType}
   * 
   * <pre>
   * &lt;xsd:complexType name="archivageUnitaireResponseType">
   *    ...     
   *    &lt;xsd:sequence>
   *       &lt;xsd:element name="idArchive" type="sae:uuidType">
   *       ...      
   *       &lt;/xsd:element>
   *    &lt;/xsd:sequence>
   * &lt;/xsd:complexType>
   * </pre>
   * 
   * @param idArchive
   *           valeur de <code>uuidType</code>
   * @return instance de {@link ArchivageUnitaireResponse}
   */
  public static ArchivageUnitaireResponse createArchivageUnitaireResponse(
                                                                          final UUID idArchive) {
    final String prefixeTrc = "createArchivageUnitaireResponse()";
    LOG.debug("{} - Début", prefixeTrc);
    final ArchivageUnitaireResponse response = createArchivageUnitaireResponse();
    final ArchivageUnitaireResponseType responseType = response
        .getArchivageUnitaireResponse();

    responseType.setIdArchive(ObjectTypeFactory.createUuidType(idArchive));
    LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, response
              .getArchivageUnitaireResponse().getIdArchive());
    LOG.debug("{} - Sortie", prefixeTrc);
    // Fin des traces debug - sortie méthode
    return response;
  }

  /**
   * instanciation de {@link ArchivageUnitaireResponse} vide.<br>
   * 
   * @return instance de {@link ArchivageUnitaireResponse}
   */
  private static ArchivageUnitaireResponse createArchivageUnitaireResponse() {

    final ArchivageUnitaireResponse response = new ArchivageUnitaireResponse();
    final ArchivageUnitaireResponseType responseType = new ArchivageUnitaireResponseType();
    response.setArchivageUnitaireResponse(responseType);

    return response;
  }



  /**
   * instanciation de {@link ArchivageUnitairePJResponse}.<br>
   * Implementation de {@link ArchivageUnitairePJResponseType}
   * 
   * <pre>
   * &lt;xsd:complexType name="archivageUnitairePJResponseType">
   *    ...     
   *    &lt;xsd:sequence>
   *       &lt;xsd:element name="idArchive" type="sae:uuidType">
   *       ...      
   *       &lt;/xsd:element>
   *    &lt;/xsd:sequence>
   * &lt;/xsd:complexType>
   * </pre>
   * 
   * @param idArchive
   *           valeur de <code>uuidType</code>
   * @return instance de {@link ArchivageUnitaireResponse}
   */
  public static ArchivageUnitairePJResponse createArchivageUnitairePJResponse(
                                                                              final UUID idArchive) {

    final String prefixeTrc = "createArchivageUnitairePJResponse()";
    LOG.debug("{} - Début", prefixeTrc);

    final ArchivageUnitairePJResponse response = createArchivageUnitairePJResponse();
    final ArchivageUnitairePJResponseType responseType = response
        .getArchivageUnitairePJResponse();

    responseType.setIdArchive(ObjectTypeFactory.createUuidType(idArchive));
    LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, response
              .getArchivageUnitairePJResponse().getIdArchive());
    LOG.debug("{} - Sortie", prefixeTrc);
    // Fin des traces debug - sortie méthode
    return response;
  }

  /**
   * instanciation de {@link ArchivageUnitairePJResponse} vide.<br>
   * 
   * @return instance de {@link ArchivageUnitairePJResponse}
   */
  private static ArchivageUnitairePJResponse createArchivageUnitairePJResponse() {

    final ArchivageUnitairePJResponse response = new ArchivageUnitairePJResponse();
    final ArchivageUnitairePJResponseType responseType = new ArchivageUnitairePJResponseType();
    response.setArchivageUnitairePJResponse(responseType);

    return response;
  }


  /**
   * instanciation de {@link ArchivageUnitairePJResponse}.<br>
   * Implementation de {@link ArchivageUnitairePJResponseType}
   * 
   * <pre>
   * &lt;xsd:complexType name="archivageUnitairePJResponseType">
   *    ...     
   *    &lt;xsd:sequence>
   *       &lt;xsd:element name="idArchive" type="sae:uuidType">
   *       ...      
   *       &lt;/xsd:element>
   *    &lt;/xsd:sequence>
   * &lt;/xsd:complexType>
   * </pre>
   * 
   * @param idArchive
   *           valeur de <code>uuidType</code>
   * @return instance de {@link ArchivageUnitaireResponse}
   */
  public static StockageUnitaireResponse createStockageUnitaireResponse(
                                                                        final UUID idArchive) {

    final String prefixeTrc = "createStockageUnitaireResponse()";
    LOG.debug("{} - Début", prefixeTrc);

    final StockageUnitaireResponse response = createStockageUnitaireResponse();

    final StockageUnitaireResponseType responseType = response.getStockageUnitaireResponse();

    responseType.setIdGed(ObjectTypeFactory.createUuidType(idArchive));
    LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, response.getStockageUnitaireResponse().getIdGed());
    LOG.debug("{} - Sortie", prefixeTrc);
    // Fin des traces debug - sortie méthode
    return response;
  }

  /**
   * instanciation de {@link StockageUnitaireResponse} vide.<br>
   * 
   * @return instance de {@link StockageUnitaireResponse}
   */
  private static StockageUnitaireResponse createStockageUnitaireResponse() {

    final StockageUnitaireResponse response = new StockageUnitaireResponse();
    final StockageUnitaireResponseType responseType = new StockageUnitaireResponseType();
    response.setStockageUnitaireResponse(responseType);

    return response;
  }

}
