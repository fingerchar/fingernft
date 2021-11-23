
<script>
export default {
  data(){
    return {
      tab: "",
      nftList: {},
    }
  },
  methods: {
    queryFunction(nfts,tab){
      this.getOwners(nfts, tab);
      this.queryLike(nfts, tab);
      this.queryMedia(nfts, tab);
      this.queryBids(nfts, tab);
    },
    getOwners(nfts, tab){
      for(let i = 0; i < this.nftList[tab].length; i++){
        let nft = this.nftList[tab][i];
        for(let j = 0; j < nfts.length; j++){
          let _nft = nfts[j];
          if(nft.nft.address != _nft.nft.address || nft.nft.tokenId != _nft.nft.tokenId) continue;
          var owners = [];
          for(let z = 0; z < _nft.itemList.length; z++){
            let owner = _nft.itemList[z].item;
            let user = _nft.itemList[z].user;
            if(owner.onsell){
              owner.payToken = this.$store.getters.payToken(
                owner.payTokenAddress,
              );
            }
            let sellQuantity = owner.sellValue - owner.completed;
            sellQuantity = sellQuantity > owner.quantity ? owner.quantity : sellQuantity;

            owner.sellQuantity = sellQuantity;
            owners.push({
              ...owner,
              completed: owner.completed,
              address: user.address,
              avatar: user.avatar,
              nickname: user.nickname,
            });
          }
          nft.owners = owners;
          break;
        }
      }
    },
    onLike(index, like) {
      this.nftList[this.tab][index].like = like;
    },
    //请求
    queryLike(nfts, tab) {
      if (!this.$store.state.connected) return;
      let _nfts = this.$tools.serializeNfts(nfts);
      let data={
        nfts:_nfts,
        address:this.$store.state.user.coinbase
      }
      this.$api("like.listuserlike", data).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.setNftLikes(res.data, tab);
        }
      });
    },
    queryBids(nfts, tab){
      if(!nfts.length) return;
      let _nfts = this.$tools.serializeNfts(nfts);
      _nfts = { info: _nfts }
      this.$api("nft.activebids", _nfts).then((res) => {
        if(this.$tools.checkResponse(res)){
          this.setNftBids(res.data, tab);
        }
      });
    },
    queryMedia(nfts, tab){
      let _nfts = [];
      for(let i = 0;i < nfts.length; i++){
        let item = nfts[i];
        if(!item.nft.metadataContent){
          _nfts.push(item)
        }
      }
      _nfts = this.$tools.serializeNfts(_nfts);
      let data={
        info:_nfts,
      }
      if(data.info.length){
        this.$api('nft.getmedia',data).then((res)=>{
          if(this.$tools.checkResponse(res)){
           this.setNftMedia(res.data, tab);
          }
        })
      }
    },
    //序列化
    setNftLikes(likes, tab) {
      for (var i = 0; i < this.nftList[tab].length; i++) {
        let nft = this.nftList[tab][i];
        for (var l = 0; l < likes.length; l++) {
          let like = likes[l];
          like = like.split(":");
          if (nft.nft.address == like[0] && nft.nft.tokenId == like[1]) {
            this.nftList[tab][i].like = true;
          }
        }
      }
    },
    setNftBids(nfts, tab){
      for(var i = 0; i < this.nftList[tab].length; i++){
        let nft = this.nftList[tab][i];
        for(var l = 0; l < nfts.length; l++){
          let _nft = nfts[l];
          if(nft.nft.address != _nft.address || nft.nft.tokenId != _nft.tokenId) continue;
          let bids = [];
          for(var z = 0; z < _nft.bids.length; z++){
            let bid = _nft.bids[z];
            let bidQuantity = bid.buyerValue - bid.completed;
            bid.bidQuantity = bidQuantity;
            let payToken = this.$store.getters.payToken(bid.sellToken);
            bid.payToken = payToken;
            bids.push({
              ...bid,
              address: bid.owner,
              type: this.$sdk.valueCommonType("BID"),
              expired: false,
              status: 0,
            });
          }
          this.nftList[tab][i].bids = bids;
          break;
        }
      }
    },
    setNftMedia(nfts, tab){
      for(let i = 0; i < this.nftList.length; i++){
        let nft = this.nftList[i];
        for(let key in nfts){
          let item = key.split(":");
          if (nft.nft.address == item[0] && nft.nft.tokenId == item[1]) {
            this.nftList[tab][i].media = nfts[key];
            break;
          }
        }
      }
    },
  }
}
</script>
