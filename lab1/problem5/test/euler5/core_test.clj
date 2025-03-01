(ns euler5.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [euler5.core :refer [euler5-for euler5-lazy euler5-map euler5-recursion euler5-reduce euler5-tail-recursion]]))

(deftest euler5-implementations-test
  (let [expected 232792560]
    (testing "Хвостовая рекурсия"
      (is (= expected (euler5-tail-recursion))))

    (testing "Обычная рекурсия"
      (is (= expected (euler5-recursion (range 1 21)))))

    (testing "Reduce реализация"
      (is (= expected (euler5-reduce))))

    (testing "Map реализация"
      (is (= expected (euler5-map))))

    (testing "For"
      (is (= expected (euler5-for))))

    (testing "Ленивые коллекции"
      (is (= expected (euler5-lazy))))))