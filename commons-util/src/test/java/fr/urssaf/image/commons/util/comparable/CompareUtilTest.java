package fr.urssaf.image.commons.util.comparable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.urssaf.image.commons.util.collection.CollectionUtils.CollectionComparator;

public class CompareUtilTest {

	private final CollectionComparator<String> comparator = new CollectionComparator<String>();

	private final static String TOTO = "toto";
	private final static String TATA = "tata";
	private final static String TITI = "titi";

	private final static String MESSAGE = "Vérification d'échec de comparaison entre 2 listes";

	@Test
	public void compareListFalse() {

		List<String> liste1 = new ArrayList<String>();
		liste1.add(TOTO);
		liste1.add(TATA);
		liste1.add(TITI);

		List<String> liste2 = new ArrayList<String>();
		liste2.add(TOTO);
		liste2.add("tutu");
		liste2.add(TITI);

		assertFalse(MESSAGE, comparator.equals(liste1, liste2));
		assertFalse(MESSAGE, comparator.equals(liste2, liste1));

	}

	@Test
	public void compareListFalse2() {

		List<String> liste1 = new ArrayList<String>();
		liste1.add(TOTO);
		liste1.add(TOTO);
		liste1.add(TOTO);

		List<String> liste2 = new ArrayList<String>();
		liste2.add(TOTO);
		liste2.add(TATA);
		liste2.add(TITI);

		assertFalse(MESSAGE, comparator.equals(liste1, liste2));
		assertFalse(MESSAGE, comparator.equals(liste2, liste1));

	}

	@Test
	public void compareListSuccess() {

		List<String> liste1 = new ArrayList<String>();
		liste1.add(TOTO);
		liste1.add(TATA);
		liste1.add(TITI);

		List<String> liste2 = new ArrayList<String>();
		liste2.add(TITI);
		liste2.add(TOTO);
		liste2.add(TATA);

		assertEquals(MESSAGE,true, comparator.equals(liste1, liste2));
		assertEquals(MESSAGE,true, comparator.equals(liste2, liste1));

	}

}
