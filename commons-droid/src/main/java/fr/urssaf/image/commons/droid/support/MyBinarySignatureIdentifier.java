package fr.urssaf.image.commons.droid.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.core.IdentificationRequestByteReaderAdapter;
import uk.gov.nationalarchives.droid.core.SignatureFileParser;
import uk.gov.nationalarchives.droid.core.SignatureParseException;
import uk.gov.nationalarchives.droid.core.interfaces.DroidCore;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationMethod;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResult;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResultCollection;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResultImpl;
import uk.gov.nationalarchives.droid.core.signature.ByteReader;
import uk.gov.nationalarchives.droid.core.signature.FileFormat;
import uk.gov.nationalarchives.droid.core.signature.FileFormatCollection;
import uk.gov.nationalarchives.droid.core.signature.FileFormatHit;
import uk.gov.nationalarchives.droid.core.signature.droid6.FFSignatureFile;
import uk.gov.nationalarchives.droid.core.signature.xml.SAXModelBuilder;

/**
 * Classe dérivée de la classe DROID BinarySignatureIdentifier, permettant le
 * chargement du fichier des signatures non plus obligatoirement à partir d'un
 * fichier physique et de son chemin complet, mais à partir d'un objet Resource
 * Spring.<br>
 * <br>
 * Cela permettant par exemple de charger un fichier de signatures depuis les
 * ressources du JAR.
 */
public class MyBinarySignatureIdentifier implements DroidCore {

  private FFSignatureFile sigFile;

  private Resource signatureFile;

  /**
   * Constructeur
   * 
   * @param signatures
   *          l'objet Resource pointant sur les signatures binaires DROID
   */
  public MyBinarySignatureIdentifier(final Resource signatures) {
    signatureFile = signatures;
  }

  /**
   * Initialises this droid core with its signature file.
   * 
   * @throws SignatureParseException
   *           When a signature could not be parsed
   */
  public void init() throws SignatureParseException {
    sigFile = parseSigFile();
    sigFile.prepareForUse();
  }

