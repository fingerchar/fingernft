<template>
  <div class="config-group">

    <div class="config-item">
      <div class="item-label">{{ $t('config.ipfsUrl')}}</div>
      <div class="item-value" v-if="edit.name != 'ipfsUrl' || edit.status != 'edit' ">
        <div class="show-value">{{ config.ipfsUrl }}</div>
        <el-link type="danger" @click="editConfig('ipfsUrl', config.ipfsUrl)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.IpfsServerIp')}}</div>
      <div class="item-value" v-if="edit.name != 'IpfsServerIp' || edit.status != 'edit' ">
        <div class="show-value">{{ config.IpfsServerIp}}</div>
        <el-link type="danger" @click="editConfig('IpfsServerIp', config.IpfsServerIp)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.IpfsServerPort')}}</div>
      <div class="item-value" v-if="edit.name != 'IpfsServerPort' || edit.status != 'edit' ">
        <div class="show-value">{{ config.IpfsServerPort}}</div>
        <el-link type="danger" @click="editConfig('IpfsServerPort', config.IpfsServerPort)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.StorageLocalPath')}}</div>
      <div class="item-value" v-if="edit.name != 'StorageLocalPath' || edit.status != 'edit' ">
        <div class="show-value">{{ config.StorageLocalPath}}</div>
        <el-link type="danger" @click="editConfig('StorageLocalPath', config.StorageLocalPath)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.IpfsRemoteServer')}}</div>
      <div class="item-value" v-if="edit.name != 'IpfsRemoteServer' || edit.status != 'edit' ">
        <div class="show-value">{{ config.IpfsRemoteServer}}</div>
        <el-link type="danger" @click="editConfig('IpfsRemoteServer', config.IpfsRemoteServer)">{{ $t('global.edit') }}</el-link>
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
  name: 'IPFS',
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

