package fr.urssaf.image.sae.droit.aspect;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;


/**
 * 
 * Classe Test de la classe {@link ParamPagmf}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamPagmfTest {

  private static final String MESSAGE_PARAM_PAGMF_RUNTIME = "Une exception IllegalArgumentException aurait dû être levée";
  private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";

  private static final String MESSAGE_EXCEPT = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : ";
  private static final String CODE_PAGMF = "[codePagmf].";

  @Autowired
  private PagmfSupport pagmfSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  @After
  public void end() throws Exception {
    // cassandraServer.resetData(true, MODE_API.HECTOR);
    cassandraServer.resetDataOnly();
  }

  @Test
  public void init() {
    try {
      if (cassandraServer.isCassandraStarted()) {
        cassandraServer.resetData();
      }
      Assert.assertTrue(true);

    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /********************************************************************************************************************************/
  /********************************************************************************************************************************/
  /********************************************************************************************************************************/
  /************** ERREUR --- PARAM PagmfSupport *****************************************************/
  /********************************************************************************************************************************/
  @Test
  public void createPagmfNull() throws FormatControlProfilNotFoundException {
    try {
      final long clock = 1;
      pagmfSupport.create(null, clock);
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [pagmf].",
                    except.getMessage());
    }
  }

  @Test
  public void createPagmfNullClockNull()
      throws FormatControlProfilNotFoundException {
    try {
      final Pagmf pagmf = new Pagmf();
      pagmfSupport.create(pagmf, Long.valueOf(-1));
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [clock, codePagmf, description, formatProfile].",
                    except.getMessage());
    }
  }

  @Test
  public void createCodePagmfNull()
      throws FormatControlProfilNotFoundException {
    try {
      final Pagmf pagmf = new Pagmf();
      pagmf.setDescription("description");
      pagmf.setCodeFormatControlProfil("codeFormatControlProfil");
      pagmfSupport.create(pagmf, Long.valueOf(1));
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }


  @Test
  public void deleteCodePagmfNull()
      throws PagmfNotFoundException {
    try {
      pagmfSupport.delete(null, Long.valueOf(1));
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }

  @Test
  public void deleteCodePagmfVide()
      throws PagmfNotFoundException {
    try {
      pagmfSupport.delete("", Long.valueOf(1));
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }

  @Test
  public void deleteCodePagmfEspace()
      throws PagmfNotFoundException {
    try {
      pagmfSupport.delete("     ", Long.valueOf(1));
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }

  @Test
  public void deleteCodePagmfInexistant() {
    try {
      pagmfSupport.delete("code", Long.valueOf(1));
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final PagmfNotFoundException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    "Le Pagmf à supprimer : [code] n'existe pas en base.",
                    except.getMessage());
    }
  }



  @Test
  public void findCodePagmfNull()
      throws PagmfNotFoundException {
    try {
      pagmfSupport.find(null);
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }

  @Test
  public void findCodePagmfVide()
      throws PagmfNotFoundException {
    try {
      pagmfSupport.find("");
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }

  @Test
  public void findCodePagmfEspace()
      throws PagmfNotFoundException {
    try {
      pagmfSupport.find("     ");
      Assert.fail(MESSAGE_PARAM_PAGMF_RUNTIME);
    } catch (final IllegalArgumentException except) {
      Assert
      .assertEquals(
                    MESSAGE_EXCEPT_INCORRECT,
                    MESSAGE_EXCEPT + CODE_PAGMF,
                    except.getMessage());
    }
  }

  @Test
  public void findCodePagmfInexistant() {
    final Pagmf pagmf = pagmfSupport.find("test");
    Assert.assertNull(pagmf); // pagmf inexistant
  }


}
