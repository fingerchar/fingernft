<template>
  <div >
    <div class="g-main f-flex">
      <div class="m-title">{{$t('search.searchResult')}}</div>
      <div class="flex justify-center">
        <div class="tabClass" :class="sortIndex=='first'?'active':''" @click="handleClick('first')">nft</div>
        <div class="tabClass ml-40rpx" :class="sortIndex=='second'?'active':''" @click="handleClick('second')">
          {{$t('search.consumer')}}</div>
      </div>
      <div v-if="sortIndex=='first'" class="NFT">
        <template v-if="loadStatus != 'loading' && !nftList.length">
          <no-content :tip="$t('search.noContent')"></no-content>
        </template>
        <template v-else>
          <div class="m-list">
            <div class="search-list" v-infinite-scroll="loadsearchList">
                <nft-item v-for="(search, i) in nftList" :nft="search" :key="i" :index="i"
                  @showDialog="showDialog" @onLike="onLike">
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
            <div class="search-list" v-infinite-scroll="loadsearchList">
              <div v-for="(account, i) in accountList" :key='i' class="user-item f-flex">
                <div class="inner" @click="goUser(account.address)">
                  <div class="user-item-left f-flex">
                    <avatar :imageUrl="$filters.fullImageUrl(account.avatar)" :address="account.address" :imgWidth="88"
                      :imgHeight="88">
                    </avatar>
                    <span class="name">{{account.nickname}}</span>
                  </div>
                  <div class="user-item-right">
                    {{$filters.ellipsisAddress(account.address)}}
                  </div>
                </div>
              </div>
              <load-status :loadStatus="loadStatus"></load-status>
            </div>
          </div>
        </template>
      </div>
      <sale-dialog :show="showSaleDialog" @close="closeDialog" @confirm="dialogConfirm"
        :asset="dialogOrder" :nft="dialogNft">
      </sale-dialog>
      <cancel-sale-dialog :show="showCancelSaleDialog" @close="closeDialog" @confirm="dialogConfirm"
        :asset="dialogOrder" :nft="dialogNft">
      </cancel-sale-dialog>
      <buy-dialog :show="showBuyDialog" @close="closeDialog" @confirm="dialogConfirm"
        :asset="dialogOrder" :nft="dialogNft">
      </buy-dialog>
      <bid-dialog :show="showBidDialog" @close="closeDialog" @confirm="dialogConfirm"
        :bid="dialogOrder" :nft="dialogNft">
      </bid-dialog>
      <cancel-bid-dialog :show="showCancelBidDialog" @close="closeDialog" @confirm="dialogConfirm"
        :bid="dialogOrder" :nft="dialogNft">
      </cancel-bid-dialog>
      <accept-dialog :show="showAcceptDialog" @close="closeDialog" @confirm="dialogConfirm"
        :bid="dialogOrder" :nft="dialogNft">
      </accept-dialog>
      <transfer-dialog :show="showTransferDialog" @close="closeDialog" @confirm="dialogConfirm"
        :asset="dialogOrder" :nft="dialogNft">
      </transfer-dialog>
      <burn-dialog :show="showBurnDialog" @close="closeDialog" @confirm="dialogConfirm"
        :asset="dialogOrder" :nft="dialogNft">
      </burn-dialog>
    </div>
  </div>
