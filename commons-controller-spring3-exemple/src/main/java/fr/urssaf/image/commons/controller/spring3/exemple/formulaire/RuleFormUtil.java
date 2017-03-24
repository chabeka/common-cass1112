package fr.urssaf.image.commons.controller.spring3.exemple.formulaire;

import java.util.Date;

import fr.urssaf.image.commons.util.date.DateCompareUtil;

public final class RuleFormUtil {

	public static class DateRule {

		private final Date date1;
		private final Date date2;

		public DateRule(Date date1, Date date2) {
			this.date1 = date1;
			this.date2 = date2;
		}

		protected boolean isValid() {
			
			boolean valid = true;
			if (this.date1 != null && this.date2 != null) {
				valid =  DateCompareUtil.sup(this.date1, this.date2);
			}
			return valid;
		}

	}

}
