/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.schema.restriction

import com.predic8.schema.Schema

class RestrictionUtil {

  static def getRestriction(type, map) {
    switch(type) {
      case "string" :
      return new StringRestriction(map)
      case "normalizedString" :
      return new NormalizedStringRestriction(map)
      case "token" :
      return new TokenRestriction(map)
      case "base64Binary" :
      return new Base64BinaryRestriction(map)
      case "hexBinary" :
      return new HexBinaryRestriction(map)
      case "integer" :
      return new IntegerRestriction(map)
      case "positiveInteger" :
      return new PositiveIntegerRestriction(map)
      case "negativeInteger" :
      return new NegativeIntegerRestriction(map)
      case "nonNegativeInteger" :
      return new NonNegativeIntegerRestriction(map)
      case "nonPositiveInteger" :
      return new NonPositiveIntegerRestriction(map)
      case "long" :
      return new LongRestriction(map)
      case "unsignedLong" :
      return new UnsignedLongRestriction(map)
      case "int" :
      return new IntRestriction(map)
      case "unsignedInt" :
      return new UnsignedIntRestriction(map)
      case "short" :
      return new ShortRestriction(map)
      case "unsignedShort" :
      return new UnsignedShortRestriction(map)
      case "byte" :
      return new ByteRestriction(map)
      case "unsignedByte" :
      return new UnsignedByteRestriction(map)
      case "decimal" :
      return new DecimalRestriction(map)
      case "float" :
      return new FloatRestriction(map)
      
      case "double" :
      return new DoubleRestriction(map)
      
      case "boolean" :
      return new BooleanRestriction(map)
      
      case "duration" :
      return new DurationRestriction(map)
      
      case "dateTime" :
      return new DateTimeRestriction(map)
      
      case "date" :
      return new DateRestriction(map)
      
      case "time" :
      return new TimeRestriction(map)
      
      case "gYear" :
      return new GYearRestriction(map)
      
      case "gYearMonth" :
      return new GYearMonthRestriction(map)
      
      case "gMonth" :
      return new GMonthRestriction(map)
      
      case "gMonthDay" :
      return new GMonthDayRestriction(map)
      
      case "gDay" :
      return new GDayRestriction(map)
      
      case "Name" :
      return new NameRestriction(map)
      
      case "QName" :
      return new QNameRestriction(map)
      
      case "NCName" :
      return new NCNameRestriction(map)
      
      case "anyURI" :
      return new AnyURIRestriction(map)
      
      case "language" :
      return new LanguageRestriction(map)
      
      case "ID" :
      return new IDRestriction(map)
      
      case "IDREF" :
      return new IDREFRestriction(map)
      
      case "IDREFS" :
      return new IDREFSRestriction(map)
      
      case "ENTITY" :
      return new ENTITYRestriction(map)
      
      case "ENTITIES" :
      return new ENTITIESRestriction(map)
      
      case "NOTATION" :
      return new NOTATIONRestriction(map)
      
      case "NMTOKEN" :
      return new NMTOKENRestriction(map)
      
      case "NMTOKENS" :
      return new NMTOKENSRestriction(map)
      
      default : throw new Exception("Restriction $type not implemented yet")
    }
  }
}

