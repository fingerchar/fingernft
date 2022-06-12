
<script>
  export default {
    data () {
      return {
        nftList: [],
      };
    },
    methods: {
      queryFunction (nfts) {
        this._queryFunction(nfts, this.nftList);
      },
      _queryFunction (nfts, nftList) {
        this.queryLike(nfts, nftList);
        this.getOwners(nfts, nftList);
        this.queryMedia(nfts, nftList);
        this.queryBids(nfts, nftList);
        this.querySells(nfts, nftList);
      },
      onLike (data, like) {
        this._onLike(data, like, this.nftList);
      },
      _onLike (data, like, nftList) {
        for (let i in nftList) {
          if (data.address == nftList[i].address && data.tokenId == nftList[i].tokenId) {
            nftList[i].like = like;
            break;
          }
        }
      },
      getOwners (nfts, nftList) {
        let nftMap = new Map();
        for (let i = 0;i < nfts.length;i++) {
          for (let j = 0;j < nfts[i].items.length;j++) {
            nfts[i].items[j].payToken = {
              address: nfts[i].items[j].paytokenAddress,
              symbol: nfts[i].items[j].paytokenSymbol,
              decimals: nfts[i].items[j].paytokenDecimals,
              name: nfts[i].items[j].paytokenName
            }
          }
          let key = nfts[i].address + nfts[i].tokenId;
          nftMap.set(key, nfts[i]);
        }
        for (let i = 0;i < nftList.length;i++) {
          let key = nftList[i].address + nftList[i].tokenId;
          let _nft = nftMap.get(key);
          if (!_nft) continue;
          nftList[i] = _nft;
        }
      },
      //请求
      async queryMedia (nfts, nftList) {
        let _nfts = [];
        for (let i = 0;i < nfts.length;i++) {
          let item = nfts[i];
          if (!item.metadataContent) {
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
              this.setNftMedia(res.data, nftList);
            }
          });
        }
      },
      async queryLike (nfts, nftList) {
        if (!this.$store.state.connected) return;
        let _nfts = this.$tools.serializeNfts(nfts);
        let data = {
          nfts: _nfts,
          address: this.$store.state.user.coinbase,
        };
        this.$api("like.listuserlike", data).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.setNftLikes(res.data, nftList);
          }
        });
      },
      async queryBids (nfts, nftList) {
        if (!nfts.length) return;
        let _nfts = this.$tools.serializeNfts(nfts);
        _nfts = { info: _nfts };
        this.$api("nft.activebids", _nfts).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.setNftBids(res.data, nftList);
          }
        });
      },
      async querySells (nfts, nftList) {
        if (!nfts.length) return;
        let _nfts = this.$tools.serializeNfts(nfts);
        _nfts = { info: _nfts };
        this.$api("nft.activesales", _nfts).then((res) => {
          if (this.$tools.checkResponse(res)) {
            this.setNftSales(res.data, nftList);
          }
        });
      },
      //序列化
      setNftMedia (nfts, nftList) {
        let nftMap = {}
        for (let i = 0;i < nfts.length;i++) {
          let key = nfts[i].address + ":" + nfts[i].tokenId;
          nftMap[key] = nfts[i];
        }
        for (let i in nftList) {
          let key = nftList[i].address + ":" + nftList[i].tokenId;
          let nft = nftMap[key];
          if (!nft) continue;
          nftList[i].media = nft;
        }
      },
      setNftBids (nfts, nftList) {
        var nftMap = new Map();
        for (let i = 0;i < nfts.length;i++) {
          let bid = nfts[i]
          bid.payToken = {
            address: bid.paytokenAddress,
            symbol: bid.paytokenSymbol,
            decimals: bid.paytokenDecimals,
            name: bid.paytokenName
          }
          bid = {
            ...bid,
            address: bid.owner,
            type: this.$sdk.valueCommonType("BID"),
            expired: false,
            status: 0,
          };
          let key = nfts[i].buyerToken + ":" + nfts[i].buyerTokenId;
          let nft = nftMap.get(key)
          let map = new Map([[bid.owner, bid]]);
          if (nft) {
            let newMap = new Map([...nft, ...map])
            nftMap.set(key, newMap)
          } else {
            nftMap.set(key, map)
          }
        }
        for (let i = 0;i < nftList.length;i++) {
          var key = nftList[i].address + ":" + nftList[i].tokenId;
          var bids = nftMap.get(key);
          if (!bids) continue;
          nftList[i].bids = bids;
        }
      },
      setNftSales (sales, nftList) {
        var saleDict = {};
        for (let i = 0;i < sales.length;i++) {
          let sale = sales[i];
          sale.payToken = {
            address: sale.paytokenAddress,
            symbol: sale.paytokenSymbol,
            decimals: sale.paytokenDecimals,
            name: sale.paytokenName
          }
          sale = {
            ...sale,
            address: sale.owner,
            expired: false,
            status: 0,
          };
          let key = sales[i].sellToken + ":" + sales[i].sellTokenId;
          saleDict[key] = sale
        }

        for (let i = 0;i < nftList.length;i++) {
          var key = nftList[i].address + ":" + nftList[i].tokenId;
          var sale = saleDict[key];
          if (!sale) continue;
          nftList[i].sale = sale;
        }

      },
      setNftLikes (likes, nftList) {
        let nftMap = {}
        for (let i in likes) {
          nftMap[likes[i]] = true;
        }
        for (var i = 0;i < nftList.length;i++) {
          let key = nftList[i].address + ":" + nftList[i].tokenId;
          nftList[i].like = nftMap[key] || false;
        }
      },
    },
  };
</script>
