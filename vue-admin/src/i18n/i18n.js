import Vue from 'vue';
import VueI18n from 'vue-i18n'
import zh from './langs/zh'
import en from './langs/en'
Vue.use(VueI18n)

const i18n = new VueI18n({
  legacy: false,
  globalInjection: true,
  locale: localStorage.getItem('locale') || 'en',
  messages: {
    en: en,
    zh: zh
  },
  silentTranslationWarn: true
});
export default i18n
