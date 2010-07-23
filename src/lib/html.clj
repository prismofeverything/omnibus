(ns lib.html
  (use hiccup.core))

(defn js-link
  [link]
  `[:script {:src (format "/js/%s.js" ~link) :type "text/javascript"}])

(defn css-link
  [link]
  `[:link {:rel "stylesheet" :type "text/css" :href (format "/css/%s.css" ~link)}])

(defmacro html-page
  [title opts & body]
  `(html
    [:html
     [:head
      [:meta {:http-equiv "Content-Type" :content "text/html; charset=utf-8"}]
      [:title ~title]
      [:link {:rel "icon" :type "image/x-icon" :href "/favicon.ico"}]
      ~@(map js-link (opts :js))
      ~@(map css-link (opts :css))]
     [:body
      ~@body]]
    (if ~(opts :script)
      [:script {:type "text/javascript"} ~(opts :script)])))

