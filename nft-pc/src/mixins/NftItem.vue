
<script>
export default {
  data() {
    return {
      nftList: [],
      hotList: [],
    };
  },
  methods: {
    queryFunction(nfts) {
      this.getOwners(nfts);
      this.queryLike(nfts);
      this.queryMedia(nfts);
      this.queryBids(nfts);
    },
    onLike(index, like) {
      this.nftList[index].like = like;
      this.hotList[index].like = like;
    },
    getOwners(nfts) {
      for (let i = 0; i < this.nftList.length; i++) {
        
        let nft = this.nftList[i];
        for (let j = 0; j < nfts.length; j++) {
          let _nft = nfts[j];
          if (
            nft.nft.address != _nft.nft.address ||
            nft.nft.tokenId != _nft.nft.tokenId
          )
            continue;

          var owners = [];
          for (let z = 0; z < _nft.itemList.length; z++) {
            let owner = _nft.itemList[z].item;
            let user = _nft.itemList[z].user;
            if (owner.onsell) {
              owner.payToken = this.$store.getters.payToken(
                owner.payTokenAddress
              );
            }
            let sellQuantity = owner.sellValue - owner.completed;
            sellQuantity =
              sellQuantity > owner.quantity ? owner.quantity : sellQuantity;
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
    //请求
    queryMedia(nfts) {
      let _nfts = [];
      for (let i = 0; i < nfts.length; i++) {
        let item = nfts[i];
        if (!item.nft.metadataContent) {
          _nfts.push(item);
        }
      }
      _nfts = this.$tools.serializeNfts(_nfts);
      let data = {
        info: _nfts,
      };
      if (data.info.length) {
        this.$api("nft.getmedia", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.setNftMedia(res.data);
          }
        });
      }
    },
    queryLike(nfts) {
      if (!this.$store.state.connected) return;
      let _nfts = this.$tools.serializeNfts(nfts);
      let data = {
        nfts: _nfts,
        address: this.$store.state.user.coinbase,
      };
      this.$api("like.listuserlike", data).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.setNftLikes(res.data);
        }
      });
    },
    queryBids(nfts) {
      if (!nfts.length) return;
      let _nfts = this.$tools.serializeNfts(nfts);
      _nfts = { info: _nfts };
      this.$api("nft.activebids", _nfts).then((res) => {
        if (this.$tools.checkResponse(res)) {
          this.setNftBids(res.data);
        }
      });
    },
    //序列化
    setNftMedia(nfts) {
      for (let i = 0; i < this.nftList.length; i++) {
        let nft = this.nftList[i];
        for (let key in nfts) {
          let item = key.split(":");
          if (nft.nft.address == item[0] && nft.nft.tokenId == item[1]) {
            this.nftList[i].media = nfts[key];
          }
        }
      }
      this.nftList = JSON.parse(JSON.stringify(this.nftList));
    },
    setNftBids(nfts) {
      for (var i = 0; i < this.nftList.length; i++) {
        let nft = this.nftList[i];
        for (var l = 0; l < nfts.length; l++) {
          let _nft = nfts[l];
          if (
            nft.nft.address != _nft.address ||
            nft.nft.tokenId != _nft.tokenId
          )
            continue;
          let bids = [];
          for (var z = 0; z < _nft.bids.length; z++) {
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
          this.nftList[i].bids = bids;
          break;
        }
      }
      this.nftList = JSON.parse(JSON.stringify(this.nftList));
    },
    setNftLikes(likes) {
      for (var i = 0; i < this.nftList.length; i++) {
        let nft = this.nftList[i];
        for (var l = 0; l < likes.length; l++) {
          let like = likes[l];
          like = like.split(":");
          if (nft.nft.address == like[0] && nft.nft.tokenId == like[1]) {
            this.nftList[i].like = true;
          }
        }
      }
      this.nftList = JSON.parse(JSON.stringify(this.nftList));
    },
  },
};
</script>
