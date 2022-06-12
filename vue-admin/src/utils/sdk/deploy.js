import utils from "./utils";

export default async (abiName, args) => {
  return await utils.deploy(abiName, args);
};
