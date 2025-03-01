(ns euler5.core
  (:gen-class))

;; Вспомогательные функции
(defn gcd [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))

(defn lcm [a b]
  (if (or (zero? a) (zero? b))
    0
    (/ (* a b) (gcd a b))))

;; Реализации
(defn euler5-tail-recursion []
  (loop [current-lcm 1
         nums (range 2 21)]
    (if (empty? nums)
      current-lcm
      (recur (lcm current-lcm (first nums))
             (rest nums)))))

(defn euler5-recursion [nums]
  (if (empty? (rest nums))
    (first nums)
    (lcm (first nums)
         (euler5-recursion (rest nums)))))

(defn euler5-reduce []
  (reduce lcm 1 (range 1 21)))

(defn euler5-map []
  (last (reductions lcm 1 (range 2 21))))

(defn euler5-for []
  (first (for [n (iterate inc 1)
               :when (every? #(zero? (mod n %)) (range 1 21))]
           n)))

(defn euler5-lazy []
  (first (filter (fn [n] (every? #(zero? (mod n %)) (range 1 21)))
                 (iterate inc 1))))

;; Main
(defn -main []
  (println "Результаты:")
  (println "Хвостовая рекурсия:" (euler5-tail-recursion))
  (println "Обычная рекурсия:" (euler5-recursion (range 1 21)))
  (println "Reduce:" (euler5-reduce))
  (println "Map:" (euler5-map))
  (println "For:" (euler5-for))
  (println "Ленивые коллекции:" (euler5-lazy)))