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

package com.predic8.xml.util

import javax.xml.stream.*

class StAXUtil {
  
  def static getType(token) {
    if(token.isStartElement()) return 'START_ELEMENT'
    if(token.isEndElement()) return 'END_ELEMENT'
    if(token.isWhiteSpace()) return 'SPACE'
    if(token.isCharacters()) return 'CHARACTERS'
    else {
      switch(token.getEventType()) {
        case XMLStreamReader.ATTRIBUTE   : return 'ATTRIBUTE '
        case XMLStreamReader.CDATA  : return 'CDATA'
        case XMLStreamReader.NAMESPACE   : return 'NAMESPACE '
        case XMLStreamReader.COMMENT  : return 'COMMENT'
        case XMLStreamReader.START_DOCUMENT : return 'START_DOCUMENT'
        case XMLStreamReader.END_DOCUMENT : return 'END_DOCUMENT'
        case XMLStreamReader.END_DOCUMENT : return 'PROCESSING_INSTRUCTION'
        case XMLStreamReader.END_DOCUMENT : return 'ENTITY_REFERENCE'
        case XMLStreamReader.DTD  : return 'DTD'
      }
      return 'Unknown Element'
    }
  }
}
