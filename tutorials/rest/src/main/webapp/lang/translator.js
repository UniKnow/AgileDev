/*
 * Copyright (C) 2014 uniknow. All rights reserved.
 *
 * This Java class is subject of the following restrictions:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must  retain the above copyright  notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include  the following  acknowledgment:  "This product includes  software
 *    developed  by uniknow."
 *    Alternately, this  acknowledgment may  appear in the software itself,  if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name ''uniknow'' must  not  be used to  endorse or promote  products
 *    derived from  this software without prior written permission.
 *
 * 5. Products  derived from this software may not  be called ''UniKnow'', nor
 *    may ''uniknow'' appear  in their name,  without prior written permission
 *    of uniknow.
 *
 * THIS SOFTWARE IS PROVIDED ''AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  WWS
 * OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY,  OR  CONSEQUENTIAL   DAMAGES   (INCLUDING, BUT  NOT  LIMITED  TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOS  OF USE, DATA, OR  PROFITS;
 * OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON ANY  THEORY  OF LIABILITY,
 * WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT (INCLUDING  NEGLIGENCE  OR
 * OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE  OF THIS  SOFTWARE,  EVEN  IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
'use strict';

/**
 * Translator for documentation pages.
 *
 * To enable translation you should include one of language-files in your index.html
 * after <script src='lang/translator.js' type='text/javascript'></script>.
 * For example - <script src='lang/ru.js' type='text/javascript'></script>
 *
 * If you wish to translate some new texts you should do two things:
 * 1. Add a new phrase pair ("New Phrase": "New Translation") into your language file (for example lang/ru.js). It will be great if you add it in other language files too.
 * 2. Mark that text it templates this way <anyHtmlTag data-sw-translate>New Phrase</anyHtmlTag> or <anyHtmlTag data-sw-translate value='New Phrase'/>.
 * The main thing here is attribute data-sw-translate. Only inner html, title-attribute and value-attribute are going to translate.
 *
 */
window.SwaggerTranslator = {

    _words:[],

    translate: function(sel) {
      var $this = this;
      sel = sel || '[data-sw-translate]';

      $(sel).each(function() {
        $(this).html($this._tryTranslate($(this).html()));

        $(this).val($this._tryTranslate($(this).val()));
        $(this).attr('title', $this._tryTranslate($(this).attr('title')));
      });
    },

    _tryTranslate: function(word) {
      return this._words[$.trim(word)] !== undefined ? this._words[$.trim(word)] : word;
    },

    learn: function(wordsMap) {
      this._words = wordsMap;
    }
};
