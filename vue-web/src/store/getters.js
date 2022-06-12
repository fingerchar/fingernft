import sdk from '@/util/sdk/index.js';
export default {
  payToken: (state) => (address) => {
    if(!address) return;
    for(var i = 0; i < state.payTokens.length; i++){
      let payToken = state.payTokens[i];
      if(payToken.address.toLocaleLowerCase() == address.toLocaleLowerCase()){
        return payToken;
      }
    }
  },
  payTokenBySymbol: (state) => (symbol) => {
    for(var i = 0; i < state.payTokens.length; i++){
      let payToken = state.payTokens[i];
      if(payToken.symbol == symbol){
        return payToken;
      }
    }
  },
  defaultSalePayToken: (state) => () => {
    if(!state.payTokens.length) return;
    let paytoken = state.defalutPayToken;
    if(paytoken) return paytoken;
    let paytokens = state.payTokens;
    let _paytokens = paytokens.filter(function(token){
      return !token.erc20;
    });
    if(_paytokens.length) return _paytokens[0];
    return paytokens[0];
  },
  defaultBidPayToken: (state) => () => {
    let paytoken = state.defalutPayToken;
    if(paytoken && paytoken.erc20) return paytoken;
    let paytokens = state.payTokens;
    let _paytokens = paytokens.filter(function(token){
      return token.erc20;
    });
    return _paytokens[0];
  },
  getBalance: (state) => (address) => {
    if(address == sdk.NULL_ADDRESS()) return state.ethBalance;
    return state.erc20Balance[address]
  },
  category: (state) => (categoryId) => {
    for(var i = 0; i < state.categorys.length; i++){
      let category = state.categorys[i];
      if(category.id == categoryId) return category;
    }
  },
}
