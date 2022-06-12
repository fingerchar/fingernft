<template>
  <div class="flex-row-left">
    <div class="flex-row-left-inner">
      <div class="profile-section global-lr align-center justify-between">
        <div class="u-custom-btn" @click="goProfile" v-if="connected && user.coinbase == userinfo.coinbase">
          <span class="iconfont icon-editor"></span>
        </div>

        <div class="margin-left-xs">
          <el-popover placement="bottom" :width="230" trigger="click">
            <share></share>
            <template #reference>
              <div class="u-custom-btn">
                <span class="iconfont icon-upload"></span>
              </div>
            </template>
          </el-popover>
        </div>
      </div>
      <div class="head-portrait">
        <avatar class="u-info-avatar" :imageUrl="$filters.fullImageUrl(userinfo.avatar)" :address="userinfo.coinbase" :imgWidth="88" :imgHeight="88" shape="circular"></avatar>
      </div>
      <div class="user-info">
        <div class="name-text m-bottom-10" v-if="userinfo.nickname">{{userinfo.nickname}}</div>
        <div class="coinbase-text m-bottom-10 flex align-center">
          <span class="m-right-5">{{ userinfo.coinbase ? $filters.ellipsisAddress(userinfo.coinbase) : '' }} </span>
          <i class="el-icon-copy-document cpointer" v-clipboard:copy="userinfo.coinbase" v-clipboard:success="onSuccessCopy" v-clipboard:error="onErrorCopy"></i>
        </div>

        <div class="brief-text m-bottom-10">
          {{ !userinfo.brief ? $t("items.introduction") : userinfo.brief }}
        </div>
        <div class="text-center m-bottom-10" v-if="userinfo.coinbase != user.coinbase">
          <el-button class="follow-btn custom-button" v-if="!isFollow" @click="addFollow()">{{ $t("MesPopup.follow") }}</el-button>
          <el-button class="follow-btn" v-else @click="deleteFollow()">{{ $t("MesPopup.followed") }}</el-button>
        </div>
        <div class="u-info-follow pt-0">
          <div class="follow-item" @click="showFollowing = true">
            <span>{{ $t("items.following") }}</span>
            <span class="u-info-follow-num">({{ stat.followCount || 0 }})</span>
          </div>
          <div class="follow-item" @click="showFollow = true">
            <span>{{ $t("items.followers") }} </span>
            <span class="u-info-follow-num">({{ stat.followerCount || 0 }})</span>
          </div>
        </div>
        <div class="sign-out">
          <template v-if="connected && user.coinbase == userinfo.coinbase">
            <div class="m-bottom-20">
              <el-button class="w100" @click="logout">{{$t('items.signOut')}}</el-button>
            </div>
          </template>
        </div>

      </div>
    </div>

    <follow-popup :show="showFollowing" ftype="follow" @close="closePopup" :address="userinfo.coinbase" @change="$emit('refreshStat')"></follow-popup>
    <follow-popup :show="showFollow" ftype="follower" @close="closePopup" :address="userinfo.coinbase" @change="$emit('refreshStat')">
    </follow-popup>

  </div>

</template>

<script>

  import FollowPopup from "@/components/FollowPopup";
  import Share from "@/components/Share";
  export default {
    name: "Side",
    components: {
      FollowPopup,
      Share,
    },
    props: {
      stat: {
        type: Object,
        default: {},
      },
      userinfo: {
        type: Object,
        default: {},
      },
    },
    emits: ['refreshStat'],
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
      isFollow () {
        return this.userinfo.isFollow;
      }
    },
    data () {
      return {
        showSetupDialog: false,
        showFollow: false,
        showFollowing: false
      }
    },
    methods: {
      onSuccessCopy () {
        this.$tools.message(this.$t("request.copySuccess"), "success");
      },
      onErrorCopy () {
        this.$tools.message(this.$t("request.copyError"));
      },
      goProfile () {
        if (!this.$tools.needLogin()) return;
        this.$router.push("/profile");
      },
      go (path) {
        this.$router.push(path);
      },
      closePopup () {
        this.showFollow = false;
        this.showFollowing = false;
      },
      logout () {
        this.$web3.disconnect();
        this.$router.push("/")
      },
      addFollow () {
        let address = { address: this.userinfo.coinbase };
        var that = this;
        this.$api("follow.add", address).then((res) => {
          if (that.$tools.checkResponse(res)) {
            this.$emit("refreshStat")
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
      deleteFollow () {
        let address = { address: this.userinfo.coinbase };
        var that = this;
        this.$api("follow.delete", address).then((res) => {
          if (that.$tools.checkResponse(res)) {
            this.$emit("refreshStat")
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      },
    },
  }

</script>

<style lang="scss" scoped>
  .head-portrait {
    margin-top: 40px;
    .u-info-avatar {
      display: flex;
      justify-content: center;
    }
  }
  .profile-section {
    display: flex;
    justify-content: flex-end;
  }
  .sign-out {
    margin-top: 80px;
  }

  .margin-left-xs {
    margin-left: 10px !important;
  }

  .pt-0 {
    padding-top: 0 !important;
  }

  .coinbase-text {
    font-weight: 800;
    margin-top: 20px;
    justify-content: center;
  }

  .name-text {
    font-size: 15px;
    font-weight: bold;
    margin-top: 15px;
    text-align: center;
  }
  .brief-text {
    font-size: 12px;
    color: #666666;
    text-align: center;
  }

  .iconfont.icon-upload {
    font-weight: bold;
  }

  .u-info-follow {
    margin-top: 40px;
    color: #444;
    padding-top: 10px;
    font-weight: bold;
    .follow-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 14px;
      cursor: pointer;
      margin-bottom: 15px;
    }
    .u-info-follow-num {
      margin-left: 2px;
      font-weight: 400;
      color: #999;
    }
  }

  .u-custom-btn {
    border-radius: 5px;
    background: #fff;
    padding: 5px 15px;
    color: #666;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    &:hover {
      color: $primaryColor;
      border-color: $primaryColor;
    }
  }

</style>

<style lang="scss">
  .head-portrait .avatar-image {
    justify-content: center;
  }
</style>
