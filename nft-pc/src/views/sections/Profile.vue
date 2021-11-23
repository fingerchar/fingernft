<template v-if="isLoggedIn">
  <div class="main-wrapper">
    <div class="main-Wrapper2">
      <div class="top">
        <div class="go-back" @click="goBack">
          <span class="iconfont icon-leftarrow"></span><span
            class="text">{{$t('profile.goBack')}}</span>
        </div>

        <div class="top-Row1">{{$t('profile.editProfile')}}</div>
        <div class="top-Row2">
          {{$t('profile.text1')}}
        </div>
      </div>
      <div class="bottom">
        <div class="left">
          <div class="chooseFile-Title">{{$t('profile.chooseFile')}}</div>
          <div class="chooseFile-Content">
            <div class="chooseFile-Text">{{$t('profile.text2')}}</div>
            <div class="chooseFile-Content-RightSection">
              <div class="file-box">
                <input type="file" class="file-btn" @change="imageChange"
                  accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" />
                <span>{{$t('profile.chooseFile')}}</span>
              </div>

              <avatar :imageUrl="$filters.fullImageUrl(imgSrc)" :address="user.coinbase"
                :imgWidth="88" :imgHeight="88"></avatar>
            </div>
          </div>
          <div class="unit">
            <div class="font1">{{$t('profile.displayName')}}</div>
            <el-input v-model="displayName" :placeholder="$t('profile.placeholder1')"></el-input>
          </div>
          <div class="unit">
            <div class="font1">{{$t('profile.bio')}}</div>
            <el-input v-model="bio" :placeholder="$t('profile.placeholder2')"></el-input>
          </div>
          
          <div class="unit">
            <div class="font1">{{$t('profile.customUrl')}}</div>
            <el-input v-model="customUrl" :placeholder="$t('profile.placeholder3')"></el-input>
          </div>
          <div class="unit">
            <div class="font1">{{$t('profile.psop')}}</div>
            <el-input v-model="personalUrl" :placeholder="'https://'"></el-input>
          </div>
         

          
          <div class="Verification">{{$t('profile.verification')}}</div>

          <div class="Verification-Content">
            <div class="Verification-Text">{{$t('profile.text3')}}
            </div>
            <el-button type="primary" class="Verification-Button">{{$t('profile.getVerified')}}</el-button>
          </div>
         

          <el-button type="primary" @click="updateImage" class="lastButton">
            {{$t('profile.updateProfile')}}</el-button>
        </div>
        <!-- <div class="right">
          <div class="right_wrapper">
            <img v-if="imgSrc!=''&&imgSrc!=null" class="theImage2" :src="imgSrc" width="88"
              height="88" />
            <jazzicon v-else :address="user.coinbase" :diameter="88"></jazzicon>
          </div>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";
