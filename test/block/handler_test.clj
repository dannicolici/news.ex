(ns block.handler-test
  (:require [clojure.test :refer :all]
            [persistence.core :as p :refer :all]
            [ring.mock.request :as mock]
            [block.handler :refer :all]
            [password.core :refer :all]))

(defn data-fixture [f]
  (p/flush-db)
  (f)
  (p/flush-db))

(use-fixtures :each data-fixture)

(deftest test-routes
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (clojure.string/includes? (:body response) "index.html"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(defn create-and-assert-user [id]
  (let [response (app (mock/request :put (str "/api/user/" id "/john/doe/" (md5 "pwd"))))]
    (is (= (:status response) 201))
    (is (= (:body response) "created"))))

(deftest test-user
  (testing "put user"
    (create-and-assert-user "nickname"))

  (testing "get user"
    (let [user (p/get-user-by-id "nickname")]
      (is (= (:id user) "nickname"))
      (is (= (:first-name user) "john"))
      (is (= (:last-name user) "doe"))
      (is (= (:password user) (md5 "pwd")))))

  (testing "put same user"
    (let [response (app (mock/request :put (str "/api/user/nickname/john/doe/" (md5 "pwd"))))]
      (is (= (:status response) 409))
      (is (= (:body response) "already exists")))))

(deftest test-news
  (testing "post news for non-existing user"
    (let [response (app (mock/request :post "/api/news/non-existing-user" {"text" "some news text"}))]
      (is (= (:status response) 409))))
  (testing "post news for existing user"
    (create-and-assert-user "zzz")
    (let [response (app (-> (mock/request :post "/api/news/zzz" {"text" "some news text"})))]
      (is (= (:status response) 201))
      (is (= (:body response) "created")))
    (let [response (app (mock/request :get "/api/news/zzz"))]
      (is (= (:status response) 200))
      (is (= (:body response) "[{\"id\":\"1\",\"text\":\"some news text\"}]")))))
