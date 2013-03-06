package com.docubase.dfce.toolkit.client.administration;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.statistic.Statistic;
import net.docubase.toolkit.service.administration.StatisticAdministrationService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.AbstractTestBase;

public class StatisticAdministrationClientIT extends AbstractTestBase {
   private final StatisticAdministrationService statisticAdministrationService = serviceProvider
         .getStatisticAdministrationService();

   private final static String STATISTIC_PREFIX = "STATISTIC_";
   private final static String STATISTIC_BASE_ID = STATISTIC_PREFIX + "BASE_ID";
   private final static String STATISTIC_CATEGORY_PREFIX = STATISTIC_PREFIX + "CATEGORY_";

   @BeforeClass
   public static void setUp() {
      connect();
   }

   @AfterClass
   public static void tearDown() {
      disconnect();
   }

   @Test
   public void testCreateStatistic() {
      try {
         createNextStatistic();
      } catch (ObjectAlreadyExistsException e) {
         e.printStackTrace();
         fail("Statistic already exists");
      }
   }

   @Test
   public void testUpdateStatistic() throws ObjectAlreadyExistsException {

      Statistic statistic = statisticAdministrationService.getStatistic(STATISTIC_PREFIX + 0);

      if (statistic == null)
         statistic = createNextStatistic();

      if (statistic.getBases().isEmpty()) {

         Base base = serviceProvider.getBaseAdministrationService().getBase(STATISTIC_BASE_ID);

         if (base == null) {
            base = ToolkitFactory.getInstance().createBase(STATISTIC_BASE_ID);
            base = serviceProvider.getBaseAdministrationService().createBase(base);
         }
         statistic.addBase(base);

      } else
         statistic.clearBases();

      statisticAdministrationService.updateStatistic(statistic);

   }

   @Test(expected = IllegalArgumentException.class)
   public void testUpdateStatisticNotExists() throws ObjectAlreadyExistsException {

      Statistic notExistStatistic = ToolkitFactory.getInstance().createStatistic(STATISTIC_PREFIX,
            null, null);

      statisticAdministrationService.updateStatistic(notExistStatistic);

   }

   @Test(expected = IllegalArgumentException.class)
   public void testUpdateStatisticIllegalCategory() throws ObjectAlreadyExistsException {

      Statistic statistic = statisticAdministrationService.getStatistic(STATISTIC_PREFIX + 0);

      if (statistic == null)
         statistic = createNextStatistic();

      statistic.getCategories().add(getStatisticCategory(0));

      statisticAdministrationService.updateStatistic(statistic);

   }

   private Statistic createNextStatistic() throws ObjectAlreadyExistsException {
      Statistic statistic = null;
      int i = 0;

      String statisticName;
      do {
         statisticName = STATISTIC_PREFIX + i++;
         statistic = statisticAdministrationService.getStatistic(statisticName);
      } while (statistic != null);

      Set<Category> categories = new HashSet<Category>(i - 1);
      for (int j = 0; j < i - 1; j++) {
         categories.add(getStatisticCategory(j));
      }

      statistic = ToolkitFactory.getInstance().createStatistic(statisticName, null, categories);

      return statisticAdministrationService.createStatistic(statistic);

   }

   private Category getStatisticCategory(int i) throws ObjectAlreadyExistsException {
      String categoryName = STATISTIC_CATEGORY_PREFIX + i;

      serviceProvider.getStorageAdministrationService().createCategory(categoryName,
            CategoryDataType.STRING);

      return serviceProvider.getStorageAdministrationService().getCategory(categoryName);
   }
}
