<template>
  <el-popover placement="bottom-start" trigger="click" :show-arrow="false" popper-class="profile-popover" :offset="0">
    <template #reference>
      <div class="user-avatar">
        <avatar class="avatar-img" :imageUrl="$filters.fullImageUrl(user.avatar)" :address="user.coinbase" :imgWidth="40" :imgHeight="40" shape="circular">
        </avatar>
      </div>
    </template>
    <div class="profile-wrapper">
      <div class="profile-wrapper-inner">
        <div class="profile-header">
          <div class="f3left">
            <div @click="goItems" class="cpointer bfont c3" v-if="user && user.nickname">
              {{ user.nickname }}
            </div>
            <div class="cpointer c6">
              <span @click="goItems"> {{ $filters.ellipsisAddress(user.coinbase) }} </span>
              <span class="el-icon-copy-document m-left-5" v-clipboard:copy="user.coinbase" v-clipboard:success="onSuccessCopy" v-clipboard:error="onErrorCopy"></span>
            </div>
            <div @click="goProfile" class="cpointer cprimary bfont f12" v-if="!user || !user.nickname">
              {{ $t('navigation.sdn')}}
            </div>
          </div>
          <div class="f1right">
            <avatar @click="goItems" class="cpointer avatar-img" :imageUrl="$filters.fullImageUrl(user.avatar)" :address="user.coinbase" :imgWidth="40" :imgHeight="40">
            </avatar>
          </div>
        </div>

        <div class="paytoken-item" v-if="defaultPaytoken">
          <avatar class="paytoken-img" :imageUrl="$filters.fullImageUrl(defaultPaytoken.avatar)" :address="defaultPaytoken.address" :imgWidth="35" :imgHeight="35"></avatar>
          <div class="m-left-10">
            <div class="paytoken-t">
              {{ $t("navigation.balance") }}
            </div>
            <div class="paytoken-v">
              <div class="c3">
                {{ erc20Balance.balance }}
                {{ defaultPaytoken.symbol }}
              </div>
            </div>
          </div>
        </div>

      </div>
      <div class="profile-line"></div>
      <router-link class="link-item" to="/items">
        {{ $t("navigation.myItems") }}</router-link>
      <div class="link-item" @click="goProfile">
        {{ $t("navigation.editProfile") }}
      </div>
      <div class="link-item" @click="logout">
        {{ $t("navigation.disconnect") }}
      </div>
    </div>
  </el-popover>

</template>
<script>
  export default {
    name: "ProfilePopover",
    computed: {
      user: function () {
        return this.$store.state.user;
      },
      defaultPaytoken () {
        return this.$store.getters.defaultSalePayToken();
      },
      erc20Balance () {
        let paytoken = this.$store.getters.payToken(this.defaultPaytoken?.address.toLocaleLowerCase());
        if (!paytoken) return { balance: 0 };
        let balance = this.$store.getters.getBalance(this.defaultPaytoken.address);
        balance = !balance ? 0 : balance;
        return {
          balance: this.$tools.decimal(balance, 3)
        };
      },
    },
    methods: {
      goItems () {
        this.$router.push({ name: "Items" });
      },
      goProfile () {
        if (!this.$tools.needLogin()) return;
        this.$router.push("/profile");
      },
      onSuccessCopy () {
        this.$tools.message(this.$t("request.copySuccess"), "success");
      },
      onErrorCopy () {
        this.$tools.message(this.$t("request.copyError"));
      },
      logout () {
        this.$web3.disconnect();
      },
    },
  }
</script>

<style lang="scss">
  .profile-popover {
    width: 290px !important;
    inset: 0 auto auto 5px !important;
  }
</style>

<style lang="scss" scoped>
  .user-avatar {
    display: flex;
    align-items: center;
  }

  .profile-wrapper {
    padding: 20px 10px;
    display: flex;
    flex-direction: column;
    .profile-wrapper-inner {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
    }

    .avatar-img {
      display: flex;
    }

    .profile-header {
      display: flex;
      width: 100%;
      padding-bottom: 20px;
      .coinbase {
        font-size: 15px;
        margin-bottom: 5px;
      }
      .f3left {
        display: flex;
        flex-direction: column;
        flex: 3;
      }
      .f1right {
        display: flex;
        flex: 1;
        justify-content: flex-end;
      }
    }

    .paytoken-item {
      display: flex;
      align-items: center;
      padding-bottom: 20px;
      .paytoken-img {
        width: 35px;
        height: 35px;
        border-radius: 50%;
      }
      .paytoken-t {
        font-size: 12px;
        font-weight: 400;
        color: #999;
      }
      .paytoken-v {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
      }
      .usdt {
        background: #eeeeee;
        border-radius: 5px;
        padding: 0 5px;
        font-size: 12px;
        line-height: 20px;
      }
    }

    .link-item {
      cursor: pointer;
      font-weight: 400;
      margin-top: 15px;
    }

    .profile-line {
      margin: 10px 0;
      border: 1px solid #eeeeee;
    }
  }
</style>

