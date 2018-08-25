(defproject into-edn "1.0.2"
  :description  "A simple library to convert an Object/Document/etc into Clojure EDN data "
  :url          "https://github.com/thebusby/into-edn"
  :license      {:name "MIT"
                 :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :profiles     {:1.2    {:dependencies [[org.clojure/clojure "1.2.0"]]}
                 :1.3    {:dependencies [[org.clojure/clojure "1.3.0"]]}
                 :1.4    {:dependencies [[org.clojure/clojure "1.4.0"]]}
                 :1.5    {:dependencies [[org.clojure/clojure "1.5.1"]]}
                 :1.6    {:dependencies [[org.clojure/clojure "1.6.0"]]}
                 :1.7    {:dependencies [[org.clojure/clojure "1.7.0"]]}
                 :1.8    {:dependencies [[org.clojure/clojure "1.8.0"]]}
                 :1.10   {:dependencies [[org.clojure/clojure "1.10.0-alpha6"]]}
                 :master {:dependencies [[org.clojure/clojure "1.10.0-master-SNAPSHOT"]]}}
  :aliases      {"all" ["with-profile" "base:1.2:1.3:1.5:1.6:1.7:1.8:1.10:master"]}
  :repositories {"sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}})
