import Layout from "@/views/layout/Layout.vue";

export default [
  {
    path: "/",
    component: Layout,
    redirect: "dashboard",
    children: [
      {
        path: "dashboard",
        components: {
          default: () => import("@/views/dashboard/index"),
        },
        name: "dashboard",
        meta: {
          title: "router.dashboard",
          icon: "dashboard",
          affix: false,
        },
      },
    ],
  },
  {
    path: "/redirect",
    component: Layout,
    hidden: true,
    children: [
      {
        path: "/redirect/:path*",
        component: () => import("@/views/redirect/index"),
      },
    ],
  },
  {
    path: "/login",
    component: () => import("@/views/login/index"),
    meta: {
      title: "login",
      icon: "dashboard",
      affix: false,
    },
    hidden: true,
  },
  {
    path: "/auth-redirect",
    component: () => import("@/views/login/authredirect"),
    hidden: true,
  },
  {
    path: "/404",
    component: () => import("@/views/errorPage/404"),
    hidden: true,
  },
];
