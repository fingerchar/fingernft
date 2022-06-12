import { createRouter, createWebHistory } from "vue-router";
import Common from "@/views/Common";
import NFooter from "@/views/NFooter";
import { markRaw, toRaw } from "vue";

import HIndex from "../views/sections/HIndex.vue";
import NFTDetail from "../views/sections/NFTDetail.vue";
import Profile from "@/views/sections/Profile.vue";
import ERC721 from "../views/sections/ERC721.vue";
import Connect from "../views/sections/Connect.vue"
import Items from "../views/sections/Items.vue"
import Account from "../views/sections/Account.vue"

import Search from "../views/sections/Search.vue"
import Collection from "../views/sections/Collection.vue"

import Message from "../views/sections/Message.vue"

import NoFound from '@/views/sections/NoFound.vue';

const routes = [
  {
    path: "/",
    name: "Root",
    component: Common,
    children: [
      {
        path: '/',
        name: "home",
        component: HIndex
      },
      {
        path: '/items',
        component: Items,
        name: "items",
        meta: {
          auth: true
        }
      },
      {
        path: '/erc721',
        name: "erc721",
        component: ERC721,
        meta: {
          auth: true
        }
      },
      {
        path: '/profile',
        name: "profile",
        component: Profile,
        meta: {
          auth: true
        }
      },
      {
        path: '/account/:address',
        name: "account",
        component: Account
      },
      {
        path: '/search',
        name: "Search",
        component: Search
      },
      {
        path: '/collection/:address',
        name: "collection",
        component: Collection
      },
      {
        path: '/message',
        name: "message",
        component: Message,
        meta: {
          auth: true
        }
      },
      {
        path: '/404',
        name: "404",
        component: NoFound
      },
    ]
  },
  {
    path: "/detail",
    component: NFooter,
    children:[
      {
        path: '/detail/:ids',
        name: "detail",
        component: NFTDetail
      },
    ]
  },
  {
    path: '/connect',
    name: 'connect',
    component: Connect,
  },
  {
    path: "/:pathMatch(.*)",
    name: "NoFound",
    redirect: "/404",
  },
];

const router = createRouter({
  scrollBehavior() {
    document.getElementById('app').scrollIntoView();
  },
  history: createWebHistory(),
  routes
});

export default router;

