(ns user
  (:require [learning-db.core :as core]))

(defn create-and-fill-database []
  (core/create-students-table)
  (core/add-students))

(defn print-last-and-first-name [{:keys [first_name last_name]}]
  (println (str "First name is: " first_name ". Last name is: " last_name)))

(defn get-students []
  (doseq [curr-student (core/get-all-students)] (print-last-and-first-name curr-student)))
