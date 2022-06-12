<template>
  <div class="config-group">

    <div class="tx-order-info">
      <div class="bfont">{{ $t('config.syncInfo') }}</div>
      <div>{{ $t('config.earlyBlockNumber') }} : <span class="cblue">{{ txOrderInfo.earlyBlockNumber }}</span> </div>
      <div>{{ $t('config.lastBlockNumber') }}: <span class="cblue"> {{ txOrderInfo.lastBlockNumber }}</span> </div>
      <div>{{ $t('config.txAmount') }}: <span class="cblue">{{ txOrderInfo.txAmount }}</span> </div>
    </div>


		<config-item :label="$t('config.lastBlock')" dataKey="lastBlock" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.lastBlock"></el-input>
			</template>
		</config-item>
		<config-item :label="$t('config.blockConfirmation')" dataKey="blockConfirmation" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.blockConfirmation"></el-input>
			</template>
		</config-item>
		<config-item :label="$t('config.maxBlockOneTime')" dataKey="maxBlockOneTime" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.maxBlockOneTime"></el-input>
			</template>
		</config-item>

  </div>
</template>
 
<script>
  import { mapState } from "vuex";
  import ConfigItem from './ConfigItem.vue'
  export default {
    name: 'SyncBlock',
    components: {
      ConfigItem
    },
    computed: {
      ...mapState({
        config (state) {
          return state.app.config || {}
        }
      })
    },
    created(){
      this.getTxOrderInfo();
    },
    data () {
      return {
        editKey: '',
        txOrderInfo: {
          lastBlockNumber: 0,
          earlyBlockNumber: 0,
          txAmount: 0
        },
      }
    },
    methods: {
      getTxOrderInfo(){
        this.$api("txorder.info").then(res => {
          if(this.$tool.checkResponse(res)){
            var info = res.data;
            this.txOrderInfo = {
              lastBlockNumber: info.lastBlockNumber ? info.lastBlockNumber : 0,
              earlyBlockNumber: info.earlyBlockNumber ? info.earlyBlockNumber : 0,
              txAmount: info.txAmount ? info.txAmount : 0,
            }
          }
        });
      },
    },
  }

</script>

<style lang="scss" scoped>
.tx-order-info{
  padding: 10px;
  background: #f0f0f0;
  margin-bottom: 20px;
  border-radius: 5px;
  line-height: 22px;
  color: #666;
}
</style>
