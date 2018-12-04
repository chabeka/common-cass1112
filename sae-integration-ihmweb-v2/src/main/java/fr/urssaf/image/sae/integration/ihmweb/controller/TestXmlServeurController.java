package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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
import fr.urssaf.image.sae.integration.ihmweb.service.listetests.TestXmlServeurService;

@Controller
@RequestMapping(value = "testXmlServeur")
public class TestXmlServeurController {

  @Autowired
  private TestConfig testConfig;

  @Autowired
  private TestXmlServeurService service;

  private static boolean isRecherche = false;

  private List<String> records = new ArrayList<>();

  List<String> rec = new ArrayList<>();

  @RequestMapping(method = RequestMethod.GET)
  public final String getDefaultView(final Model model) {
    if (!isRecherche) {
      records = new ArrayList<>();
    }
    final File repertoireXml = new File(testConfig.getTestXml());
    if (repertoireXml != null && repertoireXml.isDirectory()) {
      final File[] filesXml = repertoireXml.listFiles();
      for (final File f : filesXml) {
        records.add(f.getName());
      }
    }
    model.addAttribute("isOk", true);
    model.addAttribute("listeTestXml", records);

    return "testXmlServeur";
  }

  @RequestMapping(method = RequestMethod.POST, params = {"action=lancerTest"})
  public final void lancerTest(@RequestParam("testXml") final String checkboxValue,
                               final Model model)
      throws IOException, XMLStreamException, SAXException,
      ParserConfigurationException {
    records = new ArrayList<>();
    final File repertoireXml = new File(testConfig.getTestXml());
    final File[] filesXml = repertoireXml.listFiles();
    for (final File f : filesXml) {
      records.add(f.getName());
    }

    final Map<String, String> res = service.lancerTest(checkboxValue);

    model.addAttribute("isOk", true);
    model.addAttribute("resTest", res);
    if (!isRecherche) {
      model.addAttribute("listeTestXml", records);
    } else {
      model.addAttribute("listeTestXml", rec);
    }

  }

  @RequestMapping(method = RequestMethod.POST, params = {"action=downloadTest"})
  public void downloadTest(@RequestParam("testXml") final String checkboxValue,
                           final HttpServletResponse response)
      throws IOException {

    final String fullPath = testConfig.getTestXml() + checkboxValue;
    final File downloadFile = new File(fullPath);
    final FileInputStream inputStream = new FileInputStream(downloadFile);

    final String mimeType = "application/octet-stream";

    response.setContentType(mimeType);
    response.setContentLength((int) downloadFile.length());

    final String headerKey = "Content-Disposition";
    final String headerValue = String.format("attachment; filename=\"%s\"",
                                             downloadFile.getName());
    response.setHeader(headerKey, headerValue);

    final OutputStream outStream = response.getOutputStream();

    final byte[] buffer = new byte[4096];
    int bytesRead = -1;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outStream.write(buffer, 0, bytesRead);
    }
    inputStream.close();
    outStream.close();
  }

  @RequestMapping(method = RequestMethod.POST, params = {"action=rechercherTest"})
  public void rechercherTest(@RequestParam("recherche") final String checkboxValue,
                             final Model model) {

    records = new ArrayList<>();
    rec = new ArrayList<>();

    final File repertoireXml = new File(testConfig.getTestXml());
    final File[] filesXml = repertoireXml.listFiles();
    for (final File f : filesXml) {
      records.add(f.getName());
    }

    if (checkboxValue.equals("")) {
      model.addAttribute("listeTestXml", records);
      isRecherche = false;
    } else {
      for (final String str : records) {
        if (str.contains(checkboxValue)) {
          rec.add(str);
        }
      }
      model.addAttribute("listeTestXml", rec);
      records = rec;
      isRecherche = true;
    }
  }

  @RequestMapping(method = RequestMethod.POST, params = {"action=supprimerTest"})
  public void supprimerTest(@RequestParam("testXml") final String checkboxValue,
                            final Model model) {
    final File file = new File(testConfig.getTestXml() + checkboxValue);
    final boolean isDelete = file.delete();
    if (isDelete) {
      model.addAttribute("isDelete", true);
    } else {
      model.addAttribute("isDelete", false);
    }
    if (!isRecherche) {
      records.remove(checkboxValue);
      model.addAttribute("listeTestXml", records);
    } else {
      records.remove(checkboxValue);
      model.addAttribute("listeTestXml", rec);
    }
  }
}
