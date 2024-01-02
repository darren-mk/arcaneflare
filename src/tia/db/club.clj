(ns tia.db.club
  (:require
   [tia.db.core :as dbc]))

(defn find-clubs-by-state [state]
  (dbc/query '{:find [?handle ?label]
               :keys [handle label]
               :in [[?state]]
               :where [[?address :address/state ?state]
                       [?address :address/id ?address-id]
                       [?club :club/address.id ?address-id]
                       [?club :club/handle ?handle]
                       [?club :club/label ?label]]}
             [state]))

(comment
  (find-clubs-by-state :nj)
  :=> #{{:handle :johnny-as-hitching-post
         :label "Johnny A’s Hitching Post"}})

(defn find-club-by-handle [handle]
  (let [ql '{:find [(pull ?club [*])
                    (pull ?address [*])]
             :keys [club address]
             :in [[handle]]
             :where [[?club :club/address.id ?address-id]
                     [?address :address/id ?address-id]]}]
    (first (dbc/query ql [handle]))))

(comment
  (find-club-by-handle :johnny-as-hitching-post)
  :=> {:club
       {:club/website "http://johnnyashitchingpost.com/",
        :club/address.id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
        :club/label "Johnny A’s Hitching Post",
        :club/nudity :bikini,
        :club/twitterx "https://twitter.com/hitchingpostnj",
        :club/instagram "https://www.instagram.com/Hitching_Post_/",
        :club/phone "1-973-684-7678",
        :club/handle :johnny-as-hitching-post,
        :club/facebook "https://www.facebook.com/JohnnyAsHitchingPost",
        :xt/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4",
        :club/status :open,
        :club/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"},
       :address
       {:address/id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
        :address/street "95 Barclay St",
        :address/city "Paterson",
        :address/state :nj,
        :address/zip "07503",
        :address/country :usa,
        :xt/id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"}})
