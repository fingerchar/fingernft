<template>
<div class="notify-item" :class="{big: size=='big'}">
  <div class="notify-inner">
    <div class="cover cpointer" @click="goDetail">
      <el-image fit="contain" class="nft-cover" :src="$filters.fullImageUrl(newGoods?newGoods.image:'')" v-if="info.target == 'NFT'"></el-image>
      <template v-else>
        <avatar shape="circular" :imageUrl="$filters.fullImageUrl(info.image)" :address="info.owner" :imgWidth="65" :imgHeight="65" v-if="size !='big'"></avatar>
        <avatar shape="circular" :imageUrl="$filters.fullImageUrl(info.image)" :address="info.owner" :imgWidth="80" :imgHeight="80" v-else></avatar>
      </template>
    </div>
    <div class="notify-content">
      <div class="title cpointer" @click="goDetail">
        <div class="bfont">{{info.title}}</div>
      </div>
      <div class="desc">
        <div class="c9">{{info.desc}}</div>
        <div class="cpointer op" @click="goAccount(info.op.address)">
          <avatar :imageUrl="$filters.fullImageUrl(info.op.image)" :address="info.op.address" :imgWidth="18" :imgHeight="18" shape="circular"></avatar>
          <span class="op-name">{{ info.op.name || $filters.ellipsisAddress(info.op.address) }}</span>
        </div>
      </div>
      <div class="time c9">{{ $filters.timeFormat(info.time) }}</div>
    </div>

  </div>
</div>
</template>
<script>
export default {
  name: "NotifyItem",
  props:{
    notify:{
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
    title(){
      return "";
    },
    desc(){
      return "";
    },
    info(){
      let info = this.parseNotify(this.notify);
      return info;
    },
    newGoods(){
      
      if(!this.notify.media)return '';
      return this.$tools.analysis(this.notify.media);
    }
  },
  created() {
    this.$nextTick(function(){
        this.loading = false;
      });
    // setTimeout(()=>{
    //   this.loading = false;
    // },3000)
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
        var ids = this.info.address + ":" + this.info.tokenId;
        this.$router.push("/detail/" + ids);
        if(this.size == "small"){
          this.$emit("close");
        }
      }else{
        this.goAccount(this.info.owner);
      }
    },
    goAccount(address){
      this.$router.push({ path: "/account/" + address });
      if(this.size == "small"){
        this.$emit("close");
      }
    },
    read(){
      this.$api("notice.read", {id: this.notify.id});
    },
    parseNotify(notify){
      switch(this.$tools.getNotifyType(notify.type)){
        case "FOLLOW":
          return this.followNotify(notify)
          break;
        case "LIKE":
          return this.likeNotify(notify);
          break;
        case "TRADE":
          return this.tradeNotify(notify);
          break;
      }
    },
    followNotify(notify){
      return {
        target: 'USER',
        image: notify.operatorImg,
        owner: notify.operator,
        title: notify.operatorName || this.$filters.ellipsisAddress(notify.operator),
        time: notify.createTime,
        op: {
          address: notify.operator,
          name: notify.operatorName,
          image: notify.operatorImg
        },
        desc: "followed by",
      }
    },
    likeNotify(notify){
      return {
        target: "NFT",
        image: notify.image,
        title: notify.name,
        address: notify.content.address,
        tokenId: notify.content.tokenId,
        op: {
          address: notify.operator,
          name: notify.operatorName,
          image: notify.operatorImg,
        },
        time: notify.createTime,
        desc: "liked by",
      }
    },
    tradeNotify(notify){
      let info = {
        target: "NFT",
        image: notify.image,
        title: notify.name,
        address: notify.content.token,
        tokenId: notify.content.tokenId,
        op: {
          address: notify.operator,
          name: notify.operatorName,
          image: notify.operatorImg,
        },
        time: notify.createTime,
      }
      switch(this.$tools.getNotifySubType(notify.subType)){
        case "CANCEL_SALE":
          info.desc = "cancel sale by"
          break;
        case "BUY":
          info.desc = "buy by"
          break;
        case "BID":
          info.desc = "bid by"
          break;
        case "EDIT_BID":
          info.desc = "edit bid by"
          break;
        case "CANCEL_BID":
          info.desc = "cancel bid by"
          break;
        case "ACCEPT_BID":
          info.desc = "accept by"
          break;
        case "MINT":
          info.desc = "mint by"
          break;
        case "LIKED":
          info.desc = "liked by"
          break;
        case "BURN":
          info.desc = "burn by"
          break;
        case "TRANSFER":
          info.desc = "transfer by"
          break;
        case "RECEIVE":
          info.desc = "receive by"
          break;
        case "BOUGHT":
          info.desc = "bought by"
          break;
        case "BIDDEN":
          info.desc = "biding by"
          break;
      }
      return info;
    }
  },
}
</script>

<style lang="scss" scoped>
.notify-item{
  background: #fff;
  display: flex;
  flex: 1;
  margin-bottom: 10px;
  border-radius: $borderRadius;
  &.big{
    margin-bottom: 20px;
    .notify-inner{
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
  .notify-inner{
    display: flex;
    padding: 10px;
  }
  .notify-content{
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

