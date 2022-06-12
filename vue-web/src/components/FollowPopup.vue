<template>
  <el-dialog
    custom-class="dialog-follow fg-dialog"
    destroy-on-close
    :model-value="visible"
    :close-on-click-modal="false"
    :show-close="false"
    @open="openDialog"
    width="400px"
  >
    <template #title>
      <div class="fg-dialog-title">
        <div class="left">
          <span>{{ title }}</span>
          <span class="dialog-title-num">({{ total }})</span>
        </div>
        <div class="right" @click="close">
          <img class="close-img" src="@/assets/create-img/pop_shut.png" />
        </div>
      </div>
    </template>

    <div class="follow-items" v-infinite-scroll="loadList">
      <template v-if="loadStatus != 'loading' && !list.length">
        <!-- <no-content :tip="'No ' + title"></no-content> -->
        <no-content
          v-if="title == 'Followers' || title == '关注者'"
          :tip="$t('account.nofollower')"
        >
        </no-content>
        <no-content
          v-else-if="title == 'Following' || title == '正在关注'"
          :tip="$t('account.nofollowing')"
        >
        </no-content>
      </template>
      <template v-else>
        <div class="follow-item" v-for="(item, i) in list" :key="i">
          <avatar
            class="avatar"
            :imageUrl="$filters.fullImageUrl(item.avatar)"
            :address="item.address"
            :imgWidth="60"
            :imgHeight="60"
            shape="circle"
            @click="goAccount(item)"
          ></avatar>
          <div class="dialog-u-info" @click="goAccount(item)">
            <span>{{ $t("MesPopup.followers") }}</span>
            <span class="dialog-u-name">{{
              item.nickname || $filters.ellipsisAddress(item.address)
            }}</span>
          </div>
          <div class="right" v-if="!connected || user.coinbase != item.address">
            <el-button
              class="follow-btn custom-button"
              v-if="!item.isFollow"
              @click="addFollow(item, i)"
              >{{ $t("MesPopup.follow") }}</el-button
            >
            <el-button
              class="follow-btn"
              v-else
              @click="deleteFollow(item, i)"
              >{{ $t("MesPopup.followed") }}</el-button
            >
          </div>
        </div>
        <follow-load :loadStatus="loadStatus"></follow-load>
      </template>
    </div>
  </el-dialog>
</template>
<script>
import Avatar from "./Avatar.vue";
export default {
  components: { Avatar },
  name: "MesPopup",
  props: {
    show: {
      type: Boolean,
      default: false,
    },
    ftype: {
      type: String,
      default: "",
    },
    address: {
      type: String,
      default: "",
    },
  },
  watch: {
    show(val) {
      this.visible = this.show;
    },
  },
  emits: ['change','close'],
  data() {
    return {
      loading: true,
      list: [],
      total: 0,
      visible: this.show,
      query: {
        page: 1,
        limit: this.$store.state.pageLimit,
      },
      loadStatus: "",
    };
  },
  computed: {
    connected() {
      return this.$store.state.connected;
    },
    user() {
      return this.$store.state.user;
    },
    title() {
      if (this.ftype == "follow") {
        return this.$t("account.Followers");
      }
      return this.$t("account.Following");
    },
  },
  created() {
    this.$nextTick(function () {
      this.loading = false;
    });
  },
  methods: {
    close() {
      this.$emit("close");
    },
    goAccount(item) {
      this.$router.push({ path: "/account/" + item.address });
      this.$emit("close");
    },
    openDialog() {
      this.query.page = 1;
      this.getList();
    },
    loadList() {
      if (this.loadStatus == "over") return;
      this.getList();
    },
    getList() {
      if (this.loadStatus == "loading") return;
      this.loadStatus = "loading";
      let url = this.ftype == "follow" ? "follow.follows" : "follow.followers";
      let params = { ...this.query, address: this.address };

      this.$api(url, params).then((res) => {
        this.loadStatus = "loaded";
        if (this.$tools.checkResponse(res)) {
          if (params.page == 1) this.list = [];
          this.list = this.list.concat(res.data.list);
          this.matchFollow(res.data.list);
          this.query.page += 1;
          this.total = res.data.total;
          this.loadStatus =
            res.data.list.length < res.data.limit ? "over" : this.loadStatus;
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
    deleteFollow(item, index) {
      let address = { address: item.address };
      var that = this;
      this.$api("follow.delete", address).then((res) => {
        if (that.$tools.checkResponse(res)) {
          this.list[index].isFollow = false;
          this.list[index].count = this.list[index].count - 1;
          this.$emit("change");
        }else{
          this.$tools.message(res.errmsg);
        }
      });
    },
    addFollow(item, index) {
      let address = { address: item.address };
      var that = this;
      this.$api("follow.add", address).then((res) => {
        if (that.$tools.checkResponse(res)) {
          that.list[index].isFollow = true;
          that.list[index].count = that.list[index].count + 1;
          this.$emit("change");
        }else{
          this.$tools.message(res.errmsg);
        }
      });
    },
    matchFollow(list){
      if(!this.connected || !list.length) return;
      var addressList = [];
      for(var i = 0; i < list.length; i++){
        addressList.push(list[i].address);
      }
      addressList = addressList.join(",");
      
      var data = {
        address: this.user.coinbase,
        userAddrs: addressList,
      }
      this.$api("follow.match", data).then(res => {
        if(this.$tools.checkResponse(res)){
          var followers = res.data;
          var followerDict= {};
          for(var i = 0; i < followers.length; i ++){
            followerDict[followers[i].followingAddress] = true;
          }
          for(var i = 0; i < this.list.length; i++){
            var address = this.list[i].address;
            this.list[i].isFollow = followerDict[address] ? true : false;
          }
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.dialog-follow {
  .follow-item {
    display: flex;
    margin: 10px auto;
    align-items: center;
    .avatar {
      cursor: pointer;
    }
    .dialog-u-info {
      display: flex;
      flex: 1;
      flex-direction: column;
      padding-left: 10px;
      cursor: pointer;
    }
    .dialog-u-detail {
      display: flex;
      width: 160px;
      justify-content: space-between;
      .dialog-u-header {
        width: 58px;
        height: 58px;
        border-radius: 50%;
      }
    }
  }
}
.dialog-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-family: "Montserrat-Regular";
  font-size: 24px;
  font-weight: 400;
}
.dialog-title-colse {
  border: none;
  padding: 0;
  .close-icon {
    width: 34px;
    height: 34px;
  }
}
.dialog-title-num {
  font-size: 16px;
  padding-left: 15px;
}
.dialog-u-name {
  font-weight: 400;
  color: #1d1e22;
}
.skeleton-image {
  width: 60px;
  height: 60px;
  border-radius: 10px;
}
.skeleton-list {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 60px;
  padding-left: 10px;
  .skeleton-list1 {
    width: 80px;
    height: 18px;
  }
  .skeleton-list2 {
    width: 120px;
    height: 18px;
    margin-top: 5px;
  }
}

</style>
<style lang = "scss">
.dialog-follow {
  .el-dialog__body {
    padding-top: 0;
  }
}
</style>
