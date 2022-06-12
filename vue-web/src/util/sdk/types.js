
const OrderType = {
  "SALE": 1,
  "BID": 2
}

const OrderKeys = Object.getOwnPropertyNames(OrderType);
const OrderValues = Object.keys(OrderType).map(function (e) { return OrderType[e] });

function keyOrderType(value){
  for(var i = 0; i < OrderValues.length; i++){
    if(OrderValues[i] == value) return OrderKeys[i];
  }
}


function valueOrderType(key){
  return OrderType[key];
}


const CommonType = {
  "SALE": 1,
  "EDIT_SALE": 2,
  "CANCEL_SALE": 3,
  "BUY": 4,
  "BID": 5,
  "EDIT_BID": 6,
  "CANCEL_BID": 7,
  "ACCEPT": 8,
  "MINT": 9,
}



const CommonKeys = Object.getOwnPropertyNames(CommonType);
const CommonValues = Object.keys(CommonType).map(function (e) { return CommonType[e] });

function keyCommonType(value){
  for(var i = 0; i < CommonValues.length; i++){
    if(CommonValues[i] == value) return CommonKeys[i];
  }
}

function valueCommonType(key){
  return CommonType[key];
}


const AssetType = {
  "ETH": 0,
  "ERC20": 1,
  "ERC721": 3,
  "ERC721Deprecated": 4,
}
const AssetKeys = Object.getOwnPropertyNames(AssetType);
const AssetValues = Object.keys(AssetType).map(function (e) { return AssetType[e] });

function keyAssetType(value){
  for(var i = 0; i < AssetValues.length; i++){
    if(AssetValues[i] == value) return AssetKeys[i];
  }
}

function valueAssetType(key){
  return AssetType[key];
}


export default {
  keyOrderType,
  valueOrderType,
  keyCommonType,
  valueCommonType,
  keyAssetType,
  valueAssetType,
}
