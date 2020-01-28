/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TODO (AC75095351) Description du type
 *
 */
import javax.activation.DataSource;

/**
 * {@link DataSource} backed by a byte buffer.
 *
 * @author Kohsuke Kawaguchi
 *         Copie du code de ce fichier pour éviter d'utiliser la dépendance maven
 *         jaxb utilisé précédemment pour obtenir ce code
 */
public final class ByteArrayDataSource implements DataSource {

  private final String contentType;

  private final byte[] buf;

  private final int len;

  public ByteArrayDataSource(final byte[] buf, final String contentType) {
    this(buf, buf.length, contentType);
  }

  public ByteArrayDataSource(final byte[] buf, final int length, final String contentType) {
    this.buf = buf;
    len = length;
    this.contentType = contentType;
  }

  @Override
  public String getContentType() {
    if (contentType == null) {
      return "application/octet-stream";
    }
    return contentType;
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(buf, 0, len);
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public OutputStream getOutputStream() {
    throw new UnsupportedOperationException();
  }
}