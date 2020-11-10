package fr.urssaf.image.rsmed.utils;

import fr.urssaf.image.rsmed.bean.xsd.generated.DocumentType;
import fr.urssaf.image.rsmed.bean.xsd.generated.ListeMetadonneeType;
import fr.urssaf.image.rsmed.bean.xsd.generated.MetadonneeType;
import fr.urssaf.image.rsmed.constantes.FileConst;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe d'écriture d'un fichier Sommaire
 */
public class SommaireWriterUtils {

    private OutputStream outputStream;

    private XMLEventWriter writer;

    private StaxWriteUtils writerUtils;

    private String pathFile;

    private static final String PREFIX_SOM = "som";

    private static final String NAMESPACE_SOM = "http://www.cirtil.fr/sae/sommaireXml";

    private static final String NAMESPACE_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    private static final String PREFIX_SOMRES = "somres";

    private static final String NAMESPACE_SOMRES = "http://www.cirtil.fr/sae/commun_sommaire_et_resultat";

    private static final String TAG_SOMMAIRE = "sommaire";

    private static final String TAG_DOCUMENTS = "documents";

    private static final String TAG_DOCUMENT = "document";

    private static final String TAG_BATCHMODE = "batchMode";

    private static final String TAG_DATE_CREATION = "dateCreation";

    private static final String TAG_DESCRIPTION = "description";

    private static final String TAG_RESTITUTION = "restitutionUuids";

    private static final String TAG_OBJET_NUMERIQUE = "objetNumerique";

    private static final String TAG_CHEMIN_FICHIER = "cheminEtNomDuFichier";

    private static final String TAG_METADONNEES = "metadonnees";

    private static final String TAG_METADONNEE = "metadonnee";

    private static final String TAG_CODE = "code";

    private static final String TAG_VALEUR = "valeur";

    private static final String TAG_NUM_PAGE = "numeroPageDebut";

    private static final String TAG_NOMBRE_PAGE = "nombreDePages";

    public SommaireWriterUtils() {
        super();
    }

    public SommaireWriterUtils(final String sommairePath) throws IOException {
        this();
        openFile(sommairePath);
    }

    public void openFile(final String sommairePath) throws IOException {
        setPathFile(sommairePath + FileConst.SOMMAIRE_FILE_NAME);
        final File file = new File(sommairePath + FileConst.SOMMAIRE_FILE_NAME);
        final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        outputStream = new FileOutputStream(file);
        writer = StaxWriteUtils.loadWriter(outputStream);
        writerUtils = new StaxWriteUtils(eventFactory, writer);
    }

