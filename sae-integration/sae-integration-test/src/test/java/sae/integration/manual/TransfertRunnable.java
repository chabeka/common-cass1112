/**
 *  TODO (AC75007340) Description du fichier
 */
package sae.integration.manual;

import java.io.PrintStream;

import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;

import sae.integration.util.SoapHelper;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.TransfertRequestType;

/**
 * TODO (AC75007340) Description du type
 *
 */
public class TransfertRunnable implements Runnable {

  private final String uuid;

  private final SaeServicePortType service;

  private final PrintStream sysout;

  private final Logger LOGGER;

  public TransfertRunnable(final String uuid, final SaeServicePortType service, final PrintStream sysout, final Logger LOGGER) {
    this.uuid = uuid;
    this.service = service;
    this.sysout = sysout;
    this.LOGGER = LOGGER;
  }

  public String getUUID() {
    return uuid;
  }

  @Override
  public void run() {


    final TransfertRequestType transfertRequest = new TransfertRequestType();
    transfertRequest.setUuid(uuid);

    try {
      service.transfert(transfertRequest);
      sysout.println(uuid + ";Transfert OK;");
      System.out.println(uuid + ";Transfert OK");
    }
    catch (final SOAPFaultException e) {
      sysout.println(uuid + ";Transfert KO;" + e.getMessage());
      System.out.println(uuid + ";Transfert KO");
      LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
    }


  }

}
