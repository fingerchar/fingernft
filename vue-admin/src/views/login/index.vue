<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >
      <div class="inner">
        <div class="title-container">
          <h3 class="title">{{ $t("login.title") }}</h3>
        </div>
        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user"> </svg-icon>
          </span>
          <el-input
            v-model="loginForm.username"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
            :placeholder="$t('placeholder.manager')"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            v-model="loginForm.password"
            :type="passwordType"
            name="password"
            auto-complete="on"
            tabindex="2"
            show-password
            :placeholder="$t('placeholder.password')"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item prop="code">
          <span class="svg-container">
            <svg-icon icon-class="lock" />
          </span>
          <el-input
            v-model="loginForm.code"
            auto-complete="off"
            name="code"
            tabindex="2"
            :placeholder="$t('login.verificationCode')"
            class="width-60"
            @keyup.enter="handleLogin"
          />
          <div class="login-code">
            <img :src="codeImg" @click="getCode" />
          </div>
        </el-form-item>

        <el-button
          :loading="loading"
          type="primary"
          class="login-btn"
          @click.prevent="handleLogin"
          >{{ $t("login.login") }}</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: "Login",
    data() {
      const validatePassword = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error(this.$t("form.limitAdministrator")));
        } else {
          callback();
        }
      };
      return {
        loginForm: {
          username: "",
          password: "",
          code: "",
        },
        loginRules: {
          username: [
            {
              required: true,
              message: this.$t("form.emptyAdministrator"),
              trigger: "blur",
            },
          ],
          password: [
            {
              required: true,
              message: this.$t("form.emptyPassword"),
              trigger: "blur",
            },
            { validator: validatePassword, trigger: "blur" },
          ],
        },
        passwordType: "password",
        loading: false,
        redirect: "",
        codeImg: "",
      };
    },
    watch: {
      $route: {
        handler: function (route) {
          const { redirect, ...otherQuery } = route.query;
          this.redirect = redirect;
          this.otherQuery = otherQuery;
        },
        immediate: true,
      },
    },
    created() {
      this.getCode();
    },
    unmounted() {},
    methods: {
      getCode() {
        this.$api("getKaptcha.kaptcha").then((response) => {
          this.codeImg = response.data;
        });
      },
      handleLogin() {
        this.$refs.loginForm.validate((valid) => {
          if (valid && !this.loading) {
            this.loading = true;
            this.$store
              .dispatch("LoginByUsername", this.loginForm)
              .then((res) => {
                this.loading = false;
                if (this.$tool.checkResponse(res)) {
                  this.$router.push({
                    path: this.redirect || "/dashboard",
                    query: this.otherQuery,
                  });
                } else {
                  this.getCode();
                }
              })
              .catch((response) => {
                this.getCode();
                this.$notify.error({
                  title: this.$t("notify.title"),
                  message: this.$t("response." + response.errno),
                });
                this.loading = false;
              });
          } else {
            return false;
          }
        });
      },
    },
  };
</script>

<style lang="scss">
  $bg: #283443;
  $light_gray: #fff;
  $cursor: #fff;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }

  .login-container {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
      .el-input__wrapper {
        box-shadow: none;
        background-color: transparent;
      }
      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        height: 47px;
        caret-color: #333;
        color: #333;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px #fff inset !important;
          -webkit-text-fill-color: #333 !important;
        }
      }
    }

    .el-form-item {
      border-bottom: 1px solid #f0f0f0;
      color: #454545;
      margin-bottom: 30px;
    }
  }
</style>

<style lang="scss" scoped>
  $bg: #2d3a4b;
  $dark_gray: #889aa4;
  $light_gray: #eee;

  .login-container {
    min-height: 100%;
    width: 100%;
    background-color: $bg;
    overflow: hidden;
    background-image: url("~@/assets/image/login-bg.png");
    background-size: cover;

    .login-form {
      box-shadow: 3px 3px 3px 3px rgb(0 0 0 / 30%);
      background: rgba(255, 255, 255, 0.3);
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 10px;
      margin: 120px auto;
      overflow: hidden;
      border-radius: 20px;
      .inner {
        padding: 60px 35px;
        background: #fff;
        border-radius: 20px;
      }
    }
    .login-code {
      padding-top: 5px;
      float: right;
      img {
        cursor: pointer;
        vertical-align: middle;
      }
    }

    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        margin: 0px auto 20px auto;
        text-align: center;
        font-weight: bold;
      }
    }
    .copyright {
      font-size: 12px;
      color: #fff;
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translate(-50%, -50%);
      margin-bottom: 20px;
      letter-spacing: 0.6px;
      a {
        font-weight: bold;
        border-bottom: 1px solid #fff;
        font-family: "PingFangSC-Semibold", sans-serif;
      }
    }
  }

  .login-btn {
    width: 100%;
    margin-top: 20px;
  }
  .width-60 {
    width: 60%;
  }
</style>
<style lang="scss">
.login-container{
  .el-input__wrapper{
    width: 100%;
  }
}
</style>