    /**
     * Ouvrir le tag sommaire en rajoutant les namespaces
     *
     * @throws XMLStreamException
     */
    public void startSommaire() throws XMLStreamException {
        writerUtils.startDocument();
        writerUtils.addStartElement(TAG_SOMMAIRE, PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addPrefix(PREFIX_SOMRES, NAMESPACE_SOMRES);
        writerUtils.addPrefix(PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addPrefix("xsi", NAMESPACE_XSI);
    }

    /**
     * Fermer le tag sommaire
     * Avec fermeture du tag "documents" pour la liste des documents
     *
     * @throws XMLStreamException
     */
    public void endSommaire() throws XMLStreamException {
        writerUtils.addEndElement(TAG_DOCUMENTS, PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.createTag("documentsVirtuels", "", PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addEndElement(TAG_SOMMAIRE, PREFIX_SOM, NAMESPACE_SOM);
        // writerUtils.createTag("documentsVirtuels", "", PREFIX_SOM, NAMESPACE_SOM);
        // writerUtils.addEndElement("documentsVirtuels", PREFIX_SOM, NAMESPACE_SOM);
        // writerUtils.addEndElement("documentsVirtuels", PREFIX_SOM, NAMESPACE_SOM);
        writer.flush();
    }

    /**
     * Ecriture des métadonnées du sommaire :
     * batchMode
     * dateCreation
     * description
     * restitutionUuids
     *
     * @throws XMLStreamException
     */
    public void addSommaireHeaders() throws XMLStreamException {
        writerUtils.addStartElement(TAG_BATCHMODE, PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addValue("PARTIEL");
        writerUtils.addEndElement(TAG_BATCHMODE, PREFIX_SOM, NAMESPACE_SOM);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strDate = sdf.format(new Date());

        writerUtils.addStartElement(TAG_DATE_CREATION, PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addValue(strDate);
        writerUtils.addEndElement(TAG_DATE_CREATION, PREFIX_SOM, NAMESPACE_SOM);

        /*writerUtils.addStartElement(TAG_DESCRIPTION, PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addValue("La description du traitement");
        writerUtils.addEndElement(TAG_DESCRIPTION, PREFIX_SOM, NAMESPACE_SOM);*/

        writerUtils.addStartElement(TAG_RESTITUTION, PREFIX_SOM, NAMESPACE_SOM);
        writerUtils.addValue(Boolean.toString(true));
        writerUtils.addEndElement(TAG_RESTITUTION, PREFIX_SOM, NAMESPACE_SOM);

        // Debut de l'écriture des documents
        writerUtils.addStartElement(TAG_DOCUMENTS, PREFIX_SOM, NAMESPACE_SOM);
    }

    /**
     * Ecriture d'un document dans le sommaire
     *
     * @param document
     * @throws XMLStreamException
     */
    public void addDocument(final DocumentType document) throws XMLStreamException {
        final ListeMetadonneeType metadonnees = document.getMetadonnees();

        writerUtils.addStartElement(TAG_DOCUMENT, PREFIX_SOMRES, NAMESPACE_SOMRES);

        writerUtils.addStartElement(TAG_OBJET_NUMERIQUE, PREFIX_SOMRES, NAMESPACE_SOMRES);

        writerUtils.addStartElement(TAG_CHEMIN_FICHIER, PREFIX_SOMRES, NAMESPACE_SOMRES);
        writerUtils.addValue(document.getObjetNumerique().getCheminEtNomDuFichier());
        writerUtils.addEndElement(TAG_CHEMIN_FICHIER, PREFIX_SOMRES, NAMESPACE_SOMRES);

        writerUtils.addEndElement(TAG_OBJET_NUMERIQUE, PREFIX_SOMRES, NAMESPACE_SOMRES);

        writerUtils.addStartElement(TAG_METADONNEES, PREFIX_SOMRES, NAMESPACE_SOMRES);
        for (final MetadonneeType meta : metadonnees.getMetadonnee()) {
            writerUtils.addStartElement(TAG_METADONNEE, PREFIX_SOMRES, NAMESPACE_SOMRES);

            writerUtils.addStartElement(TAG_CODE, PREFIX_SOMRES, NAMESPACE_SOMRES);
            writerUtils.addValue(meta.getCode());
            writerUtils.addEndElement(TAG_CODE, PREFIX_SOMRES, NAMESPACE_SOMRES);

            writerUtils.addStartElement(TAG_VALEUR, PREFIX_SOMRES, NAMESPACE_SOMRES);
            writerUtils.addValue(meta.getValeur().replace("&", "&amp;"));
            writerUtils.addEndElement(TAG_VALEUR, PREFIX_SOMRES, NAMESPACE_SOMRES);

            writerUtils.addEndElement(TAG_METADONNEE, PREFIX_SOMRES, NAMESPACE_SOMRES);
        }

        // Ajouter la meta IdGed pour chaque document
        /*
         * writerUtils.addStartElement(TAG_METADONNEE, PREFIX_SOMRES, NAMESPACE_SOMRES);
         * writerUtils.addStartElement(TAG_CODE, PREFIX_SOMRES, NAMESPACE_SOMRES);
         * writerUtils.addValue("IdGed");
         * writerUtils.addEndElement(TAG_CODE, PREFIX_SOMRES, NAMESPACE_SOMRES);
         * writerUtils.addStartElement(TAG_VALEUR, PREFIX_SOMRES, NAMESPACE_SOMRES);
         * writerUtils.addValue(UUID.randomUUID().toString());
         * writerUtils.addEndElement(TAG_VALEUR, PREFIX_SOMRES, NAMESPACE_SOMRES);
         * writerUtils.addEndElement(TAG_METADONNEE, PREFIX_SOMRES, NAMESPACE_SOMRES);
         */
        // fin d'ajout la meta IdGed pour chaque document


        writerUtils.addEndElement(TAG_METADONNEES, PREFIX_SOMRES, NAMESPACE_SOMRES);


        writerUtils.addStartElement(TAG_NUM_PAGE, PREFIX_SOMRES, NAMESPACE_SOMRES);
        writerUtils.addValue(String.valueOf(document.getNumeroPageDebut()));
        writerUtils.addEndElement(TAG_NUM_PAGE, PREFIX_SOMRES, NAMESPACE_SOMRES);


        writerUtils.addStartElement(TAG_NOMBRE_PAGE, PREFIX_SOMRES, NAMESPACE_SOMRES);
        writerUtils.addValue(String.valueOf(document.getNombreDePages()));
        writerUtils.addEndElement(TAG_NOMBRE_PAGE, PREFIX_SOMRES, NAMESPACE_SOMRES);

        writerUtils.addEndElement(TAG_DOCUMENT, PREFIX_SOMRES, NAMESPACE_SOMRES);
    }

    public void closeStream() throws IOException, XMLStreamException {

        if (writer != null) {
            writer.close();
        }
    }

    /**
     * @return the pathFile
     */
    public String getPathFile() {
        return pathFile;
    }

    /**
     * @param pathFile the pathFile to set
     */
    public void setPathFile(final String pathFile) {
        this.pathFile = pathFile;
    }

}
