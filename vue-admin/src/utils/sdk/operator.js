import utils from "./utils.js";

export default {
  addOperator: async (operator, address) => {
    try {
      const _contract = await utils.contractAt("OwnableOperatorRole", address);
      const account = utils.getAccount();
      return await _contract.addOperator(operator, {
        from: account,
      });
    } catch (e) {
      return { error: e.message };
    }
  },
  isOperator: async (operator, address) => {
    try {
      const _contract = await utils.contractAt("OwnableOperatorRole", address);
      return await _contract.isOperator(operator);
    } catch (e) {
      return {
        error: e,
      };
    }
  },
};
