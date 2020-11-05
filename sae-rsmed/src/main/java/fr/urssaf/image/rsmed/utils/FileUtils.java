package fr.urssaf.image.rsmed.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {

    }

    public static final String getHash(final String filePath) throws IOException {
        try {
            final File f = new File(filePath);
            final InputStream contenu = new FileInputStream(f);

            final String hash = DigestUtils.sha1Hex(contenu);
            LOGGER.debug("File: {}, Hash: {}", filePath, hash);

        return hash;
        }catch (IOException e){
            throw new IOException(e);
        }
    }


}
