(ns gmail.core)

(def file "example.txt")

(defn read-file [file]
  (with-open
    [rdr (clojure.java.io/reader
          (clojure.java.io/resource "example.txt"))]
    (->> (line-seq rdr)
         (take 3) ;Limiter
         (partition-all 3)
         (into []))))

(read-file "example.txt")

(defn read-next-email [rdr]
  (let [first-line (take 1 (line-seq rdr))
        remain (->> (line-seq rdr)
                    (take-while
                     #(not (re-matches #"From (.*)@(.*) (Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|March|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(.*)" %)))
                    )]
    (concat first-line remain)))

(comment

  (with-open
    [rdr (clojure.java.io/reader
          (clojure.java.io/resource "example.txt"))]
    (->> (line-seq rdr)
         (take 3)
         (partition-all 3)
         (into [])))

  (with-open
    [rdr (clojure.java.io/reader
          (clojure.java.io/resource "Inbox.mbox"))]
    (->> (line-seq rdr)
         (take 10)
         (into [])))

  (with-open
    [rdr (clojure.java.io/reader
          (clojure.java.io/resource "Inbox.mbox"))]
    (clojure.pprint/pprint (take 1 (line-seq rdr)))
    (->> (line-seq rdr)
         (take-while
          #(not (re-matches #"From (.*)@(.*) (Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|March|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(.*)" %)))
         (into [])))

  (with-open
    [rdr (clojure.java.io/reader
          (clojure.java.io/resource "Inbox.mbox"))]
    (clojure.pprint/pprint (read-next-email rdr)))

  )



(def resp
  (with-open
    [rdr (clojure.java.io/reader
          (clojure.java.io/resource "Inbox.mbox"))]
    (->> (line-seq rdr)
         (take 10000)
         (into []))))


(count (filter identity (map #(re-matches #"From (.*)@(.*) (Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|March|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(.*)" %) resp)))

(re-matches #"From (.*)" (ffirst resp))

