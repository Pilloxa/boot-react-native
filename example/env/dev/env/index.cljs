(ns env.index
  (:require [env.dev :as dev]))

;; undo main.js goog preamble hack
(set! js/window.goog js/undefined)

(-> (js/require "figwheel-bridge")
    (.withModules #js {"react-native" (js/require "react-native"), "./assets/cljs.png" (js/require "../../assets/cljs.png"), "react" (js/require "react")}
)
    (.start "SimpleExampleApp"))
