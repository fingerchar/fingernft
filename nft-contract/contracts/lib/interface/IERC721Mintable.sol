// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

/**
 * @title ERC721 token mint interface
 * @dev Interface for any contract that wants to support safeTransfers
 * from ERC721 asset contracts.
 */
interface IERC721Mintable {
  function mint(uint256 tokenId, string memory tokenURI) external;
  function safeMint(address to, uint256 tokenId, string memory tokenURI) external;
}
