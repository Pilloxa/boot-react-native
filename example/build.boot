(set-env!
 :source-paths   #{"src" "react-support" }
 :resource-paths   #{"resources"}
 :exclusions ['cljsjs/react]
 :dependencies '[
                 [boot-react-native/boot-react-native      "0.3-rc1337" :scope "test"]
                 [adzerk/boot-cljs               "1.7.228-1"       :scope  "test"]
                 ;[adzerk/boot-cljs-repl          "0.3.3"           :scope  "test"]
                 ;[adzerk/boot-reload             "0.4.12"          :scope  "test"]
                 [com.cemerick/piggieback        "0.2.1"           :scope  "test"]
                 [weasel                         "0.7.0"           :scope  "test"]
                 [org.clojure/tools.nrepl        "0.2.13"          :scope  "test"]
                 [org.clojure/clojure            "1.9.0-alpha10"]
                 [org.clojure/clojurescript      "1.9.542"]
                 [reagent "0.6.1" :exclusions [cljsjs/react
                                 cljsjs/react-dom
                                 cljsjs/react-dom-server]]
                 [ajchemist/boot-figwheel "0.5.4-6" :scope "test"] ;; latest release
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [figwheel-sidecar "0.5.4-7" :scope "test"]
                 [react-native-externs "0.0.2-SNAPSHOT" :scope "test"]
                 ]
 )

(require
 '[adzerk.boot-cljs             :refer  [cljs]]
 '[boot-figwheel :refer [figwheel cljs-repl]]
 '[user :as user]
 '[boot.util                    :as     u]
 '[externs :as externs]
 '[clojure.string               :as     s]
 '[mattsum.boot-react-native    :as     rn])

(require 'boot.repl)
(swap! boot.repl/*default-middleware*
       conj 'cemerick.piggieback/wrap-cljs-repl)

(deftask fdev
  "boot dev, then input (cljs-repl)"
  []
(set-env! :source-paths #(conj % "env/dev"))
  (user/prepare)

  (comp
   (figwheel
    :build-ids  ["main"]
    :target-path "app/target"
    :all-builds [{:id "main"
                  :source-paths ["src" "env/dev"]
                  :figwheel true
                  :compiler     {:output-to     "not-used.js"
                                 :main          "env.main"
                                 :optimizations :none
                                 :output-dir    "."}}]
    :figwheel-options {:open-file-command "emacsclient"
                       :validate-config false})
   (repl)))

(deftask prod
  []
  (set-env! :source-paths #(conj % "env/prod"))
  (println "Start to compile clojurescript ...")
  (comp       
    (cljs :ids #{"index"})
    (target :dir ["app/target/env"])))

#_(deftask build
  []
  (comp
   (reload :on-jsload 'mattsum.simple-example.core/on-js-reload
           :port 8079
           :ws-host "localhost")
   (rn/before-cljsbuild)
   (cljs-repl :ws-host "localhost"
              :port 9001
              :ip "0.0.0.0")
   (cljs :ids #{"main"})
   (rn/after-cljsbuild :server-url "localhost:8081")
   (target :dir ["app/build"])))

#_(deftask dev
  "Build app and watch for changes"
  []
  (comp
        (watch)
        (build)
        (speak)))

#_(deftask dist
  "Build a distributable bundle of the app"
  []
  (comp
   (cljs :ids #{"dist"})
   (rn/bundle :files {"dist.js" "main.jsbundle"})
   (target :dir ["app/dist"])))
