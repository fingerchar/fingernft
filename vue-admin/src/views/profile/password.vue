<template>
  <div class="app-container">
    <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left"
      label-width="150px" class="dataFormClass">
      <el-form-item  :label="$t('password.oldPassword')" prop="oldPassword">
        <el-input v-model="dataForm.oldPassword" type="password" />
      </el-form-item>
      <el-form-item :label="$t('password.newPassword')" prop="newPassword">
        <el-input v-model="dataForm.newPassword" type="password" auto-complete="off" />
      </el-form-item>
      <el-form-item :label="$t('password.confirmPassword')" prop="newPassword2">
        <el-input v-model="dataForm.newPassword2" type="password" auto-complete="off" />
      </el-form-item>
    </el-form>
    <div class="ml-100px" >
      <el-button @click="cancel">{{$t('util.cancel')}}</el-button>
      <el-button @click="change" type="primary" >{{$t('util.confirm')}}</el-button>
    </div>
  </div>
</template>

<script>

export default {
  name: "ChangePassword",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t('profile.emptyPassword')));
      } 
      else if(value.length < 6){
        callback(new Error(this.$t('profile.limitPassword')));
      }
      else {
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t('profile.resetPassword')));
      } else if (value !== this.dataForm.newPassword) {
        callback(new Error(this.$t('profile.unsamePassword')));
      } else {
        callback();
      }
    };
    return {
      dataForm: {
        oldPassword: "",
        newPassword: "",
        newPassword2: "",
      },
      rules: {
        oldPassword: [
          { required: true, message: this.$t('profile.emptyOldPassword'), trigger: "blur" },
        ],
        newPassword: [
          { required: true, message: this.$t('profile.emptyNewPassword'), trigger: "blur" },
          { validator: validatePass, trigger: "blur" },
        ],
        newPassword2: [
          { required: true, message: this.$t('profile.emptyConfirmPassword'), trigger: "blur" },
          { validator: validatePass2, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    cancel() {
      this.dataForm = {
        oldPassword: "",
        newPassword: "",
        newPassword2: "",
      };
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    change() {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) return
        this.$api('admin.setpwd', this.dataForm).then((response) => {
          if(this.$tool.checkResponse(response)){
            this.$notify.success({
              title: this.$t('global.success'),
              message: this.$t('profile.changeSuccess'),
            })
            this.cancel();
          }else{
            this.$notify.error({
              title: this.$t('global.fail'),
              message: response.errmsg
            })
          }
        }).catch(response => {
          this.$notify.error({
            title: this.$t('global.fail'),
            message: response.errmsg
          })
        })
      })
    }
  },
};
</script>
<style lang="scss" scoped>
.dataFormClass{
width: 400px; 
margin-left:50px;
}
.ml-100px{
  margin-left:100px;
}
</style>
