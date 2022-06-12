<template>
  <div class="main-wrapper">
    <div class="message-wrapper">
      <div class="title">{{$t('message.message')}}</div>

      <el-tabs class="message-tab-menus" @tab-click="tabindexFunc" v-model="tab">
        <el-tab-pane class="tab-content-info" v-for="(tab,i) in tabs" :key="i" :name="tab.name">
          <template #label>
            <div class="tab-content-title">
              <span>{{tab.title}}</span>
              <span v-if="tab.name == 'all'">({{ message.total }})</span>
              <span v-else>({{ message.unread }})</span>
            </div>
          </template>

          <div class="message-box" v-infinite-scroll="loadList">
            <template v-if="loadStatus != 'loading' && !list[tab.name].length">
              <no-content tip="No Message"></no-content>
            </template>
            <template v-else>
              <div v-if="tab.name=='unread'" class="read-all">
                <span @click="readAll" class='cpointer link bfont'>{{$t('message.readAll')}}</span>
              </div>

              <notice-item v-for="(notice, i) in list[tab.name]" :key="i" :notice="notice" size="big" @close="$emit('close')">
              </notice-item>
              <notice-load size="big" :loadStatus="loadStatus"></notice-load>
            </template>
          </div>

        </el-tab-pane>
      </el-tabs>

    </div>

  </div>
</template>
<script>
  import NoticeItem from '@/components/NoticeItem';
  import NoticeLoad from '@/components/loading/NoticeLoad';
  export default {
    components: { NoticeItem, NoticeLoad },
    data: function () {
      return {
        loadStatus: "",
        list: {
          all: [],
          unread: [],
        },
        tab: "all",
        tabs: [
          { name: 'all', title: this.$t('message.all'), count: 0 },
          { name: "unread", title: this.$t('message.unread'), count: 0 },
        ],
        query: {
          page: 1,
          limit: this.$store.state.pageLimit,
        },
      };
    },
    computed: {
      user () {
        return this.$store.state.user
      },
      message () {
        return this.$store.state.message;
      },
    },
    watch: {
      tab (val, old) {
        if (val == old) return;
        this.query.page = 1;
        this.$router.push({
          path: '/message', query: { tab: this.tab },
        });
        this.loadStatus = "";
        this.getList();
      }
    },
    created () {
      if (this.$route.query.tab) {
        this.tab = this.$route.query.tab;
      }
      this.init();
    },
    methods: {
      init () {
        this.$store.dispatch("countNotices");
        this.query.page = 1;
        this.getList();
      },
      readAll () {
        this.$api("notice.readall").then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.init();
          }
        });
      },
      loadList () {
        if (this.loadStatus == 'over') return;
        this.getList();
      },
      tabindexFunc () {
        return;
      },
      queryMedia (nfts, tab) {
        let _nfts = [];
        for (let i = 0;i < nfts.length;i++) {
          let item = nfts[i];
          _nfts.push(item.content)
        }
        _nfts = this.$tools.serializeNfts2(_nfts);
        let data = {
          info: _nfts,
        }
        if (data.info.length) {
          this.$api('nft.getmedia', data).then((res) => {
            if (this.$tools.checkResponse(res)) {
              this.setNftMedia(res.data, tab);
            }
          })
        }
      },
      setNftMedia (nfts, tab) {
        for (let i = 0;i < this.list[tab].length;i++) {
          let nft = this.list[tab][i];
          for (let key in nfts) {
            let item = key.split(":");
            if (nft.content.address == item[0] && nft.content.tokenId == item[1]) {
              this.list[tab][i].media = nfts[key];
            } else if (nft.content.sellToken == item[0] && nft.content.sellTokenId == item[1]) {
              this.list[tab][i].media = nfts[key];
            } else if (nft.content.buyToken == item[0] && nft.content.buyTokenId == item[1]) {
              this.list[tab][i].media = nfts[key];
            }
          }
        }
      },
      getList () {
        if (this.loadStatus == 'loading') return;
        this.loadStatus = 'loading';
        let params = {
          ...this.query,
          address: this.$store.state.user.coinbase
        }
        let tab = this.tab == 'unread' ? 'unread' : 'all';
        if (tab == 'unread') params.isRead = 0;

        this.$api("notice.list", params).then((res) => {
          this.loadStatus = 'loaded'
          if (this.$tools.checkResponse(res)) {
            if (params.page == 1) this.list[tab] = []
            this.list[tab] = this.list[tab].concat(res.data.list);
            // this.queryMedia(res.data.list, tab);
            this.query.page += 1;
            this.loadStatus = res.data.list.length < res.data.limit ? 'over' : this.loadStatus;
          } else {
            this.$tools.message(res.errmsg)
          }
        });
      },
    },
  };
</script>
<style lang="scss" scoped>
  .message-wrapper {
    max-width: 600px;
    .title {
      padding: 30px 0;
      font-size: 23px;
      font-weight: bold;
    }
    .read-all {
      padding-bottom: 10px;
      border-bottom: 1px solid #f0f0f0;
      margin-bottom: 10px;
      text-align: right;
      .link {
        color: $primaryColor;
      }
    }
    .tab-content-title {
      min-width: $tabTitleWidth;
      text-align: center;
    }
  }

</style>
<style lang="scss">
  .message-tab-menus {
    .el-tabs__item {
      color: #999;
      height: 35px;
      line-height: 35px;
      &:hover,
      &:active,
      &:focus {
        color: #333;
      }
      &.is-active {
        color: #333;
      }
    }
    .el-tabs__active-bar {
      background-color: #333;
    }
  }
</style>
