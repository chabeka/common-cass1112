/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.controle.PrmdControle;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;

/**
 * Classe d'implémentation du service {@link PrmdService}. Cette classe est un
 * singleton est peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Component
public class PrmdServiceImpl implements PrmdService {

  @Autowired
  private ApplicationContext context;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(PrmdServiceImpl.class);

  private static final String TRC_CHECK = "checkBean()";
  private static final String TRC_LUCENE = "createLucene()";

  private final static String DOMAINE_RH = "DomaineRH";
  private final static String DOMAINE_COTISANT = "DomaineCotisant";
  private final static String DOMAINE_COMPTABLE = "DomaineComptable";
  private final static String DOMAINE_TECHNIQUE = "DomaineTechnique";
  private final static String DOMAINE_RSI = "DomaineRSI";

  /**
   * {@inheritDoc}
   */
  @Override
  public final String createLucene(final String lucene, final List<SaePrmd> prmds) {

    LOGGER.debug("{} - Debut de la creation de la requete", TRC_LUCENE);

    LOGGER.debug("{} - Requête LUCENE de départ : {}", new String[] {
                                                                     TRC_LUCENE, lucene });

    String currentRequete;
    Prmd prmd;
    SaePrmd saePrmd;

    final List<String> sousRequetes = new ArrayList<>();

    for (int index = 0; index < prmds.size(); index++) {
      saePrmd = prmds.get(index);
      currentRequete = StringUtils.EMPTY;
      prmd = saePrmd.getPrmd();

      if (StringUtils.isNotEmpty(prmd.getLucene())) {
        LOGGER.debug("{} - Concaténation avec la requete lucène du PRMD",
                     TRC_LUCENE);
        currentRequete = createLucene(prmd, saePrmd.getValues());

      } else if (StringUtils.isNotEmpty(prmd.getBean())) {
        LOGGER.debug("{} - Concaténation avec la requête du bean",
                     TRC_LUCENE);
        currentRequete = createBean(prmd, saePrmd.getValues());

      } else {
        LOGGER.info("pas de définition de requête pour le PRMD "
            + prmd.getCode());
      }

      if (StringUtils.isNotEmpty(currentRequete)) {
        sousRequetes.add(currentRequete);
      }

    }

    LOGGER.debug("{} - Assemblage de la sous requête", TRC_LUCENE);
    final String sousRequete = createSousRequete(sousRequetes);

    String requete = lucene;
    if (StringUtils.isNotEmpty(sousRequete)) {
      LOGGER.debug("{} - Assemblage de la requête définitive", TRC_LUCENE);

      // Suite à la découverte d'un problème dans l'analyseur de
      // requête DFCE en 1.1.0 (JIRA CRTL-95), on gère le cas particulier
      // d'1 seul PRMD (1 seule sous-requête)
      // En effet, la requête suivante ne fonctionne pas dans DFCE 1.1.0 :
      // (Meta1:Valeur1) AND ((Meta2:Valeur2))
      // Alors que celle-ci fonctionne :
      // (Meta1:Valeur1) AND (Meta2:Valeur2)
      if (sousRequetes.size() == 1) {

        // Cas particulier d'1 seul PRMD : pas besoin d'ajouter de
        // parenthèse
        // supplémentaire autour de la sous-requête. Les parenthèses sont
        // déjà
        // ajoutées lors de la construction de cette sous-requête.
        requete = "(" + requete + ") AND " + sousRequete;

      } else {

        requete = "(" + requete + ") AND (" + sousRequete + ")";
      }

    }

    LOGGER.debug("{} - Requête LUCENE travaillée : {}", new String[] {
                                                                      TRC_LUCENE, requete });
    return requete;
  }

  /**
   * @param currentRequete
   * @return
   */
  private String createSousRequete(final List<String> sousRequetes) {

    final StringBuffer buffer = new StringBuffer();
    for (int index = 0; index < sousRequetes.size(); index++) {
      if (index != 0) {
        buffer.append(" OR ");
      }
      buffer.append("(" + sousRequetes.get(index) + ")");
    }

    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPermitted(final List<UntypedMetadata> metadatas,
                                   final List<SaePrmd> prmds) {

    boolean match = false;
    int index = 0;
    SaePrmd saePrmd;
    Prmd prmd;

    final Map<String, String> metaValues = getMapFromMeta(metadatas);

    while (!match && index < prmds.size()) {

      saePrmd = prmds.get(index);
      prmd = saePrmd.getPrmd();

      if (MapUtils.isNotEmpty(prmd.getMetadata())) {

        match = checkPrmd(prmd, saePrmd.getValues(), metaValues);

      } else if (StringUtils.isNotEmpty(prmd.getBean())) {

        match = checkBean(prmd, metadatas, saePrmd.getValues());

      } else {
        LOGGER.debug("pas de périmètre défini");
      }

      index++;

    }

    return match;
  }

  /**
   * @param bean
   * @param metadatas
   * @param values
   * @return
   */
  private boolean checkBean(final Prmd prmd, final List<UntypedMetadata> metadatas,
                            final Map<String, String> values) {

    boolean match = false;

    PrmdControle controle;
    try {
      controle = context.getBean(prmd.getBean(), PrmdControle.class);

      Map<String, String> valeurs;
      if (values == null) {
        valeurs = new HashMap<>();
      } else {
        valeurs = values;
      }

      match = controle.isPermitted(metadatas, valeurs);

    } catch (final BeansException e) {
      LOGGER.warn("{} - Aucune fonction {} n'existe pour le Prmd {}",
                  new String[] { TRC_CHECK, prmd.getCode(), prmd.getBean() });
    }

    return match;
  }

  /**
   * @param metadatas
   * @return
   */
  private Map<String, String> getMapFromMeta(final List<UntypedMetadata> metadatas) {

    final Map<String, String> map = new HashMap<>();
    for (final UntypedMetadata untypedMetadata : metadatas) {

      if (StringUtils.isNotBlank(untypedMetadata.getLongCode())
          && StringUtils.isNotBlank(untypedMetadata.getValue())) {
        map.put(untypedMetadata.getLongCode().toUpperCase(),
                untypedMetadata.getValue().toUpperCase());
      }
    }

    return map;
  }

  /**
   * Vérifie que le périmètre est bon
   * 
   * @param prmd
   * @param values
   * @param metaValues
   * @return
   */
  private boolean checkPrmd(final Prmd prmd, final Map<String, String> dynamicValues,
                            Map<String, String> metaValues) {

    if (metaValues == null) {
      metaValues = new HashMap<>();
    }

    boolean match = true;
    Map<String, List<String>> parametres = prmd.getMetadata();

    Map<String, String> dynamicParam;
    if (dynamicValues == null) {
      dynamicParam = new HashMap<>();
    } else {
      dynamicParam = dynamicValues;
    }

    if (parametres == null) {
      parametres = new HashMap<>();
    }

    final Iterator<Entry<String, List<String>>> iterator = parametres.entrySet()
        .iterator();
    // Iterator<String> keyIterator = parametres.keySet().iterator();
    String key;
    while (iterator.hasNext() && match) {

      key = iterator.next().getKey();

      final boolean metaStatic = containsIgnoreCase(metaValues.keySet(), key)
          && containsIgnoreCase(parametres.get(key),
                                metaValues.get(key.toUpperCase()));

      final boolean metaDynamic = containsIgnoreCase(dynamicParam.keySet(), key)
          && metaValues.get(key.toUpperCase()).equalsIgnoreCase(
                                                                dynamicParam.get(key));

      if (!metaStatic && !metaDynamic) {
        match = false;
      }

    }

    return match;
  }

  private boolean containsIgnoreCase(final Collection<String> collection,
                                     final String value) {
    boolean found = false;

    final Iterator<String> iterator = collection.iterator();
    String currentValue;

    while (iterator.hasNext() && !found) {
      currentValue = iterator.next().toUpperCase();
      if (value.toUpperCase().matches(currentValue)) {
        found = true;
      }
    }

    return found;
  }

  private String createBean(final Prmd prmd, Map<String, String> parametres) {
    String requete;

    try {
      final PrmdControle controle = context.getBean(prmd.getBean(),
                                                    PrmdControle.class);

      if (parametres == null) {
        parametres = new HashMap<>();
      }
      requete = controle.createLucene(parametres);

    } catch (final BeansException e) {
      requete = null;
    }

    return requete;
  }

  private String createLucene(final Prmd prmd, final Map<String, String> values) {

    String requete = prmd.getLucene();
    if (MapUtils.isNotEmpty(values)) {

      for (final Entry<String, String> entry : values.entrySet()) {
        requete = requete.replace("<%" + entry.getKey() + "%>",
                                  values.get(entry.getKey()));
      }
    }

    return requete;
  }

  /**
   * Teste si une clé de mata correspond à l'un des trois domaines :
   * {DOMAINE_RH, DOMAIN_COTISANT, DOMAIN_COMPTABLE, DOMAINE_RSI,
   * DOMAINE_TECHNIQUE}
   * 
   * @param value
   *           : la valeur à tester
   * @return
   */
  private boolean isDomaineTechRhCotCpt(final String value) {
    if (value.equals(DOMAINE_RH) || value.equals(DOMAINE_COTISANT)
        || value.equals(DOMAINE_COMPTABLE) || value.equals(DOMAINE_RSI)
        || value.equals(DOMAINE_TECHNIQUE)) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws UnexpectedDomainException
   * @throws InvalidPagmsCombinaisonException
   */
  @Override
  public void addDomaine(final List<UntypedMetadata> metadatas, final List<SaePrmd> prmds)
      throws UnexpectedDomainException, InvalidPagmsCombinaisonException {

    // -- On vérifie qu'aucune métadonnée « Domaine* » n’est présente
    for (final UntypedMetadata meta : metadatas) {
      if (isDomaineTechRhCotCpt(meta.getLongCode())) {
        // -- Le domaine est présent
        String mssg = "La ou les métadonnées suivantes ne sont "
            + "pas autorisées à l'archivage : %s";
        mssg = String.format(mssg, meta.getLongCode());
        throw new UnexpectedDomainException(mssg);
      }
    }

    // -- Aucun domaine n'est présent
    int addCount = 0;
    String metadata = null;

    // -- On boucle sur la liste des prmds passée en paramètre
    for (final SaePrmd saePrmd : prmds) {

      PrmdControle controle;
      final Prmd prmd = saePrmd.getPrmd();
      final String prmdName = prmd.getBean();
      Map<String, String> prmdValues = saePrmd.getValues();
      if (prmdValues == null) {
        prmdValues = new HashMap<>();
      }
      final Map<String, List<String>> prmdMetas = prmd.getMetadata();
      if (addCount == 0) {
        // -- Cas d'un prdm de type bean
        if (!StringUtils.isEmpty(prmdName)) {
          try {
            controle = context.getBean(prmdName, PrmdControle.class);
            controle.addDomaine(metadatas, prmdValues);
            addCount++;
          } catch (final BeansException e) {
            LOGGER.warn(
                        "{} - Aucune fonction {} n'existe pour le Prmd {}",
                        new String[] { TRC_CHECK, prmd.getCode(),
                                       prmd.getBean() });
          }
        }
        // -- Prmd dynamique
        else if (!MapUtils.isEmpty(prmdValues)) {
          for (final Map.Entry<String, List<String>> entry : prmdMetas
              .entrySet()) {
            if (isDomaineTechRhCotCpt(entry.getKey())
                && prmdValues.containsKey(entry.getKey())) {
              final String valeur = prmdValues.get(entry.getKey());
              metadatas.add(new UntypedMetadata(entry.getKey(), valeur));
              addCount++;
              break;
            }
          }
        }
        // -- Prmd classique
        else {
          for (final Map.Entry<String, List<String>> entry : prmdMetas
              .entrySet()) {
            if (isDomaineTechRhCotCpt(entry.getKey())) {
              if (entry.getValue().size() == 1) {
                final String valeur = entry.getValue().get(0);
                metadatas.add(new UntypedMetadata(entry.getKey(),
                                                  valeur));
                addCount++;
                break;
              }
            }
          }
        }
      }

      // -- On vérifie que le prmds ne comporte qu'un et un seul domaine
      if (prmdMetas != null) {
        for (final Map.Entry<String, List<String>> entry : prmdMetas.entrySet()) {
          if (isDomaineTechRhCotCpt(entry.getKey())) {
            if (metadata == null) {
              // -- Get first found domain metadata
              metadata = entry.getKey();
              continue;
            }
            if (!metadata.equals(entry.getKey())) {
              final String mssg = "Les Pagms présents dans le VI sont incompatibles : "
                  + "plusieurs domaines différents trouvés.";
              throw new InvalidPagmsCombinaisonException(mssg);
            }
          }
        }
      }
    }

    // -- Aucun domaine n’a été ajouté à la liste des métadonnées
    if (addCount == 0) {
      // -- Ajout du DomaineCotisant par défaut si aucun autre n'a pu être
      // ajouté
      metadatas.add(new UntypedMetadata(DOMAINE_COTISANT, "true"));
    }
  }
}
