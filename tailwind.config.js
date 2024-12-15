/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./resources/public/index.html",
        "./resources/public/js/compiled/*.js",
        "./resources/public/js/compiled/cljs-runtime/*.js"
    ],
    theme: {
        extend: {},
    },
    plugins: [],
}
