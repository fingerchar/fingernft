// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

/**
 * @title ERC1155 token mint interface
 * @dev Interface for any contract that wants to support safeTransfers
 * from ERC1155 asset contracts.
 */
interface IERC1155Mintable {
  function safeMint(address to, uint256 tokenId, uint256 supply, string memory tokenURI) external;
}

