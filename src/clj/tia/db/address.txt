(ns tia.db.address
  (:require
   [tia.db.common :as dbc]))

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

(defn get-states [nation-id]
  (map first
       (dbc/query
        '{:find [(pull ?state [*])]
          :in [[?nation-id]]
          :where [[?state :state/nation-id ?nation-id]]}
        [nation-id])))

(comment
  (take 1 (get-states #uuid "efa7048d-1e56-42f7-8ecf-5b5083ab42cf"))
  :=> '({:state/id #uuid "ea1f9cbe-6d3b-4e68-93c9-6f465d17588a",
         :state/label "New York",
         :state/acronym "NY",
         :state/nation-id #uuid "efa7048d-1e56-42f7-8ecf-5b5083ab42cf",
         :xt/id #uuid "ea1f9cbe-6d3b-4e68-93c9-6f465d17588a"}))

(defn get-counties [state-id]
  (map first
       (dbc/query
        '{:find [(pull ?county [*])]
          :in [[?state-id]]
          :where [[?county :county/state-id ?state-id]]}
        [state-id])))

(defn get-cities [county-id]
  (map first
       (dbc/query
        '{:find [(pull ?county [*])]
          :in [[?county-id]]
          :where [[?county :city/county-id ?county-id]]}
        [county-id])))

(comment
  (take 1 (get-cities #uuid "7a695942-3cb7-4af0-94b1-5c24cc48fc3c"))
  :=> '({:city/id #uuid "0e82fb6b-dc3f-4040-b51a-3857050392ba",
         :city/label "Hackensack",
         :city/county-id #uuid "7a695942-3cb7-4af0-94b1-5c24cc48fc3c",
         :xt/id #uuid "0e82fb6b-dc3f-4040-b51a-3857050392ba"}))