import { getLocalStorage } from "../../util/local-storage";
export default {
  name: "Profile",
  data: function () {
    return {
      imageFile: "",
      imgSrc: "",

      id: "",
      address: "",
      displayName: "",
      bio: "",
      customUrl: "",
      pic_data: "",
      coinbase: "",
      networkId: "",
      defaultImgSrc: "",
      personalUrl: ""
    };
  },
  created() {
    this.$api("user.profile").then((res) => {
      if (this.$tools.checkResponse(res)) {
        (this.displayName = res.data.user.nickname),
          (this.bio = res.data.user.brief),
          (this.customUrl = res.data.user.shortUrl);
        this.imgSrc = res.data.user.avatar;
        this.id = res.data.user.id;
        this.address = res.data.user.address;
      } else {
        this.$tools.message(res.errmsg);
      }
    });
  },
  computed: {
    user: function () {
      return this.$store.state.user;
    },
  },
  methods: {
    imageChange(e) {
      let file = new FileReader();
      this.imageFile = e.target.files[0];
      file.readAsDataURL(e.target.files[0]);
      file.onload = () => {
        this.imgSrc = file.result;
      };
    },
    goBack() {
      this.$router.go(-1);
    },
    async updateImage() {
      if (!this.imageFile) {
        this.UpdateProfile();
      } else {
        const formData = new FormData();
        formData.append("file", this.imageFile);
        this.$api("storage.upload", formData).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.imgSrc = res.data.url;

            this.UpdateProfile();
          } else {
            this.$tools.message(res.errmsg);
          }
        });
      }
    },
    UpdateProfile() {
      let temporary = {
        address: this.address,
        id: this.id,
        avatar: this.imgSrc,
        nickname: this.displayName,
        brief: this.bio,
        shortUrl: this.customUrl,
      };
      this.$api("user.setprofile", temporary).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.$tools.message("修改成功", "success");
          this.$store.dispatch("authinfo");
          this.goBack();
        } else {
          this.$tools.message(res.errmsg);
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.main-Wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}
.main-Wrapper2 {
  display: flex;
  flex-direction: column;
  width: 680px;
  margin: 0 auto;
  padding-bottom: 100px;
}
.blockchain-message {
  float: left;
  margin-top: 20px;
  font-size: 14px;
  border: 1px solid #dcdede;
  color: #4d4c49;
  width: auto;
  padding: 10px;
}
.message {
  height: 80px;
  line-height: 40px;
}
.top {
  max-width: 64%;
  margin-top: 20px;
}
.go-back {
  cursor: pointer;
  display: flex;
  align-items: center;
  font-weight: 500;
  font-size: 16px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #000000;
  line-height: 48px;
}
.top-Row1 {
  font-size: 32px;
  font-weight: bold;
  color: #000000;
  line-height: 66px;
}
.top-Row2 {
  width: 573px;
  height: 42px;
  font-size: 16px;
  font-family: "Montserrat-Regular";
  font-weight: 400;
  color: #000000;
  line-height: 24px;
  opacity: 0.5;
  margin-top: 9px;
}
.bottom {
  display: flex;
  margin-bottom: 32px;
}
.unit {
  margin-bottom: 24px;
  border: none;
}
// .unitInputWrapper {
//   padding-top: 14px;
//   padding-bottom: 14px;
//   display: flex;
// }
// .unitInputWrapper > input {
//   display: flex;
//   flex: 1;
// }
// .unitInputWrapper > input::placeholder {
//   font-size: 16px;
//   font-weight: 700;
//   color: grey;
// }
.font1 {
  margin-bottom: 10px;
  font-size: 16px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #000000;
  line-height: 24px;
}
.left {
  flex: 1;
}
.right {
  flex: 0.2;
  // margin-left: 40px;
  margin-top: 50px;
  display: flex;
  flex-direction: column;
}
.right_wrapper {
  text-align: right;
  position: sticky;
  top: 116px;
}
.right_wrapperButton {
  display: flex;
  flex-flow: row nowrap;
  height: 40px;
  padding-left: 22px;
  padding-right: 22px;
  min-width: auto;
  border: 1px solid transparent;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  border-radius: 40px;
  font-size: 14px;
  font-weight: 900;
  font-family: inherit;
  transition: all 0.12s ease-in-out 0s;
  transform-origin: center center;
  user-select: none;
  cursor: pointer;
  border-color: transparent;
  color: rgb(0, 102, 255);
  background: rgba(0, 102, 255, 0.15);
  width: 80px;
}
.lastUnit {
  display: flex;
}
.lastButton {
  margin-top: 39px;
  margin-left: 50%;
  transform: translateX(-50%);
  width: 250px;
  height: 54px;
  font-size: 16px;
  font-family: "Montserrat-Regular";
  font-weight: 400;
  line-height: 22px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.font2 {
  color: rgba(4, 4, 5, 0.5);
  font-size: 14px;
  font-weight: 900;
}
.font3 {
  color: rgb(0, 102, 255);
  font-weight: 800;
  font-size: 15px;
  cursor: pointer;
}
.font4 {
  color: rgba(4, 4, 5, 0.5);
  font-size: 14px;
  line-height: 19.32px;
  font-family: inherit;
  font-weight: 500;
  margin-top: 4px;
}
// .unitInputWrapperSpanFont {
//   font-size: 16px;
//   font-weight: 900;
// }
.chooseFile-Title {
  margin-top: 24px;
  font-size: 16px;
  font-weight: 400;
  color: #000000;
  line-height: 24px;
  font-family: Montserrat-Regular;
}
.chooseFile-Text {
  margin-top: 12px;
  font-size: 14px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #000000;
  line-height: 22px;
  opacity: 0.5;
  margin-right: 25px;
}
.chooseFile-Content {
  margin-bottom: 17px;
  display: flex;
  justify-content: space-between;
}
.Verification {
  font-size: 16px;
  font-family: "Montserrat-Regular";
  font-weight: 400;
  color: #000000;
  line-height: 24px;
}
.Verification-Content {
  margin-top: 8px;
  display: flex;
  justify-content: space-between;
}
.Verification-Text {
  font-size: 14px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #000000;
  line-height: 24px;
  opacity: 0.5;
  margin-right: 33px;
}
.Verification-Button {
  padding: 0 20px;
  width: fit-content;
  height: 33px;
  line-height: 33px;
  font-size: 8px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  display: flex;
  justify-content: center;
  align-items: center;
  white-space: nowrap;
  min-height: auto;
}
.file-box {
  white-space: nowrap;
  margin-right: 8px;
  position: relative;
  width: fit-content;
  height: 33px;
  // background:$secondPrimaryColor;
  background: #59A6F3;
  border-radius: 8px;
  padding: 6px 20px;
  font-size: 8px;
  font-family: Montserrat-Regular;
  font-weight: 400;
  color: #fff;
  line-height: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  span {
    cursor: pointer;
  }
}
// .file-box:hover {
//   opacity: 0.5;
// }
.file-btn {
  position: absolute;
  z-index: 1000;
  width: 100%;
  height: 100%;
  opacity: 0;
  font-size: 0;
  cursor: pointer;
}
.chooseFile-Content-RightSection {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}
.theImage1 {
  border-radius: 10px;
  overflow: hidden;
}
.theImage2 {
  border-radius: 50px;
  overflow: hidden;
}
</style>
<style lang="scss">
.jazziconClass {
  div {
    border-radius: 8px !important;
  }
}
.unit input {
  margin-top: 8px !important;
  // width: 387px !important;
  height: 40px !important;
  background: #eeeeee !important;
  border-radius: 5px !important;

  font-size: 14px !important;
  font-family: Montserrat-Regular !important;
  font-weight: 400 !important;
  color: #000000 !important;
  padding-left: 6px !important;
}
.unit input::placeholder {
  font-size: 8px !important;
  font-family: Montserrat-Regular !important;
  font-weight: 400 !important;
  color: #000000 !important;
  line-height: 14px !important;
  opacity: 0.4 !important;
}
</style>
