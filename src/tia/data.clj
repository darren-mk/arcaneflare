(ns tia.data)

(def set-cookie
  "Set-Cookie")

(def uri
  {:places "/places"
   :review "/reviews"
   :post "/post"})

(def ui-style
  [:link
   {:rel :stylesheet
    :crossorigin :anonymous
    :integrity "sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
    :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"}])

(def ui-action
  [:script
   {:crossorigin :anonymous
    :src "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
    :integrity "sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"}])

(def html-prop
  {:lang :en
   :data-bs-core :modern
   :data-bs-theme :dark})

(def location
  "Location")

(def content-type
  "Content-Type")

(def text-plain
  "text/plain")

(def text-css
  "text/css")

(def text-html
  "text/html")

(def css-link
  [:link
   {:rel :stylesheet
    :type text-css
    :href "/css"}])

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

(def content-deletion-msg
  "... content deleted by user ...")

(def icon-prop
  {:xmlns "http://www.w3.org/2000/svg"
   :width 16
   :height 16
   :fill :currentColor
   :viewBox [0 0 16 16]})

(def heart-outlined-icon
  [:svg icon-prop
   [:path
    {:d [:m8 :2.748-.717-.737C5.6.281 :2.514.878
         :1.4 :3.053c-.523 :1.023-.641 :2.5.314
         :4.385.92 :1.815 :2.834 :3.989 :6.286
         :6.357 :3.452-2.368 :5.365-4.542
         :6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878
         :10.4.28 :8.717 :2.01zM8 :15C-7.333 :4.868
         :3.279-3.04 :7.824 :1.143q.09.083.176.171a3
         :3 :0 :0 :1 :.176-.17C12.72-3.042 :23.333
         :4.867 :8 :15]}]])

(def heart-filled-icon
  [:svg icon-prop
   [:path
    {:fill-rule :evenodd
     :d [:M8 :1.314C12.438-3.248 :23.534 :4.735 :8
         :15-7.534 :4.736 :3.562-3.248 :8 :1.314]}]])

(def hand-thumbs-up-outlined-icon
  [:svg icon-prop
   [:path
    {:d [:M8.864.046C7.908-.193 :7.02.53 :6.956
         :1.466c-.072 :1.051-.23 :2.016-.428
         :2.59-.125.36-.479 :1.013-1.04 :1.639-.557.623-1.282
         :1.178-2.131 :1.41C2.685 :7.288 :2 :7.87 :2 :8.72v4.001c0
         :.845.682 :1.464 :1.448 :1.545 :1.07.114 :1.564.415
         :2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217
         :1.466.217h3.5c.937 :0 :1.599-.477 :1.934-1.064a1.86
         :1.86 :0 :0 :0
         :.254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857
         :0-.288-.036-.585-.113-.856a2 :2 :0 :0 :0-.138-.362
         :1.9 :1.9 :0 :0 :0 :.234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a10
         :10 :0 :0 :0-.443.05 :9.4 :9.4 :0 :0 :0-.062-4.509A1.38
         :1.38 :0 :0 :0 :9.125.111zM11.5 :14.721H8c-.51
         :0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65
         :1.095-.3 :1.977-.996 :2.614-1.708.635-.71 :1.064-1.475
         :1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34
         :8.34 :0 :0 :1-.145 :4.725.5.5 :0 :0 :0 :.595.644l.003-.001.014-.003.058-.014a9
         :9 :0 :0 :1 :1.036-.157c.663-.06 :1.457-.054 :2.11.164.175.058.45.3.57.65.107.308.087.67-.266
         :1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 :0
         :.212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.2
         :2.2 :0 :0 :1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.9.9
         :0 :0 :1-.121.416c-.165.288-.503.56-1.066.56z]}]])

(def hand-thumbs-down-outlined-icon
[:svg icon-prop
 [:path
  {:d [:M8.864
       :15.674c-.956.24-1.843-.484-1.908-1.42-.072-1.05-.23-2.015-.428-2.59-.125-.36-.479-1.012-1.04-1.638-.557-.624-1.282-1.179-2.131-1.41C2.685
       :8.432 :2 :7.85 :2 :7V3c0-.845.682-1.464 :1.448-1.546 :1.07-.113 :1.564-.415
       :2.068-.723l.048-.029c.272-.166.578-.349.97-.484C6.931.08
       :7.395 :0 :8 :0h3.5c.937 :0 :1.599.478 :1.934 :1.064.164.287.254.607.254.913 :0
       :.152-.023.312-.077.464.201.262.38.577.488.9.11.33.172.762.004
       :1.15.069.13.12.268.159.403.077.27.113.567.113.856s-.036.586-.113.856c-.035.12-.08.244-.138.363.394.571.418
       :1.2.234 :1.733-.206.592-.682 :1.1-1.2 :1.272-.847.283-1.803.276-2.516.211a10
       :10 :0 :0 :1-.443-.05 :9.36 :9.36 :0 :0 :1-.062 :4.51c-.138.508-.55.848-1.012.964zM11.5 :1H8c-.51
       :0-.863.068-1.14.163-.281.097-.506.229-.776.393l-.04.025c-.555.338-1.198.73-2.49.868-.333.035-.554.29-.554.55V7c0
       :.255.226.543.62.65 :1.095.3 :1.977.997 :2.614 :1.709.635.71 :1.064 :1.475 :1.238 :1.977.243.7.407 :1.768.482
       :2.85.025.362.36.595.667.518l.262-.065c.16-.04.258-.144.288-.255a8.34
       :8.34 :0 :0 :0-.145-4.726.5.5 :0 :0 :1 :.595-.643h.003l.014.004.058.013a9 :9 :0 :0 :0
       :1.036.157c.663.06 :1.457.054
       :2.11-.163.175-.059.45-.301.57-.651.107-.308.087-.67-.266-1.021L12.793
       :7l.353-.354c.043-.042.105-.14.154-.315.048-.167.075-.37.075-.581s-.027-.414-.075-.581c-.05-.174-.111-.273-.154-.315l-.353-.354.353-.354c.047-.047.109-.176.005-.488a2.2
       :2.2 :0 :0 :0-.505-.804l-.353-.354.353-.354c.006-.005.041-.05.041-.17a.9.9
       :0 :0 :0-.121-.415C12.4 :1.272 :12.063 :1 :11.5 :1]}]])

(def hand-thumbs-up-filled-icon
  [:svg icon-prop
   [:path
    {:d [:M6.956 :1.745C7.021.81 :7.908.087 :8.864.325l.261.066c.463.116.874.456
         :1.012.965.22.816.533 :2.511.062 :4.51a10 :10 :0 :0 :1 :.443-.051c.713-.065
         :1.669-.072 :2.516.21.518.173.994.681 :1.2 :1.273.184.532.16 :1.162-.234
         :1.733q.086.18.138.363c.077.27.113.567.113.856s-.036.586-.113.856c-.039.135-.09.273-.16.404.169.387.107.819-.003
         :1.148a3.2 :3.2 :0 :0 :1-.488.901c.054.152.076.312.076.465 :0 :.305-.089.625-.253.912C13.1
         :15.522 :12.437 :16 :11.5 :16H8c-.605 :0-1.07-.081-1.466-.218a4.8 :4.8 :0 :0
         :1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 :14.464 :2 :13.846
         :2 :13V9c0-.85.685-1.432 :1.357-1.615.849-.232 :1.574-.787
         :2.132-1.41.56-.627.914-1.28 :1.039-1.639.199-.575.356-1.539.428-2.59z]}]])

(def hand-thumbs-down-filled-icon
  [:svg icon-prop
   [:path
    {:d  [:M6.956 :14.534c.065.936.952 :1.659 :1.908 :1.42l.261-.065a1.38 :1.38 :0 :0 :0
          :1.012-.965c.22-.816.533-2.512.062-4.51q.205.03.443.051c.713.065 :1.669.071
          :2.516-.211.518-.173.994-.68 :1.2-1.272a1.9 :1.9 :0 :0
          :0-.234-1.734c.058-.118.103-.242.138-.362.077-.27.113-.568.113-.856
          :0-.29-.036-.586-.113-.857a2 :2 :0 :0 :0-.16-.403c.169-.387.107-.82-.003-1.149a3.2
          :3.2 :0 :0 :0-.488-.9c.054-.153.076-.313.076-.465a1.86 :1.86 :0 :0 :0-.253-.912C13.1.757
          :12.437.28 :11.5.28H8c-.605 :0-1.07.08-1.466.217a4.8 :4.8 :0 :0
          :0-.97.485l-.048.029c-.504.308-.999.61-2.068.723C2.682 :1.815 :2 :2.434
          :2 :3.279v4c0 :.851.685 :1.433 :1.357 :1.616.849.232 :1.574.787 :2.132
          :1.41.56.626.914 :1.28 :1.039 :1.638.199.575.356 :1.54.428 :2.591]}]])

(def chat-empty-icon
  [:svg icon-prop
   [:path
    {:d [:M2 :1a1 :1 :0 :0 :0-1 :1v8a1 :1 :0 :0 :0 :1 :1h9.586a2 :2 :0
         :0 :1 :1.414.586l2 :2V2a1 :1 :0 :0 :0-1-1zm12-1a2 :2 :0 :0 :1
         :2 :2v12.793a.5.5 :0 :0 :1-.854.353l-2.853-2.853a1 :1 :0 :0
         :0-.707-.293H2a2 :2 :0 :0 :1-2-2V2a2 :2 :0 :0 :1 :2-2z]}]])

(def chat-filled-icon
  [:svg icon-prop
   [:path
    {:d [:M14 :0a2 :2 :0 :0 :1 :2 :2v12.793a.5.5 :0 :0
         :1-.854.353l-2.853-2.853a1 :1 :0 :0 :0-.707-.293H2a2
         :2 :0 :0 :1-2-2V2a2 :2 :0 :0 :1 :2-2z]}]])
