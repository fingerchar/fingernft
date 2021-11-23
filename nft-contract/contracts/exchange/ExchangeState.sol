// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "./ExchangeDomain.sol";
import "./OwnableOperatorRole.sol";


contract ExchangeState is OwnableOperatorRole {

    // keccak256(OrderKey) => completed
    mapping(bytes32 => uint256) public completed;

    function getCompleted(ExchangeDomain.OrderKey calldata key) view external returns (uint256) {
        return completed[getCompletedKey(key)];
    }

    function setCompleted(ExchangeDomain.OrderKey calldata key, uint256 newCompleted) external onlyOperator {
        completed[getCompletedKey(key)] = newCompleted;
    }

    function getCompletedKey(ExchangeDomain.OrderKey memory key) pure public returns (bytes32) {
        return keccak256(abi.encodePacked(key.owner, key.sellAsset.token, key.sellAsset.tokenId, key.buyAsset.token, key.buyAsset.tokenId, key.salt));
    }
}
