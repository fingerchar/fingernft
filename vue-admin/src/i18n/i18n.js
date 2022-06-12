import { createI18n } from "vue3-i18n";
import zh from "./langs/zh";
import en from "./langs/en";
import Cookies from "js-cookie";

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: Cookies.get("language") || "en",
  messages: {
    en: en,
    zh: zh,
  },
  silentTranslationWarn: true,
});
export default i18n;
