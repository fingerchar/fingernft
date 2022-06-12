import router from "./router";
import store from "./store";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { getToken } from "@/utils/auth";
import i18n from "@/i18n/i18n.js";
NProgress.configure({ showSpinner: false }); // NProgress Configuration

// permission judge function
function hasPermission(perms, permissions) {
  if (perms.indexOf("*") >= 0) return true; // admin permission passed directly
  if (!permissions) return true;
  return perms.some((perm) => permissions.indexOf(perm) >= 0);
}

const whiteList = ["/login", "/auth-redirect"]; // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  NProgress.start(); // start progress bar
  const hasToken = getToken();
  document.title =
    i18n.t("title.manageBackground") + `-` + i18n.t(to.meta.title);

  if (store.getters.perms.length) {
    // 已经拉取user_info
    const { perms } = store.getters;
    if (to.fullPath == `/login` && hasToken) {
      next({
        path: `/dashboard`,
        replace: true,
      });
      NProgress.done();
    } else {
      if (hasPermission(perms, to.meta.perms)) {
        next();
      } else {
        next({
          path: `/login`,
          query: {
            ...to.query,
            redirect: whiteList.includes(to.path) ? "/dashboard" : to.path,
          },
          replace: true,
        });
      }
    }
  } else if (hasToken) {
    if (whiteList.includes(to.path)) {
      next();
    } else {
      try {
        await store.dispatch("GetUserInfo");
        const perms = store.getters.perms;
        if (
          perms.indexOf("POST /admin/config/fetch") > -1 ||
          perms.indexOf("*") > -1
        ) {
          store.dispatch("findConfig");
        }
        try {
          await store.dispatch("GenerateRoutes", { perms });
          store.getters.addRouters?.forEach((r) => {
            router.addRoute(r);
          });
          next({ ...to, replace: true });
        } catch (e) {
          console.error(e);
          next({
            path: `/login`,
            query: {
              ...to.query,
              redirect: to.path === "/login" ? "/dashboard" : to.path,
            },
            replace: true,
          });
        }
      } catch (e) {
        await store.dispatch("LogOut");
        next({
          path: `/login`,
          query: {
            ...to.query,
            redirect: to.path === "/login" ? "/dashboard" : to.path,
          },
          replace: true,
        });
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next();
    } else {
      next({
        path: `/login`,
        query: {
          ...to.query,
          redirect: to.path === "/login" ? "/dashboard" : to.path,
        },
        replace: true,
      });
      NProgress.done(); // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
});

router.afterEach(() => {
  NProgress.done(); // finish progress bar
});
