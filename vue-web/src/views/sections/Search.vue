<template>
  <div class="main-wrapper">
    <div class="g-main flex">
      <div class="title">{{$t('search.searchResult')}}</div>
      <div class="flex justify-center">
        <div class="tabClass" :class="sortIndex=='first'?'active':''" @click="handleClick('first')">nft</div>
        <div class="tabClass m-left-20" :class="sortIndex=='second'?'active':''" @click="handleClick('second')">
          {{$t('search.consumer')}}</div>
      </div>
      <div v-if="sortIndex=='first'" class="nft-tab">
        <template v-if="loadStatus != 'loading' && !nftList.length">
          <no-content :tip="$t('search.noContent')"></no-content>
        </template>
        <template v-else>
          <div class="m-list">
            <div class="flex-box" v-infinite-scroll="loadsearchList">
              <nft-item v-for="(nft, i) in nftList" :nft="nft" :key="i" :index="i" @showDialog="showDialog" @onLike="onLike">
              </nft-item>
              <nft-item-load :loadStatus="loadStatus"></nft-item-load>
            </div>
          </div>
        </template>
      </div>
      <div v-if="sortIndex=='second'" class="user-tab">
        <template v-if="loadStatus != 'loading' && !accountList.length">
          <no-content :tip="$t('search.noContent')"></no-content>
        </template>
        <template v-else>
          <div class="m-list">
            <div class="flex-box" v-infinite-scroll="loadsearchList">
              <account-item v-for="(account, i) in accountList" :key="i" :account="account" class="account-item">
              </account-item>
              <load-status :loadStatus="loadStatus"></load-status>
            </div>
          </div>
        </template>
      </div>

      <sale-dialog :show="showSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </sale-dialog>
      <cancel-sale-dialog :show="showCancelSaleDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </cancel-sale-dialog>
      <buy-dialog :show="showBuyDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </buy-dialog>
      <bid-dialog :show="showBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </bid-dialog>
      <cancel-bid-dialog :show="showCancelBidDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </cancel-bid-dialog>
      <accept-dialog :show="showAcceptDialog" @close="closeDialog" @confirm="dialogConfirm" :bid="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </accept-dialog>
      <transfer-dialog :show="showTransferDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </transfer-dialog>
      <burn-dialog :show="showBurnDialog" @close="closeDialog" @confirm="dialogConfirm" :asset="dialogOrder" :nft="dialogNft" :uri="dialogNftURI">
      </burn-dialog>

    </div>
  </div>
</template>
<script>
  import NftDialog from "@/mixins/NftDialog";
  import NftItem from "@/mixins/NftItem";
  import AccountItem from '@/components/AccountItem';
  export default {
    mixins: [NftDialog, NftItem],
    components: {
      AccountItem,
    },
    data: function () {
      return {
        nftList: [],
        query: {
          search: this.$route.query.keyword || "",
          page: 1,
          limit: this.$store.state.pageLimit,
        },
        loadStatus: "",
        sortIndex: "first",
        accountList: []
      };
    },
    watch: {
      $route (to, from) {
        if (this.$route.path == "/search") {
          this.query.search = this.$route.query.keyword;
          this.query.page = 1;
          this.loadStatus = "";
          this.init();
        }
      },
    },
    created () {
      this.init();
    },
    mounted () { },
    computed: {
      showAddress () {
        return (search) => {
          var res =
            search.address.slice(0, 11) + "..." + search.address.slice(-4);
          return res;
        };
      },
      user: function () {
        var user = this.$store.state.user;
        return user;
      },
    },
    methods: {
      goUser (address) {
        this.$router.push({ path: "/account/" + address });
      },
      handleClick (tab) {
        this.loadStatus = "";
        this.sortIndex = tab;
        this.query.page = 1;
        this.search();
      },
      init () {
        this.search();
      },
      reloadList () {
        this.query.page = 1;
        this.search();
      },
      loadsearchList () {
        if (this.loadStatus == "over") return;
        this.search();
      },
      search () {
        if (this.loadStatus == "loading") return;
        this.loadStatus = "loading";
        let data = {
          ...this.query,
        };
        if (this.sortIndex == "second") {
          this.getAccounts(data);
        } else {
          this.getNFTs(data);
        }
      },
      getNFTs (parameter) {
        var data = {
          ...this.query,
        };
        this.$api("home.search", data).then((res) => {
          this.loadStatus = "loaded";
          if (this.$tools.checkResponse(res)) {
            if (data.page == 1) this.nftList = [];
            this.nftList = this.nftList.concat(res.data.list);
            this.queryFunction(res.data.list);
            if (res.data.list.length < data.limit) {
              this.loadStatus = "over";
            } else {
              this.query.page += 1;
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      getAccounts (data) {
        this.$api("home.searchuser", data).then((res) => {
          this.loadStatus = "loaded";
          if (this.$tools.checkResponse(res)) {
            if (data.page == 1) this.accountList = [];
            this.accountList = this.accountList.concat(res.data.list);
            if (res.data.list.length < data.limit) {
              this.loadStatus = "over";
            } else {
              this.query.page += 1;
            }
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
    },
  };
</script>
<style lang="scss" scoped>
  .g-main {
    flex-direction: column;
    align-items: center;
    padding: 20px 0;
  }
  .title {
    margin-bottom: 20px;
    margin-top: 13px;
    font-size: 17px;
    font-weight: 400;
    color: #999;
  }

  .m-list {
    margin-top: 28px;
    width: 100%;
  }

  .user-tab {
    width: 80%;
    margin-bottom: 40px;
  }
  .user-item {
    justify-content: space-between;
    align-items: center;
    padding-bottom: 10px;
    width: 50%;
    .inner {
      width: 100%;
      display: flex;
      align-items: center;
      background: #fff;
      padding: 10px;
      border-radius: $borderRadius;
      margin: 0 5px;
      cursor: pointer;
    }
    .user-item-left {
      cursor: pointer;
      align-items: center;
      flex: 1;
      .image2 {
        align-self: flex-end;
        margin-left: -13px;
        width: 13px;
        height: 13px;
      }
      .name {
        margin-left: 9px;
        font-size: 13px;
        font-weight: bold;
        color: #1b1b1b;
      }
    }
    .user-item-right {
      font-size: 12px;
      color: #999;
    }
  }

  .nft-tab {
    width: 100%;
  }

  .account-item {
    width: 50%;
  }

  .tabClass {
    cursor: pointer;
    font-size: 20px;
    &.active {
      color: $primaryColor;
      border-bottom: 2px solid $primaryColor;
    }
  }
</style>
