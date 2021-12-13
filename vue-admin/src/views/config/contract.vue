<template>
  <div class="config-group">

    <div class="config-item">
      <div class="item-label">{{ $t('config.sellerFee')}}</div>
      <div class="item-value" v-if="edit.name != 'sellerFee' || edit.status != 'edit' ">
        <div class="show-value">{{ config.sellerFee }}</div>
        <el-link type="danger" @click="editConfig('sellerFee', config.sellerFee)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input-number v-model="edit.data" :min="0" :max="5000"></el-input-number>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>


    <div class="config-item">
      <div class="item-label">{{ $t('config.buyerFee')}}</div>
      <div class="item-value" v-if="edit.name != 'buyerFee' || edit.status != 'edit' ">
        <div class="show-value">{{ config.buyerFee}}</div>
        <el-link type="danger" @click="editConfig('buyerFee', config.buyerFee)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input-number v-model="edit.data" :min="0" :max="5000"></el-input-number>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>

    <div class="config-item">
      <div class="item-label">{{ $t('config.NFT721')}}</div>
      <div class="item-value" v-if="edit.name != 'NFT721' || edit.status != 'edit' ">
        <div class="show-value">{{ config.NFT721}}</div>
        <el-link type="danger" @click="editConfig('NFT721', config.NFT721)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.TransferProxy')}}</div>
      <div class="item-value" v-if="edit.name != 'TransferProxy' || edit.status != 'edit' ">
        <div class="show-value">{{ config.TransferProxy }}</div>
        <el-link type="danger" @click="editConfig('TransferProxy', config.TransferProxy)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.TransferProxyForDeprecated')}}</div>
      <div class="item-value" v-if="edit.name != 'TransferProxyForDeprecated' || edit.status != 'edit' ">
        <div class="show-value">{{ config.TransferProxyForDeprecated}}</div>
        <el-link type="danger" @click="editConfig('TransferProxyForDeprecated', config.TransferProxyForDeprecated)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.ERC20TransferProxy')}}</div>
      <div class="item-value" v-if="edit.name != 'ERC20TransferProxy' || edit.status != 'edit' ">
        <div class="show-value">{{ config.ERC20TransferProxy}}</div>
        <el-link type="danger" @click="editConfig('ERC20TransferProxy', config.ERC20TransferProxy)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.NftExchange')}}</div>
      <div class="item-value" v-if="edit.name != 'NftExchange' || edit.status != 'edit' ">
        <div class="show-value">{{ config.NftExchange}}</div>
        <el-link type="danger" @click="editConfig('NftExchange', config.NftExchange)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.MintKey')}}</div>
      <div class="item-value" v-if="edit.name != 'MintKey' || edit.status != 'edit' ">
        <div class="show-value">{{ config.MintKey}}</div>
        <el-link type="danger" @click="editConfig('MintKey', config.MintKey)">{{ $t('global.edit') }}</el-link>
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
      <div class="item-label">{{ $t('config.TransKey')}}</div>
      <div class="item-value" v-if="edit.name != 'TransKey' || edit.status != 'edit' ">
        <div class="show-value">{{ config.TransKey}}</div>
        <el-link type="danger" @click="editConfig('TransKey', config.TransKey)">{{ $t('global.edit') }}</el-link>
      </div>
      <div class="item-value" v-else>
        <div class="show-value input-value">
          <el-input type="textarea" :rows="2" v-model="edit.data"></el-input>
        </div>
        <el-link class="mr-10" type="primary" @click="updateConfig">{{ $t('global.update') }}</el-link>
        <el-link type="info" @click="cancelConfig">{{ $t('global.cancel') }}</el-link>
      </div>

    </div>

  </div>
</template>
<script>
export default {
  name: 'Contract',
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

