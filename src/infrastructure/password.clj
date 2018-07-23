(ns infrastructure.password
  (:require [cemerick.friend.credentials :as creds]))

(defn encrypt [pwd] (creds/hash-bcrypt pwd))

(defn matches [text crypted] (creds/bcrypt-verify text crypted))