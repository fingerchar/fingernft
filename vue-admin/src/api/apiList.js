export default {
  statistics: {
    list: {
      url: "/statistics/list",
      method: "post",
    },
    blindboxlist: {
      url: "/statistics/blindboxlist",
      method: "post",
    }
  },
  auth: {
    login: {
      url: "/auth/login",
      method: "post",
    },
    info:{
      url: "/auth/info",
      method: "post",
    },
    logout:{
      url: '/auth/logout',
      method: 'post'
    }
  },
  user: {
    list: {
      url: "/user/list",
      method: "post",
    },
    detail: {
      url: "/user/detail",
      method: "post",
    },
    verify: {
      url: "/user/verify",
      method: "post",
    },
    enable: {
      url: "/user/enable",
      method: "post",
    },
    userVerify:{
      url: '/user/verify',
      method: 'post',
    },
    white: {
      url: "/user/white",
      method: "post"
    },
    addwhite: {
      url: "/user/addwhite",
      method: "post"
    },
    deletewhite: {
      url: "/user/deletewhite",
      method: "post"
    }
  },
  category: {
    list: {
      url: "/category/list",
      method: "post",
    },
    create: {
      url: "/category/create",
      method: "post",
    },
    update: {
      url: "/category/update",
      method: "post",
    },
    delete: {
      url: "/category/delete",
      method: "post",
    },
    createCategory:{
      url: '/category/create',
      method: 'post',
    },
    updateCategory:{
      url: '/category/update',
      method: 'post',
    },
    disableCategory:{
      url: '/category/disable',
      method: 'post',
    },
    enableCategory:{
      url: '/category/enable',
      method: 'post',
    }
  },
  paytoken: {
    list: {
      url: "/paytoken/list",
      method: "post",
    },
    create:{
      url: "/paytoken/create",
      method: "post",
    },
    update: {
      url: "/paytoken/update",
      method: "post",
    },
    delete: {
      url: "/paytoken/delete",
      method: "post",
    },
  },
  contract: {
    list: {
      url: "/contract/list",
      method: "post",
    },
    verify: {
      url: "/contract/verify",
      method: "post",
    },
    enable: {
      url: "/contract/enable",
      method: "post",
    },
  },
  nft: {
    list: {
      url: "/nft/list",
      method: "post",
    },
    onsale:{
      url: "/nft/onsale",
      method: "post",
    },
    detail: {
      url: "/nft/detail",
      method: 'post',
    },
    verify: {
      url: "/nft/verify",
      method: 'post',
    },
    disaleNft:{
      url:"/nft/disable",
      method:'post'
    }
  },
  order: {
    list: {
      url: "/order/list",
      method: "post",
    },
    transaction:{
      url: "/statistics/transaction",
      method:"post",
    }
  },

  log:{
    list:{
      url:"/log/list",
      method:"post"
    }
  },
  role:{
    list:{
      url: '/role/list',
      method: 'post',
    },
    getPermission:{
      url: '/role/getpermissions',
      method: 'post',
    },
    updatePermission:{
      url: '/role/updatepermissions',
      method: 'post',
    },
    createRole:{
      url: '/role/create',
      method: 'post',
    },
    deleteRole:{
      url: '/role/delete',
      method: 'post',
    },
    updateRole:{
      url: '/role/update',
      method: 'post',
    },
    roleOptions:{
      url: '/role/options',
      method: 'post',
    },
  },
  admin:{
    listAdmin:{
      url: '/adminuser/list',
      method: 'post',
    },
    createAdmin:{
      url: '/adminuser/create',
      method: 'post',
    },
    updateAdmin:{
      url: '/adminuser/update',
      method: 'post',
    },
    deleteAdmin:{
      url: '/adminuser/delete',
      method: 'post',
    },
    setpwd: {
      url: '/adminuser/setpwd',
      method: 'post',
    },
  },
  getKaptcha:{
    kaptcha:{
      url:'/auth/kaptcha',
      method:'post',
    }
  },
  blind: {
    type: {
      list:{
        url: '/blind/type/list',
        method: 'post'
      },
      add: {
        url: '/blind/type/add',
        method: 'post'
      },
      edit: {
        url: '/blind/type/edit',
        method: 'post'
      },
      delete: {
        url: '/blind/type/delete',
        method: 'post'
      },
      all: {
        url: "/blind/type/all",
        method: "post",
      },
    },
    box: {
      list: {
        url: '/blind/box/list',
        method: 'post'  
      },
      add: {
        url: '/blind/box/add',
        method: 'post'  
      },
      edit: {
        url: '/blind/box/edit',
        method: 'post'  
      },
      deleted: {
        url: '/blind/box/deleted',
        method: 'post'  
      },
      addNfts: {
        url: '/blind/box/addNfts',
        method: 'post'  
      },
      all: {
        url: '/blind/box/all',
        method: 'post'
      },
      sync: {
        url: "/blind/box/sync",
        method: "post",
      },
      nfts: {
        url: "/blind/box/nfts",
        method: "post",
      },
      prepare: {
        url: "/blind/box/prepare",
        method: "post",
      },
      verify: {
        url: "/blind/box/verify",
        method: "post",
      }
    },
    nft: {
      list: {
        url: '/blind/nft/list',
        method: 'post'
      },
      add: {
        url: '/blind/nft/add',
        method: 'post'
      },
      edit: {
        url: '/blind/nft/edit',
        method: 'post'
      },
      deleted: {
        url: '/blind/nft/deleted',
        method: 'post'
      },
      all: {
        url: "/blind/nft/all",
        method: "post",
      },
      sync: {
        url: "/blind/nft/sync",
        method: "post",
      },
    },
    paytoken: {
        list: {
          url: "/blind/paytoken/list",
          method: "post",
        },
        save: {
          url: "/blind/paytoken/save",
          method: "post",
        },
        delete: {
          url: "/blind/paytoken/delete",
          method: "post",
        },
        map: {
          url: "/blind/paytoken/map",
          method: "post",
        },
    },
    config: {
      list: {
        url: "/blind/config/list",
          method: "post",
      },
      add: {
        url: "/blind/config/add",
          method: "post",
      },
      edit: {
        url: "/blind/config/edit",
          method: "post",
      },
      delete: {
        url: "/blind/config/delete",
          method: "post",
      }
    }
  },
  config: {
    fetch: {
      url: '/config/fetch',
      method: 'post'
    },
    update: {
      url: '/config/update',
      method: 'post',
    },
  },
  storage: {
    create: {
      url: '/storage/create',
      method: 'post'
    },
    multiupload: {
      url: '/storage/multiupload',
      method: 'post'
    }
  },
}
