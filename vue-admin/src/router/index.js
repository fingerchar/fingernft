import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'
import AppMain from '@/views/layout/components/AppMain'

export const constantRouterMap = [{
    path: '/',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      components: {
        default: () => import('@/views/dashboard/index')
      },
      name: 'dashboard',
      meta: {
        title: 'router.dashboard',
        icon: 'dashboard',
        affix: false
      }
    }]
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [{
      path: '/redirect/:path*',
      component: () => import('@/views/redirect/index')
    }]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    meta: {
      title: '登录',
      icon: 'dashboard',
      affix: false
    },
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  }
]

export const asyncRouterMap = [{
    path: '/userManagement',
    component: Layout,
    redirect: 'noredirect',
    name: 'userManagement',
    meta: {
      title: "router.userManagement",
    },
    children: [{
      path: 'userData',
      component: () => import('@/views/userManagement/userData'),
      name: 'userData',
      meta: {
        // title: '用户管理',
        title: 'router.userManagement',
        noCache: true,
        icon: 'goods',
        perms: ['POST /admin/user/stat', 'POST /admin/user/detail', 'POST /admin/user/update', 'POST /admin/user/delete', 'POST /admin/user/list', 'POST /admin/user/verify', 'POST /admin/user/enable', 'POST /admin/user/disable'],
      }
    }], 
  },
  {
    path: '/category',
    component: Layout,
    redirect: 'noredirect',
    children: [{
      path: 'category',
      components: {
        default: () => import('@/views/category/list')
      },
      name: 'category',
      meta: {
        title: 'router.sortManagement',
        icon: 'goods',
        affix: false,
        perms: ['POST /admin/category/update', 'POST /admin/category/delete', 'POST /admin/category/create', 'POST /admin/category/list'],
      }
    }]
  },
  {
    path: '/nft',
    component: Layout,
    redirect: 'noredirect',
    // alwaysShow: true,
    name: 'nftManagement',
    meta: {
      title: 'router.nftManagement',
      icon: 'goods'
    },
    children: [{
        path: '/list',
        component: () => import('@/views/nft/list'),
        name: 'nftList',
        meta: {
          title: 'router.nftManagement',
          noCache: true,
          perms: ['POST /admin/nft/detail', 'POST /admin/nft/list', 'POST /admin/nft/verify', 'POST /admin/nft/enable', 'POST /admin/nft/disable'],
        }
      },
    ]
  },
  {
    path: '/transactionManagement',
    component: Layout,
    redirect: 'noredirect',
    name: 'TransactionManagement',
    meta: {
      title: 'router.transactionManagement',
      icon: 'goods'
    },
    children: [{
        path: 'message',
        components: {
          default: () => import('@/views/transactionManagement/message')
        },
        name: 'TransactionMessage',
        meta: {
          title: 'router.transactionInformation',
          icon: 'goods',
          affix: false,
          perms: ['POST /admin/statistics/transaction'],
        }
      },
      {
        path: 'statistics',
        components: {
          default: () => import('@/views/transactionManagement/statistics')
        },
        name: 'TransactionStatistics',
        meta: {
          title: 'router.transactionStatistics',
          icon: 'goods',
          affix: false,
          perms: ['POST /admin/statistics/transaction'],
        }
      }
    ]
  },
  {
    path: '/sys',
    component: Layout,
    redirect: 'noredirect',
    name: 'system',
    meta: {
      title: 'router.systemManagement',
      icon: 'system'
    },
    children: [{
        path: 'admin',
        component: () => import('@/views/sys/admin'),
        name: 'admin',
        meta: {
          perms: ['POST /admin/adminuser/list', 'POST /admin/adminuser/create', 'POST /admin/adminuser/update', 'POST /admin/adminuser/delete'],
          title: 'router.administratorManagement',
          noCache: true,
          icon: 'goods',
        }
      },
      {
        path: 'log',
        component: () => import('@/views/sys/log'),
        name: 'log',
        meta: {
          perms: ['POST /admin/log/list'],
          title: 'router.operationLog',
          noCache: true,
          icon: 'goods',
        }
      },
      {
        path: 'role',
        component: () => import('@/views/sys/role'),
        name: 'role',
        meta: {
          perms: ['POST /admin/role/list', 'POST /admin/role/create', 'POST /admin/role/update', 'POST /admin/role/delete', 'POST /admin/role/permissions', 'POST /admin/role/permissions'],
          title: 'router.roleManagement',
          noCache: true,
          icon: 'goods',
        }
      },
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: 'noredirect',
    // alwaysShow: true,
    name: 'profile',
    meta: {
      title: 'router.setting',
      icon: 'system'
    },
    children: [{
        path: 'password',
        component: () => import('@/views/profile/password'),
        name: 'password',
        meta: {
          title: 'router.passwordUpdate',
          noCache: true
        }
      },
      {
        path: 'language',
        component: () => import('@/views/profile/language'),
        name: 'language',
        meta: {
          title: 'router.languageUpdate',
          noCache: true
        }
      }
    ],
    hidden: true
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  },
]

export default new Router({
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
})
