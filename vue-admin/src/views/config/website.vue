<template>
	<div class="config-group">
		<config-item :label="$t('config.site')" dataKey="website" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.website"></el-input>
			</template>
		</config-item>

		<config-item :label="$t('config.cdnUrl')" dataKey="cdnUrl" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.cdnUrl"></el-input>
			</template>
		</config-item>

		<config-item :label="$t('config.nftVerify')" dataKey="nftDefaultVerify" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
        <el-switch v-model="config.nftDefaultVerify"></el-switch>
			</template>
		</config-item>

		<config-item :label="$t('config.staticLocalPath')" dataKey="staticLocalPath" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.staticLocalPath" />
			</template>
		</config-item>


		<config-item :label="$t('config.loginMessage')" dataKey="loginMessage" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input v-model="config.loginMessage"></el-input>
			</template>
		</config-item>

		<config-item :label="$t('config.sellerFee')" dataKey="sellerFee" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input-number v-model="config.sellerFee" :min="0" :max="5000" controls-position="right" style="width: 100%;" />
			</template>
		</config-item>

		<config-item :label="$t('config.buyerFee')" dataKey="buyerFee" :editKey="editKey" @setEditKey="editKey = $event" @clearEditKey="editKey = ''">
			<template #default="{ config }">
				<el-input-number v-model="config.buyerFee" :min="0" :max="5000" controls-position="right" style="width: 100%;" />
			</template>
		</config-item>
	</div>
</template>
<script>
	import { mapState } from 'vuex'
	import ConfigItem from './ConfigItem.vue'
	export default {
		name: 'Website',
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
		data () {
			return {
				editKey: '',
			}
		}
	}
</script>


