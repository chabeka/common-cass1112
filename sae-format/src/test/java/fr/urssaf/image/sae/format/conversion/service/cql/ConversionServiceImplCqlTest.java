package fr.urssaf.image.sae.format.conversion.service.cql;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConvertisseurInitialisationException;
import fr.urssaf.image.sae.format.conversion.service.ConversionService;
import fr.urssaf.image.sae.format.conversion.service.impl.ConversionServiceImpl;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.AbstractReferentielFormatCqlTest;
import fr.urssaf.image.sae.format.utils.Utils;
import junit.framework.Assert;

/**
 * Classe testant les services de la classe {@link ConversionServiceImpl}
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = {"/applicationContext-sae-format-test.xml"})
public class ConversionServiceImplCqlTest extends AbstractReferentielFormatCqlTest {

  @Autowired
  private ConversionService conversionService;

  @Autowired
  @Qualifier("referentielFormatSupportFacade")
  private ReferentielFormatSupportFacade refFormatSupport;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;
  @Before
  public void start() {
    modeApiSupport.initTables(MODE_API.DATASTAX);
  }


  @Test
  public void convertirFichierFile_success() throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    // Appel de la méthode à tester
    final byte[] result = conversionService.convertirFichier("fmt/353", ressource.getFile(), null, null);

    Assert.assertNotNull("Le fichier aurait dû être converti", result);
    Assert.assertTrue("Le fichier aurait dû être converti", result.length > 0);
  }

  @Test(expected = ConversionParametrageException.class)
  public void convertirFichierFile_erreur_parametres()
      throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    // Appel de la méthode à tester
    conversionService.convertirFichier("fmt/353", ressource.getFile(), null, Integer.valueOf(0));
  }

  @Test(expected = UnknownFormatException.class)
  public void convertirFichierFile_format_inconnu()
      throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    // Appel de la méthode à tester
    conversionService.convertirFichier("inconnu", ressource.getFile(), null, null);
  }

  @Test(expected = ConvertisseurInitialisationException.class)
  public void convertirFichierFile_format_bean_inexistant()
      throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // 1 - CREATION DU FORMAT LAMBDA
    final FormatFichier refFormat = Utils.genererRefFormatLambda();
    final String idFormat = refFormat.getIdFormat();
    Assert.assertEquals("lambda", idFormat);
    refFormatSupport.create(refFormat);

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    // Appel de la méthode à tester
    conversionService.convertirFichier("lambda", ressource.getFile(), null, null);

    // 2 - SUPPRESSION DU FORMAT LAMBDA
    refFormatSupport.delete(idFormat);
  }

  @Test
  public void convertirFichierByte_success() throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource.getFile()));

    // Appel de la méthode à tester
    final byte[] result = conversionService.convertirFichier("fmt/353", fichier, null, null);

    Assert.assertNotNull("Le fichier aurait dû être converti", result);
    Assert.assertTrue("Le fichier aurait dû être converti", result.length > 0);
  }

  @Test(expected = ConversionParametrageException.class)
  public void convertirFichierByte_erreur_parametres()
      throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource.getFile()));

    // Appel de la méthode à tester
    conversionService.convertirFichier("fmt/353", fichier, null, Integer.valueOf(0));
  }

  @Test(expected = UnknownFormatException.class)
  public void convertirFichierByte_format_inconnu()
      throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource.getFile()));

    // Appel de la méthode à tester
    conversionService.convertirFichier("inconnu", fichier, null, null);
  }

  @Test(expected = ConvertisseurInitialisationException.class)
  public void convertirFichierByte_format_bean_inexistant()
      throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

    // 1 - CREATION DU FORMAT LAMBDA
    final FormatFichier refFormat = Utils.genererRefFormatLambda();
    final String idFormat = refFormat.getIdFormat();
    Assert.assertEquals("lambda", idFormat);
    refFormatSupport.create(refFormat);

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

    final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource.getFile()));

    // Appel de la méthode à tester
    conversionService.convertirFichier("lambda", fichier, null, null);

    // 2 - SUPPRESSION DU FORMAT LAMBDA
    refFormatSupport.delete(idFormat);
  }
}
