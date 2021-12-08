const codeMessage = {
  501: {
    type: 'error',
    title: '错误',
    message: '身份信息过期，请重新登录',
    confirmButtonText: '确定'
  },
  502: {
    type: 'error',
    title: '错误',
    message: '系统内部错误，请联系管理员维护',
    confirmButtonText: '确定'
  },
  503: {
    type: 'error',
    title: '警告',
    message: '请求业务目前未支持',
    confirmButtonText: '确定'
  },
  504: {
    type: 'error',
    title: '警告',
    message: '更新数据已经失效，请刷新页面重新操作',
    confirmButtonText: '确定'
  },
  505: {
    type: 'error',
    title: '错误',
    message: '更新失败，请再尝试一次',
    confirmButtonText: '确定'
  },
  506: {
    type: 'error',
    title: '错误',
    message: '没有操作权限，请联系管理员授权',
    confirmButtonText: '确定'
  },
  507: {
    type: 'error',
    title: '错误',
    message: '参数错误',
    confirmButtonText: '确定'
  },
  555: {
    type: 'error',
    title: '发送失败',
    message: '发送达到上限'
  },
  '*': {
    type: 'error',
    title: '错误',
    message: '登录连接超时（后台不能连接，请联系系统管理员）',
    confirmButtonText: '确定'
  }
}

// 就算报错也不提示的接口
const whiteList = [
  '/admin/goods/getBySn',
  '/admin/goods/checkBySn'
]

/**
 * 处理errno
 * @param res
 */
export default res => {
  const { data = {}, message, response, config } = res
  const { errno, errmsg } = data
  const options = {}
  const url = config.url.match(/(\/admin|\/jzshop)(.*)/)[0]
  if (whiteList.includes(url)) {
    options.errno = errno
    options.errmsg = errmsg
    options.type = 'success'
  } else if (typeof errno !== 'undefined') { // 正常的报错
    if (errno === 0) {
      options.errno = 0
      options.errmsg = errmsg
      options.type = 'success'
    } else {
      options.errno = errno
      options.type = 'error'
      if (errmsg) {
        options.errmsg = errmsg
      } else if (errno in codeMessage) {
        const { type, message } = codeMessage[res.errno]
        options.errmsg = message
        options.type = type
      } else {
        const { type, message } = codeMessage['*']
        options.errmsg = message
        options.type = type
      }
    }
  } else { // 不正常的报错、例如请求超时
    options.type = 'error'
    if (!response) {
      options.errmsg = message
      options.errno = 408
    } else {
      options.errmsg = response.statusText || message
      options.errno = response.status
    }
  }

  return options
}

