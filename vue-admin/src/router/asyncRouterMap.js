import Layout from "@/views/layout/Layout.vue";
export default [
  {
    path: "/userManagement",
    component: Layout,
    redirect: "noredirect",
    name: "userManagement",
    meta: {
      title: "router.userManagement",
    },
    children: [
      {
        path: "userData",
        component: () => import("@/views/userManagement/userData"),
        name: "userData",
        meta: {
          // title: '用户管理',
          title: "router.userManagement",
          noCache: true,
          icon: "goods",
          perms: [
            "POST /admin/user/stat",
            "POST /admin/user/detail",
            "POST /admin/user/update",
            "POST /admin/user/delete",
            "POST /admin/user/list",
            "POST /admin/user/verify",
            "POST /admin/user/enable",
            "POST /admin/user/disable",
          ],
        },
      },
    ],
  },
  {
    path: "/category",
    component: Layout,
    redirect: "noredirect",
    children: [
      {
        path: "category",
        components: {
          default: () => import("@/views/category/list"),
        },
        name: "category",
        meta: {
          title: "router.sortManagement",
          icon: "goods",
          affix: false,
          perms: [
            "POST /admin/category/update",
            "POST /admin/category/delete",
            "POST /admin/category/create",
            "POST /admin/category/list",
          ],
        },
      },
    ],
  },
  {
    path: "/contract",
    component: Layout,
    redirect: "noredirect",
    children: [
      {
        path: "contract",
        components: {
          default: () => import("@/views/contract/list"),
        },
        name: "contract",
        meta: {
          title: "router.contract",
          icon: "goods",
          affix: false,
          perms: [
            "POST /admin/contract/list",
            "POST /admin/contract/create",
            "POST /admin/contract/update",
            "POST /admin/contract/delete",
          ],
        },
      },
    ],
  },
  {
    path: "/paytoken",
    component: Layout,
    redirect: "noredirect",
    children: [
      {
        path: "paytoken",
        components: {
          default: () => import("@/views/paytoken/list"),
        },
        name: "bpaytoken",
        meta: {
          title: "router.paytoken",
          icon: "goods",
          affix: false,
          perms: [
            "POST /admin/paytoken/list",
            "POST /admin/paytoken/create",
            "POST /admin/paytoken/update",
            "POST /admin/paytoken/enable",
            "POST /admin/paytoken/disable",
          ],
        },
      },
    ],
  },
  {
    path: "/nft",
    component: Layout,
    redirect: "noredirect",
    name: "nftManagement",
    meta: {
      title: "router.nftManagement",
      icon: "goods",
    },
    children: [
      {
        path: "/list",
        component: () => import("@/views/nft/list"),
        name: "nftList",
        meta: {
          title: "router.nftManagement",
          noCache: true,
          perms: [
            "POST /admin/nft/detail",
            "POST /admin/nft/list",
            "POST /admin/nft/verify",
            "POST /admin/nft/enable",
            "POST /admin/nft/disable",
          ],
        },
      },
    ],
  },
  {
    path: "/config",
    component: Layout,
    redirect: "noredirect",
    meta: {
      title: "router.config",
    },
    children: [
      {
        path: "webConfig",
        components: {
          default: () => import("@/views/config/WebConfig.vue"),
        },
        name: "webConfig",
        meta: {
          title: "router.webConfig",
          icon: "goods",
          affix: false,
          perms: ["POST /admin/config/update", "POST /admin/config/fetch"],
        },
      },
      {
        path: "contractConfig",
        components: {
          default: () => import("@/views/config/ContractConfig.vue"),
        },
        name: "contractConfig",
        meta: {
          title: "router.contractConfig",
          icon: "goods",
          affix: false,
          perms: ["POST /admin/config/update", "POST /admin/config/fetch"],
        },
      },
    ],
  },
  {
    path: "/sys",
    component: Layout,
    redirect: "noredirect",
    name: "system",
    meta: {
      title: "router.systemManagement",
      icon: "system",
    },
    children: [
      {
        path: "admin",
        component: () => import("@/views/sys/admin"),
        name: "admin",
        meta: {
          perms: [
            "POST /admin/adminuser/list",
            "POST /admin/adminuser/create",
            "POST /admin/adminuser/update",
            "POST /admin/adminuser/delete",
          ],
          title: "router.administratorManagement",
          noCache: true,
          icon: "goods",
        },
      },
      {
        path: "log",
        component: () => import("@/views/sys/log"),
        name: "log",
        meta: {
          perms: ["POST /admin/log/list"],
          title: "router.operationLog",
          noCache: true,
          icon: "goods",
        },
      },
      {
        path: "role",
        component: () => import("@/views/sys/role"),
        name: "role",
        meta: {
          perms: [
            "POST /admin/role/list",
            "POST /admin/role/create",
            "POST /admin/role/update",
            "POST /admin/role/delete",
            "POST /admin/role/permissions",
          ],
          title: "router.roleManagement",
          noCache: true,
          icon: "goods",
        },
      },
    ],
  },
  {
    path: "/profile",
    component: Layout,
    redirect: "noredirect",
    name: "profile",
    meta: {
      title: "router.setting",
      icon: "system",
    },
    children: [
      {
        path: "password",
        component: () => import("@/views/profile/password"),
        name: "password",
        meta: {
          title: "router.passwordUpdate",
          noCache: true,
        },
      },
      {
        path: "language",
        component: () => import("@/views/profile/language"),
        name: "language",
        meta: {
          title: "router.languageUpdate",
          noCache: true,
        },
      },
    ],
    hidden: true,
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404",
    hidden: true,
  },
];
