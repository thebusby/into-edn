
Available via [clojars](http://clojars.org/into-edn),  
Current stable version: [into-edn "1.0.1"]
[![Build Status](https://api.travis-ci.org/thebusby/into-edn.png?branch=master)](https://travis-ci.org/thebusby/into-edn)


# into-edn

into-edn is a Clojure library to simplify the conversion of data from one format, into Clojure EDN. It does not handle the reading or parsing of data, but instead provides a standard method for defining the EDN format as well as how to generate it.
 
Why write this library?  
For too often when dealing with XML, Java objects, etc; I end up converting them to a Clojure map. +into-edn+ simplifies, and standardizes, that process across libraries.


## Usage

#### Handling a Java object
```clojure
(require '[into-edn :refer [into-edn]])

(def string-spec
  {:bytes  #(.getBytes %)
   :length #(.length %)
   :hash   #(.hashCode %)
   :empty? #(.isEmpty %)})

(into-edn string-spec "Hello World")
;; -->
{:bytes [72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100],
 :length 11,
 :hash -862545276,
 :empty? false}
```


#### Handling XML
```clojure
(require '[into-edn :refer [into-edn]]
         '[clojure.zip :as zip]
         '[clojure.data.xml :as xml]
         '[clojure.data.zip.xml :as zf])

(def xml-string "
<responses>
  <response status=\"okay\">
    <ident>1234</ident>
    <title lang=\"English\">Creativity fails me</title>
    <children>
      <child><name>Humphrey</name><age>3</age></child>
      <child><name>Laura</name><age>5</age></child>
      <child><name>Mikey</name><age>7</age></child>
    </children>
  </response>
</responses>")

(def xml-spec
  [#(zf/xml-> % :response)
   {:status #(zf/xml1-> % (zf/attr :status))
    :ident  #(some-> %
                     (zf/xml1-> :ident zf/text)
                     (Long/parseLong))
    :title #(zf/xml1-> % :title zf/text)
    :title-lang #(zf/xml1-> % :title (zf/attr :lang))
    :children [#(zf/xml-> % :children :child)
               {:name #(zf/xml1-> % :name zf/text)
                :age  #(some-> %
                               (zf/xml1-> :age zf/text)
                               (Long/parseLong))}]}])


(into-edn xml-spec (some-> xml-string
                           xml/parse-str
                           zip/xml-zip))
;; -->
[{:status "okay",
  :ident 1234,
  :title "Creativity fails me",
  :title-lang "English",
  :children
  [{:name "Humphrey", :age 3}
   {:name "Laura", :age 5}
   {:name "Mikey", :age 7}]}]
```


## Artifacts

+into-edn+ artifacts are [released to Clojars](https://clojars.org/into-edn).

If you are using Maven, add the following repository definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars</id>
  <url>http://clojars.org/repo</url>
</repository>
```

### The Most Recent Release

With Leiningen:
```clojure
    [into-edn "1.0.1"]
```

With Maven:
``` xml
    <dependency>
      <groupId>into-edn</groupId>
      <artifactId>into-edn</artifactId>
      <version>1.0.1</version>
    </dependency>
```


## License

MIT
http://opensource.org/licenses/MIT

Copyright © 2013 Alan Busby

