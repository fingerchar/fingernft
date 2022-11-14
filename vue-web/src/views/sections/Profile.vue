<template>
	<div class="main-wrapper">
		<div class="profile-wrapper">
			<div class="top">
				<div class="go-back" @click="goBack">
					<span class="iconfont icon-leftarrow"></span><span class="text">{{$t('profile.goBack')}}</span>
				</div>

				<div class="top-Row1">{{$t('profile.editProfile')}}</div>
				<div class="top-Row2">
					{{$t('profile.text1')}}
				</div>
			</div>
			<div class="bottom">
				<div class="flex1">
					<div class="chooseFile-Title">{{$t('profile.chooseFile')}}</div>
					<div class="chooseFile-Content">
						<div class="chooseFile-Text">{{$t('profile.text2')}}</div>
						<div class="chooseFile-Content-RightSection">
							<avatar :imageUrl="$filters.fullImageUrl(imgSrc)" :address="user.coinbase" :imgWidth="88" :imgHeight="88"></avatar>
							<div class="file-box">
								<input type="file" class="file-btn" @change="imageChange"
                  :accept="$tools.imageType()"
                />
								<span>{{$t('profile.chooseFile')}}</span>
							</div>
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

					<el-button type="primary" @click="updateImage" class="lastButton">
						{{$t('profile.updateProfile')}}</el-button>
				</div>
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
		created () {
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
			imageChange (e) {
				let file = new FileReader();
				this.imageFile = e.target.files[0];
				file.readAsDataURL(e.target.files[0]);
				file.onload = () => {
					this.imgSrc = file.result;
				};
			},
			goBack () {
				this.$router.go(-1);
			},
			async updateImage () {
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
			UpdateProfile () {
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
	.profile-wrapper {
		display: flex;
		flex-direction: column;
		width: 680px;
		margin: 0 auto;
		padding-bottom: 100px;
	}
	.top {
		margin-top: 20px;
	}
	.go-back {
		cursor: pointer;
		font-size: 16px;
		font-weight: 400;
		line-height: 48px;
	}
	.top-Row1 {
		font-size: 32px;
		font-weight: bold;
		line-height: 66px;
	}
	.top-Row2 {
		font-size: 16px;
		font-weight: 400;
		color: #999;
		margin-top: 9px;
	}
	.bottom {
		display: flex;
		margin-bottom: 32px;
	}
	.unit {
		margin-bottom: 24px;
	}
	.font1 {
		margin-bottom: 10px;
		font-size: 16px;
		font-weight: 400;
	}
	.lastButton {
		margin-top: 39px;
		margin-left: 50%;
		transform: translateX(-50%);
		width: 250px;
		height: 54px;
		font-size: 16px;
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
	.chooseFile-Title {
		margin-top: 24px;
		font-size: 16px;
		font-weight: 400;
		line-height: 24px;
	}
	.chooseFile-Text {
		margin-top: 12px;
		font-size: 14px;
		font-weight: 400;
		color: #999;
	}
	.chooseFile-Content {
		margin-bottom: 17px;
		display: flex;
		justify-content: space-between;
	}
	.Verification {
		font-size: 16px;
		font-weight: 400;
		line-height: 24px;
	}
	.Verification-Content {
		margin-top: 8px;
		display: flex;
		justify-content: space-between;
	}
	.Verification-Text {
		font-size: 14px;
		font-weight: 400;
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
		font-weight: 400;
		display: flex;
		justify-content: center;
		align-items: center;
		white-space: nowrap;
		min-height: auto;
	}
	.file-box {
		margin-top: 10px;
		background: $primaryColor;
		border-radius: 8px;
		padding: 9px 20px;
		font-size: 12px;
		font-weight: 400;
		color: #fff;
		cursor: pointer;
		position: relative;
		.file-btn {
			position: absolute;
			z-index: 1;
			left: 0;
			right: 0;
			top: 0;
			bottom: 0;
			opacity: 0;
			cursor: pointer;
		}
	}

	.chooseFile-Content-RightSection {
		display: flex;
		flex-direction: column;
		align-items: center;
	}
</style>
<style lang="scss">
	.unit input {
		margin-top: 8px !important;
		height: 40px !important;
		background: #eeeeee !important;
		border-radius: 5px !important;

		font-size: 14px !important;
		font-weight: 400 !important;
		color: #000000 !important;
		padding-left: 6px !important;
	}
	.unit input::placeholder {
		font-size: 8px !important;
		font-weight: 400 !important;
		color: #000000 !important;
		line-height: 14px !important;
		opacity: 0.4 !important;
	}
</style>
