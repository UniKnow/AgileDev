/*
 * 
 * Copyright 2006 TheLadders.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */

/**
 * client-side indexing and searching.
 * 
 * @author Larry Ogrodnek <larry@theladders.com>
 */

// start RpG
function print(what) {
	var div = document.getElementById('searchOutput');
	div.innerHTML += what;
}

function clr() {
	var div = document.getElementById('searchOutput');
	div.innerHTML = '';
}

function clr2() {
	var div = document.getElementById('searchOutput');
	div.innerHTML = '';
	var q = document.getElementById('q');
	q.value = '';
}

function go(q) {
	clr();
	var searchTerms = [q];
	
	for (var w in searchTerms) {
		print("String <b>" + searchTerms[w] + "</b> found in:");
		print("<ul>");
		var result = index.search(searchTerms[w]);
		for (var r in result) {
			print('<li><a href="' + result[r] + '">' + titles.get(result[r]) + '</a></li>');
		}
		print("</ul>");
	}
}

// end RpG


// kind of hacky, might be better to have a conventional namespace include ala yui.
try
{
  eval("LADDERS");
}
catch(e)
{
  LADDERS = {};
}
    
LADDERS.search = {};

LADDERS.search =
{
    /**
     * Search Index.
	 *     
     * @constructor
     */
	index: function()
	{
	  this._index = {};
	  this._analyzer = new LADDERS.search.StandardAnalyzer();
	},

	/**
	 * A document to be indexed.
	 *
	 * @constructor
	 */
	document:function()
	{
		this._fields = {};
	},

    /**
     * Search hit.
     *
     * @constructor
     */
	hit: function()
	{
	  this._matchedFields = 0;
	  this._score = 0;
	},

    /**
     * StandardAnalyzer
     * @constructor
     */
	StandardAnalyzer: function()
	{
	},

	/**
	 * Default list of stop words.  These words will be removed from
	 * both indexed text and query strings.
	 */
	STOP_WORDS:
	[
	  "a", "an", "and", "are", "as", "at", "be", "but",
  	  "by", "for", "if", "in", "into", "is", "it", "no",
	  "no", "not", "of", "on", "or", "s", "such", "t",
	  "that", "the", "their", "they", "then", "there", 
  	  "these", "this", "to", "was", "will", "with"
	]
};


/**
 * Initialize stop words.
 */
LADDERS.search.StandardAnalyzer.prototype._stopWords = function(stopWords)
  {
    var result = {};
	  for (var i=0; i< stopWords.length; i++)
	  {
	    result[stopWords[i]] = 1;
	  }
	  return result;
  }(LADDERS.search.STOP_WORDS);


/**
 * Converts a string into an array of tokens, removing stopwords and punctuation.
 */
LADDERS.search.StandardAnalyzer.prototype.tokens = function(s)
{
  var results = new Array();

  var words = s.toLowerCase().replace(/-/g, " ").split(/\s+/);

  for (var i=0; i< words.length; i++)
  {
    var w = words[i].replace(/[,\.'"-]+/g, "");
	  
	if (w.length > 0 && (! this._stopWords[w]))
	{
	  results[results.length] = w;
	}
  }

  return results;
}

var titles = new LADDERS.search.document();

/**
 * Add a document to the index.
 *
 */
LADDERS.search.index.prototype.addDocument = function(d)
{
  //RpG
  var title = (d._fields['title']);
  titles.add(d._fields.id, title); 
  
  for (var f in d._fields)
  {
    // only index string fields
    if (typeof d._fields[f] == 'string')
	{
	  var words = this._analyzer.tokens(d._fields[f]);

	  for (var i=0; i< words.length; i++)
	  {
	    var word = words[i];

	    var id = d._fields.id;	    
	    this._markOccurance(id, word);
      }  
    }
  }  
};

LADDERS.search.index.prototype._getOccurances = function(word)
{
  var c = word.charAt(0);
  
  if (! this._index[c])
  {
     this._index[c] = {};
  }
  
  if (! this._index[c][word])
  {
    this._index[c][word] = {};
  }
  
  return this._index[c][word];
};

LADDERS.search.index.prototype._markOccurance = function(id, word)
{
  var a = this._getOccurances(word);
  
  if (! a[id])
  {
    a[id] = 1;
  }
  else
  {
    a[id]++;
  }
};

/**
 * Index search.  
 *
 */
LADDERS.search.index.prototype.search = function(query)
{
  var words = this._analyzer.tokens(query);

  var results = {};

  var findWild = /(.+)\*$/;
		
  for (var i=0; i< words.length; i++)
  {
    var w;

	var match = words[i].match(findWild);
	
	// we have a keyword with a wildcard  
	if (match != null)
    {
	  w = this._escape(match[1]);

	  var wr = this.searchWildcard(w);
		   
      for (var j=0; j< wr.length; j++)
	  {
	    // new document match
	    if (! results[wr[j]])
	    {
	      results[wr[j]] = new LADDERS.search.hit();
	    }
			
  	    // increase document score		
		results[wr[j]]._matchedFields++;
		results[wr[j]]._score += 1;
      }

	  continue;
    }

    // we have a non-wildcard keyword
	w = this._escape(words[i]);
	  
	// in index?
	var docs = this._getOccurances(w);
	
	if (docs)
	{
	  for (var d in docs)
	  {
	    // new document match
	    if (! results[d])
	    {
	      results[d] = new LADDERS.search.hit();
	    }

		//increase document score
		results[d]._matchedFields++;
		results[d]._score += docs[d];
	  }
	}
  }

  var ar = new Array();
  
  // AND match, all words must match
  var needed = words.length;
  
  for (var r in results)
  {
    var hit = results[r];

	if (hit._matchedFields == needed)
	{
	  ar[ar.length] = r;
    }
  }

  return ar;
};


/**
 * Perform a "starts-with" match against the index.
 */
LADDERS.search.index.prototype.searchWildcard = function(word)
{
  var pattern = new RegExp(word.toLowerCase() + ".*", "g");

  var results = {};

  // go through all words in the index...
  
  var possible = this._index[word.charAt(0)];
  
  for (var k in possible)
  {
    if (pattern.test(k))
    {
	  for (var d in possible[k])
      {
        // always mark as one hit
	    results[d] = 1;
      }
    }
  }

  var ar = new Array();
  for (d in results)
  {
    ar[ar.length] = d;
  }	  

  return ar;
}

/**
 * escape our strings.
 */
LADDERS.search.index.prototype._escape =
  function(word)
  {
	  var quote = /([\\\!\@\#\$\%\^\&\.\+\?])/g;
	  return word.replace(quote, "\\$1");
  }
    
/**
 * Add a fieldName, fieldText pair to the document.
 *
 * Adding the same field more than once erases the previous contents.
 *
 */
LADDERS.search.document.prototype.add = 
	function(k, v)
	{
		this._fields[k] = v;
	};
  
/**
 * Get the fieldText for a given fieldName from a document.
 *
 */    
LADDERS.search.document.prototype.get =
	function(k)
	{
		return this._fields[k];
	};
