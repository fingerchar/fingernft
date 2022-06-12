<template>
<div class="share-title">{{$t("items.share")}}</div>
<div class="popover-share">
  <span @click="popup('twitter')" class="share-item">
    <img src="@/assets/img/share/twitter.png" />
    <p>{{$t("items.twitter")}}</p>
  </span>
  <span @click="popup('facebook')" class="share-item">
    <img src="@/assets/img/share/facebook.png" />
    <p>{{$t("items.facebook")}}</p>
  </span>
  <!-- <span @click="popup('telegram')" class="share-item"></span> -->
  <span @click="send()" class="share-item">
    <img src="@/assets/img/share/email.png" />
    <p>{{$t("items.email")}}</p>
  </span>
</div>

</template>

<script>
export default{
  name: "Share",
  data(){
    return {
    }
  },
  methods: {
    popup(type) {
      let url;
      var share = this.$store.state.share;
      var site = this.$store.state.config.website;
      var site_name = this.$store.state.name;
      switch(type){
        case "twitter":
          url = "https://twitter.com/intent/tweet?url=" +
            encodeURIComponent(document.location.href) +
            "&text=" +
            encodeURIComponent(document.title) +
            "&hashtags=" + site_name +
            "%2Cethereum%2Cdigitalasset%2Cnft&via=" + site;
          break;
        case "telegram":
          url =
            "https://telegram.me/share/?url=" +
            encodeURIComponent(document.location.href) +
            "&title=" +
            encodeURIComponent(document.title);
          break;
        case "facebook":
          url =
            "https://www.facebook.com/sharer.php?u=" +
            encodeURIComponent(document.location.href) +
            "&quote=nft-market";
          break;
      }
      window.open(
        url,
        "newWindow",
        "toolbar=no,menubar=no,directories=no,status=no,width=600,height=450,scrollbars=yes,resizable=no"
      );
    },
    send(){
      var site_name = this.$store.state.name;
      var share = this.$store.state.share;
      location = "mailto:?cc=" + share.email.url + "&subject=" + site_name + "&body=email content";
    }
  }
}
</script>

<style lang="scss" scoped>
.share-title {
  margin: 5px 0 10px;
  text-align: center;
  color: #000;
}
.popover-share {
  display: flex;
  .share-item {
    flex: 1;
    display: flex;
    font-size: 12px;
    justify-content: center;
    cursor: pointer;
    border-radius: $borderRadius;
    flex-wrap: wrap;
    img { 
      width: 30px;
      height: auto;
    }
    p{
      margin: 5px 0;
      color: #888;
      width: 100%;
      text-align: center;
    }
    &:hover,
    &:active {
      background: #f0f0f0;
    }
  }
}
</style>

