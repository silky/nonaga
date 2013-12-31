(ns nonaga.core-test
  (:require-macros [cemerick.cljs.test
                    :refer (deftest is are)])
  (:use [nonaga.core :only [nw ne e se sw w neighbours invalid-space? move
                            initial-game valid-slide? valid-slides
                            move-towards distance valid-destinations]])
  (:require [cemerick.cljs.test :as t]))

(deftest directional-functions
  (are [function start expected]
       (= expected (take 5 (iterate function start)))

       nw [3 0] [[3 0] [2 1] [2 2] [1 3] [1 4]]
       ne [1 0] [[1 0] [1 1] [2 2] [2 3] [3 4]]
       e  [0 2] [[0 2] [1 2] [2 2] [3 2] [4 2]]
       se [1 4] [[1 4] [1 3] [2 2] [2 1] [3 0]]
       sw [3 4] [[3 4] [2 3] [2 2] [1 1] [1 0]]
       w  [4 2] [[4 2] [3 2] [2 2] [1 2] [0 2]]))

(deftest neighbouring-cells
  (is (= #{[1 3] [2 3] [1 2] [3 2] [1 1] [2 1]} (neighbours [2 2]))))

(deftest invalid-space
  (are [coord expected]
       (= expected (invalid-space? initial-game coord))

       [0 4] true
       [1 4] true
       [2 4] false))

(deftest movement
  (are [coord direction expected]
       (= expected (move initial-game coord direction))

       [3 0] nw [1 3]
       [0 3] e  [3 3]
       [0 1] e  [3 1])

  (is (= #{[2 1] [2 4] [0 3]} (valid-destinations initial-game [1 4]))))

(def board-with-gap
  {:rings #{[1 1] [3 0] [2 0]}})

(deftest which-slides-are-valid
  (are [coord direction valid?]
       (= valid? (valid-slide? initial-game coord direction))
       [3 0] ne false
       [3 0] se true)
  (is (not (valid-slide? board-with-gap [2 0] ne)))
  (is (valid-slide? board-with-gap [3 0] ne)))

(deftest distances
  (are [endpoint dist]
       (= dist (distance [0 0] endpoint))
       [12 0]   12
       [14 7]   17
       [12 11]  17
       [0 5]    5
       [3 10]   10
       [4 8]    8
       [5 8]    9
       [-16 -9] 20))

(deftest enumerate-valid-directions-to-slide
  (is (= 3 (count (valid-slides board-with-gap [2 1])))) )

(deftest path-finding
  (are [valid? board] (= valid? (move-towards {:rings board} [0 2] [4 2]))

       ; No obstructions
       true
       #{[0 2]}

       ; Distination obstructed
       false
       #{[0 2]                   [4 2]}

       ; Non blocking obsticle
       true
       #{[0 2]      [2 2]}

       ; Backtracking obsticle
       true
       #{ [0 3] [1 3] [2 3] [3 3]
        [0 2]             [3 2]
          [0 1] [1 1] [2 1] [3 1]}

       ; Source surrounded
       false
       #{   [-1 4] [0 4] [1 4]
          [-2 3]             [1 3]
        [-2 2]       [0 2]       [2 2]
          [-2 1]             [1 1]
            [-1 0] [0 0] [1 0]}

       ; Source surrounded with gap
       false
       #{   [-1 4] [0 4]
          [-2 3]             [1 3]
        [-2 2]       [0 2]       [2 2]
          [-2 1]             [1 1]
            [-1 0] [0 0] [1 0]}


       ;Destination surrounded
       false
       #{   [3 4] [4 4] [5 4]  
          [2 3]             [5 3]
        [2 2]                   [6 2] 
          [2 1]             [5 1] 
            [3 0] [4 0] [5 0]}

       ;Destination surrounded with gap
       false
       #{   [3 4] [4 4]        
          [2 3]             [5 3]
        [2 2]                   [6 2] 
          [2 1]             [5 1] 
            [3 0] [4 0] [5 0]}))
