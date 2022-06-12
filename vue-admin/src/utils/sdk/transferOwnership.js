import utils from "./utils.js";

export default {
  async transferOwnership(address, newOwner) {
    try {
      const _contract = await utils.contractAt("Ownable", address);
      const account = utils.getAccount();
      return await _contract.transferOwnership(newOwner, {
        from: account,
      });
    } catch (e) {
      return { error: e.message };
    }
  },
  async getOwner(address) {
    try {
      const _contract = await utils.contractAt("Ownable", address);
      return await _contract.owner();
    } catch (e) {
      return {
        error: e,
      };
    }
  },
};
