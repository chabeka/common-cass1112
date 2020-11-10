package fr.urssaf.image.rsmed.utils;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static final String EXTENSION_XML = ".xml";
    public static final String EXTENSION_ZIP = ".zip";

    public static String getHash(String filePath) throws IOException {

        final File f = new File(filePath);
        final InputStream contenu = new FileInputStream(f);

        final String hash = DigestUtils.sha1Hex(contenu);

        return hash;

    }


}
