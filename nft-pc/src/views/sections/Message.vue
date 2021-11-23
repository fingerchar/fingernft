<template>
  <div class="headClass">
    <div class="notify-Wrapper">
      <div class="titleAndClose">{{$t('message.message')}}</div>

      <el-tabs class="message-tab-menus"
          @tab-click="tabindexFunc"
          v-model="tab"
        >
        <el-tab-pane class="tab-content-info"
          v-for="(tab,i) in tabs"
          :key="i"
          :name="tab.name"
        >
          <template #label>
            <div class="tab-content-title">
              <span>{{tab.title}}</span>
              <span>({{tab.count}})</span>
            </div>
          </template>

          <div class="message-box" v-infinite-scroll="loadList">
            <template v-if="loadStatus != 'loading' && !nftList[tab.name].length">
              <no-content tip="No Message"></no-content>
            </template>
            <template v-else>
              <div v-if="tab.name=='unread'" class="read-all">
                <span @click="readAll" class='cpointer link bfont'>{{$t('message.readAll')}}</span>
              </div>

              <notify-item v-for="(notify, i) in nftList[tab.name]" :key="i" :notify="notify" size="big" 
                @close="$emit('close')"
              >
              </notify-item>
              <notify-load size="big" :loadStatus="loadStatus"></notify-load>
            </template>
          </div>

        </el-tab-pane>
      </el-tabs>

    </div>
  </div>
</template>
<script>

import NotifyItem from '@/components/NotifyItem';
export default {
  components: { NotifyItem },
  data: function () {
    return {
      loadStatus: "",
      nftList:{
        all: [],
        unread: [],
      },
      tab: "all",
      tabs: [
        { name: 'all', title: "All", count: 0 },
        { name: "unread", title: "UnRead", count: 0 },
      ],
      query: {
        page: 1,
        limit: this.$store.state.pageLimit,
      },
    };
  },
  computed: {
    user(){
      return this.$store.state.user
    }
  },
  watch:{
    tab(val, old){
      if(val == old) return;
      this.query.page = 1;
      this.$router.push({
        path: '/message', query: {tab: this.tab},
      });
      this.loadStatus = "";
      this.getList();
    }
  },

  created(){
    if(this.$route.query.tab){
      this.tab = this.$route.query.tab;
    }
    this.init();
  },
  methods: {
    init(){
      this.noticesCount();
      this.query.page = 1;
      this.getList();
    },
    noticesCount(){
      let data={
        address:this.$store.state.user.coinbase
      }
      this.$api("notice.count", data).then((res) => {
        if(this.$tools.checkResponse(res)){
          this.tabs[0].count = res.data.totalCount;
          this.tabs[1].count = res.data.unreadCount;
        }
      });
    },
    readAll(){
      this.$api("notice.readall").then((res) => {
        if(this.$tools.checkResponse(res)){
          this.init();
        }
      });
    },
    loadList(){
      if(this.loadStatus == 'over') return;
      this.getList();
    },
    queryMedia(nfts,tab){
      let _nfts=[];
      for(let i=0;i<nfts.length;i++){
        let item=nfts[i];
          _nfts.push(item.content)
      }
      _nfts = this.$tools.serializeNfts2(_nfts);
      let data={
        info:_nfts,
        }
      if(data.info.length){
        this.$api('nft.getmedia',data).then((res)=>{
          if(this.$tools.checkResponse(res)){
           this.setNftMedia(res.data,tab);
                //  console.log(this.nftList,'看看')
          }
        })
      }
    },
    setNftMedia(nfts,tab){
      for(let i = 0; i < this.nftList[tab].length; i++){
        let nft = this.nftList[tab][i];
        for(let key in nfts){
          let item = key.split(":");
          if (nft.content.token == item[0] && nft.content.tokenId == item[1]) {
            this.nftList[tab][i].media = nfts[key];
          }
        }
      }
    },
    getList(){
      if(this.loadStatus == 'loading') return;
      this.loadStatus = 'loading';
      let params = {
        ...this.query,
        address:this.$store.state.user.coinbase
      }
      let tab = this.tab == 'unread' ? 'unread' : 'all';
      if(tab == 'unread') params.isRead = 0;

      this.$api("notice.list", params).then((res) => {
        this.loadStatus = 'loaded'
        if(this.$tools.checkResponse(res)){
          if(params.page == 1) this.nftList[tab] = []
          this.nftList[tab] = this.nftList[tab].concat(res.data.list);
          this.queryMedia(res.data.list,tab);
          this.query.page += 1;
          this.loadStatus = res.data.list.length < res.data.limit ? 'over' : this.loadStatus;
        }else{
          this.$tools.message(res.errmsg)
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.titleAndClose{
  padding: 30px 0;
  font-size: 23px;
  font-weight: bold;
}
.notify-Wrapper{
  max-width: 600px;
}
.message-box{
}
.read-all{
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 10px;
  text-align: right;
  .link{
    color: $primaryColor;
  }
}
.tab-content-title{
  min-width: $tabTitleWidth;
  text-align: center;
}


</style>
<style lang="scss">
.message-tab-menus{
  .el-tabs__item{
    color: #999;
    height: 35px;
    line-height: 35px;
    &:hover, &:active, &:focus{
      color: #333;
    }
    &.is-active{
      color: #333;
    }
  }
  .el-tabs__active-bar{
    background-color: #333;
  }
}

</style>
