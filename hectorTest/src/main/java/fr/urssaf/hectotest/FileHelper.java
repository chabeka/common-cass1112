package fr.urssaf.hectotest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHelper {

   // Récupère la dernière ligne d'un fichier
   public static String tail(File file) throws IOException {
      RandomAccessFile fileHandler = new RandomAccessFile(file, "r");
      long fileLength = file.length() - 1;
      StringBuilder sb = new StringBuilder();

      for (long filePointer = fileLength; filePointer != -1; filePointer--) {
         fileHandler.seek(filePointer);
         int readByte = fileHandler.readByte();

         if (readByte == 0xA) {
            if (filePointer == fileLength) {
               continue;
            } else {
               break;
            }
         } else if (readByte == 0xD) {
            if (filePointer == fileLength - 1) {
               continue;
            } else {
               break;
            }
         }

         sb.append((char) readByte);
      }
      String lastLine = sb.reverse().toString();
      return lastLine;
   }

   // Récupère les x dernières lignes d'un fichier
   public static String tail(File file, int lines) throws IOException {
      java.io.RandomAccessFile fileHandler = new java.io.RandomAccessFile(file,
            "r");
      long fileLength = file.length() - 1;
      StringBuilder sb = new StringBuilder();
      int line = 0;

      for (long filePointer = fileLength; filePointer != -1; filePointer--) {
         fileHandler.seek(filePointer);
         int readByte = fileHandler.readByte();

         if (readByte == 0xA) {
            line = line + 1;
            if (line == lines) {
               if (filePointer == fileLength) {
                  continue;
               } else {
                  break;
               }
            }
         }
         sb.append((char) readByte);
      }

      //sb.deleteCharAt(sb.length() - 1);
      String lastLine = sb.reverse().toString();
      fileHandler.close();
      return lastLine;
   }

   // Renvoie la 1ere ligne d'un fichier
   public static String head(String fileName) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line = br.readLine();
      br.close();
      return line;
   }

}
