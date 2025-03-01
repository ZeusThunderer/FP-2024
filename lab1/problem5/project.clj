(defproject euler5 "0.1.0"
  :description "Project Euler Problem 5 Solutions"
  :url "https://github.com/yourusername/euler5"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main euler5.core
  
  :test-paths ["test"]
  :profiles {:dev {:plugins [[lein-cljfmt "0.9.2"]
                             [lein-kibit "0.1.8"]
                             [com.github.clj-kondo/lein-clj-kondo "0.2.5"]]
                  }}
  :aliases {"lint" ["do" ["clj-kondo" "--lint" "src" "test"] 
                    ["cljfmt" "check"]]
            "fix"  ["do" ["cljfmt" "fix"]]})