</template>
<script>
import NftDialog from "@/mixins/NftDialog";
import NftItem from "@/mixins/NftItem";
export default {
  mixins: [NftDialog, NftItem],
  data: function () {
    return {
      nftList: [],
      query: {
        search: this.$route.query.keyword || "",
        page: 1,
        limit: this.$store.state.pageLimit,
      },
      loadStatus: "",
      isSortActive: "first",
      sortIndex: "first",
    };
  },
  watch: {
    $route(to, from) {
      if (this.$route.path == "/search") {
        this.query.search = this.$route.query.keyword;
        this.query.page = 1;
        this.loadStatus = "";
        this.init();
      }
    },
  },
  created() {
    this.init();
  },
  mounted() {},
  computed: {
    showAddress() {
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
    goUser(address) {
      this.$router.push({ path: "/account/" + address });
    },
    handleClick(tab) {
      this.loadStatus = "";
      this.sortIndex = tab;
      this.query.page = 1;
      this.search();
    },
    init() {
      this.search();
    },
    reloadList() {
      this.query.page = 1;
      this.search();
    },
    loadsearchList() {
      if (this.loadStatus == "over") return;
      this.search();
    },
    search() {
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
    getNFTs(parameter) {
      var data = {
        ...this.query,
      };
      this.$api("home.search", data).then((res) => {
        this.loadStatus = "loaded";
        if (parameter != undefined) {
          this.isSortActive = parameter;
        }
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
    getAccounts(data) {
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
.f-flex {
  display: flex;
}
.g-main {
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}
.m-title {
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
.m-none {
  width: 100%;
  height: 100%;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 122px;
  margin-bottom: 182px;
}
.search-list {
  margin: 0 -10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.m-none-text {
  margin-top: 23px;
  font-size: 17px;
  font-weight: 400;
  color: #1b1b1b;
  line-height: 8px;
}
.user-tab{
  width: 80%;
  margin-bottom: 40px;
}
.user-item {
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  width: 50%;
  .inner{
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


.NFT {
  width: 100%;
}
.image1 {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  z-index: -1;
}

.banner-img {
  width: 100%;
  height: 100%;
}

.category-tags {
  background: #f4f4f4;
  border-radius: 5px;
  margin-left: 50%;
  transform: translateX(-50%);
  display: flex;
  justify-content: space-around;
}
.category-tags-item {
  background: none;
  border: none;
  color: #999;
  font-weight: bold;
  padding: 2px 6px;
  height: auto;
  margin: 0 10px;
  border-radius: 0;
  cursor: pointer;
  &.active {
    font-size: 8px;
    color: #1b1b1b;
    opacity: 1 !important;
    border-bottom: 2px solid $primaryColor;
  }
}
.search-section {
  margin-bottom: 19px;
  display: flex;
  align-items: center;
  margin-top: 27px;
  justify-content: space-between;
}
.search-sorts {
  display: flex;
  align-items: center;
  line-height: 18px;
  transform: translateX(-10px);
}
.search-sorts-item {
  white-space: nowrap;
  display: flex;
  align-items: center;
  font-size: 8px;
  font-weight: 400;
  cursor: pointer;
  margin-right: 16px;
  .iconfont {
    font-size: 16px;
    font-weight: bold;
    &.active {
      color: $primaryColor;
    }
  }
  &.active {
    color: $primaryColor;
    .iconfont {
      color: $primaryColor;
    }
  }
}
.filter-btn {
  cursor: pointer;
  white-space: nowrap;
  display: flex;
  align-items: center;
  width: fit-content;
  border: 1px solid #eeeeee;
  border-radius: 6px;
  padding: 12px 8px;
  font-size: 8px;
  font-weight: 400;
  color: #1b1b1b;
  line-height: 7px;
  img {
    margin-right: 5px;
  }
}

.filter-popover {
  transform: translateX(-37px);
}
.popover-filters {
  padding: 10px 9px 13px 10px;
  box-shadow: 0px 0px 6px 0px rgba(153, 153, 153, 0.3);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 400;
  color: #444444;
  line-height: 12px;
  width: 160px;
  background: #ffffff;
  border: 1px solid #eeeeee;
  border-radius: 5px;
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
}
.popover-filters-item {
  cursor: pointer;
  margin-left: 8px;
  margin-bottom: 8px;
  background: #f4f4f4;
  border-radius: 5px;
  padding: 6px 8px 6px 7px;
  display: flex;
  justify-content: center;
  align-items: center;
  &.active {
    color: white !important;
    background: $primaryColor !important;
  }
}

.search {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.loading-status {
  padding-bottom: 50px;
}
.search-loading {
  height: 60px;
}
.search-loading-over {
  text-align: center;
  padding: 30px 0;
  color: $grayColor;
}
.tabClass {
  cursor: pointer;
  font-size: 20px;
  &.active {
    color: $primaryColor;
    border-bottom: 2px solid $primaryColor;
  }
}
.ml-40rpx {
  margin-left: 20px;
}
</style>
