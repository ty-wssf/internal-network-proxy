import{g as be,d as Ee}from"./amis-4161c463.js";function we(ee,he){for(var X=0;X<he.length;X++){const c=he[X];if(typeof c!="string"&&!Array.isArray(c)){for(const U in c)if(U!=="default"&&!(U in ee)){const G=Object.getOwnPropertyDescriptor(c,U);G&&Object.defineProperty(ee,U,G.get?G:{enumerable:!0,get:()=>c[U]})}}}return Object.freeze(Object.defineProperty(ee,Symbol.toStringTag,{value:"Module"}))}var me={exports:{}};/* @license
Papa Parse
v5.4.1
https://github.com/mholt/PapaParse
License: MIT
*/(function(ee,he){(function(X,c){ee.exports=c()})(Ee,function X(){var c=typeof self<"u"?self:typeof window<"u"?window:c!==void 0?c:{},U=!c.document&&!!c.postMessage,G=c.IS_PAPA_WORKER||!1,ne={},ve=0,f={parse:function(t,e){var r=(e=e||{}).dynamicTyping||!1;if(g(r)&&(e.dynamicTypingFunction=r,r={}),e.dynamicTyping=r,e.transform=!!g(e.transform)&&e.transform,e.worker&&f.WORKERS_SUPPORTED){var i=function(){if(!f.WORKERS_SUPPORTED)return!1;var h=(I=c.URL||c.webkitURL||null,b=X.toString(),f.BLOB_URL||(f.BLOB_URL=I.createObjectURL(new Blob(["var global = (function() { if (typeof self !== 'undefined') { return self; } if (typeof window !== 'undefined') { return window; } if (typeof global !== 'undefined') { return global; } return {}; })(); global.IS_PAPA_WORKER=true; ","(",b,")();"],{type:"text/javascript"})))),d=new c.Worker(h),I,b;return d.onmessage=ke,d.id=ve++,ne[d.id]=d}();return i.userStep=e.step,i.userChunk=e.chunk,i.userComplete=e.complete,i.userError=e.error,e.step=g(e.step),e.chunk=g(e.chunk),e.complete=g(e.complete),e.error=g(e.error),delete e.worker,void i.postMessage({input:t,config:e,workerId:i.id})}var s=null;return f.NODE_STREAM_INPUT,typeof t=="string"?(t=function(h){return h.charCodeAt(0)===65279?h.slice(1):h}(t),s=e.download?new se(e):new re(e)):t.readable===!0&&g(t.read)&&g(t.on)?s=new oe(e):(c.File&&t instanceof File||t instanceof Object)&&(s=new ae(e)),s.stream(t)},unparse:function(t,e){var r=!1,i=!0,s=",",h=`\r
`,d='"',I=d+d,b=!1,a=null,w=!1;(function(){if(typeof e=="object"){if(typeof e.delimiter!="string"||f.BAD_DELIMITERS.filter(function(n){return e.delimiter.indexOf(n)!==-1}).length||(s=e.delimiter),(typeof e.quotes=="boolean"||typeof e.quotes=="function"||Array.isArray(e.quotes))&&(r=e.quotes),typeof e.skipEmptyLines!="boolean"&&typeof e.skipEmptyLines!="string"||(b=e.skipEmptyLines),typeof e.newline=="string"&&(h=e.newline),typeof e.quoteChar=="string"&&(d=e.quoteChar),typeof e.header=="boolean"&&(i=e.header),Array.isArray(e.columns)){if(e.columns.length===0)throw new Error("Option columns is empty");a=e.columns}e.escapeChar!==void 0&&(I=e.escapeChar+d),(typeof e.escapeFormulae=="boolean"||e.escapeFormulae instanceof RegExp)&&(w=e.escapeFormulae instanceof RegExp?e.escapeFormulae:/^[=+\-@\t\r].*$/)}})();var u=new RegExp(ie(d),"g");if(typeof t=="string"&&(t=JSON.parse(t)),Array.isArray(t)){if(!t.length||Array.isArray(t[0]))return q(null,t,b);if(typeof t[0]=="object")return q(a||Object.keys(t[0]),t,b)}else if(typeof t=="object")return typeof t.data=="string"&&(t.data=JSON.parse(t.data)),Array.isArray(t.data)&&(t.fields||(t.fields=t.meta&&t.meta.fields||a),t.fields||(t.fields=Array.isArray(t.data[0])?t.fields:typeof t.data[0]=="object"?Object.keys(t.data[0]):[]),Array.isArray(t.data[0])||typeof t.data[0]=="object"||(t.data=[t.data])),q(t.fields||[],t.data||[],b);throw new Error("Unable to serialize unrecognized input");function q(n,y,S){var E="";typeof n=="string"&&(n=JSON.parse(n)),typeof y=="string"&&(y=JSON.parse(y));var T=Array.isArray(n)&&0<n.length,x=!Array.isArray(y[0]);if(T&&i){for(var A=0;A<n.length;A++)0<A&&(E+=s),E+=D(n[A],A);0<y.length&&(E+=h)}for(var o=0;o<y.length;o++){var l=T?n.length:y[o].length,v=!1,R=T?Object.keys(y[o]).length===0:y[o].length===0;if(S&&!T&&(v=S==="greedy"?y[o].join("").trim()==="":y[o].length===1&&y[o][0].length===0),S==="greedy"&&T){for(var _=[],L=0;L<l;L++){var C=x?n[L]:L;_.push(y[o][C])}v=_.join("").trim()===""}if(!v){for(var m=0;m<l;m++){0<m&&!R&&(E+=s);var j=T&&x?n[m]:m;E+=D(y[o][j],m)}o<y.length-1&&(!S||0<l&&!R)&&(E+=h)}}return E}function D(n,y){if(n==null)return"";if(n.constructor===Date)return JSON.stringify(n).slice(1,25);var S=!1;w&&typeof n=="string"&&w.test(n)&&(n="'"+n,S=!0);var E=n.toString().replace(u,I);return(S=S||r===!0||typeof r=="function"&&r(n,y)||Array.isArray(r)&&r[y]||function(T,x){for(var A=0;A<x.length;A++)if(-1<T.indexOf(x[A]))return!0;return!1}(E,f.BAD_DELIMITERS)||-1<E.indexOf(s)||E.charAt(0)===" "||E.charAt(E.length-1)===" ")?d+E+d:E}}};if(f.RECORD_SEP=String.fromCharCode(30),f.UNIT_SEP=String.fromCharCode(31),f.BYTE_ORDER_MARK="\uFEFF",f.BAD_DELIMITERS=["\r",`
`,'"',f.BYTE_ORDER_MARK],f.WORKERS_SUPPORTED=!U&&!!c.Worker,f.NODE_STREAM_INPUT=1,f.LocalChunkSize=10485760,f.RemoteChunkSize=5242880,f.DefaultDelimiter=",",f.Parser=fe,f.ParserHandle=ce,f.NetworkStreamer=se,f.FileStreamer=ae,f.StringStreamer=re,f.ReadableStreamStreamer=oe,c.jQuery){var te=c.jQuery;te.fn.parse=function(t){var e=t.config||{},r=[];return this.each(function(h){if(!(te(this).prop("tagName").toUpperCase()==="INPUT"&&te(this).attr("type").toLowerCase()==="file"&&c.FileReader)||!this.files||this.files.length===0)return!0;for(var d=0;d<this.files.length;d++)r.push({file:this.files[d],inputElem:this,instanceConfig:te.extend({},e)})}),i(),this;function i(){if(r.length!==0){var h,d,I,b,a=r[0];if(g(t.before)){var w=t.before(a.file,a.inputElem);if(typeof w=="object"){if(w.action==="abort")return h="AbortError",d=a.file,I=a.inputElem,b=w.reason,void(g(t.error)&&t.error({name:h},d,I,b));if(w.action==="skip")return void s();typeof w.config=="object"&&(a.instanceConfig=te.extend(a.instanceConfig,w.config))}else if(w==="skip")return void s()}var u=a.instanceConfig.complete;a.instanceConfig.complete=function(q){g(u)&&u(q,a.file,a.inputElem),s()},f.parse(a.file,a.instanceConfig)}else g(t.complete)&&t.complete()}function s(){r.splice(0,1),i()}}}function B(t){this._handle=null,this._finished=!1,this._completed=!1,this._halted=!1,this._input=null,this._baseIndex=0,this._partialLine="",this._rowCount=0,this._start=0,this._nextChunk=null,this.isFirstChunk=!0,this._completeResults={data:[],errors:[],meta:{}},(function(e){var r=de(e);r.chunkSize=parseInt(r.chunkSize),e.step||e.chunk||(r.chunkSize=null),this._handle=new ce(r),(this._handle.streamer=this)._config=r}).call(this,t),this.parseChunk=function(e,r){if(this.isFirstChunk&&g(this._config.beforeFirstChunk)){var i=this._config.beforeFirstChunk(e);i!==void 0&&(e=i)}this.isFirstChunk=!1,this._halted=!1;var s=this._partialLine+e;this._partialLine="";var h=this._handle.parse(s,this._baseIndex,!this._finished);if(!this._handle.paused()&&!this._handle.aborted()){var d=h.meta.cursor;this._finished||(this._partialLine=s.substring(d-this._baseIndex),this._baseIndex=d),h&&h.data&&(this._rowCount+=h.data.length);var I=this._finished||this._config.preview&&this._rowCount>=this._config.preview;if(G)c.postMessage({results:h,workerId:f.WORKER_ID,finished:I});else if(g(this._config.chunk)&&!r){if(this._config.chunk(h,this._handle),this._handle.paused()||this._handle.aborted())return void(this._halted=!0);h=void 0,this._completeResults=void 0}return this._config.step||this._config.chunk||(this._completeResults.data=this._completeResults.data.concat(h.data),this._completeResults.errors=this._completeResults.errors.concat(h.errors),this._completeResults.meta=h.meta),this._completed||!I||!g(this._config.complete)||h&&h.meta.aborted||(this._config.complete(this._completeResults,this._input),this._completed=!0),I||h&&h.meta.paused||this._nextChunk(),h}this._halted=!0},this._sendError=function(e){g(this._config.error)?this._config.error(e):G&&this._config.error&&c.postMessage({workerId:f.WORKER_ID,error:e,finished:!1})}}function se(t){var e;(t=t||{}).chunkSize||(t.chunkSize=f.RemoteChunkSize),B.call(this,t),this._nextChunk=U?function(){this._readChunk(),this._chunkLoaded()}:function(){this._readChunk()},this.stream=function(r){this._input=r,this._nextChunk()},this._readChunk=function(){if(this._finished)this._chunkLoaded();else{if(e=new XMLHttpRequest,this._config.withCredentials&&(e.withCredentials=this._config.withCredentials),U||(e.onload=H(this._chunkLoaded,this),e.onerror=H(this._chunkError,this)),e.open(this._config.downloadRequestBody?"POST":"GET",this._input,!U),this._config.downloadRequestHeaders){var r=this._config.downloadRequestHeaders;for(var i in r)e.setRequestHeader(i,r[i])}if(this._config.chunkSize){var s=this._start+this._config.chunkSize-1;e.setRequestHeader("Range","bytes="+this._start+"-"+s)}try{e.send(this._config.downloadRequestBody)}catch(h){this._chunkError(h.message)}U&&e.status===0&&this._chunkError()}},this._chunkLoaded=function(){e.readyState===4&&(e.status<200||400<=e.status?this._chunkError():(this._start+=this._config.chunkSize?this._config.chunkSize:e.responseText.length,this._finished=!this._config.chunkSize||this._start>=function(r){var i=r.getResponseHeader("Content-Range");return i===null?-1:parseInt(i.substring(i.lastIndexOf("/")+1))}(e),this.parseChunk(e.responseText)))},this._chunkError=function(r){var i=e.statusText||r;this._sendError(new Error(i))}}function ae(t){var e,r;(t=t||{}).chunkSize||(t.chunkSize=f.LocalChunkSize),B.call(this,t);var i=typeof FileReader<"u";this.stream=function(s){this._input=s,r=s.slice||s.webkitSlice||s.mozSlice,i?((e=new FileReader).onload=H(this._chunkLoaded,this),e.onerror=H(this._chunkError,this)):e=new FileReaderSync,this._nextChunk()},this._nextChunk=function(){this._finished||this._config.preview&&!(this._rowCount<this._config.preview)||this._readChunk()},this._readChunk=function(){var s=this._input;if(this._config.chunkSize){var h=Math.min(this._start+this._config.chunkSize,this._input.size);s=r.call(s,this._start,h)}var d=e.readAsText(s,this._config.encoding);i||this._chunkLoaded({target:{result:d}})},this._chunkLoaded=function(s){this._start+=this._config.chunkSize,this._finished=!this._config.chunkSize||this._start>=this._input.size,this.parseChunk(s.target.result)},this._chunkError=function(){this._sendError(e.error)}}function re(t){var e;B.call(this,t=t||{}),this.stream=function(r){return e=r,this._nextChunk()},this._nextChunk=function(){if(!this._finished){var r,i=this._config.chunkSize;return i?(r=e.substring(0,i),e=e.substring(i)):(r=e,e=""),this._finished=!e,this.parseChunk(r)}}}function oe(t){B.call(this,t=t||{});var e=[],r=!0,i=!1;this.pause=function(){B.prototype.pause.apply(this,arguments),this._input.pause()},this.resume=function(){B.prototype.resume.apply(this,arguments),this._input.resume()},this.stream=function(s){this._input=s,this._input.on("data",this._streamData),this._input.on("end",this._streamEnd),this._input.on("error",this._streamError)},this._checkIsFinished=function(){i&&e.length===1&&(this._finished=!0)},this._nextChunk=function(){this._checkIsFinished(),e.length?this.parseChunk(e.shift()):r=!0},this._streamData=H(function(s){try{e.push(typeof s=="string"?s:s.toString(this._config.encoding)),r&&(r=!1,this._checkIsFinished(),this.parseChunk(e.shift()))}catch(h){this._streamError(h)}},this),this._streamError=H(function(s){this._streamCleanUp(),this._sendError(s)},this),this._streamEnd=H(function(){this._streamCleanUp(),i=!0,this._streamData("")},this),this._streamCleanUp=H(function(){this._input.removeListener("data",this._streamData),this._input.removeListener("end",this._streamEnd),this._input.removeListener("error",this._streamError)},this)}function ce(t){var e,r,i,s=Math.pow(2,53),h=-s,d=/^\s*-?(\d+\.?|\.\d+|\d+\.\d+)([eE][-+]?\d+)?\s*$/,I=/^((\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d\.\d+([+-][0-2]\d:[0-5]\d|Z))|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d([+-][0-2]\d:[0-5]\d|Z))|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d([+-][0-2]\d:[0-5]\d|Z)))$/,b=this,a=0,w=0,u=!1,q=!1,D=[],n={data:[],errors:[],meta:{}};if(g(t.step)){var y=t.step;t.step=function(o){if(n=o,T())E();else{if(E(),n.data.length===0)return;a+=o.data.length,t.preview&&a>t.preview?r.abort():(n.data=n.data[0],y(n,b))}}}function S(o){return t.skipEmptyLines==="greedy"?o.join("").trim()==="":o.length===1&&o[0].length===0}function E(){return n&&i&&(A("Delimiter","UndetectableDelimiter","Unable to auto-detect delimiting character; defaulted to '"+f.DefaultDelimiter+"'"),i=!1),t.skipEmptyLines&&(n.data=n.data.filter(function(o){return!S(o)})),T()&&function(){if(!n)return;function o(v,R){g(t.transformHeader)&&(v=t.transformHeader(v,R)),D.push(v)}if(Array.isArray(n.data[0])){for(var l=0;T()&&l<n.data.length;l++)n.data[l].forEach(o);n.data.splice(0,1)}else n.data.forEach(o)}(),function(){if(!n||!t.header&&!t.dynamicTyping&&!t.transform)return n;function o(v,R){var _,L=t.header?{}:[];for(_=0;_<v.length;_++){var C=_,m=v[_];t.header&&(C=_>=D.length?"__parsed_extra":D[_]),t.transform&&(m=t.transform(m,C)),m=x(C,m),C==="__parsed_extra"?(L[C]=L[C]||[],L[C].push(m)):L[C]=m}return t.header&&(_>D.length?A("FieldMismatch","TooManyFields","Too many fields: expected "+D.length+" fields but parsed "+_,w+R):_<D.length&&A("FieldMismatch","TooFewFields","Too few fields: expected "+D.length+" fields but parsed "+_,w+R)),L}var l=1;return!n.data.length||Array.isArray(n.data[0])?(n.data=n.data.map(o),l=n.data.length):n.data=o(n.data,0),t.header&&n.meta&&(n.meta.fields=D),w+=l,n}()}function T(){return t.header&&D.length===0}function x(o,l){return v=o,t.dynamicTypingFunction&&t.dynamicTyping[v]===void 0&&(t.dynamicTyping[v]=t.dynamicTypingFunction(v)),(t.dynamicTyping[v]||t.dynamicTyping)===!0?l==="true"||l==="TRUE"||l!=="false"&&l!=="FALSE"&&(function(R){if(d.test(R)){var _=parseFloat(R);if(h<_&&_<s)return!0}return!1}(l)?parseFloat(l):I.test(l)?new Date(l):l===""?null:l):l;var v}function A(o,l,v,R){var _={type:o,code:l,message:v};R!==void 0&&(_.row=R),n.errors.push(_)}this.parse=function(o,l,v){var R=t.quoteChar||'"';if(t.newline||(t.newline=function(C,m){C=C.substring(0,1048576);var j=new RegExp(ie(m)+"([^]*?)"+ie(m),"gm"),F=(C=C.replace(j,"")).split("\r"),N=C.split(`
`),K=1<N.length&&N[0].length<F[0].length;if(F.length===1||K)return`
`;for(var z=0,k=0;k<F.length;k++)F[k][0]===`
`&&z++;return z>=F.length/2?`\r
`:"\r"}(o,R)),i=!1,t.delimiter)g(t.delimiter)&&(t.delimiter=t.delimiter(o),n.meta.delimiter=t.delimiter);else{var _=function(C,m,j,F,N){var K,z,k,O;N=N||[",","	","|",";",f.RECORD_SEP,f.UNIT_SEP];for(var Z=0;Z<N.length;Z++){var p=N[Z],V=0,W=0,Y=0;k=void 0;for(var Q=new fe({comments:F,delimiter:p,newline:m,preview:10}).parse(C),$=0;$<Q.data.length;$++)if(j&&S(Q.data[$]))Y++;else{var J=Q.data[$].length;W+=J,k!==void 0?0<J&&(V+=Math.abs(J-k),k=J):k=J}0<Q.data.length&&(W/=Q.data.length-Y),(z===void 0||V<=z)&&(O===void 0||O<W)&&1.99<W&&(z=V,K=p,O=W)}return{successful:!!(t.delimiter=K),bestDelimiter:K}}(o,t.newline,t.skipEmptyLines,t.comments,t.delimitersToGuess);_.successful?t.delimiter=_.bestDelimiter:(i=!0,t.delimiter=f.DefaultDelimiter),n.meta.delimiter=t.delimiter}var L=de(t);return t.preview&&t.header&&L.preview++,e=o,r=new fe(L),n=r.parse(e,l,v),E(),u?{meta:{paused:!0}}:n||{meta:{paused:!1}}},this.paused=function(){return u},this.pause=function(){u=!0,r.abort(),e=g(t.chunk)?"":e.substring(r.getCharIndex())},this.resume=function(){b.streamer._halted?(u=!1,b.streamer.parseChunk(e,!0)):setTimeout(b.resume,3)},this.aborted=function(){return q},this.abort=function(){q=!0,r.abort(),n.meta.aborted=!0,g(t.complete)&&t.complete(n),e=""}}function ie(t){return t.replace(/[.*+?^${}()|[\]\\]/g,"\\$&")}function fe(t){var e,r=(t=t||{}).delimiter,i=t.newline,s=t.comments,h=t.step,d=t.preview,I=t.fastMode,b=e=t.quoteChar===void 0||t.quoteChar===null?'"':t.quoteChar;if(t.escapeChar!==void 0&&(b=t.escapeChar),(typeof r!="string"||-1<f.BAD_DELIMITERS.indexOf(r))&&(r=","),s===r)throw new Error("Comment character same as delimiter");s===!0?s="#":(typeof s!="string"||-1<f.BAD_DELIMITERS.indexOf(s))&&(s=!1),i!==`
`&&i!=="\r"&&i!==`\r
`&&(i=`
`);var a=0,w=!1;this.parse=function(u,q,D){if(typeof u!="string")throw new Error("Input must be a string");var n=u.length,y=r.length,S=i.length,E=s.length,T=g(h),x=[],A=[],o=[],l=a=0;if(!u)return M();if(t.header&&!q){var v=u.split(i)[0].split(r),R=[],_={},L=!1;for(var C in v){var m=v[C];g(t.transformHeader)&&(m=t.transformHeader(m,C));var j=m,F=_[m]||0;for(0<F&&(L=!0,j=m+"_"+F),_[m]=F+1;R.includes(j);)j=j+"_"+F;R.push(j)}if(L){var N=u.split(i);N[0]=R.join(r),u=N.join(i)}}if(I||I!==!1&&u.indexOf(e)===-1){for(var K=u.split(i),z=0;z<K.length;z++){if(o=K[z],a+=o.length,z!==K.length-1)a+=i.length;else if(D)return M();if(!s||o.substring(0,E)!==s){if(T){if(x=[],Y(o.split(r)),ue(),w)return M()}else Y(o.split(r));if(d&&d<=z)return x=x.slice(0,d),M(!0)}}return M()}for(var k=u.indexOf(r,a),O=u.indexOf(i,a),Z=new RegExp(ie(b)+ie(e),"g"),p=u.indexOf(e,a);;)if(u[a]!==e)if(s&&o.length===0&&u.substring(a,a+E)===s){if(O===-1)return M();a=O+S,O=u.indexOf(i,a),k=u.indexOf(r,a)}else if(k!==-1&&(k<O||O===-1))o.push(u.substring(a,k)),a=k+y,k=u.indexOf(r,a);else{if(O===-1)break;if(o.push(u.substring(a,O)),J(O+S),T&&(ue(),w))return M();if(d&&x.length>=d)return M(!0)}else for(p=a,a++;;){if((p=u.indexOf(e,p+1))===-1)return D||A.push({type:"Quotes",code:"MissingQuotes",message:"Quoted field unterminated",row:x.length,index:a}),$();if(p===n-1)return $(u.substring(a,p).replace(Z,e));if(e!==b||u[p+1]!==b){if(e===b||p===0||u[p-1]!==b){k!==-1&&k<p+1&&(k=u.indexOf(r,p+1)),O!==-1&&O<p+1&&(O=u.indexOf(i,p+1));var V=Q(O===-1?k:Math.min(k,O));if(u.substr(p+1+V,y)===r){o.push(u.substring(a,p).replace(Z,e)),u[a=p+1+V+y]!==e&&(p=u.indexOf(e,a)),k=u.indexOf(r,a),O=u.indexOf(i,a);break}var W=Q(O);if(u.substring(p+1+W,p+1+W+S)===i){if(o.push(u.substring(a,p).replace(Z,e)),J(p+1+W+S),k=u.indexOf(r,a),p=u.indexOf(e,a),T&&(ue(),w))return M();if(d&&x.length>=d)return M(!0);break}A.push({type:"Quotes",code:"InvalidQuotes",message:"Trailing quote on quoted field is malformed",row:x.length,index:a}),p++}}else p++}return $();function Y(P){x.push(P),l=a}function Q(P){var _e=0;if(P!==-1){var le=u.substring(p+1,P);le&&le.trim()===""&&(_e=le.length)}return _e}function $(P){return D||(P===void 0&&(P=u.substring(a)),o.push(P),a=n,Y(o),T&&ue()),M()}function J(P){a=P,Y(o),o=[],O=u.indexOf(i,a)}function M(P){return{data:x,errors:A,meta:{delimiter:r,linebreak:i,aborted:w,truncated:!!P,cursor:l+(q||0)}}}function ue(){h(M()),x=[],A=[]}},this.abort=function(){w=!0},this.getCharIndex=function(){return a}}function ke(t){var e=t.data,r=ne[e.workerId],i=!1;if(e.error)r.userError(e.error,e.file);else if(e.results&&e.results.data){var s={abort:function(){i=!0,pe(e.workerId,{data:[],errors:[],meta:{aborted:!0}})},pause:ge,resume:ge};if(g(r.userStep)){for(var h=0;h<e.results.data.length&&(r.userStep({data:e.results.data[h],errors:e.results.errors,meta:e.results.meta},s),!i);h++);delete e.results}else g(r.userChunk)&&(r.userChunk(e.results,s,e.file),delete e.results)}e.finished&&!i&&pe(e.workerId,e.results)}function pe(t,e){var r=ne[t];g(r.userComplete)&&r.userComplete(e),r.terminate(),delete ne[t]}function ge(){throw new Error("Not implemented.")}function de(t){if(typeof t!="object"||t===null)return t;var e=Array.isArray(t)?[]:{};for(var r in t)e[r]=de(t[r]);return e}function H(t,e){return function(){t.apply(e,arguments)}}function g(t){return typeof t=="function"}return G&&(c.onmessage=function(t){var e=t.data;if(f.WORKER_ID===void 0&&e&&(f.WORKER_ID=e.workerId),typeof e.input=="string")c.postMessage({workerId:f.WORKER_ID,results:f.parse(e.input,e.config),finished:!0});else if(c.File&&e.input instanceof File||e.input instanceof Object){var r=f.parse(e.input,e.config);r&&c.postMessage({workerId:f.WORKER_ID,results:r,finished:!0})}}),(se.prototype=Object.create(B.prototype)).constructor=se,(ae.prototype=Object.create(B.prototype)).constructor=ae,(re.prototype=Object.create(re.prototype)).constructor=re,(oe.prototype=Object.create(B.prototype)).constructor=oe,f})})(me);var ye=me.exports;const Ce=be(ye),Re=we({__proto__:null,default:Ce},[ye]);export{Re as p};
