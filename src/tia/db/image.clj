(ns tia.db.image
  (:require
   [tia.db.common :as dbc]))

(defn create! [image]
  (dbc/record! image))

(defn get-all-images []
  (dbc/pull-all-having-key :image/id))

(comment
  (get-all-images)
  :=> '({:image/id #uuid "002d5a3b-a826-4077-aac8-3dab57e4d5af",
         :image/objk "ba509037-25b9-4ea1-b604-f659fb8c831e",
         :image/place-id #uuid "e4dd31a3-22e0-42c9-af5e-61702602b6d0",
         :image/person-id #uuid "11381509-5e3b-448b-958d-6a23b242ce61",
         :image/filename "675439.jpg",
         :image/size 33472,
         :xt/id #uuid "002d5a3b-a826-4077-aac8-3dab57e4d5af"}))
