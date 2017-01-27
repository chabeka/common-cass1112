package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.service.listetests.TestXmlExistantService;

@Controller
@RequestMapping(value = "testXmlExistant")
public class TestXmlExistant {
   
   @Autowired
   private TestXmlExistantService service;
   
   @Autowired
   private TestConfig testConfig;

   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultview(Model model){
      File repertoireXml = new File(testConfig.getTestRegression());
      File[] filesXml = repertoireXml.listFiles();
      List<String> records = new ArrayList<String>();
      for (File f : filesXml) {   
                  records.add(f.getName());    
         }        
      model.addAttribute("isOk", true);
      model.addAttribute("listeTestXml", records);
      return "testXmlExistant";
   }
   
   @RequestMapping(method = RequestMethod.POST, params = { "action=lancerTest" })
   public final String lancerTest(@RequestParam("checkboxName")String[] checkboxValue, Model model) throws IOException, XMLStreamException, SAXException, ParserConfigurationException
   {
      Map<String, Map<String, String>> resStub = new LinkedHashMap<String, Map<String, String>>(); 
      resStub = service.lancerTest(checkboxValue);
      
      model.addAttribute("resTest", resStub);
      return "resultatTestCheckbox";
      
   }
}
