AgileDev
========

Tooling for Agile development

See [Project documentation](http://uniknow.github.io/AgileDev/site/0.1.0-SNAPSHOT/index.html) for more information.

## Current issues:

* Sometime it is required to include maven properties in the maven site documentation. This can be realized by extending the maven pages `<page name>.md` with a `.vm` extension. This causes however that header 2, etc are not shown because ## is for the velocity the comment sign. This can be fixed as follows (but has to be included on each page):


    #set($h1 = '#')
    #set($h2 = '##')
    #set($h3 = '###')
    #set($h4 = '####')

    $h1 Header 1
    $h2 Header 2

