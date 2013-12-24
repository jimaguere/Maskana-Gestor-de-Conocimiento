package org.apache.lucene.analysis.cn;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource.AttributeFactory;

/** 
 * Factory for {@link ChineseTokenizer}
 * @deprecated Use {@link org.apache.lucene.analysis.standard.StandardTokenizerFactory} instead.
 */
@Deprecated
public class ChineseTokenizerFactory extends TokenizerFactory {
  
  /** Creates a new ChineseTokenizerFactory */
  public ChineseTokenizerFactory(Map<String,String> args) {
    super(args);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
  }
  
  @Override
  public ChineseTokenizer create(AttributeFactory factory, Reader in) {
    return new ChineseTokenizer(factory, in);
  }
}