  /**
   * Sets the signature file.
   * 
   * @param signatureFile
   *          the signature file to set
   */
  @Override
  public void setSignatureFile(final String signatureFile) {
    this.signatureFile = new InputStreamResource(getClass().getClassLoader().getResourceAsStream(signatureFile));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IdentificationResultCollection matchBinarySignatures(final IdentificationRequest request) {
    // BNO: Called once for each identification request
    final IdentificationResultCollection results = new IdentificationResultCollection(request);
    results.setRequestMetaData(request.getRequestMetaData());
    final ByteReader byteReader = new IdentificationRequestByteReaderAdapter(request);
    sigFile.runFileIdentification(byteReader);
    final int numHits = byteReader.getNumHits();
    for (int i = 0; i < numHits; i++) {
      final FileFormatHit hit = byteReader.getHit(i);
      final IdentificationResultImpl result = new IdentificationResultImpl();
      result.setMimeType(hit.getMimeType());
      result.setName(hit.getFileFormatName());
      result.setVersion(hit.getFileFormatVersion());
      result.setPuid(hit.getFileFormatPUID());
      result.setMethod(IdentificationMethod.BINARY_SIGNATURE);
      results.addResult(result);
    }
    results.setFileLength(request.size());
    results.setRequestMetaData(request.getRequestMetaData());
    return results;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IdentificationResultCollection matchExtensions(final IdentificationRequest request, final boolean allExtensions) {
    final IdentificationResultCollection results = new IdentificationResultCollection(request);
    results.setRequestMetaData(request.getRequestMetaData());
    final String fileExtension = request.getExtension();
    if (fileExtension != null && !fileExtension.isEmpty()) {
      List<FileFormat> fileFormats;
      if (allExtensions) {
        fileFormats = sigFile.getFileFormatsForExtension(fileExtension);
      } else {
        fileFormats = sigFile.getTentativeFormatsForExtension(fileExtension);
      }
      if (fileFormats != null) {
        final int numFormats = fileFormats.size();
        for (int i = 0; i < numFormats; i++) {
          final FileFormat format = fileFormats.get(i);
          final IdentificationResultImpl result = new IdentificationResultImpl();
          result.setName(format.getName());
          result.setVersion(format.getVersion());
          result.setPuid(format.getPUID());
          result.setMimeType(format.getMimeType());
          result.setMethod(IdentificationMethod.EXTENSION);
          results.addResult(result);
        }
      }
    }
    results.setFileLength(request.size());
    results.setRequestMetaData(request.getRequestMetaData());
    return results;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSignatureForPuid(final String puid) {
    sigFile.puidHasOverridingSignatures(puid);
  }

  /**
   * @return the sigFile
   */
  FFSignatureFile getSigFile() {
    return sigFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxBytesToScan(final long maxBytes) {
    sigFile.setMaxBytesToScan(maxBytes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeLowerPriorityHits(final IdentificationResultCollection results) {
    // Build a set of format ids the results have priority over:
    final FileFormatCollection allFormats = sigFile.getFileFormatCollection();
    final Set<Integer> lowerPriorityIDs = new HashSet<>();
    final List<IdentificationResult> theResults = results.getResults();
    int numResults = theResults.size();
    for (int i = 0; i < numResults; i++) {
      final IdentificationResult result = theResults.get(i);
      final String resultPUID = result.getPuid();
      final FileFormat format = allFormats.getFormatForPUID(resultPUID);
      lowerPriorityIDs.addAll(format.getFormatIdsHasPriorityOver());
    }

    // If a result has an id in this set, add it to the remove list;
    final List<IdentificationResult> lowerPriorityResults = new ArrayList<>();
    for (int i = 0; i < numResults; i++) {
      final IdentificationResult result = theResults.get(i);
      final String resultPUID = result.getPuid();
      final FileFormat format = allFormats.getFormatForPUID(resultPUID);
      if (lowerPriorityIDs.contains(format.getID())) {
        lowerPriorityResults.add(result);
      }
    }

    // Now remove any lower priority results from the collection:
    numResults = lowerPriorityResults.size();
    for (int i = 0; i < numResults; i++) {
      final IdentificationResult result = lowerPriorityResults.get(i);
      results.removeResult(result);
    }
  }

  /**
   * If there is no extension, then issue a mismatch warning if
   * any of the file formats have an extension defined.
   * If there is an extension, then issue a mismatch warning if
   * any of the result formats do not match the given extension,
   * If there are no identified file formats at all, then do not
   * issue a format mismatch warning no matter what the extension.
   * {@inheritDoc}
   */
  @Override
  public void checkForExtensionsMismatches(final IdentificationResultCollection results, final String fileExtension) {
    if (fileExtension == null || fileExtension.isEmpty()) {
      final FileFormatCollection allFormats = sigFile.getFileFormatCollection();
      final List<IdentificationResult> theResults = results.getResults();
      // garbage reduction: use indexed loop instead of allocating iterator.
      final int numResults = theResults.size();
      for (int i = 0; i < numResults; i++) {
        final IdentificationResult result = theResults.get(i);
        final String resultPUID = result.getPuid();
        final FileFormat format = allFormats.getFormatForPUID(resultPUID);
        if (format.getNumExtensions() > 0) {
          results.setExtensionMismatch(true);
          break;
        }
      }
    } else {
      final FileFormatCollection allFormats = sigFile.getFileFormatCollection();
      final List<IdentificationResult> theResults = results.getResults();
      // garbage reduction: use indexed loop instead of allocating iterator.
      final int numResults = theResults.size();
      for (int i = 0; i < numResults; i++) {
        final IdentificationResult result = theResults.get(i);
        final String resultPUID = result.getPuid();
        final FileFormat format = allFormats.getFormatForPUID(resultPUID);
        if (format.hasExtensionMismatch(fileExtension)) {
          results.setExtensionMismatch(true);
          break;
        }
      }
    }
  }

  /**
   * Create a new signature file object based on a signature file.
   *
   * @param theFileName
   *          the file name
   * @return sig file
   * @throws SignatureParseException
   *           if there is a problem parsing the signature file.
   */
  FFSignatureFile parseSigFile() throws SignatureParseException {

    final SAXModelBuilder mb = new SAXModelBuilder();
    final XMLReader parser = getXMLReader(mb);

    // read in the XML file
    try {
      final BufferedReader in = new BufferedReader(new InputStreamReader(signatureFile.getInputStream(), "UTF-8"));
      parser.parse(new InputSource(in));
      in.close();
    } catch (final IOException e) {
      throw new SignatureParseException(e.getMessage(), e);
    } catch (final SAXException e) {
      throw new SignatureParseException(e.getMessage(), e);
    }
    return (FFSignatureFile) mb.getModel();
  }

  /**
   * Create the XML parser for the signature file.
   *
   * @param mb
   *          sax builder
   * @return XMLReader
   * @throws SignatureParseException
   *           on error
   */
  private XMLReader getXMLReader(final SAXModelBuilder mb) throws SignatureParseException {

    final SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(true);
    SAXParser saxParser;
    try {
      saxParser = factory.newSAXParser();
      final XMLReader parser = saxParser.getXMLReader();
      mb.setupNamespace(SignatureFileParser.SIGNATURE_FILE_NS, true);
      parser.setContentHandler(mb);
      return parser;
    } catch (final ParserConfigurationException e) {
      throw new SignatureParseException(e.getMessage(), e);
    } catch (final SAXException e) {
      throw new SignatureParseException(e.getMessage(), e);
    }
  }

}
