(ns tia.data)

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

(def continents
  {:north-america {:full-name "North America"
                   :countries #{:usa :canada}}
   :south-america {:full-name "South America"
                   :countries #{:mexico}}})

(def countries
  {:usa {:full-name "United States"
         :states #{:al :ak :az :ar :ca :co :ct :de :dc :fl
                   :ga :hi :id :il :in :ia :ks :ky :la :me
                   :md :ma :mi :mn :ms :mo :mt :ne :nv :nh
                   :nj :nm :ny :nc :nd :oh :ok :or :pa :ri
                   :sc :sd :tn :tx :ut :vt :va :wa :wv :wi
                   :wy}}
   :canada {:full-name "Canada"
            :states #{:on :qc :ns :mb :bc :pe :sk :ab :nl :nb}}
   :mexico {:full-name "Mexico"}})

(def states
  {:al {:full-name "Alabama"}
   :ak {:full-name "Alaska"}
   :az {:full-name "Arizona"}
   :ar {:full-name "Arkansas"}
   :ca {:full-name "California"}
   :co {:full-name "Colorado"}
   :ct {:full-name "Connecticut"}
   :de {:full-name "Delaware"}
   :dc {:full-name "District of Columbia"}
   :fl {:full-name "Florida"}
   :ga {:full-name "Georgia"}
   :hi {:full-name "Hawaii"}
   :id {:full-name "Idaho"}
   :il {:full-name "Illinois"}
   :in {:full-name "Indiana"}
   :ia {:full-name "Iowa"}
   :ks {:full-name "Kansas"}
   :ky {:full-name "Kentucky"}
   :la {:full-name "Louisiana"}
   :me {:full-name "Maine"}
   :md {:full-name "Maryland"}
   :ma {:full-name "Massachusetts"}
   :mi {:full-name "Michigan"}
   :mn {:full-name "Minnesota"}
   :ms {:full-name "Mississippi"}
   :mo {:full-name "Missouri"}
   :mt {:full-name "Montana"}
   :ne {:full-name "Nebraska"}
   :nv {:full-name "Nevada"}
   :nh {:full-name "New Hampshire"}
   :nj {:full-name "New Jersey"}
   :nm {:full-name "New Mexico"}
   :ny {:full-name "New York"}
   :nc {:full-name "North Carolina"}
   :nd {:full-name "North Dakota"}
   :oh {:full-name "Ohio"}
   :ok {:full-name "Oklahoma"}
   :or {:full-name "Oregon"}
   :pa {:full-name "Pennsylvania"}
   :ri {:full-name "Rhode Island"}
   :sc {:full-name "South Carolina"}
   :sd {:full-name "South Dakota"}
   :tn {:full-name "Tennessee"}
   :tx {:full-name "Texas"}
   :ut {:full-name "Utah"}
   :vt {:full-name "Vermont"}
   :va {:full-name "Virginia"}
   :wa {:full-name "Washington"}
   :wv {:full-name "West Virginia"}
   :wi {:full-name "Wisconsin"}
   :wy {:full-name "Wyoming"}
   :on {:full-name "Ontario"}
   :qc {:full-name "Quebec"}
   :ns {:full-name "Nova Scotia"}
   :mb {:full-name "Manitoba"}
   :bc {:full-name "British Columbia"}
   :pe {:full-name "Prince Edward Island"}
   :sk {:full-name "Saskatchewan"}
   :ab {:full-name "Alberta"}
   :nl {:full-name "Newfoundland and Labrador"}
   :nb {:full-name "New Brunswick"}})
