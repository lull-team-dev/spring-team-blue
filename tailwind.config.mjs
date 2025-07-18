// const plugin = require("tailwindcss/plugin");

// module.exports = {
//   content: ["./src/**/*.{html,js}", "./templates/**/*.html"],
//   safelist: [
//     {
//       pattern: /animate-(fadeIn|fadeOut|slide-fwd-center)/,
//     },
//   ],
//   theme: {
//     extend: {
//       colors: {
//         main: "var(--main-color)",
//         text: "var(--text-color)",
//         "bg-form": "var(--bg-form)",
//       },
//       keyframes: {
//         "fade-slide-in": {
//           "0%": {
//             transform: "scale(0.01) translateX(0%) translateY(143%)",
//             opacity: "0",
//             filter: "blur(20px)",
//           },
//           "100%": {
//             transform: "scale(1) translateX(0%) translateY(0%)",
//             opacity: "1",
//             filter: "blur(0px)",
//           },
//         },
//         "slide-fwd-center": {
//           "0%": {
//             transform: "translateZ(0)",
//           },
//           "100%": {
//             transform: "translateZ(160px)",
//           },
//         },

//         fadeIn: {
//           "0%": { opacity: "0" },
//           "100%": { opacity: "1" },
//         },
//         fadeOut: {
//           "0%": { opacity: "1" },
//           "100%": { opacity: "0" },
//         },
//       },
//       animation: {
//         fadeIn: "fadeIn 0.5s ease-out forwards",
//         fadeOut: "fadeOut 0.5s ease-out forwards",
//         "slide-fwd-center":
//           "slide-fwd-center 0.45s cubic-bezier(0.250, 0.460, 0.450, 0.940) both",
//       },
//     },
//   },
//   plugins: [require("tailwindcss-motion")],
// };
