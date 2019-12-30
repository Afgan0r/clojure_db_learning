(ns learning-db.core
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers])
  (:gen-class))

(def db {:dbtype "h2" :dbname "learning"})

(def ds (jdbc/get-datasource db))

(defn create-students-table []
  (jdbc/execute! ds ["
DROP TABLE IF EXISTS Students;
CREATE TABLE Students(
id_student INT AUTO_INCREMENT PRIMARY KEY,
first_name varchar(32),
last_name varchar(32),
age int,
email varchar(255)
);"]))

(defn add-students []
  (let [insert (-> (insert-into :Students)
                   (columns :first_name :last_name :age :email)
                   (values 
                    [["Alexandr", "Pavlov", "18", "somemail @mail.ru"]
                     ["Artem", "Guderian","17","ag_mail @google.com"]])
                   sql/format)]
    (jdbc/execute! ds insert)))

(defn get-all-students []
  (let [query (-> (select :first_name :last_name)
                  (from :Students)
                  sql/format)]
    (jdbc/execute! ds query
                   {:builder-fn rs/as-unqualified-lower-maps})))

(defn print-last-and-first-name [{:keys [first_name last_name]}]
  (println (str "First name is: " first_name ". Last name is: " last_name)))

(defn -main [& args]
  (doseq [curr-student (get-all-students)] (print-last-and-first-name curr-student)))