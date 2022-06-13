<template>
	<div class="config-group">
		<config-item :label="$t('config.ipfsUrl')" dataKey="ipfsUrl" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.ipfsUrl" />
			</template>
		</config-item>

		<config-item :label="$t('config.ipfsServerIp')" dataKey="ipfsServerIp" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.ipfsServerIp" />
			</template>
		</config-item>

		<config-item :label="$t('config.ipfsServerPort')" dataKey="ipfsServerPort" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.ipfsServerPort" />
			</template>
		</config-item>
		<config-item :label="$t('config.ipfsRemoteServer')" dataKey="ipfsRemoteServer" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.ipfsRemoteServer" />
			</template>
		</config-item>
	</div>
</template>
<script>
	import ConfigItem from './ConfigItem.vue'
	export default {
		name: 'IPFS',
		components: {
			ConfigItem
		},
		data () {
			return {
				editKey: ''
			}
		}
	}
</script>

