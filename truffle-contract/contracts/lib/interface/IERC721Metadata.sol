// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "./IERC721.sol";

abstract contract IERC721Metadata is IERC721 {
    function name() external view virtual returns (string memory);
    function symbol() external view virtual returns (string memory);
    function tokenURI(uint256 tokenId) external view virtual returns (string memory);
}
