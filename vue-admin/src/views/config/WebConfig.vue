<template>
	<div class="app-container config-container">
		<el-tabs v-model="tab">
			<el-tab-pane v-for="(item, i) in tabs" :key="i" :label="item.label" :name="item.name">
				<website v-if="item.name == 'website'"></website>
				<ipfs v-else-if="item.name == 'ipfs'"></ipfs>
				<sync-block v-else-if="item.name == 'syncBlock'"></sync-block>
			</el-tab-pane>
		</el-tabs>
	</div>

</template>
<script>
	import Website from './Website';
	import Ipfs from './Ipfs';
	import SyncBlock from './SyncBlock';

	export default {
		components: {
			Website,
			Ipfs,
			SyncBlock,
		},
		name: 'Network',
		data () {
			return {
				tab: "website",
				tabs: [
					{ name: "website", label: this.$t('config.website') },
					{ name: 'ipfs', label: this.$t('config.ipfs') },
					{ name: "syncBlock", label: this.$t('config.syncBlock') },
				],
			}
		}
	}
</script>
<style lang="scss" scoped>
</style>


