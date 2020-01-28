/**
 *AC75095351
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.droit.dao.cql.IFormatControlProfilDaoCql;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

/**
 * Support de la classe DAO {@link IFormatControlProfilDaoCql}
 */
@Service
public class FormatControlProfilCqlSupport {

  @Autowired
  IFormatControlProfilDaoCql formatcontrolprofildaocql;

  public FormatControlProfilCqlSupport(final IFormatControlProfilDaoCql formatcontrolprofildaocql) {
    this.formatcontrolprofildaocql = formatcontrolprofildaocql;
  }

  /**
   * Création d'un formatControlProfil
   *
   * @param formatControlProfil
   *          formatControlProfil à créer
   */
  public void create(final FormatControlProfil formatControlProfil) {
    saveOrUpdate(formatControlProfil);
  }

  /**
   * Méthode de suppression d'un formatControlProfil
   *
   * @param code
   *          identifiant de la formatControlProfil
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    formatcontrolprofildaocql.deleteById(code);
  }

  /**
   * Recherche et retourne l'enregistrement de formatControlProfil en
   * fonction du code fourni
   *
   * @param code
   *          code du formatControlProfil
   * @return l'enregistrement du formatControlProfil correspondante
   */
  public final FormatControlProfil find(final String code) {
    return findById(code);

  }

  /**
   * {@inheritDoc}
   */
  public FormatControlProfil findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return formatcontrolprofildaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * Sauvegarde du formatControlProfil
   * 
   * @param formatControlProfil
   */
  private void saveOrUpdate(final FormatControlProfil formatControlProfil) {
    Assert.notNull(formatControlProfil, "l'objet formatcontrolprofil  ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "";


    if (isValidCode) {

      // recuperation de l'objet ayant le meme codeevt dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<FormatControlProfil> formatControlProfilOpt = formatcontrolprofildaocql.findWithMapperById(formatControlProfil.getFormatCode());
      if (formatControlProfilOpt.isPresent()) {
        final FormatControlProfil formatControlProfiFromBD = formatControlProfilOpt.get();


        formatcontrolprofildaocql.saveWithMapper(formatControlProfiFromBD);
      } else {
        formatcontrolprofildaocql.saveWithMapper(formatControlProfil);
      }
    } else {
      throw new DroitRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

  }

  /**
   * Recherche tous les formatControlProfil
   * 
   * @return liste des FormatControlProfil
   */
  public List<FormatControlProfil> findAll() {
    final Iterator<FormatControlProfil> it = formatcontrolprofildaocql.findAllWithMapper();
    final List<FormatControlProfil> list = new ArrayList<>();
    while (it.hasNext()) {

      list.add(it.next());

    }
    return list;
  }
}
