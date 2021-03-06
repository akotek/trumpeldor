(ns trumpeldor.core
  (:require [tech.ml.dataset :as ds]
            [tech.v2.datatype.functional :as dfn]
            [clojure.string :as s]))

; ============================================================

;; initial init
(def df (ds/->dataset "Dwelling Pivot 2010-2017.xlsx"))

;; explore data set
(def shape (ds/shape df))

(ds/column-names df)

(->> df
     ds/columns
     (take 6)
     (map #(meta %)))

;; some pre processing...
;; using clojure idioms!
(def ds
  (-> df
      (dissoc "neighborhood_number")
      (ds/remove-rows [0])))

;; && more explorations
;; min, max, valid...etc
(->> df
     ds/descriptive-stats)

;; missing/invalid values?
(->> (ds/descriptive-stats df)
     :n-missing
     (filter #(not= 0 %)))

(->> (ds/descriptive-stats df)
     :n-valid
     (filter #(not= (second shape) %)))

; ============================================================
;; calc units for biz
(def new-cols-s (map #(str "units_biz_" (str %)) (range 2010 2018)))

(let [cols (ds/column-names ds)
      units-total (ds/columns (ds/select-columns ds (filter #(s/starts-with? % "units_total") cols)))
      units-dwell (ds/columns (ds/select-columns ds (filter #(s/starts-with? % "units_dwell") cols)))
      units-biz (map dfn/- units-total units-dwell)]
  (-> (zipmap new-cols-s units-biz)
      ds/->dataset))

;; units numbers over time

;; growth in units over time

;; Neighborhood vs sqm Of Biz

;; neighborhood vs sqm of living