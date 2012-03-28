

          var indexes = new Array();

          function hideAdd(inputName){
            var add = xGetElementById("add:"+inputName);
            xVisibility(add,false);
          }
          
          function showAdd(inputName){
            var add = xGetElementById("add:"+inputName);
            xVisibility(add,true);
          }
          
          function getIndex(key){
            if(indexes[key]){
              return ++indexes[key];
            }
            return indexes[key] = 2;
          }
          
          function add(inputName){
            var tbody = document.getElementById(inputName);
            var tr = document.createElement('tr');
            var td1 = document.createElement('td');
            
            var input = document.createElement('input');
            input.type = 'text';
            if(inputName.indexOf('@')==-1)
              input.name = inputName+'['+getIndex(inputName)+']';
            else
              input.name = inputName
	    var span = document.createElement('span');            
	    var a = document.createElement('a');
            var img = a.appendChild(document.createElement('img'));
            img.src = 'file://C:/temp/FormCreatorTest/icons/delete.png' 
            img.onclick = a.onclick;
            a.onclick = remove;
            td1.width = '100'
	    td1.appendChild(input);

            td1.appendChild(a);
            tr.appendChild(td1);
	    td1.appendChild(span);            
            tbody.appendChild(tr);
            var maxOccurs = tbody.attributes['maxoccurs']? tbody.attributes['maxoccurs'].value : 1;
            var inputCount = tbody.getElementsByTagName("tr").length;
            if(maxOccurs=='unbounded') return;
            var left = maxOccurs - inputCount;
            if(left < 0)
              hideAdd(inputName);
          }
          
          function getEventTarget(event){
            var e = event || window.event;
            if(!e) return;
            if(e.target) return e.target;
            return e.srcElement;
          }
          
          function remove(event){
            var row = getEventTarget(event).parentNode.parentNode.parentNode;
            var tbody = row.parentNode;
            tbody.removeChild(row);
            showAdd(tbody.id);
          }
          
          function removeFirst(path){
            var tbody = document.getElementById(path);
            var row = tbody.getElementsByTagName("tr")[1];
            tbody.removeChild(row);
            showAdd(tbody.id);
          }
          
          function isIE(){
            return navigator.appName == "Microsoft Internet Explorer";
          }