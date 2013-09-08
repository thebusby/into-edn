(defproject into-edn "1.0.1"
  :description  "A simple library to convert an Object/Document/etc into Clojure EDN data "
  :url          "https://github.com/thebusby/into-edn"
  :license      {:name "MIT"
                 :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles     {:1.2    {:dependencies [[org.clojure/clojure "1.2.0"]]}
                 :1.3    {:dependencies [[org.clojure/clojure "1.3.0"]]}
                 :1.4    {:dependencies [[org.clojure/clojure "1.4.0"]]}
                 :1.6    {:dependencies [[org.clojure/clojure "1.6.0-master-SNAPSHOT"]]}
                 :master {:dependencies [[org.clojure/clojure "1.6.0-master-SNAPSHOT"]]}}
  :aliases      {"all" ["with-profile" "base:1.2:1.3:1.4:1.6:master"]}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}})
