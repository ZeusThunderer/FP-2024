(ns rb-tree.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [rb-tree.core :refer [delete filter-tree foldl foldr insert leaf? make-leaf tree-values union valid-red-black-tree?]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :refer [for-all]]
            [clojure.test.check.clojure-test :refer [defspec]])
  (:import [com.timurzakirov CustomObject]))

(defspec prop-insert-maintains-tree-properties
  10
  (for-all [values (gen/vector gen/small-integer)]
           (let [tree (reduce insert (make-leaf) values)]
             (valid-red-black-tree? tree))))

(defspec prop-delete-removes-element
  10
  (for-all [value gen/small-integer]
           (let [tree (insert (make-leaf) value)
                 new-tree (delete tree value)]
             (leaf? new-tree))))

(defspec prop-union-contains-all-elements
  10
  (for-all [values1 (gen/vector gen/small-integer)
            values2 (gen/vector gen/small-integer)]
           (let [tree1 (reduce insert (make-leaf) values1)
                 tree2 (reduce insert (make-leaf) values2)
                 union-tree (union tree1 tree2)
                 union-values (set (tree-values union-tree))]
             (and (every? union-values values1)
                  (every? union-values values2)))))

(deftest test-insert
  (testing "Insert elements into the red-black tree"
    (let [tree (insert (make-leaf) 10)]
      (is (= 10 (:value tree)))
      (is (= :black (:color tree))))))

(deftest test-delete
  (testing "Delete elements from the red-black tree"
    (let [tree (insert (make-leaf) 10)
          new-tree (delete tree 10)]
      (is (leaf? new-tree)))))

(deftest test-filter-tree
  (testing "Filter elements in the red-black tree"
    (let [tree (insert (insert (make-leaf) 5) 10)
          filtered-tree (filter-tree tree #(> % 5))]
      (is (= 10 (:value filtered-tree))))))

(deftest test-foldl
  (testing "Left fold over the red-black tree"
    (let [tree (insert (insert (make-leaf) 5) 10)]
      (is (= 15 (foldl + 0 tree))))))

(deftest test-foldr
  (testing "Right fold over the red-black tree"
    (let [tree (insert (insert (make-leaf) 5) 10)]
      (is (= 15 (foldr + 0 tree))))))

(deftest test-insert-strings
  (testing "Insert strings into the red-black tree"
    (let [tree (insert (make-leaf) "apple")]
      (is (= "apple" (:value tree)))
      (is (= :black (:color tree))))))

(deftest test-delete-strings
  (testing "Delete strings from the red-black tree"
    (let [tree (insert (make-leaf) "apple")
          new-tree (delete tree "apple")]
      (is (leaf? new-tree)))))

(deftest test-insert-custom-objects
  (testing "Insert custom objects into the red-black tree"
    (let [obj1 (CustomObject. 1)
          obj2 (CustomObject. 2)
          tree (insert (make-leaf) obj1)]
      (is (= obj1 (:value tree)))
      (is (= :black (:color tree)))
      (let [tree (insert tree obj2)]
        (is (= obj1 (:value tree)))
        (is (= obj2 (:value (:right tree))))))))

(deftest test-delete-custom-objects
  (testing "Delete custom objects from the red-black tree"
    (let [obj1 (CustomObject. 1)
          obj2 (CustomObject. 2)
          tree (insert (insert (make-leaf) obj1) obj2)
          new-tree (delete tree obj1)]
      (is (= obj2 (:value new-tree)))
      (is (= :black (:color new-tree))))))
