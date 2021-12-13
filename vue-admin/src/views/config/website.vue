<template>
  <div class="config-group">
    <div class="config-item">
      <div class="item-label">{{ $t('config.site')}}</div>
      <div class="item-value" v-if="edit.name != 'WebSite' || edit.status != 'edit' ">
        <div class="show-value">{{ config.WebSite }}</div>
        <el-link type="danger" @click="editConfig('WebSite', config.WebSite)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>

    <div class="config-item">
      <div class="item-label">{{ $t('config.staticUrl')}}</div>
      <div class="item-value" v-if="edit.name != 'StaticPath' || edit.status != 'edit'">
        <div class="show-value" >{{ config.StaticPath }}</div>
        <el-link type="danger" @click="editConfig('StaticPath', config.StaticPath)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>

    <div class="config-item">
      <div class="item-label">{{ $t('config.nftVerify')}}</div>
      <div class="item-value" v-if="edit.name != 'NftDefaultVerify' || edit.status != 'edit'">
        <div class="show-value">
          <el-tag type="info" v-if="config.NftDefaultVerify == '0'">{{$t('config.manual')}}</el-tag>
          <el-tag type="success" v-else-if="config.NftDefaultVerify == '1' ">{{$t('config.auto')}}</el-tag>
        </div>
        <el-link type="danger" @click="editConfig('NftDefaultVerify', config.NftDefaultVerify)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-radio-group v-model="edit.data">
            <el-radio :label="'0'">{{$t('config.manual')}}</el-radio>
            <el-radio :label="'1'">{{ $t('config.auto') }}</el-radio>
          </el-radio-group>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>


    <div class="config-item">
      <div class="item-label">{{ $t('config.loginMessage')}}</div>
      <div class="item-value" v-if="edit.name != 'LoginMessage' || edit.status != 'edit'">
        <div class="show-value">{{ config.LoginMessage }}</div>
        <el-link type="danger" @click="editConfig('LoginMessage', config.LoginMessage)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input type="textarea" :rows="2" v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>

    <div class="config-item">
      <div class="item-label">{{ $t('config.ethRPC')}}</div>
      <div class="item-value" v-if="edit.name != 'ChainApiUrl' || edit.status != 'edit'">
        <div class="show-value">{{ config.ChainApiUrl }}</div>
        <el-link type="danger" @click="editConfig('ChainApiUrl', config.ChainApiUrl)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>
    </div>

    <div class="config-item">
      <div class="item-label">{{ $t('config.networkId')}}</div>
      <div class="item-value"  v-if="edit.name != 'networkId' || edit.status != 'edit'">
        <div class="show-value">{{ config.networkId }}</div>
        <el-link type="danger" @click="editConfig('networkId', config.networkId)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>

    <div class="config-item">
      <div class="item-label">{{ $t('config.LastBlock')}}</div>
      <div class="item-value"  v-if="edit.name != 'LastBlock' || edit.status != 'edit'">
        <div class="show-value">{{ config.LastBlock }}</div>
        <el-link type="danger" @click="editConfig('LastBlock', config.LastBlock)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>



  </div>
</template>
<script>
export default {
  name: 'Website',
  props: {
    config: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      edit: {
        name: null,
        status: null,
        data: "",
      },
    }
  },
  methods: {
    editConfig(name, data){
      this.edit = {
        name: name,
        status: "edit",
        data: data
      };
    },
    cancelConfig(){
      this.edit = { name: "", status: "", data: "" }
    },
    updateConfig(){
      this.$confirm(this.$t('global.updateTip'), this.$t('global.tip'), {
        confirmButtonText: this.$t('global.confirm'),
        cancelButtonText: this.$t('global.cancel'),
        type: "warning",
      })
      .then(() => {
        var data = {
          key: this.edit.name,
          value: this.edit.data,
        };
        this.$api("config.update", data)
          .then((res) => {
            if(this.$tool.checkResponse(res)){
              this.$notify.success({
                title: this.$t('global.success'),
                message: this.$t('global.updateSuccess'),
              });
              this.cancelConfig();
              this.$emit('init');
            }else{
              this.$notify.error({
                title: this.$t('global.fail'),
                message: res.errmsg,
              });
            }
          })
          .catch((res) => {
            this.$notify.error({
              title: this.$t('global.fail'),
              message: this.$t('response.' + res.data.errno),
            });
          });
      });
    },
  }
}
</script>


