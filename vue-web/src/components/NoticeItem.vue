<template>
<div class="notice-item" :class="{big: size=='big'}">
  <div class="notice-inner">
    <div class="cover cpointer" @click="goDetail">
      <el-image fit="contain" class="nft-cover" 
          :src="$filters.fullImageUrl(metadataContent.image)"
          v-if="info.target == 'NFT'">
      </el-image>
      <avatar fit="contain" class="nft-cover" 
          :imageUrl="$filters.fullImageUrl(notice.content.contract.cover)"
          :address="notice.content.contract.address"
          v-if="info.target == 'CONTRACT'"
          :imgWidth="80" :imgHeight="80"
          >
      </avatar>
      <template v-else-if="info.target == 'USER'">
        <avatar shape="circular" :imageUrl="$filters.fullImageUrl(notice.content.user.avatar)" :address="notice.content.user.owner" :imgWidth="65" :imgHeight="65" v-if="size !='big'"></avatar>
        <avatar shape="circular" :imageUrl="$filters.fullImageUrl(notice.content.user.avatar)" :address="notice.content.user.owner" :imgWidth="80" :imgHeight="80" v-else></avatar>
      </template>
    </div>
    <div class="notice-content">
      <div class="title cpointer" @click="goDetail">
        <div class="bfont">
          {{info.title}}
          <el-tag size="small" v-if="info.tag">{{info.tag}}</el-tag>
        </div>
      </div>
      <div class="desc">
        <div class="c9">{{info.desc}}</div>
        <div class="cpointer op" @click="goAccount(notice.operator.address)">
          <avatar
              :imageUrl="$filters.fullImageUrl(notice.operator.avatar)"
              :address="notice.operator.address" :imgWidth="18" :imgHeight="18" shape="circular"></avatar>
          <span class="op-name">{{ notice.operator.nickname || $filters.ellipsisAddress(notice.operator.address) }}</span>
        </div>
      </div>
      <div class="time c9">{{ $filters.timeFormat(notice.createTime) }}
        <span v-if="notice.content.txHash" @click="toTx" class="tx-link iconfont icon-link"></span>
      </div>
    </div>

  </div>
</div>
</template>
<script>
export default {
  name: "NotifyItem",
  props:{
    notice:{
      type: Object,
      default: {},
    },
    size: {
      type: String,
      default: "",
    },
  },
  data(){
    return {
      loading:true,
    };
  },
  computed:{
    info(){
      var subType = this.$tools.getNotifySubType(this.notice.subType);
      var desc = null;
      var info = {};
      switch(subType){
        case "LIKED":
          desc = "liked by";
        case "BID":
          desc = desc ? desc : "bid by";
        case "BUY":
          desc = desc ? desc : "buy by";
        case "TRANSFER":
          desc = desc ? desc : "transfer by";
        case "RECEIVE":
          desc = desc ? desc : "receive by";
        case "ACCEPT_BID":
          desc = desc ? desc : "accept bid by";
        case "CANCEL_BID":
          desc = desc ? desc : "cancel bid by";
        case "CANCEL_SALE":
          desc = desc ? desc : "cancel sale by";
          info = {
            title: this.metadataContent.name,
            target: "NFT",
            desc: desc,
          }
          break;
        case "FOLLOWED":
          info = {
            title: this.notice.operator.nickname ?
                this.notice.operator.nickname : 
                this.$filters.ellipsisAddress(this.notice.operator.address),
            target: "USER",
            desc: "follow by",
          }
          break;
      }
      return info;
    },
    metadataContent(){
      if(!this.notice.content.nft) return {};
      var metadataContent = this.notice.content.nft.metadataContent;
      if(!metadataContent) return {};
      return JSON.parse(metadataContent);
    },
    newNft(){
      if(!this.notice.media)return '';
      return this.$tools.analysis(this.notice.media);
    },
  },
  created() {
    this.$nextTick(function(){
      this.loading = false;
    });
  },
  methods: {
    thumbnail(url){
      if(url){
        if(url.endsWith('.gif')) return url;
        return url + '!300x300';
      }
      else{
        return ''
      }
    },
    goDetail(){
      this.read();
      if(this.info.target == "NFT"){
        var ids = this.notice.content.nft.address + ":" + this.notice.content.nft.tokenId;
        this.$router.push("/detail/" + ids);
        if(this.size == "small"){
          this.$emit("close");
        }
      }else if(this.info.target == "CONTRACT"){
        this.$router.push("/collection/" + this.notice.content.contract.address);
      }else if(this.info.target == "USER"){
        this.goAccount(this.info.operator.address);
      }
    },
    goAccount(address){
      this.$router.push({ path: "/account/" + address });
      if(this.size == "small"){
        this.$emit("close");
      }
    },
    read(){
      this.$api("notice.read", {id: this.notice.id});
    },
    toTx(){
      var explorer = this.$store.state.config.network.explorer;
      if(!explorer) return;
      var url = explorer + "/tx/" + this.notice.content.txHash;
      window.open(url);
    }
  },
}
</script>

<style lang="scss" scoped>
.notice-item{
  background: #fff;
  display: flex;
  flex: 1;
  margin-bottom: 10px;
  border-radius: $borderRadius;
  &.big{
    margin-bottom: 20px;
    .notice-inner{
      padding: 20px;
    }
    .nft-cover{
      width: 80px;
      height: 80px;
    }
    .title{
      font-size: 18px;
    }
  }
  .notice-inner{
    display: flex;
    padding: 10px;
  }
  .notice-content{
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-left: 10px;
    justify-content: center;
  }
  .nft-cover{
    width: 65px;
    height: 65px;
    border-radius: $borderRadius;
  }
  .title{
    padding-bottom: 5px;
  }
  .time{
    font-size: 12px;
  }
  .desc{
    display: flex;
    flex-direction: row;
    align-items: center;
    padding-bottom: 5px;
    .op{
      display: flex;
      align-items: center;
    }
    .c9{
      padding-right: 5px;
    }
    .op-name{
      padding-left: 5px;
    }
  }
  .tx-link{
    font-weight: bold;
    font-size: 18px;
    color: #666;
    cursor: pointer;
  }

}
 .skeleton-image{
   width:80px;
   height:80px;
 }
 .skeleton-list{
   display: flex;
   flex-direction: column;
   justify-content: center;
   margin-left:10px;
   height:80px;
  .skeleton-list1{
   width:80px;
   height:22px;
 }
 .skeleton-list2{
   width:200px;
   height:23px;
   padding:5px 0;
 }
 .skeleton-list3{
   width:150px;
   height:22px;
 }
 }
</style>

