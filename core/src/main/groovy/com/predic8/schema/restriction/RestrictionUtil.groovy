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

  static def getRestriction(type, ctx) {
    switch(type) {
	  case "anySimpleType" :
	  return new AnySimpleTypeRestriction(ctx)
	  case "anyType" :
      return new AnyTypeRestriction(ctx)
      case "string" :
      return new StringRestriction(ctx)
      case "normalizedString" :
      return new NormalizedStringRestriction(ctx)
      case "token" :
      return new TokenRestriction(ctx)
      case "base64Binary" :
      return new Base64BinaryRestriction(ctx)
      case "hexBinary" :
      return new HexBinaryRestriction(ctx)
      case "integer" :
      return new IntegerRestriction(ctx)
      case "positiveInteger" :
      return new PositiveIntegerRestriction(ctx)
      case "negativeInteger" :
      return new NegativeIntegerRestriction(ctx)
      case "nonNegativeInteger" :
      return new NonNegativeIntegerRestriction(ctx)
      case "nonPositiveInteger" :
      return new NonPositiveIntegerRestriction(ctx)
      case "long" :
      return new LongRestriction(ctx)
      case "unsignedLong" :
      return new UnsignedLongRestriction(ctx)
      case "int" :
      return new IntRestriction(ctx)
      case "unsignedInt" :
      return new UnsignedIntRestriction(ctx)
      case "short" :
      return new ShortRestriction(ctx)
      case "unsignedShort" :
      return new UnsignedShortRestriction(ctx)
      case "byte" :
      return new ByteRestriction(ctx)
      case "unsignedByte" :
      return new UnsignedByteRestriction(ctx)
      case "decimal" :
      return new DecimalRestriction(ctx)
      case "float" :
      return new FloatRestriction(ctx)
      
      case "double" :
      return new DoubleRestriction(ctx)
      
      case "boolean" :
      return new BooleanRestriction(ctx)
      
      case "duration" :
      return new DurationRestriction(ctx)
      
      case "dateTime" :
      return new DateTimeRestriction(ctx)
      
      case "date" :
      return new DateRestriction(ctx)
      
      case "time" :
      return new TimeRestriction(ctx)
      
      case "gYear" :
      return new GYearRestriction(ctx)
      
      case "gYearMonth" :
      return new GYearMonthRestriction(ctx)
      
      case "gMonth" :
      return new GMonthRestriction(ctx)
      
      case "gMonthDay" :
      return new GMonthDayRestriction(ctx)
      
      case "gDay" :
      return new GDayRestriction(ctx)
      
      case "Name" :
      return new NameRestriction(ctx)
      
      case "QName" :
      return new QNameRestriction(ctx)
      
      case "NCName" :
      return new NCNameRestriction(ctx)
      
      case "anyURI" :
      return new AnyURIRestriction(ctx)
      
      case "language" :
      return new LanguageRestriction(ctx)
      
      case "ID" :
      return new IDRestriction(ctx)
      
      case "IDREF" :
      return new IDREFRestriction(ctx)
      
      case "IDREFS" :
      return new IDREFSRestriction(ctx)
      
      case "ENTITY" :
      return new ENTITYRestriction(ctx)
      
      case "ENTITIES" :
      return new ENTITIESRestriction(ctx)
      
      case "NOTATION" :
      return new NOTATIONRestriction(ctx)
      
      case "NMTOKEN" :
      return new NMTOKENRestriction(ctx)
      
      case "NMTOKENS" :
      	return new NMTOKENSRestriction(ctx)
      	

      	
      case "derivationControl" :
      	return new DerivationControlRestriction(ctx)
      	
      case "allNNI" :
      	return new AllNNIRestriction(ctx)
      	
      default : throw new Exception("Restriction $type not implemented yet")
    }
  }
}

