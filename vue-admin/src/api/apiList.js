export default {
  dasheboard: {
    stat: {
      url: "/stat",
      method: "post",
    },
    statOneDay: {
      url: "/statOneDay",
      method: "post",
    },
  },
  auth: {
    login: {
      url: "/auth/login",
      method: "post",
    },
    info: {
      url: "/auth/info",
      method: "post",
    },
    logout: {
      url: "/auth/logout",
      method: "post",
    },
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
    userVerify: {
      url: "/user/verify",
      method: "post",
    },
    white: {
      url: "/user/white",
      method: "post",
    },
    addwhite: {
      url: "/user/addwhite",
      method: "post",
    },
    deletewhite: {
      url: "/user/deletewhite",
      method: "post",
    },
    login: {
      url: "/user/login",
      method: "post",
    },
    info: {
      url: "/user/info",
      method: "post",
    },
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
    createCategory: {
      url: "/category/create",
      method: "post",
    },
    updateCategory: {
      url: "/category/update",
      method: "post",
    },
    disableCategory: {
      url: "/category/disable",
      method: "post",
    },
    enableCategory: {
      url: "/category/enable",
      method: "post",
    },
  },
  paytoken: {
    list: {
      url: "/paytoken/list",
      method: "post",
    },
    create: {
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
    create: {
      url: "/contract/create",
      method: "post",
    },
    delete: {
      url: "/contract/delete",
      method: "post",
    },
    update: {
      url: "/contract/update",
      method: "post",
    },
  },
  nft: {
    list: {
      url: "/nft/list",
      method: "post",
    },
    onsale: {
      url: "/nft/onsale",
      method: "post",
    },
    detail: {
      url: "/nft/detail",
      method: "post",
    },
    verify: {
      url: "/nft/verify",
      method: "post",
    },
    disaleNft: {
      url: "/nft/disable",
      method: "post",
    },
  },
  log: {
    list: {
      url: "/log/list",
      method: "post",
    },
  },
  role: {
    list: {
      url: "/role/list",
      method: "post",
    },
    getPermission: {
      url: "/role/getpermissions",
      method: "post",
    },
    updatePermission: {
      url: "/role/updatepermissions",
      method: "post",
    },
    createRole: {
      url: "/role/create",
      method: "post",
    },
    deleteRole: {
      url: "/role/delete",
      method: "post",
    },
    updateRole: {
      url: "/role/update",
      method: "post",
    },
    roleOptions: {
      url: "/role/options",
      method: "post",
    },
  },
  admin: {
    listAdmin: {
      url: "/adminuser/list",
      method: "post",
    },
    createAdmin: {
      url: "/adminuser/create",
      method: "post",
    },
    updateAdmin: {
      url: "/adminuser/update",
      method: "post",
    },
    deleteAdmin: {
      url: "/adminuser/delete",
      method: "post",
    },
    setpwd: {
      url: "/adminuser/setpwd",
      method: "post",
    },
  },
  getKaptcha: {
    kaptcha: {
      url: "/auth/kaptcha",
      method: "post",
    },
  },
  config: {
    fetch: {
      url: "/config/fetch",
      method: "post",
    },
    update: {
      url: "/config/update",
      method: "post",
    },
    unauthFetch: {
      url: "/config/unauthFetch",
      method: "post",
    },
  },
  txorder: {
    info: {
      url: "/txorder/info",
      method: "post",
    },
  },
  storage: {
    create: {
      url: "/storage/create",
      method: "post",
    },
    multiupload: {
      url: "/storage/multiupload",
      method: "post",
    },
  },
};
