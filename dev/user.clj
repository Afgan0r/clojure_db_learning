(ns user
  (:require [learning-db.core :as core]))

(defn create-and-fill-database []
  (core/create-tables)
  (core/add-groups-and-students))

(defn print-last-and-first-name [{:keys [first_name last_name]}]
  (println (str "Student name is: " first_name " " last_name ".")))

(defn print-student-name-and-his-group [{:keys [first_name last_name group_name]}]
  (println (str "Student name is: " first_name " " last_name ", his group is: " group_name)))

(defn get-students []
  (println "Students names:")
  (doseq [curr-student (core/get-all-students-with-groups)] 
    (print-last-and-first-name curr-student))
  (println "Students names and his groups:")
  (doseq [curr-student (core/get-all-students-with-groups)]
    (print-student-name-and-his-group curr-student))
  )
