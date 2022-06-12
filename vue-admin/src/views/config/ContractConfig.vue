<template>
  <div class="app-container config-container">
    <el-tabs v-model="tab">
      <el-tab-pane v-for="(item, i) in tabs" :key="i" :label="item.label" :name="item.name">
        <network v-if="item.name === 'network'" @toggleTab="tab = $event" />
        <contract v-else-if="item.name == 'contract'" @toggleTab="tab = $event"></contract>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>
<script>
  import Contract from './Contract.vue';
  import Network from './Network.vue'
  export default {
    components: {
      Network,
      Contract
    },
    computed: {
      isPreset () {
        if (!this.config.configNetwork) {
          return false
        }
        const nw = JSON.parse(this.config.configNetwork)
        return nw.name && nw.symbol && nw.rpc && nw.explorer
      },
    },
    data () {
      return {
        tab: "network",
        tabs: [
          { name: "network", label: this.$t('config.network') },
          { name: 'contract', label: this.$t('config.contract') },
        ],
        config: null,
      }
    }
  }
</script>
<style lang="scss" scoped>
</style>


