(ns ^:figwheel-no-load env.main
  (:require [reagent.core :as r]
            [mattsum.simple-example.core :as core]
            [figwheel.client :as figwheel :include-macros true]
            [env.dev]))

(enable-console-print!)

(def cnt (r/atom 0))
(defn reloader [] @cnt [core/root-view])
(def root-el (r/as-element [reloader]))

(figwheel/watch-and-reload
 :websocket-url (str "ws://" env.dev/ip ":3449/figwheel-ws")
 :heads-up-display false
 :jsload-callback #(swap! cnt inc))
(js/console.log "Hej")
(core/main)
