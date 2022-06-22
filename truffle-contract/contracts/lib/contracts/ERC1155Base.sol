// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "../math/SafeMath.sol";
import "./HasSecondarySaleFees.sol";
import "./HasContractURI.sol";
import "./ERC1155MetadataURI.sol";
import "./ERC1155.sol";
import "../utils/Ownable.sol";

abstract contract ERC1155Base is HasSecondarySaleFees, Ownable, ERC1155MetadataURI, HasContractURI, ERC1155 {

    using SafeMath for uint256;

    struct Fee {
        address payable recipient;
        uint256 value;
    }

    // id => creator
    mapping (uint256 => address) public creators;

    constructor(string memory contractURI, string memory tokenURIPrefix) HasContractURI(contractURI) ERC1155MetadataURI(tokenURIPrefix) {

    }

    // Creates a new token type and assings _initialSupply to minter
    function _mint(address to, uint256 _id, uint256 _supply, string memory _uri) internal {
        require(creators[_id] == address(0x0), "Token is already minted");
        require(_supply != 0, "Supply  be positive");
        require(bytes(_uri).length > 0, "uri  be set");

        creators[_id] = to;
        balances[_id][to] = _supply;
        _setTokenURI(_id, _uri);

        // Transfer event with mint semantic
        emit TransferSingle(msg.sender, address(0x0), msg.sender, _id, _supply);
        emit URI(_uri, _id);
    }

    function burn(address _owner, uint256 _id, uint256 _value) external {

        require(_owner == msg.sender || operatorApproval[_owner][msg.sender] == true, "Need operator approval for 3rd party burns.");

        // SafeMath will throw with insuficient funds _owner
        // or if _id is not valid (balance will be 0)
        balances[_id][_owner] = balances[_id][_owner].sub(_value);

        // MUST emit event
        emit TransferSingle(msg.sender, _owner, address(0x0), _id, _value);
    }

    /**
     * @dev Internal function to set the token URI for a given token.
     * Reverts if the token ID does not exist.
     * @param tokenId uint256 ID of the token to set its URI
     * @param uri string URI to assign
     */
    function _setTokenURI(uint256 tokenId, string memory uri) internal override{
        require(creators[tokenId] != address(0x0), "_setTokenURI: Token  exist");
        super._setTokenURI(tokenId, uri);
    }

    function setTokenURIPrefix(string memory tokenURIPrefix) public onlyOwner {
        _setTokenURIPrefix(tokenURIPrefix);
    }

    function setContractURI(string memory contractURI) public onlyOwner {
        _setContractURI(contractURI);
    }
}
