import Clipboard from "./clipboard";

export default {
  install(Vue) {
    Vue.directive("clipboard", Clipboard);
  },
};

/* const install = function(Vue) {
  Vue.directive('Clipboard', Clipboard)
}

if (window.Vue) {
  window.clipboard = Clipboard
  Vue.use(install); // eslint-disable-line
}

Clipboard.install = install
export default Clipboard*/
