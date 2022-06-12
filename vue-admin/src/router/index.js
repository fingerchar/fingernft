import { createRouter, createWebHashHistory } from "vue-router";
import constantRouterMap from "./constantRouterMap";

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRouterMap,
});

export default router;
