package fr.urssaf.image.commons.taglib;

import java.util.Collection;

public final class SelectUtil {
	
	private SelectUtil(){
		
	}

	@SuppressWarnings("unchecked")
	public static boolean contains(Object option, Object value) {

		boolean result;
	   
	   if (value == null)
	   {
	      result = false;
		}
	   else
	   {
	      if (Collection.class.isAssignableFrom(value.getClass())) {

            result = ((Collection) value).contains(option);
         }
         else
         {
            result = option.equals(value);
         }
	   }

		return result;
	}
}
