(ns nonaga.rules.rings
  (:require [clojure.set :refer [difference]])
  (:use [nonaga.rules.coord :only [neighbouring-directions tag-with-distance]]))

(defn valid-slide? [rings coord direction]
  (let [sliding-into-ring? (rings (direction coord))
        gap-too-small? (apply #(and %1 %2)
                              (map rings
                                   (map #(% coord)
                                        (neighbouring-directions direction))))]
    (not (or sliding-into-ring? gap-too-small?))))

(defn valid-slides [rings coord]
  (filter (partial valid-slide? rings coord)
          (keys neighbouring-directions)))

(defn neighbour-distances [rings source destination]
  (->> (valid-slides rings source)
       (map #(% source))
       (map (partial tag-with-distance destination))))

(defn move-towards
  ([rings source destination]
   (if (rings destination)
     false
     (move-towards rings
                   (sorted-set (tag-with-distance destination source))
                   (sorted-set)
                   destination
                   0)))
  ([rings unexploded exploded destination count]
   (let [next-choice    (first unexploded)
         [_ source]     next-choice
         explosion      (neighbour-distances rings source destination)
         new-exploded   (conj exploded next-choice) 
         new-unexploded (difference (into unexploded explosion) new-exploded)]
     (if (new-unexploded [0 destination]) true
       (if (empty? new-unexploded) false
         (if (> count 500) false
           (recur rings new-unexploded new-exploded destination (inc count))))))))
