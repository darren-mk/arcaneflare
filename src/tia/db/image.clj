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

(defn get-images-by-post-id [post-id]
  (let [qr '{:find [(pull ?image [*])]
             :in [[?post-id]]
             :where [[?image :image/id]
                     [?image :image/post-id ?post-id]]}
        raw (dbc/query qr [post-id])]
    (map first raw)))

(comment
  (get-images-by-post-id #uuid "35b55225-950e-4300-8aed-0627c8b83c40")
  :=> ({:image/id #uuid "360b5105-4c21-4d36-9fbb-016c097e057a",
        :image/objk "ec2364f2-88e4-4f42-8506-beac2594eda8",
        :image/post-id #uuid "35b55225-950e-4300-8aed-0627c8b83c40",
        :image/filename "97856444_012_94b1.jpg",
        :image/size 84874,
        :xt/id #uuid "360b5105-4c21-4d36-9fbb-016c097e057a"}
       {:image/id #uuid "013b54ac-417c-4feb-be75-82df3d8d672b",
        :image/objk "681bda2d-011b-48e8-a964-2902d21df84e",
        :image/post-id #uuid "35b55225-950e-4300-8aed-0627c8b83c40",
        :image/filename "68f4555c6f4c96fe2bf9908bb8492124.jpg",
        :image/size 206523,
        :xt/id #uuid "013b54ac-417c-4feb-be75-82df3d8d672b"}))
