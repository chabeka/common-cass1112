package fr.urssaf.image.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.gov.nationalarchives.droid.command.ResultPrinter;
import uk.gov.nationalarchives.droid.command.action.CommandExecutionException;
import uk.gov.nationalarchives.droid.container.ContainerSignatureDefinitions;
import uk.gov.nationalarchives.droid.container.ContainerSignatureSaxParser;
import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;
import uk.gov.nationalarchives.droid.core.SignatureParseException;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResultCollection;
import uk.gov.nationalarchives.droid.core.interfaces.RequestIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.resource.FileSystemIdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.resource.RequestMetaData;
import uk.gov.nationalarchives.droid.core.interfaces.signature.SignatureFileException;
import uk.gov.nationalarchives.droid.profile.DirectoryProfileResource;
import uk.gov.nationalarchives.droid.profile.ProfileInstance;
import uk.gov.nationalarchives.droid.profile.ProfileInstanceManagerImpl;
import uk.gov.nationalarchives.droid.profile.ProfileSpec;
import uk.gov.nationalarchives.droid.profile.ProfileState;
import uk.gov.nationalarchives.droid.profile.referencedata.Format;
import uk.gov.nationalarchives.droid.signature.FormatCallback;
import uk.gov.nationalarchives.droid.signature.SaxSignatureFileParser;

public class ValidateFile {
   
   private ClassPathXmlApplicationContext context;
   
   public ValidateFile(){
      context = new ClassPathXmlApplicationContext("classpath*:/META-INF/sae-droid.xml");
      
   }
   
   /**
    * @param args
    * @throws Exception 
    */
   
   public static void main(String[] args) throws Exception {
      
      //System.setProperty("droidUserDir", "z:\\dd");
      ValidateFile vf = new ValidateFile();
      vf.identifyFile();
      //loadDroidSignatureFromPronom("G:/rghurbhurn/eclipse_workspace/commons-droid/target/classes/DROID_SignatureFile_V63.xml");
      
   }
   
   private void identifyFile() throws CommandExecutionException{
      // fichier de signature de PRONOM- obligatoire
      String fileSignaturesFileName ="Z:/dd/signature_files/DROID_SignatureFile_V60.xml";
      String FORWARD_SLASH = "/";
      String BACKWARD_SLASH = "\\";
      // fichier de signature permettant de lire les fichiers archives ZIP et OLE2 - obligatoire
      String containerSignaturesFileName ="Z:/dd/container_sigs/container-signature-20110114.xml";
      // extension des fichiers à traiter
      String[] extensions ={"pdf"};
      // recherche dans les répertoires fils
      boolean recursive=true;
      // point de départ
      String searchPath ="Z:/PDF/Bavaria_testsuite";
      
      Long maxBytesToScan=new Long(65536);
      // est ce qu'on traite les fichiers compressés
      boolean archives=false;
      
      File dirToSearch = new File(searchPath);
      if (!dirToSearch.isDirectory()) {
          throw new CommandExecutionException("Resources directory not found");
      }
      // chargement du fichier de signature PRONOM.
      BinarySignatureIdentifier binarySignatureIdentifier = new BinarySignatureIdentifier();
      File fileSignaturesFile = new File(fileSignaturesFileName);
      if (!fileSignaturesFile.exists()) {
          throw new CommandExecutionException("Signature file not found");
      }

      binarySignatureIdentifier.setSignatureFile(fileSignaturesFileName);
      try {
          binarySignatureIdentifier.init();
      } catch (SignatureParseException e) {
          throw new CommandExecutionException("Can't parse signature file");
      }
      
      binarySignatureIdentifier.setMaxBytesToScan(maxBytesToScan);
      String path = fileSignaturesFile.getAbsolutePath();
      String slash = path.contains(FORWARD_SLASH) ? FORWARD_SLASH : BACKWARD_SLASH;
      String slash1 = slash;
      
      // on prend en compte les fichier compressés.
      ContainerSignatureDefinitions containerSignatureDefinitions = null;
      if (containerSignaturesFileName != null) {
          File containerSignaturesFile = new File(containerSignaturesFileName);
          if (!containerSignaturesFile.exists()) {
              throw new CommandExecutionException("Container signature file not found");
          }
          try {
              final InputStream in = new FileInputStream(containerSignaturesFileName);
              final ContainerSignatureSaxParser parser = new ContainerSignatureSaxParser();
              containerSignatureDefinitions = parser.parse(in);
          } catch (SignatureParseException e) {
              throw new CommandExecutionException("Can't parse container signature file");
          } catch (IOException ioe) {
              throw new CommandExecutionException(ioe);
          } catch (JAXBException jaxbe) {
              throw new CommandExecutionException(jaxbe);
          }
      }
      path = "";
      // on instancie l'objet permettant l'affichage à l'écran.
      ResultPrinter resultPrinter =
          new ResultPrinter(binarySignatureIdentifier, containerSignatureDefinitions,
              path, slash, slash1, archives);
      // on filtre les fichier suivant le répertoire spécifié et suivant les extension défini plus haut.
      Collection<File> matchedFiles =
              FileUtils.listFiles(dirToSearch, extensions, recursive);
      String fileName = null;
      for (File file : matchedFiles) {
          try {
              fileName = file.getCanonicalPath();
          } catch (IOException e) {
              throw new CommandExecutionException(e);
          }
          URI uri = file.toURI();
          // récupération de la signature externe
          RequestMetaData metaData =
              new RequestMetaData(file.length(), file.lastModified(), fileName);
          RequestIdentifier identifier = new RequestIdentifier(uri);
          identifier.setParentId(1L);
          
          // appel de la méthode d'identification à partie la signature interne.
          InputStream in = null;
          IdentificationRequest request = new FileSystemIdentificationRequest(metaData, identifier);
          try {
              in = new FileInputStream(file);
              request.open(in);
              IdentificationResultCollection results =
                  binarySignatureIdentifier.matchBinarySignatures(request);
              // affichage du résultat dans la console.
              resultPrinter.print(results, request);
          } catch (IOException e) {
              throw new CommandExecutionException(e);
          } finally {
              if (in != null) {
                  try {
                      in.close();
                  } catch (IOException e) {
                      throw new CommandExecutionException(e);
                  }
              }
          }
      }

   }
   
   
   private static void loadDroidSignatureFromPronom(String filePath) throws SignatureFileException, IOException {
      URI uri = new File(filePath).toURI();
      SaxSignatureFileParser parser = new SaxSignatureFileParser(uri);
      DirectoryProfileResource directoryResource = new DirectoryProfileResource(new File("G:/rghurbhurn/eclipse_workspace/commons-droid/target/classes"), false);
      
      ProfileInstanceManagerImpl profileInstanceManager = new ProfileInstanceManagerImpl();
      ProfileSpec profileSpec = new ProfileSpec();
      profileSpec.addResource(directoryResource);
      
      ProfileInstance profile = new ProfileInstance(ProfileState.STOPPED);
      profile.setProfileSpec(profileSpec);
      profile.setSignatureFileVersion(26);
      
      profileInstanceManager.setProfile(profile);
      profileInstanceManager.initProfile(new File("G:/rghurbhurn/eclipse_workspace/commons-droid/target/classes/DROID_SignatureFile_V63.xml").toURI());
      

      FormatCallback callback = new FormatCallback() {
         
         public void onFormat(Format format) {
            System.out.println(format.getName() + " / " + format.getPuid());
            
         }
      };

      parser.formats(callback);
      

   }
   
}
