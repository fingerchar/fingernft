// import { asyncRouterMap as _asyncRouterMap, constantRouterMap } from '@/router'
import _asyncRouterMap from "@/router/asyncRouterMap.js";
import constantRouterMap from "@/router/constantRouterMap.js";
/**
 * 通过meta.perms判断是否与当前用户权限匹配
 * @param perms
 * @param route
 */
function hasPermission(perms, route) {
  if (route.meta && route.meta.perms) {
    return perms.some((perm) => route.meta.perms.includes(perm));
  } else {
    return true;
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param perms
 */
function filterAsyncRouter(routes, perms) {
  const res = [];

  routes.forEach((route) => {
    const tmp = { ...route };
    if (tmp.children) {
      tmp.children = filterAsyncRouter(tmp.children, perms);
      if (tmp.children && tmp.children.length > 0) {
        res.push(tmp);
      }
    } else {
      if (hasPermission(perms, tmp)) {
        res.push(tmp);
      }
    }
  });
  return res;
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: [],
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers;
      state.routers = constantRouterMap.concat(routers);
    },
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise((resolve) => {
        const { perms } = data;
        let accessedRouters;
        // const asyncRouterMap = this.getters.rolesId === 2
        //   ? _asyncRouterMap.filter(item => item.path !== '/dashboard') : _asyncRouterMap
        if (perms.includes("*")) {
          accessedRouters = _asyncRouterMap;
        } else {
          accessedRouters = filterAsyncRouter(_asyncRouterMap, perms);
        }
        commit("SET_ROUTERS", accessedRouters);
        resolve();
      });
    },
  },
};

export default permission;
