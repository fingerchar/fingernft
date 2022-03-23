// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "../token/NFT721.sol";
import "../lib/contracts/HasHorse.sol";
import "@openzeppelin/contracts-upgradeable/proxy/utils/Initializable.sol";
import "@openzeppelin/contracts-upgradeable/access/OwnableUpgradeable.sol";
import "@chainlink/contracts/src/v0.8/interfaces/AggregatorV3Interface.sol";

contract SalesContract is Initializable, OwnableUpgradeable{

    enum TokenType {Horse, TrainingCenter}
    NFT721 private nft721;
    HasHorse private hasHorseContract;

    // address public token0;
    // address public token1;

    // uint256 public token0Price;
    // uint256 public token1Price;

    event NewHorse(uint horseId);

    //token id => address 
    mapping (uint256 => address) public horseToOwner;
    //address => token id
    mapping (address => uint256) public ownerToHorse;
    // token id => TokenType
    mapping (uint256 => uint256) public tokenIdToTokenType;

    AggregatorV3Interface internal priceFeed;

    /**
     * Network: BNB
     * Aggregator: BNB/USD
     * Address: 0x0567F2323251f0Aab15c8dFb1967E4e8A7D42aeE
     */

    function __SalesContract_init(address _nft721, address _hasHorseContract) internal initializer {
        __Ownable_init();
        nft721 = NFT721(_nft721);
        hasHorseContract = HasHorse(_hasHorseContract);
         priceFeed = AggregatorV3Interface(
            0x8993ED705cdf5e84D0a3B754b5Ee0e1783fcdF16 //current is kovan
        );
    }

    /**
     * Returns the latest BNB price from chainlink
     */
    function getLatestPrice() public view returns (int256) {
        (
            /*uint80 roundID*/,
            int256 priceData,
            /*uint startedAt*/,
            /*uint timeStamp*/,
            /*uint80 answeredInRound*/
        ) = 
            priceFeed.latestRoundData();
        return priceData;
    }

    function buyBlindBox(uint256 latestPrice, uint quantity, uint256[] memory tokenId, uint8 v, bytes32 r, bytes32 s, NFT721.Fee[] memory _fees, string memory tokenURI) external payable {
        require(msg.sender != address(0), "Mint to zero address");
        require(tokenId.length == quantity);
        require(msg.value >= latestPrice*quantity, "Insufficient payment");
        payable(owner()).transfer(address(this).balance);
        for (uint256 i; i < quantity; i++) {
            nft721.mint(tokenId[i], v, r, s, _fees, tokenURI);
            createHorse(tokenId[i]);
        }
    }


    // function withdrawToken(IERC20 _token) external onlyOwner {
    //     uint256 tokenBalance = _token.balanceOf(address(this));
    //     require(tokenBalance > 0, "Insufficient balance");
    //     _token.safeTransfer(msg.sender, tokenBalance);
    // }

    function withdraw() external onlyOwner {
        payable(owner()).transfer(address(this).balance);
    }

    // function getTokenBalance(address _tokenAddress)
    //     external
    //     view
    //     onlyOwner
    //     returns (uint256)
    // {
    //     IERC20 token = IERC20(_tokenAddress);
    //     return token.balanceOf(address(this));
    // }

    function getBalance() external view onlyOwner returns (uint256) {
        return address(this).balance;
    }


    //Get nft info. TokenType 0 = Horse, 1 = TrainingCenter
    function getInfo(uint256 _tokenId) external view returns (address, string memory, uint256, HasHorse.Horse memory){
        address _addressOwner = _ownerOf(_tokenId);
        string memory _tokenURI = nft721.tokenURI(_tokenId);
        uint256 _tokenType = tokenIdToTokenType[_tokenId];
        HasHorse.Horse memory _horse = hasHorseContract.getHorse(_tokenId);
        return (_addressOwner, _tokenURI, _tokenType, _horse);
    }

    function createHorse(uint256 tokenId) internal {
        horseToOwner[tokenId] = msg.sender;
        tokenIdToTokenType[tokenId] = uint(TokenType.Horse); //0
        emit NewHorse(tokenId);
    }

    function setHorseBreedCount(uint256 tokenId,  uint16 _breedCount) external onlyOwner{
        hasHorseContract.setBreedCount(tokenId, _breedCount);
    }

    //NFT721 function callers
    function _balanceOf(address owner) public view virtual returns (uint256){
        return nft721.balanceOf(owner);
    }

    function _ownerOf(uint256 tokenId) public view virtual returns (address){
        address owner = nft721.ownerOf(tokenId);
        return owner;
    }
}
