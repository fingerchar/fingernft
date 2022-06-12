import utils from "./utils";
import { ethers } from "ethers";

export default async (params, abiName) => {
  try {
    const contract = await utils.getContract("Multicall");
    if (contract.error) return contract;
    const abi = require(`./abi/${abiName}.json`);
    const itf = new ethers.utils.Interface(abi.abi);
    const calldata = params.map((param) => [
      param.address.toLowerCase(),
      itf.encodeFunctionData(param.name, param.params),
    ]);
    let { returnData } = await contract.aggregate(calldata);
    const res = returnData.map((param, i) => {
      return itf.decodeFunctionResult(params[i].name, param);
    });
    return res;
  } catch (e) {
    return { error: e.message };
  }
};
