[1mdiff --git a/deps.edn b/deps.edn[m
[1mindex d589169..b198665 100644[m
[1m--- a/deps.edn[m
[1m+++ b/deps.edn[m
[36m@@ -1,7 +1,8 @@[m
 {:paths ["src"][m
  :deps {org.clojure/clojure {:mvn/version "1.10.1"}[m
         seancorfield/next.jdbc {:mvn/version "1.0.13"}[m
[31m-        com.h2database/h2 {:mvn/version "1.4.199"}}[m
[32m+[m[32m        com.h2database/h2 {:mvn/version "1.4.199"}[m[41m[m
[32m+[m[32m        honeysql {:mvn/version "0.9.8"}}[m[41m[m
  :mvn/repos {"central" {:url "https://repo1.maven.org/maven2/"}[m
              "clojars" {:url "https://repo.clojars.org/"}}[m
  :aliases[m
[1mdiff --git a/learning.mv.db b/learning.mv.db[m
[1mindex 30ad880..9665ab2 100644[m
Binary files a/learning.mv.db and b/learning.mv.db differ
[1mdiff --git a/src/learning_db/core.clj b/src/learning_db/core.clj[m
[1mindex fbc6319..d616205 100644[m
[1m--- a/src/learning_db/core.clj[m
[1m+++ b/src/learning_db/core.clj[m
[36m@@ -1,6 +1,8 @@[m
 (ns learning-db.core[m
   (:require [next.jdbc :as jdbc][m
[31m-            [next.jdbc.result-set :as rs])[m
[32m+[m[32m            [next.jdbc.result-set :as rs][m[41m[m
[32m+[m[32m            [honeysql.core :as sql][m[41m[m
[32m+[m[32m            [honeysql.helpers :refer :all :as helpers])[m[41m[m
   (:gen-class))[m
 [m
 (def db {:dbtype "h2" :dbname "learning"})[m
[36m@@ -9,24 +11,30 @@[m
 [m
 (defn create-students-table [][m
   (jdbc/execute! ds ["[m
[32m+[m[32mDROP TABLE IF EXISTS Students;[m[41m[m
 CREATE TABLE Students([m
 id_student INT AUTO_INCREMENT PRIMARY KEY,[m
 first_name varchar(32),[m
 last_name varchar(32),[m
 age int,[m
 email varchar(255)[m
[31m-)"]))[m
[32m+[m[32m);"]))[m[41m[m
 [m
 (defn add-students [][m
[31m-  (jdbc/execute! ds ["[m
[31m-INSERT INTO Students (first_name,last_name,age,email)[m
[31m-    VALUES ('Alexandr','Pavlov','18','somemail@mail.ru'),[m
[31m-           ('Artem','Guderian','17','ag_mail@google.com')[m
[31m-"]))[m
[32m+[m[32m  (let [insert (-> (insert-into :Students)[m[41m[m
[32m+[m[32m                   (columns :first_name :last_name :age :email)[m[41m[m
[32m+[m[32m                   (values[m[41m [m
[32m+[m[32m                    [["Alexandr", "Pavlov", "18", "somemail @mail.ru"][m[41m[m
[32m+[m[32m                     ["Artem", "Guderian","17","ag_mail @google.com"]])[m[41m[m
[32m+[m[32m                   sql/format)][m[41m[m
[32m+[m[32m    (jdbc/execute! ds insert)))[m[41m[m
 [m
 (defn get-all-students [][m
[31m-  (jdbc/execute! ds ["SELECT first_name,last_name,age,email FROM Students"][m
[31m-                 {:builder-fn rs/as-unqualified-lower-maps}))[m
[32m+[m[32m  (let [query (-> (select :first_name :last_name)[m[41m[m
[32m+[m[32m                  (from :Students)[m[41m[m
[32m+[m[32m                  sql/format)][m[41m[m
[32m+[m[32m    (jdbc/execute! ds query[m[41m[m
[32m+[m[32m                   {:builder-fn rs/as-unqualified-lower-maps})))[m[41m[m
 [m
 (defn print-last-and-first-name [{:keys [first_name last_name]}][m
   (println (str "First name is: " first_name ". Last name is: " last_name)))[m
