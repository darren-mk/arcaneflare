(ns tia.db.club
  (:require
   [tia.db.common :as common]))

(defn find-clubs-by-state [state]
  (common/query '{:find [?handle ?label]
                  :keys [handle label]
                  :in [[?state]]
                  :where [[?address :address/state ?state]
                          [?address :address/id ?address-id]
                          [?club :place/address.id ?address-id]
                          [?club :place/handle ?handle]
                          [?club :place/label ?label]]}
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
             :where [[?club :place/address.id ?address-id]
                     [?address :address/id ?address-id]]}]
    (first (common/query ql [handle]))))

(comment
  (find-club-by-handle :johnny-as-hitching-post)
  :=> {:club
       {:place/website "http://johnnyashitchingpost.com/",
        :place/address.id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
        :place/label "Johnny A’s Hitching Post",
        :place/nudity :bikini,
        :place/twitterx "https://twitter.com/hitchingpostnj",
        :place/instagram "https://www.instagram.com/Hitching_Post_/",
        :place/phone "1-973-684-7678",
        :place/handle :johnny-as-hitching-post,
        :place/facebook "https://www.facebook.com/JohnnyAsHitchingPost",
        :xt/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4",
        :place/status :open,
        :place/id #uuid "23f58509-1cbe-4f11-a1d8-6a1fde6a85e4"},
       :address
       {:address/id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c",
        :address/street "95 Barclay St",
        :address/city "Paterson",
        :address/state :nj,
        :address/zip "07503",
        :address/country :usa,
        :xt/id #uuid "c1cb1901-d48d-46dc-9ea5-2deb66b4da5c"}})
