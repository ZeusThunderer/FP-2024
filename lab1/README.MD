## Выполнил

- ФИО: `Закиров Тимур Альбертович`
- Номер ИСУ: `305246`
- Группа: `P34092`
- Вариант: `5,26`


## Решение задания 5

```clojure
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
```

### Тесты

[Тесты](/lab1/problem5/test/euler5/core_test.clj)

### Реализация на традиционном языке программирования для сравнения

```python
def euler5():
    from math import gcd
    from functools import reduce
    return reduce(lambda a,b: a*b//gcd(a,b), range(1,21))

print(euler5())

```

## Решение задания 26

```clojure
(ns euler26.core
  (:gen-class))

(defn recurring-cycle-length [d]
  (letfn [(cycle-length [n rems pos]
            (let [rem (mod (* 10 n) d)]
              (cond
                (= rem 0) 0
                (contains? rems rem) (- pos (rems rem))
                :else (recur rem (assoc rems rem pos) (inc pos)))))]
    (cycle-length 1 {} 0)))

; Монолитная реализация с использованием хвостовой рекурсии
(defn recurring-tail-recursion [d limit max-d max-len]
  (if (>= d limit)
    max-d
    (let [len (recurring-cycle-length d)]
      (if (> len max-len)
        (recur (inc d) limit d len)
        (recur (inc d) limit max-d max-len)))))

; Монолитная реализация с использованием рекурсии 
(defn recurring-recursion [d limit max-d max-len]
  (if (>= d limit)
    max-d
    (let [len (recurring-cycle-length d)]
      (if (> len max-len)
        (recurring-recursion (inc d) limit d len)
        (recurring-recursion (inc d) limit max-d max-len)))))

; Модульная реализация с использованием filter
(defn generate-cycle-lengths [limit]
  (map (fn [d] [d (recurring-cycle-length d)]) (range 2 limit)))

(defn filter-non-zero-cycles [pairs]
  (filter (fn [[_ length]] (pos? length)) pairs))

(defn find-max-cycle [pairs]
  (reduce (fn [max-pair current-pair]
            (if (> (second current-pair) (second max-pair))
              current-pair
              max-pair))
          [0 0]
          pairs))

(defn recurring-modular [limit]
  (let [pairs (generate-cycle-lengths limit)
        filtered-pairs (filter-non-zero-cycles pairs)
        [d _] (find-max-cycle filtered-pairs)]
    d))

; Генерация последовательности при помощи отображения (map)
(defn recurring-map [limit]
  (first (apply max-key second
                (map (fn [d] [d (recurring-cycle-length d)])
                     (range 2 limit)))))

; Работа со специальным синтаксисом для циклов (for)
(defn recurring-for [limit]
  (let [lengths (for [d (range 2 limit)]
                  [d (recurring-cycle-length d)])]
    (first (apply max-key second lengths))))

; Итератор
(defn recurring-iterator [limit]
  (let [state (atom 1)
        next-pair (fn []
                    (let [d (swap! state inc)]
                      (when (< d limit)
                        [d (recurring-cycle-length d)])))
        pairs (fn pairs []
                (lazy-seq
                 (when-let [pair (next-pair)]
                   (cons pair (pairs)))))]
    (first (apply max-key second (pairs)))))

(defn -main
  "Solutions for problem 26"
  []
  (println "Monolith tail recursion:")
  (println (recurring-tail-recursion 2 1000 0 0))
  (println "Monolith recursion:")
  (println (recurring-recursion 2 1000 0 0))
  (println "Modular:")
  (println (recurring-modular 1000))
  (println "Map:")
  (println (recurring-map 1000))
  (println "For:")
  (println (recurring-for 1000))
  (println "Iterator:")
  (println (recurring-iterator 1000)))
```

### Тесты

[Тесты](/lab1/problem26/test/euler26/core_test.clj)

### Реализация на традиционном языке программирования для сравнения

```python
def reciprocal_cycles(n):
    max_number = 0
    result = 1
    for number in range(1, n):
        new_list = []
        x = 1
        rem = 0
        rec_num = 0
        while rem not in new_list:
            new_list.append(rem)
            rem = x % number
            x = rem * 10
            rec_num += 1
        if max_number < rec_num:
            max_number = rec_num
            result = number
    return result

print(reciprocal_cycles(1000))
```

# Выводы

В ходе лабораторной работы были реализованы различные подходы к решению математических задач на языке Clojure. Поначалу очень непривычно использовать менять мышление с императивного на функциональное, однако со временем, становится понятнее и проще.

Использование хвостовой рекурсии позволило создать эффективную монолитную функцию без переполнения стека. В реализация с обычной рекурсией использовались вложенные функции, letfn, trampoline.

Модульный подход с использованием функций высшего порядка (filter, map, nth) показал силу функционального программирования в обработке и преобразовании последовательностей. Использование ленивых коллекций позволило эффективно работать с бесконечными последовательностями без излишней нагрузки на память.

Cинтаксис циклов for предоставил способ генерации и фильтрации данных в одном выражении.

В целом, использование идиоматических приемов языка Clojure представило для меня возможность решения задач в другом свете и подчеркнуло преимущества функционального подхода в обработке данных.
