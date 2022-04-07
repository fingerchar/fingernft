// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
pragma experimental ABIEncoderV2;

import "@openzeppelin/contracts-upgradeable/proxy/utils/Initializable.sol";
import "@openzeppelin/contracts-upgradeable/access/OwnableUpgradeable.sol";


contract HasHorse is Initializable, OwnableUpgradeable {
    event Birth(
        address indexed owner,
        uint256 horseId,
        uint256 matronId,
        uint256 sireId
    );

    struct Horse {
        //basics
        string name;
        uint16 level;
        uint16 age; //+1 everyday, 91 = retirement + enable breeding
        uint16 gender;
        uint16 rarity;
        //horse stats
        uint16 motivation; //starts from 100%, every 8hrs -10%. PVP recovers to 100%. (All stats)*motivation = final stats.
        uint16 stamina;
        uint16 speed;
        uint16 tenacity;
        uint16 grassFieldBonus;
        uint16 muddyFieldBonus;
        uint16 equipmentId;
        //winrates
        uint16 winCount;
        uint16 lossCount;
        //breeding
        uint16 generation;
        uint16 breedCount;
        uint16 bredLimit;
        uint32 breedingMateId;
        uint32 sireTokenId; //father token
        uint32 matronTokenId; //mother token
        //racing
        uint32 raceId;
        //cooldowns
        uint64 raceCooldownEndTime;
        uint64 breedCooldownEndTime;
        uint64 feedingCooldownEndTime;
        uint64 trainingCooldownEndTime;
        uint64 birthTime;
    }

    address salesContract;

    //token id => horse
    mapping(uint256 => Horse) public horse;

    function __HasHorse_init() internal initializer {}

    modifier onlySalesContract {
        require(msg.sender == salesContract);
        _;
    }

    function setSalesContractAddress(address _salesContract) external onlyOwner{
        salesContract = _salesContract;
    }

    function getHorse(uint256 tokenId) public view returns (Horse memory) {
        return horse[tokenId];
    }
    
    //do onlygamecontract 
    function setName(uint256 tokenId, string memory _name) external onlySalesContract{
        horse[tokenId].name = _name;
    }

    function setLevel(uint256 tokenId, uint16 _level) external onlySalesContract{
        horse[tokenId].level = _level;
    }

    function setAge(uint256 tokenId, uint16 _age) external onlySalesContract{
        horse[tokenId].age = _age;
    }

    function setGender(uint256 tokenId, uint16 _gender) external onlySalesContract{
        horse[tokenId].gender = _gender;
    }

    function setRarity(uint256 tokenId, uint16 _rarity) external onlySalesContract{
        horse[tokenId].rarity = _rarity;
    }

    function setMotivation(uint256 tokenId, uint16 _motivation) external onlySalesContract{
        horse[tokenId].motivation = _motivation;
    }

    function setSpeed(uint256 tokenId, uint16 _speed) external onlySalesContract{
        horse[tokenId].speed = _speed;
    }

    function setStamina(uint256 tokenId, uint16 _stamina) external onlySalesContract{
        horse[tokenId].stamina = _stamina;
    }

    function setTenacity(uint256 tokenId, uint16 _tenacity) external onlySalesContract{
        horse[tokenId].tenacity = _tenacity;
    }

    function setGrassFieldBonus(uint256 tokenId, uint16 _grassFieldBonus)
        external onlySalesContract
    {
        horse[tokenId].grassFieldBonus = _grassFieldBonus;
    }

    function setMuddyFieldBonus(uint256 tokenId, uint16 _muddyFieldBonus)
        external onlySalesContract
    {
        horse[tokenId].muddyFieldBonus = _muddyFieldBonus;
    }

    function setEquipmentId(uint256 tokenId, uint16 _equipmentId) external onlySalesContract{
        horse[tokenId].equipmentId = _equipmentId;
    }

    function setWinCount(uint256 tokenId, uint16 _winCount) external onlySalesContract{
        horse[tokenId].winCount = _winCount;
    }

    function setLossCount(uint256 tokenId, uint16 _lossCount) external onlySalesContract{
        horse[tokenId].lossCount = _lossCount;
    }

    function setGeneration(uint256 tokenId, uint16 _generation) external onlySalesContract{
        horse[tokenId].generation = _generation;
    }

    function setBreedCount(uint256 tokenId, uint16 _breedCount) external onlySalesContract{
        horse[tokenId].breedCount = _breedCount;
    }

    function setBredLimit(uint256 tokenId, uint16 _bredLimit) external onlySalesContract{
        horse[tokenId].bredLimit = _bredLimit;
    }

    function setBreedingMateId(uint256 tokenId, uint32 _breedingMateId)
        external onlySalesContract
    {
        horse[tokenId].breedingMateId = _breedingMateId;
    }

    function setSireTokenId(uint256 tokenId, uint32 _sireTokenId) external onlySalesContract{
        horse[tokenId].sireTokenId = _sireTokenId;
    }

    function setMatronTokenId(uint256 tokenId, uint32 _matronTokenId) external onlySalesContract{
        horse[tokenId].matronTokenId = _matronTokenId;
    }

    function setRaceId(uint256 tokenId, uint32 _raceId) external onlySalesContract{
        horse[tokenId].raceId = _raceId;
    }

    function setRaceCooldownEndTime(
        uint256 tokenId,
        uint64 _raceCooldownEndTime
    ) external onlySalesContract{
        horse[tokenId].raceCooldownEndTime = _raceCooldownEndTime;
    }

    function setBreedCooldownEndTime(
        uint256 tokenId,
        uint64 _breedCooldownEndTime
    ) external onlySalesContract{
        horse[tokenId].breedCooldownEndTime = _breedCooldownEndTime;
    }

    function setFeedingCooldownEndTime(
        uint256 tokenId,
        uint64 _feedingCooldownEndTime
    ) external onlySalesContract{
        horse[tokenId].feedingCooldownEndTime = _feedingCooldownEndTime;
    }

    function setTrainingCooldownEndTime(
        uint256 tokenId,
        uint64 _trainingCooldownEndTime
    ) external onlySalesContract{
        horse[tokenId].trainingCooldownEndTime = _trainingCooldownEndTime;
    }

    function setBirthTime(uint256 tokenId, uint64 _birthTime) external onlySalesContract{
        horse[tokenId].birthTime = _birthTime;
    }
}
