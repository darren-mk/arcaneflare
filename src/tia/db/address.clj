(ns tia.db.address
  (:require
   [malli.core :as m]
   [tia.model :as md]
   [tia.db.common :as dbc]
   [tia.db.person :as pdb]
   [tia.util :as u]))

(defn get-all-nations []
  (map first
       (dbc/query
        '{:find [(pull ?nation [*])]
          :where [[?nation :nation/id]]})))

(comment
  (get-all-nations)
  :=> '({:nation/id #uuid "efa7048d-1e56-42f7-8ecf-5b5083ab42cf",
         :nation/label "United States",
         :nation/acronym "US",
         :xt/id #uuid "efa7048d-1e56-42f7-8ecf-5b5083ab42cf"}
        {:nation/id #uuid "cee4df9b-70bc-4862-9d7c-e2a0b0caf63c",
         :nation/label "Canada",
         :nation/acronym "CA",
         :xt/id #uuid "cee4df9b-70bc-4862-9d7c-e2a0b0caf63c"}))

(defn get-all-states []
  (map first
       (dbc/query
        '{:find [(pull ?state [*])]
          :where [[?state :state/id]]})))

(comment
  (get-all-states)
  :=> '({:state/id #uuid "ea1f9cbe-6d3b-4e68-93c9-6f465d17588a",
         :state/label "New York",
         :state/acronym "NY",
         :state/nation-id #uuid "efa7048d-1e56-42f7-8ecf-5b5083ab42cf",
         :xt/id #uuid "ea1f9cbe-6d3b-4e68-93c9-6f465d17588a"}
        {:state/id #uuid "f14a7a37-7607-4878-b0ff-d908804f80ae",
         :state/label "California",
         :state/acronym "CA",
         :state/nation-id #uuid "efa7048d-1e56-42f7-8ecf-5b5083ab42cf",
         :xt/id #uuid "f14a7a37-7607-4878-b0ff-d908804f80ae"}))

(defn get-counties [state-id]
  (map first
       (dbc/query
        '{:find [(pull ?county [*])]
          :in [[?state-id]]
          :where [[?county :county/state-id state-id]]}
        [state-id])))

(comment
  (->> #uuid "c6b891cf-2d87-450e-81f7-57fb982d4073"
       get-counties
       (take 2))
  :=> '({:county/id #uuid "f50b0721-f76a-47c7-91f4-4166d023ab8c",
         :county/label "Essex",
         :county/state-id #uuid "c6b891cf-2d87-450e-81f7-57fb982d4073",
         :xt/id #uuid "f50b0721-f76a-47c7-91f4-4166d023ab8c"}
        {:county/id #uuid "e4223320-d8d8-4c50-af35-fa045dec55e0",
         :county/label "Warren",
         :county/state-id #uuid "c6b891cf-2d87-450e-81f7-57fb982d4073",
         :xt/id #uuid "e4223320-d8d8-4c50-af35-fa045dec55e0"}))
