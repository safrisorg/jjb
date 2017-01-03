/* Copyright (c) 2015 Seva Safris
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.safris.xjb.runtime;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import org.safris.commons.util.CachedReader;

public abstract class JSObject extends JSObjectUtil {
  @SuppressWarnings("unchecked")
  public static <T extends JSObject>T parse(final Class<?> type, final Reader reader) throws DecodeException, IOException {
    try {
      final CachedReader stringBuilderReader = reader instanceof CachedReader ? (CachedReader) reader : new CachedReader(reader, new StringBuilder());
      final char ch = next(stringBuilderReader);

      if (ch == '[')
        return (T)decodeValue(ch, stringBuilderReader, type, null);

      if (!JSObject.class.isAssignableFrom(type))
        throw new DecodeException("[" + stringBuilderReader.getLength() + "] Expected a JSObject type " + type.getName(), stringBuilderReader.readFully(), null);

      return (T)decode(stringBuilderReader, ch, ((Class<T>)type).newInstance());
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  public JSObject(final JSObject object) {
  }

  public JSObject() {
  }

  protected String _encode(final int depth) {
    return "";
  }

  protected abstract String _name();
  protected abstract Binding<?> _getBinding(final String name);
  protected abstract Collection<Binding<?>> _bindings();
  protected abstract boolean _skipUnknown();
  protected abstract JSBundle _bundle();
}