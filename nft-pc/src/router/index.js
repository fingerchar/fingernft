import { createRouter, createWebHistory } from "vue-router";
import Common from "@/views/Common";
import NFooter from "@/views/NFooter";
import { markRaw } from "vue";

import HIndex from "../views/sections/HIndex.vue";
import NFTDetail from "../views/sections/NFTDetail.vue";
import Profile from "@/views/sections/Profile.vue";
import ERC721 from "../views/sections/ERC721.vue";
import Connect from "../views/sections/Connect.vue"
import Items from "../views/sections/Items.vue"
import Account from "../views/sections/Account.vue"
import Search from "../views/sections/Search.vue"
import Following from "../views/sections/Following.vue"
import Collection from "../views/sections/Collection.vue"

import Message from "../views/sections/Message.vue"

const routes = [
  {
    path: "/",
    name: "Root",
    component: Common,
    meta: {
      view: markRaw(HIndex)
    }
  },
  {
    path: "/items",
    name: "Items",
    component: NFooter,
    meta: {
      auth: true,
      view: markRaw(Items)
    }
  },
  {
    path: '/detail/:ids',
    name: 'NFTDetail',
    component: NFooter,
    meta: {
      view: markRaw(NFTDetail)
    }
  },
  {
    path: '/create',
    name: 'Create',
    component: NFooter,
    meta: {
      auth: true,
      view: markRaw(ERC721)
    }
  },
  {
    path: '/connect',
    name: 'connect',
    component: Connect,
  }, {
    path: '/profile',
    name: 'profile',
    component: NFooter,
    meta: {
      auth: true,
      view: markRaw(Profile)
    }
  },
  {
    path: '/account/:address',
    name: 'account',
    component: NFooter,
    meta: {
      view: markRaw(Account)
    }
  }, {
    path: '/search',
    name: 'Search',
    component: Common,
    meta: {
      view: markRaw(Search)
    }
  }, {
    path: '/following',
    name: 'Following',
    component: Common,
    meta: {
      auth: true,
      view: markRaw(Following)
    }
  },
  {
    path: '/collection/:address',
    name: 'collection',
    component: Common,
    meta: {
      view: markRaw(Collection)
    }
  },
  {
    path: '/message',
    name: 'Message',
    component: NFooter,
    meta: {
      auth: true,
      view: markRaw(Message)
    }
  }
];

const router = createRouter({
  scrollBehavior() {
    document.getElementById('app').scrollIntoView();
  },
  history: createWebHistory(),
  routes
});

export default router;

