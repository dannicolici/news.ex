(ns news.websocket
  (:require [cljsjs.phoenix]))

(defn connect!  [address]
  (let [socket (js/Phoenix.Socket. address
                                   (clj->js {"params:" {"userToken:" "123"}}))]
    (.connect socket)
    socket))

(defn create-channel! [socket topic-name]
  (.channel socket topic-name))

(defn join-with-handlers! [topic socket ok-handler err-handler]
  (let [chan (create-channel! socket topic)]
    (-> chan
        (.join)
        (.receive "ok" ok-handler)
        (.receive "error" err-handler))
    chan))

(defn push-with-handlers! [chan msg_type payload ok-handler err-handler]
    (-> chan
       (.push msg_type payload)
       (.receive "ok" ok-handler)
       (.receive "error" err-handler)))