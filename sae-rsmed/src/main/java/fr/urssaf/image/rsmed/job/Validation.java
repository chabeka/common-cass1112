package fr.urssaf.image.rsmed.job;

import fr.urssaf.image.rsmed.bean.xsd.generated.ListeMetadonneeType;
import fr.urssaf.image.rsmed.bean.xsd.generated.MetadonneeType;
import fr.urssaf.image.rsmed.exception.FunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Validation {

    private static Logger LOGGER = LoggerFactory.getLogger(Validation.class);

    private enum RequiredMetaDatas {
        Titre,
        DateCreation,
        ApplicationProductrice,
        CodeOrganismeProprietaire,
        CodeOrganismeGestionnaire,
        CodeRND,
        Hash,
        TypeHash,
        NbPages,
        FormatFichier
    }

    public static File validateAndGetXmlInputFile(List<File> xmlInputFiles) {
        LOGGER.info("Récupération et validation du fichier Xml en entrée");
        if (xmlInputFiles == null || xmlInputFiles.size() != 1) {
            LOGGER.error("Erreur de récuperation du fichier xml");
            throw new FunctionalException(new RuntimeException("Erreur de récuperation du fichier xml"));
        }
        return xmlInputFiles.get(0);
    }

    public static void validateNumeroCompteExterne(String idV2) {
        if (StringUtils.isEmpty(idV2) || idV2.length() != 28) {
            LOGGER.error("Le champ ID_V2 n'est pas valide");
            throw new FunctionalException(new RuntimeException("Le champ ID_V2 n'est pas valide"));
        }
    }

    public static void validateMetadonnees(ListeMetadonneeType listeMetadonneeType) {
        Arrays.stream(RequiredMetaDatas.values()).forEach(field -> {
                    boolean metaValid = false;
                    for (MetadonneeType metadonnee : listeMetadonneeType.getMetadonnee()) {
                        if (field.name().equalsIgnoreCase(metadonnee.getCode())
                        && ! StringUtils.isEmpty(metadonnee.getValeur())) {
                            metaValid = true;
                            break;
                        }
                    }
                    if (!metaValid) {
                        throw new FunctionalException(new RuntimeException("Le champ " + field.name() + " n'est pas valide"));
                    }

                }
        );
    }
}

