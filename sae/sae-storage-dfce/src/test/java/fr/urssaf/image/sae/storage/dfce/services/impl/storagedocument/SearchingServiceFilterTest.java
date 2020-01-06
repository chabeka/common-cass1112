package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.ValueFilter;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class SearchingServiceFilterTest {

  @Autowired
  @Qualifier("searchingService")
  private SearchingService searchingService;

  List<AbstractFilter> list = new ArrayList<>();

  @Before
  public void init() {

    final ValueFilter dmc = new ValueFilter("dmc", "DateMiseEnCorbeille", "201501050000000");
    final ValueFilter dmc1 = new ValueFilter("dmc", "DateMiseEnCorbeille", "201501050000000");
    list.add(dmc);
    list.add(dmc1);

    final ValueFilter doc = new ValueFilter("SM_DOCUMENT_TYPE", "CodeRND", "1.A.X.X.X");
    final ValueFilter doc1 = new ValueFilter("SM_DOCUMENT_TYPE", "CodeRND", "1.B.X.X.X");
    final ValueFilter doc2 = new ValueFilter("SM_DOCUMENT_TYPE", "CodeRND", "1.1.3.1.4");
    list.add(doc);
    list.add(doc1);
    list.add(doc2);

    final ValueFilter vf = new ValueFilter("str", "Siret", "124");
    final ValueFilter vf1 = new ValueFilter("str", "Sriet", "546");
    final ValueFilter vf2 = new ValueFilter("str", "Siret", "658");
    final ValueFilter vf3 = new ValueFilter("str", "Sriet", "987");
    list.add(vf);
    list.add(vf1);
    list.add(vf2);
    list.add(vf3);

    final ValueFilter stn2 = new ValueFilter("srn", "Siren", "658");
    final ValueFilter stn3 = new ValueFilter("srn", "Siren", "987");
    list.add(stn2);
    list.add(stn3);

  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValueFilter() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {



    final Method method = searchingService.getClass().getDeclaredMethod("getValueFilter", List.class);
    method.setAccessible(true);

    final Map<String, List<AbstractFilter>> map0 = (Map<String, List<AbstractFilter>>) method.invoke(searchingService, new ArrayList<>());
    Assert.assertNotNull(map0);

    final Map<String, List<AbstractFilter>> map = (Map<String, List<AbstractFilter>>) method.invoke(searchingService, list);
    Assert.assertNotNull(map);
    final List<AbstractFilter> dmcF = map.get("dmc");
    Assert.assertEquals("La map doit contenir une key dmcF", dmcF.size(), 2); 
    final List<AbstractFilter> vf1 = map.get("str");
    Assert.assertEquals("La map doit contenir une key str", vf1.size(), 4); 

  }

}
