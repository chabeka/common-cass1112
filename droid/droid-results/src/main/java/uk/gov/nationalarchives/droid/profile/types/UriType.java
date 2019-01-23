/**
 * Copyright (c) 2012, The National Archives <pronom@nationalarchives.gsi.gov.uk>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following
 * conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of the The National Archives nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.gov.nationalarchives.droid.profile.types;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * @author rflitcroft
 */
public class UriType implements UserType {

  private static final long serialVersionUID = -5484571563787862267L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int[] sqlTypes() {
    return new int[] {Types.VARCHAR};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class returnedClass() {
    return URI.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object x, final Object y) throws HibernateException {
    return x != null && y != null && x.equals(y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode(final Object x) throws HibernateException {
    return x != null ? x.hashCode() : 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object nullSafeGet(final ResultSet rs, final String[] names, final SessionImplementor session, final Object owner)
      throws HibernateException, SQLException {
    try {
      return new URI(rs.getString(names[0]));
    }
    catch (final URISyntaxException e) {
      throw new HibernateException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void nullSafeSet(final PreparedStatement st, final Object value, final int index, final SessionImplementor session)
      throws HibernateException, SQLException {
    if (value == null) {
      st.setObject(index, null, Types.OTHER);
    } else {
      final URI theUri = (URI) value;
      st.setObject(index, theUri.toString(), Types.OTHER);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object deepCopy(final Object value) throws HibernateException {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMutable() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable disassemble(final Object value) throws HibernateException {
    return (Serializable) value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
    return cached;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
    return target;
  }

}
