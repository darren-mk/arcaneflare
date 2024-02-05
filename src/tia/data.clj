(ns tia.data)

(def set-cookie
  "Set-Cookie")

(def ui-style
  [:link
   {:rel :stylesheet
    :crossorigin :anonymous
    :integrity "sha256-SsJizWSIG9JT9Qxbiy8xnYJfjCAkhEQ0hihxRn7jt2M="
    :href "https://cdn.jsdelivr.net/npm/halfmoon@2.0.1/css/halfmoon.min.css"}])

(def theme-style
  [:link
   {:rel :stylesheet
    :crossorigin :anonymous
    :integrity "sha256-DD6elX+jPmbFYPsGvzodUv2+9FHkxHlVtQi0/RJVULs="
    :href "https://cdn.jsdelivr.net/npm/halfmoon@2.0.1/css/cores/halfmoon.modern.css"}])

(def ui-action
  [:script
   {:crossorigin :anonymous
    :src "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
    :integrity "sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"}])

(def html-prop
  {:lang :en
   :data-bs-core :modern
   :data-bs-theme :dark})

(def content-type
  (let [k "Content-Type"]
    {:plain {k "text/plain"}
     :css {k "text/css"}
     :html {k "text/html"}}))

(def css-link
  [:link
   {:rel :stylesheet
    :type "text/css"
    :href "/css"}])

(def icons
  [:script
   {:src "https://kit.fontawesome.com/4c71c3e02a.js"
    :crossorigin :anonymous}])

(def htmx
  [:script
   {:src "https://unpkg.com/htmx.org@1.9.10",
    :integrity "sha384-D1Kt99CQMDuVetoL1lrYwg5t+9QdHe7NLX/SoJYkXDFfX37iInKRy5xLSi8nO7UC"
    :crossorigin :anonymous}])

(def divisions
  {:us-northeast
   {:label "US Northeast"
    :states #{:ct :me :ma :nh :ri :vt
              :ny :nj :pa}}
   :us-midwest
   {:label "US Midwest"
    :states #{:il :in :mi :oh :wi :ia
              :ks :mn :mo :ne :nd :sd}}
   :us-south
   {:label "US South"
    :states #{:de :fl :ga :md :nc :sc
              :va :dc :wv :al :ky :ms
              :tn :ar :la :ok :tx}}
   :us-west
   {:label "US West"
    :states #{:az :co :id :mt :nv :nm
              :ut :wy :ak :ca :hi :or
              :wa}}
   :canada
   {:label "Canada"
    :states #{:on :qc :ns :mb :bc :pe
              :sk :ab :nl :nb}}})

(def countries
  {:us {:label "United States"
         :states #{:al :ak :az :ar :ca :co :ct :de :dc :fl
                   :ga :hi :id :il :in :ia :ks :ky :la :me
                   :md :ma :mi :mn :ms :mo :mt :ne :nv :nh
                   :nj :nm :ny :nc :nd :oh :ok :or :pa :ri
                   :sc :sd :tn :tx :ut :vt :va :wa :wv :wi
                   :wy}}
   :canada {:label "Canada"
            :states #{:on :qc :ns :mb :bc :pe :sk :ab :nl :nb}}
   :mexico {:label "Mexico"}})

(def states
  {:al {:label "Alabama"}
   :ak {:label "Alaska"}
   :az {:label "Arizona"}
   :ar {:label "Arkansas"}
   :ca {:label "California"}
   :co {:label "Colorado"}
   :ct {:label "Connecticut"}
   :de {:label "Delaware"}
   :dc {:label "District of Columbia"}
   :fl {:label "Florida"}
   :ga {:label "Georgia"}
   :hi {:label "Hawaii"}
   :id {:label "Idaho"}
   :il {:label "Illinois"}
   :in {:label "Indiana"}
   :ia {:label "Iowa"}
   :ks {:label "Kansas"}
   :ky {:label "Kentucky"}
   :la {:label "Louisiana"}
   :me {:label "Maine"}
   :md {:label "Maryland"}
   :ma {:label "Massachusetts"}
   :mi {:label "Michigan"}
   :mn {:label "Minnesota"}
   :ms {:label "Mississippi"}
   :mo {:label "Missouri"}
   :mt {:label "Montana"}
   :ne {:label "Nebraska"}
   :nv {:label "Nevada"}
   :nh {:label "New Hampshire"}
   :nj {:label "New Jersey"
        :regions #{:north-nj :central-nj :south-nj}}
   :nm {:label "New Mexico"}
   :ny {:label "New York"
        :regions #{:manhattan :queens :brooklyn
                   :staten-island :westchester :ny-other-areas}}
   :nc {:label "North Carolina"}
   :nd {:label "North Dakota"}
   :oh {:label "Ohio"}
   :ok {:label "Oklahoma"}
   :or {:label "Oregon"}
   :pa {:label "Pennsylvania"
        :regions #{:philadelphia :pittsburgh :Allentown-Bethlehem
                   :Harrisburg-Carlisle :Scranton-Wilkes-Barre
                   :Lancaster-York :PA-Other-Areas}}
   :ri {:label "Rhode Island"}
   :sc {:label "South Carolina"}
   :sd {:label "South Dakota"}
   :tn {:label "Tennessee"}
   :tx {:label "Texas"}
   :ut {:label "Utah"}
   :vt {:label "Vermont"}
   :va {:label "Virginia"}
   :wa {:label "Washington"}
   :wv {:label "West Virginia"}
   :wi {:label "Wisconsin"}
   :wy {:label "Wyoming"}
   :on {:label "Ontario"}
   :qc {:label "Quebec"}
   :ns {:label "Nova Scotia"}
   :mb {:label "Manitoba"}
   :bc {:label "British Columbia"}
   :pe {:label "Prince Edward Island"}
   :sk {:label "Saskatchewan"}
   :ab {:label "Alberta"}
   :nl {:label "Newfoundland and Labrador"}
   :nb {:label "New Brunswick"}})

(def regions
  {:north-nj {:label "North New Jersey"}
   :central-nj {:label "Central New Jersey"}
   :south-nj {:label "South New Jersey"}
   :new-york-city {:label "New Yok City"}
   :manhattan {:label "Manhattan"}
   :queens {:label "Queens"}
   :brooklyn {:label "Brooklyn"}
   :staten-island {:label "Staten Island"}
   :westchester {:label "Westchester"}
   :ny-other-areas {:label "NY Other Areas"}
   :philadelphia {:label "Philadelphia"}
   :pittsburgh {:label "Pittsburgh"}
   :Allentown-Bethlehem {:label "Allentown / Bethlehem"}
   :Harrisburg-Carlisle {:label "Harrisburg / Carlisle"}
   :Scranton-Wilkes-Barre {:label "Scranton / Wilkes-Barre"}
   :Lancaster-York {:label "Lancaster / York"}
   :PA-Other-Areas {:label "PA Other Areas"}})

(def gplace-uri
  "https://places.googleapis.com/v1/places:searchText")

(def gplace-fields
  #{:googleMapsUri :internationalPhoneNumber
    :displayName :websiteUri :businessStatus
    :id :formattedAddress})

(def address-comp-types
  {:country :keyword
   :state :keyword
   :zip :string
   :county :string
   :city :string
   :street :string
   :number :string})

(def industries
  {:club {:label "strip club"}
   :parlor {:label "massage parlor"}})
