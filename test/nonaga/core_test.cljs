(ns nonaga.core-test
  (:require-macros [cemerick.cljs.test :refer (deftest is are)])
  (:require [nonaga.core :as c]
            [cemerick.cljs.test :as t]))

(def after-white-move
  {:rings
    #{   [1 4] [2 4] [3 4]
       [0 3] [1 3] [2 3] [3 3]
     [0 2] [1 2] [2 2] [3 2] [4 2]
       [0 1] [1 1] [2 1] [3 1]
         [1 0] [2 0] [3 0]}

   :whites
    #{

                             [4 2]

         [1 0]       [3 0]}

   :blacks
    #{               [3 4]

     [0 2]

                     [3 0]}})

(deftest ball-move
  (is (= after-white-move (c/move-ball c/initial-game :whites [1 4] [3 0]))))

(def after-ring-move
  {:rings
    #{         [2 4] [3 4]
       [0 3] [1 3] [2 3] [3 3]
     [0 2] [1 2] [2 2] [3 2] [4 2]
       [0 1] [1 1] [2 1] [3 1]
         [1 0] [2 0] [3 0] [4 0]}

   :whites
    #{   [1 4]

                             [4 2]

         [1 0]}

   :blacks
    #{               [3 4]

     [0 2]

                     [3 0]}})

(deftest ring-move
  (is (= after-ring-move (c/move-ring c/initial-game [1 4] [4 0]))))
