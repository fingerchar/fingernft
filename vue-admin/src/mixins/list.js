/*
 * @Author: Rotten
 * @Date: 2020-06-30 10:04:57
 * @Last Modified by: Rotten
 * @Last Modified time: 2020-07-01 15:07:00
 */

const List = {
  data() {
    return {
      // 增
      add_data: null,
      add_data_show: false,
      // 删
      del_data: null,
      del_data_show: false,
      // 改
      edit_data: null,
      edit_data_show: false,
      // 查
      detail_data: null,
      detail_data_show: false,
      // 查列表
      list_data: [],
      list_total: null,
      list_loading: false,
      // 可继承拓展
      list_query: {
        page: 1,
        limit: 10
      },
      // 旧参数
      old_list_query: null,
      rules_query: {} // 预留
    }
  },
  created() {
    // searchForm-没生成
  },
  mounted() {
    this.ooGetList()
    // 丐帮拷贝法
    this.old_list_query = JSON.stringify(this.list_query)
  },
  methods: {
    // 请求列表数据，保留外部继承
    ooGetList() {
      // pass
    },
    // 改变条数
    ooSizeChange(val) {
      this.list_query.limit = val
      this.ooGetList()
    },
    // 改变页码
    ooCurrentChange(val) {
      this.list_query.page = val
      this.ooGetList()
    },
    // 条件搜索
    ooFilter() {
      this.list_query.page = 1
      this.ooGetList()
    },
    // 重置条件
    ooReset() {
      this.list_query = JSON.parse(this.old_list_query)
    },
    // 新增
    ooAdd() {
      this.add_data_show = true
    },
    // 关闭新增
    ooCancelAdd() {
      this.add_data_show = false
      this.add_data = null
    },
    // 新增确认回调
    ooConfirmAdd() {
      this.add_data_show = false
      this.add_data = null
      this.ooGetList()
    },
    // 编辑
    ooEdit(val) {
      this.edit_data_show = true
      this.edit_data = val
    },
    // 关闭编辑
    ooCancelEdit() {
      this.edit_data_show = false
      this.edit_data = null
    },
    // 编辑确认回调
    ooConfirmEdit() {
      this.edit_data_show = false
      this.edit_data = null
      this.ooGetList()
    },
    // 查看数据
    ooDetail(val) {
      this.detail_data = val
      this.detail_data_show = true
    },
    // 关闭查看
    ooDetailEdit() {
      this.detail_data_show = false
      this.detail_data = null
    },
    // 删除
    ooDelete(val) {
      this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.ooConfirmDelete(val)
        })
        .catch(() => {
          // pass
          this.ooCancelDelete()
        })
    },
    // 取消删除
    ooCancelDelete(val) {
      // 保留
    },
    // 删除确认回调
    ooConfirmDelete(val) {
      // 保留
    }
  }
}

export default List
