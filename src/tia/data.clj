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
  {:usa {:label "United States"
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
   :nj {:label "New Jersey"}
   :nm {:label "New Mexico"}
   :ny {:label "New York"}
   :nc {:label "North Carolina"}
   :nd {:label "North Dakota"}
   :oh {:label "Ohio"}
   :ok {:label "Oklahoma"}
   :or {:label "Oregon"}
   :pa {:label "Pennsylvania"}
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
