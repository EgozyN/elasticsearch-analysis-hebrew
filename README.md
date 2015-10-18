# Hebrew analyzer for Elasticsearch

Powered by HebMorph (https://github.com/synhershko/HebMorph) and licensed under the AGPL3

![](https://travis-ci.org/synhershko/elasticsearch-analysis-hebrew.svg?branch=master)

## Installation

First, install the plugin by invoking the command which fits your elasticsearch version (older versions can be found at the bottom):
Elasticsearch versions 1.4.0 - 1.7.3:
```
    bin/plugin --install analysis-hebrew --url https://bintray.com/artifact/download/synhershko/elasticsearch-analysis-hebrew/elasticsearch-analysis-hebrew-1.7.zip
```

You will need hebrew dictionary files. The open-sourced hspell files can be downloaded here: https://github.com/synhershko/HebMorph/tree/master/hspell-data-files. Next, Elasticsearch needs to be configured to load the dictionary from that folder: this is done by adding the following line to elasticsearch.yml file:
```
    hebrew.dict.path: /PATH/TO/HSPELL/FOLDER
```
The easiest way to make sure the plugin is installed correctly is to request /_hebrew/check-word/בדיקה on your server (for example: browse to http://localhost:9200/_hebrew/check-word/בדיקה). If it loads, it means everything is set up and you are good to go.

## Commercial

Hebmorph is released open-sourced, alongside with hspell dictionary files. The Commercial option will grant you further support in making Hebrew search even better, and it comes with a proprietary dictionary. For more information, check out http://code972.com/hebmorph.

## Usage

Use "hebrew" as analyzer name for fields containing Hebrew text

Query using "hebrew_query" or "hebrew_query_light" to enable exact matches support. "hebrew_exact" analyzer is available for query_string / match queries to be searched exact without lemma expansion.

Because Hebrew uses quote marks to mark acronyms, it is recommended to use the match family queries and not query_string. This is the official recommendation anyway. This plugin does not currently ship with a QueryParser implementation that can be used to power query_string queries.

More documentation coming soon

## On the cloud

[Qbox.io](http://qbox.io/) allow this plugin to be installed on all of their cluster offerings:

![Hebrew analysis plugin on qbox.io](http://cdn2.hubspot.net/hub/307608/file-525764352-png/Hebrew-Analysis.png?t=1392398980000)

## Older Versions

~/elasticsearch-0.90.11$ bin/plugin --install analysis-hebrew --url https://bintray.com/artifact/download/synhershko/elasticsearch-analysis-hebrew/elasticsearch-analysis-hebrew-1.0.zip

~/elasticsearch-1.0.0$ bin/plugin --install analysis-hebrew --url https://bintray.com/artifact/download/synhershko/elasticsearch-analysis-hebrew/elasticsearch-analysis-hebrew-1.2.zip

~/elasticsearch-1.2.1$ bin/plugin --install analysis-hebrew --url https://bintray.com/artifact/download/synhershko/elasticsearch-analysis-hebrew/elasticsearch-analysis-hebrew-1.4.zip

~/elasticsearch-1.3.2$ bin/plugin --install analysis-hebrew --url https://bintray.com/artifact/download/synhershko/elasticsearch-analysis-hebrew/elasticsearch-analysis-hebrew-1.5.zip

## License

AGPL3, see LICENSE
