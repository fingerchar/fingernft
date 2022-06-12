function addressToNumber(address) {
  return parseInt(address.slice(2, 10), 16);
}
export default addressToNumber;
