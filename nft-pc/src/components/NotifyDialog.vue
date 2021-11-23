<template>
<el-dialog 
  :model-value="visible" :show-close="false" :close-on-click-modal="false"
  @close="$emit('close')"
  @closed="closed"
  @open="openDialog"
  custom-class="cel-dialog notify-dialog" width="400px"
  >
  <template #title>
    <div class="cel-dialog-title">
      <div class="left">
        <span>{{$t('notifyDialog.message')}}</span>
      </div>
      
      <div class="right" @click="$emit('close')">
        <img class="close-img" src="@/assets/create-img/pop_shut.png ">
      </div>
    </div>
  </template>
  <div class="cel-dialog-body">
    <div class="notify-OuterLayer-Wrapper">
      <div class="message-box" v-infinite-scroll="loadList">
        <template v-if="loadStatus != 'loading' && !list.length">
          <no-content :tip="$t('notifyDialog.noMessage')"></no-content>
        </template>
        <template v-else>

          <notify-item v-for="(notify, i) in list" :key="i" :notify="notify" size="small" 
            @close="$emit('close')"
          >
          </notify-item>
          <notify-load :loadStatus="loadStatus"></notify-load>
        </template>

      </div>
      <div @click="seeAll" class="seeAll">{{$t('notifyDialog.seeAll')}}</div>
    </div>
  </div>


</el-dialog>
</template>
<script>
import NotifyItem from '@/components/NotifyItem';
export default {
  name: "NotifyDialog",
  components: { NotifyItem},
  name: "MesPopup",
  props: {
    show:{
      type: Boolean,
      default: false,
    },
  },
  watch:{
    show(val){
      this.visible = this.show;
    }
  },
  data(){
    return {
      visible: this.show,
      loadStatus: "",
      list: [],
      query: {
        page: 1,
        limit: this.$store.state.pageLimit,
      }
    }
  },
  computed:{
  },
  methods:{
    seeAll(){
      this.$router.push("/message?tab=unread");
      this.$emit("close");
    },
    openDialog(){
      this.query.page = 1;
      this.loadStatus = "";
      this.getList();
    },
    loadList(){
      if(this.loadStatus == 'over') return;
      this.getList();
    },
    getList(){
      if(this.loadStatus == 'loading') return;
      this.loadStatus = 'loading';
      let params = {
        ...this.query,
        isRead: 0,
      }
      this.$api("notice.list", params).then((res) => {
        this.loadStatus = 'loaded'
        if(this.$tools.checkResponse(res)){
          if(params.page == 1) this.list = []
          this.list = this.list.concat(res.data.list);
          this.query.page += 1;
          this.loadStatus = res.data.list.length < res.data.limit ? 'over' : this.loadStatus;
        }else{
          this.$tools.message(res.errmsg)
        }
      });
    },
    closed(){
      this.loadStatus = "";
      this.query.page = 1;
      this.list = [];
    },

  }
}
</script>
<style lang="scss" scoped>
.message-box {
  height: 260px;
  padding-right: 5px;
  overflow: auto;
}

.notify-dialog{
  .cel-dialog-body{
    margin-top: -10px !important;
  }
}

.seeAll {
  cursor: pointer;
  font-size: 13px;
  font-weight: 400;
  color: $primaryColor;
  padding-top: 10px;
}

</style>
