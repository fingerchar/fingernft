<script>
export default {
  computed: {
    connected() {
      return this.$store.state.connected;
    },
    user() {
      return this.$store.state.user;
    },
  },
  methods: {
    analysisAsset(asset){
      asset.address = asset.address.toLocaleLowerCase();
      if(!asset.onsell) return asset;
      asset.usdtPrice = this.$tools.decimal( asset.payToken ? asset.price * asset.payToken.rate : "0");
      return asset;
    },
    getActiveAddressBid(bids, address) {
      for (var i = 0; i < bids.length; i++) {
        let bid = bids[i];
        bid.address = bid.address.toLocaleLowerCase();
        if (
          bid.expired ||
          bid.status != 0 ||
          bid.type == this.$sdk.valueCommonType("CANCEL_BID") ||
          bid.type == this.$sdk.valueCommonType("ACCEPT")
        )
          continue;
        if (address.toLocaleLowerCase() == bid.address.toLocaleLowerCase())
          return bid;
      }
    },
    getHighestBid(bids) {
      let highestBid;
      for (var i = 0; i < bids.length; i++) {
        let bid = bids[i];
        bid.address = bid.address.toLocaleLowerCase();
        if (
          bid.expired ||
          bid.status != 0 ||
          bid.type == this.$sdk.valueCommonType("CANCEL_BID") ||
          bid.type == this.$sdk.valueCommonType("ACCEPT")
        )
          continue;
        bid.singlePrice = this.$tools.singlePrice(
          bid.sellValue,
          bid.buyerValue
        );
        bid.singleUsdtPrice = this.$tools.decimal(
          bid.payToken ? bid.singlePrice * bid.payToken.rate : "0"
        );
        if (
          !highestBid ||
          parseFloat(highestBid.singleUsdtPrice) <
            parseFloat(bid.singleUsdtPrice)
        ) {
          highestBid = bid;
        }
      }
      return highestBid;
    },
  },
};
</script>
