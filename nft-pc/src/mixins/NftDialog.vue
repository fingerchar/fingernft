<script>
export default {
  data(){
    return {
      showSaleDialog: false,
      showCancelSaleDialog: false,
      showBuyDialog: false,

      showBidDialog: false,
      showCancelBidDialog :false,
      showAcceptDialog: false,

      showTransferDialog: false,
      showBurnDialog: false,

      showSaleTypeDialog: false,
      dialogNft: {},
      dialogOrder: {},
      dialogMyAsset: {},
      dialogToken: {},
      dialogNftURI: {},
    }
  },
  computed: {
    isLogin(){
      return this.$store.state.isLogin;
    },
    connected(){
      return this.$store.state.connected;
    },
  },
  methods: {
    showDialog(type, nft, order ){
      if(!this.$tools.needLogin(this.$route.path)) return;
      switch(type){
        case "sale":
          this.showSaleDialog = true,
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "edit_sale":
          this.showSaleDialog = true;
          this.dialogNft = nft;
          this.dialogOrder = order;
          break;
        case "cancel_sale":
          this.showCancelSaleDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "buy":
          this.showBuyDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "bid":
          this.showBidDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "edit_bid":
          this.showBidDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "cancel_bid":
          this.showCancelBidDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "accept":
          this.showAcceptDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "transfer":
          this.showTransferDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
        case "burn":
          this.showBurnDialog = true;
          this.dialogOrder = order;
          this.dialogNft = nft;
          break;
      }
    },
    closeDialog(){
      this.showSaleDialog = false;
      this.showCancelSaleDialog = false;
      this.showBuyDialog = false;

      this.showBidDialog = false;
      this.showCancelBidDialog = false;
      this.showAcceptDialog = false;

      this.showTransferDialog = false;
      this.showBurnDialog = false;
    },
    dialogConfirm(){
      this.closeDialog();
      this.reloadList();
    },
    dialogSelect(selectVal) {
      this.showSaleTypeDialog = false;
      this.$nextTick(() => {
        this.showDialog("sale", this.dialogNft, this.dialogOrder);
      });
    },
  }
}
</script>
