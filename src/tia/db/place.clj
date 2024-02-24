(ns tia.db.place
  (:require
   [tia.db.common :as common]))

(defn find-places-in-city [city]
  (map first
       (common/query
        '{:find [(pull ?place [*])]
          :in [[?city]]
          :where [[?address :address/city ?city]
                  [?address :address/id ?aid]
                  [?place :place/address-id ?aid]]}
        [city])))

(comment
  (find-places-in-city "Passaic")
  :=> '({:place/phone "+1 973-779-7455",
         :place/address-id #uuid "1378f7f9-7460-4dc8-b2c4-608db8b2b13c",
         :place/google-uri "https://maps.google.com/?cid=14480630176803765776",
         :place/language :en,
         :place/status :operational,
         :place/id #uuid "0deec79e-12b9-4c10-88b6-95e5e3cae65c",
         :place/google-id "ChIJ0XE9giD_wokREL4wlGyI9cg",
         :place/website "http://instagram.com/platinumdollzpassaicnj",
         :xt/id #uuid "0deec79e-12b9-4c10-88b6-95e5e3cae65c",
         :place/label "Platinum Dollz Gentlemens Lounge",
         :place/industry :strip-club,
         :place/handle :platinum-dollz-gentlemens-lounge}))

(defn find-cities-in-state [state]
  (map first
       (common/query
        '{:find [?city]
          :in [[?state]]
          :where [[?address :address/state ?state]
                  [?address :address/city ?city]]}
        [state])))

(defn find-places-by-state [state]
  (common/query
   '{:find [?handle ?label ?city]
     :keys [handle label city]
     :in [[?state]]
     :where [[?address :address/state ?state]
             [?address :address/id ?address-id]
             [?address :address/city ?city]
             [?place :place/address-id ?address-id]
             [?place :place/handle ?handle]
             [?place :place/label ?label]]}
   [state]))

(comment
  (find-places-by-state "NJ")
  :=> #{{:handle :nj-lodi-satin-dolls,
         :label "Satin Dolls"}
        {:handle :ragtime-gentlemens-club,
         :label "Ragtime Gentlemen's Club"}})

(defn find-place-by-handle [handle]
  (let [ql '{:find [(pull ?place [*])]
             :in [[?handle]]
             :where [[?place :place/handle ?handle]]}]
    (ffirst (common/query ql [handle]))))

(comment
  (find-place-by-handle :platinum-dollz-gentlemens-lounge)
  :=> {:place/phone "+1 973-779-7455",
       :place/address-id #uuid "1378f7f9-7460-4dc8-b2c4-608db8b2b13c",
       :place/google-uri
       "https://maps.google.com/?cid=14480630176803765776",
       :place/language :en,
       :place/status :operational,
       :place/id #uuid "0deec79e-12b9-4c10-88b6-95e5e3cae65c",
       :place/google-id "ChIJ0XE9giD_wokREL4wlGyI9cg",
       :place/website "http://instagram.com/platinumdollzpassaicnj",
       :xt/id #uuid "0deec79e-12b9-4c10-88b6-95e5e3cae65c",
       :place/label "Platinum Dollz Gentlemens Lounge",
       :place/industry :strip-club,
       :place/handle :platinum-dollz-gentlemens-lounge})

(defn find-place-and-address [handle]
  (let [ql '{:find [(pull ?place [*])
                    (pull ?address [*])]
             :keys [place address]
             :in [[?handle]]
             :where [[?place :place/address-id ?address-id]
                     [?place :place/handle ?handle]
                     [?address :address/id ?address-id]]}]
    (first (common/query ql [handle]))))

(comment
  (find-place-and-address :johnny-as-hitching-post)
  :=> {:place
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

(defn place-handle->id [handle]
  (let [ql '{:find [?place]
             :in [[?handle]]
             :where [[?place :place/handle ?handle]]}]
    (ffirst (common/query ql [handle]))))

(comment
  (place-handle->id :flamingo-gentlemens-club)
  :=> #uuid "deae0e86-5a02-4d1b-8894-f59734aa009b")
