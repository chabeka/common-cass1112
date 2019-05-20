package fr.urssaf.image.sae.client.vi;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

/**
 * Handler SOAP pour ajouter le Vecteur d'Identification dans l'en-tête SOAP.<br>
 * <br>
 * Ce handler peut être branché dans la phase "MessageOut".
 */
public class JaxWsVIHandler implements SOAPHandler<SOAPMessageContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(JaxWsVIHandler.class);

  private KeyStoreInterface iKeyStore = null;

  private List<String> pagms = null;

  private String issuer = null;

  private String login = null;

  /**
   * Constructeur
   *
   * @param iKeyStore
   *          keystore à utiliser
   * @param pagms
   *          liste des pagms
   * @param issuer
   *          issuer
   * @param login
   *          login de l'utilisateur demandeur du service
   */
  public JaxWsVIHandler(final KeyStoreInterface iKeyStore, final List<String> pagms,
                        final String issuer, final String login) {
    super();
    this.iKeyStore = iKeyStore;
    this.issuer = issuer;
    this.pagms = pagms;
    this.login = login;

    LOGGER.debug("Instanciation d'un VIHandler avec les paramètres suivants : issuer={}, login={}, pagms={}, iKeyStore={}",
                 issuer,
                 login,
                 pagms,
                 iKeyStore);
  }

  /*
   * (non-Javadoc)
   * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public boolean handleMessage(final SOAPMessageContext msgCtx) {

    // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource
    // XML
    try {

      // Génération de l'en-tête wsse
      final Document doc = VIGenerator.getWsseHeader(issuer, login, pagms, iKeyStore.getKeystore(), iKeyStore.getAlias(), iKeyStore.getPassword());

      // Ajout du message dans le header
      final SOAPMessage soapMsg = msgCtx.getMessage();
      final SOAPPart soapPart = soapMsg.getSOAPPart();
      final SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
      SOAPHeader soapHeader = soapEnvelope.getHeader();
      if (soapHeader == null) {
        soapHeader = soapEnvelope.addHeader();
      }
      addXMLDocumentToSoapHeader(doc, soapMsg);

    }
    catch (final Exception e) {
      throw new ViSignatureException(e);
    }
    return true;

  }

  /*
   * (non-Javadoc)
   * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public void close(final MessageContext arg0) {
    // do nothing
  }

  /*
   * (non-Javadoc)
   * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public boolean handleFault(final SOAPMessageContext arg0) {
    // Return true to continue processing
    return true;
  }

  /*
   * (non-Javadoc)
   * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
   */
  @Override
  public Set<QName> getHeaders() {
    // Pas de traitement spécifique sur les headers
    return null;
  }

  /*
   * Permet d'ajouter un fragment XML dans les entêtes SOAP
   */
  private static void addXMLDocumentToSoapHeader(final Document document, final SOAPMessage soapMessage) throws SOAPException {
    final DocumentFragment docFrag = document.createDocumentFragment();
    final Element rootElement = document.getDocumentElement();
    if (rootElement != null) {
      docFrag.appendChild(rootElement);
      final Document ownerDoc = soapMessage.getSOAPHeader().getOwnerDocument();
      final org.w3c.dom.Node replacingNode = ownerDoc.importNode(docFrag, true);
      soapMessage.getSOAPHeader().appendChild(replacingNode);
    }
  }

}
