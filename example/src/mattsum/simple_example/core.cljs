(ns mattsum.simple-example.core
  (:require [reagent.core :as r]
            [cljs.test :as test]))


(enable-console-print!)

;; we need set! for advanced compilation

;;(set! js/React (js/require "react-native"))
(defonce react (js/require "react-native"))

;; Assets need to be relative path, starting from the `app/build/node_modules'
;; directory. The packager only finds images located in the `app/' folder
;; (the directory that contains package.json) or below.
;;
;; We use `defonce' to prevent errors on subsequent reloads.
(js/console.log "IN CLJS LAND" react)

;(defonce logo (js/require "../../assets/cljs.png"))

(def view (r/adapt-react-class (.-View react)))
(def text (r/adapt-react-class (.-Text react)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight react)))
(def image (r/adapt-react-class (.-Image react)))

(js/console.log view)

(defonce !state (r/atom {:count 0}))

(defn root-view
  []
  (js/console.log "### rendering BRO")
  [view {:style {:margin-top 50
                 :margin-left 8
                 :justify-content "center"
                 :align-items "center"}}
   [text {:style {:font-family "Helvetica"
                  :font-size 20
                  :margin-bottom 20}}
    "Welcome to boot-react-native with figwheel!!!"]
   #_[image {:style {:width 350
                   :height 348
                   :margin-bottom 20}
           :source logo}]
   [touchable-highlight {:style {:padding 20
                                 :background-color "#e0e0e0"}
                         :on-press (fn []
                                     (swap! !state update :count inc))
                         :underlay-color "#f0f0f0"}
    [text {:style {:font-family "Helvetica"
                   :font-size 14}}
     "Counter: " (:count @!state) ", click to increase"]]])

(def cnt (r/atom 0))


(defn root-container
  "Wraps root-view. This is to make sure live reloading using boot-reload and
  reagent works as expected. Instead of editing root-container, edit root-view"
  []
  @cnt
  [root-view])

(defn ^:export main
  []
  (js/console.log "##### MAIN")
  (enable-console-print!)
  (.registerComponent (.-AppRegistry react)
                      "SimpleExampleApp"
                      #(r/reactify-component #'root-container)))

