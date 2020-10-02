package fr.urssaf.image.sae.webservices.handler;

import java.io.StringWriter;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler permettant d'intercepter le message SOAP<br>
 * de requête pour la consommation d'un Webservice.
 */
public class LogHandler extends AbstractHandler {

  /**
   * LOGGER
   */
  private static final Logger LOG = LoggerFactory.getLogger(LogHandler.class);

  @Override
  public InvocationResponse invoke(final MessageContext msgContext) throws AxisFault {
    logServiceInformation("Begin", msgContext);
    final StringWriter sWriter = new StringWriter();
    sWriter.append(Long.toString(System.currentTimeMillis()));
    msgContext.setProperty("soapRequestTimeBegin", sWriter);

    return InvocationResponse.CONTINUE;
  }

  @Override
  public void flowComplete(final MessageContext msgContext) {
    super.flowComplete(msgContext);
    final StringWriter sWriter = (StringWriter) msgContext.getProperty("soapRequestTimeBegin");
    // Durée maximum d'une requete SOAP en seconde
    final int dureeMaxRequeteSoap = (int) msgContext.getProperty("soapRequestMaxTime");
    final long timeBegin = Long.valueOf(sWriter.toString());
    final long timeEnd = System.currentTimeMillis();
    final long diff = timeEnd - timeBegin;
    if (diff / 1000 >= dureeMaxRequeteSoap) {
      logRequestInformation(msgContext, dureeMaxRequeteSoap);
    }

    logServiceInformation("End [" + Long.toString(diff) + "ms]", msgContext);
  }

  /**
   * Log les information de la requete SOAP
   * 
   * @param msgContext Context SOAP
   * @param dureeMaxRequeteSoap Durée maximum d'une requete SOAP en seconde
   */
  private void logRequestInformation(final MessageContext msgContext, final int dureeMaxRequeteSoap) {
    final String messageContextID = msgContext.getLogCorrelationID();
    LOG.warn("MessageContextID::" + messageContextID + " - Temps d'exécution de la requête est supérieur à " + dureeMaxRequeteSoap + " secondes");
    if (msgContext.getEnvelope() != null && msgContext.getEnvelope().getBody() != null) {
      LOG.warn("MessageContextID::" + messageContextID + " - Request : " + msgContext.getEnvelope().getBody());
    }
  }

  @SuppressWarnings("static-access")
  private void logServiceInformation(final String prefix, final MessageContext msgContext) {
    LOG.info(prefix + " - messageContextID::"
        + msgContext.getLogCorrelationID());
  }

}
