package fr.urssaf.image.commons.webservice.spring.ws.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HumanResourceServiceImpl implements HumanResourceService {

   private static final Logger LOG = Logger
         .getLogger(HumanResourceServiceImpl.class);

   @Override
   public void bookHoliday(Date startDate, Date endDate, String name) {

      LOG.info("bookHoliday:" + name + " start:" + startDate + " end:"
            + endDate);
   }

}
