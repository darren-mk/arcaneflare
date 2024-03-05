(defproject tia "0.1.0-SNAPSHOT"
  :dependencies [[amazonica "0.3.165"]
                 [ch.qos.logback/logback-classic "1.4.4"]
                 [cheshire "5.11.0"]
                 [clojure.java-time "1.4.2"]
                 [clj-http "3.12.3"]
                 [com.github.seancorfield/honeysql "2.6.1126"]
                 [com.xtdb/xtdb-core "1.24.3"]
                 [com.xtdb/xtdb-rocksdb "1.24.3"]
                 [com.xtdb/xtdb-jdbc "1.24.3"]
                 [cprop "0.1.19"]
                 [conman "0.9.6"]
                 [expound "0.9.0"]
                 [funcool/struct "1.4.0"]
                 [garden "1.3.10"]
                 [hiccup "2.0.0-RC2"]
                 [json-html "0.4.7"]
                 [luminus-migrations "0.7.5"]
                 [luminus-transit "0.1.5"]
                 [luminus-undertow "0.1.16"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [metosin/malli "0.13.0"]
                 [metosin/muuntaja "0.6.8"]
                 [metosin/reitit "0.5.18"]
                 [metosin/ring-http-response "0.9.3"]
                 [mount "0.1.16"]
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.cli "1.0.214"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.postgresql/postgresql "42.3.2"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-defaults "0.3.4"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [selmer "1.12.55"]
                 [tick "0.7.5"]]
  :min-lein-version "2.0.0"
  :source-paths ["src"]
  :test-paths ["test"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot tia.core
  :plugins [[lein-cljfmt "0.9.2"]]
  :profiles {:uberjar {:omit-source true
                       :aot :all
                       :uberjar-name "tia.jar"
                       :source-paths ["env/prod/clj"]
                       :resource-paths ["env/prod/resources"]}
             :dev [:project/dev :profiles/dev]
             :project/dev {:jvm-opts ["-Dconf=dev-config.edn"]
                           :dependencies [[org.clojure/tools.namespace "1.3.0"]
                                          [pjstadig/humane-test-output "0.11.0"]
                                          [prone "2021-04-23"]
                                          [ring/ring-devel "1.9.6"]
                                          [ring/ring-mock "0.4.0"]]
                           :plugins [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                     [jonase/eastwood "1.2.4"]
                                     [cider/cider-nrepl "0.30.0"]]
                           :source-paths ["env/dev/clj"]
                           :resource-paths ["env/dev/resources"]
                           :repl-options {:init-ns user
                                          :timeout 120000}
                           :injections [(require 'pjstadig.humane-test-output)
                                        (pjstadig.humane-test-output/activate!)]}
             :profiles/dev {}
             :profiles/test {}})
