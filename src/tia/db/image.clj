(ns tia.db.image
  (:require
   [tia.db.common :as dbc]))

(defn create! [image]
  (dbc/record! image))

(defn get-all-images []
  (dbc/pull-all-having-key :image/id))

(comment
  (get-all-images)  
  :=> '({:image/id #uuid "391be961-bb6d-492b-b062-c23f8e436e32",
         :image/objk "91d0c008-4535-43c7-b8b5-b2e3156374d0",
         :image/place-id #uuid "deae0e86-5a02-4d1b-8894-f59734aa009b",
         :image/filename "14387088_014_307d.jpg",
         :image/size 104161,
         :xt/id #uuid "391be961-bb6d-492b-b062-c23f8e436e32"}))
