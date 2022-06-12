<script>
  export default {
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
    },
    methods: {
      getActiveAddressBid (bids, address) {
        let bid = bids.get(address)
        if (bid) {
          return bid
        } else {
          return;
        }
      },
      getHighestBid (bids, itemOwner) {
        let highestBid;
        let _this = this;
        bids.forEach(function (bid, key) {
          if (bid.owner == itemOwner) return;
          if (
            bid.expired ||
            bid.type == _this.$sdk.valueCommonType("CANCEL_BID") ||
            bid.type == _this.$sdk.valueCommonType("ACCEPT")
          )
            return;
          bid.singlePrice = _this.$tools.singlePrice(
            bid.sellValue,
            bid.buyerValue,
            bid.payToken
          );
          if (!highestBid) {
            highestBid = bid;
          }
        }, bids)
        return highestBid;
      }
    },
  };
</script>
