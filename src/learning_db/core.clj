(ns learning-db.core
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers])
  (:gen-class))

(def db {:dbtype "h2" :dbname "learning"})

(def ds (jdbc/get-datasource db))

(defn create-tables []
  (jdbc/execute! ds ["
DROP TABLE IF EXISTS Students;
DROP TABLE IF EXISTS Groups;

CREATE TABLE Groups (
id_group INT AUTO_INCREMENT PRIMARY KEY,
curator_name varchar(32),            
group_name varchar (10)
);

CREATE TABLE Students(
id_student INT AUTO_INCREMENT PRIMARY KEY,
first_name varchar(32),
last_name varchar(32),
age int,
email varchar(255),
group_id int,
FOREIGN KEY (group_id) REFERENCES Groups (id_group)
);
"]))

(defn add-groups-and-students []
  (let [insert-groups (-> (insert-into :Groups)
                          (columns :curator_name :group_name)
                          (values 
                           [["Evdokim Leontevich", "9ISiP171"]
                            ["Filimon Andreevich", "11PO191"]])
                          sql/format)
        insert-students (-> (insert-into :Students)
                            (columns :first_name :last_name :age :email :group_id)
                            (values
                             [["Alexandr", "Pavlov", "18", "somemail @mail.ru", 1]
                              ["Artem", "Guderian","17","ag_mail @google.com", 1]])
                            sql/format)]
    (jdbc/execute! ds insert-groups)
    (jdbc/execute! ds insert-students)))

(defn get-all-students-with-groups []
  (let [query (-> (select :*)
                  (from :Students)
                  (join :Groups [:= :group_id :id_group])
                  sql/format)]
    (jdbc/execute! ds query
                   {:builder-fn rs/as-unqualified-lower-maps})))

(defn -main [& args])