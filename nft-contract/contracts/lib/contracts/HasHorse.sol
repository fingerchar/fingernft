// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
pragma experimental ABIEncoderV2;

import "@openzeppelin/contracts-upgradeable/proxy/utils/Initializable.sol";

contract HasHorse is Initializable {
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

    //token id => horse
    mapping(uint256 => Horse) public horse;

    function __HasHorse_init() internal initializer {}

    function getHorse(uint256 tokenId) public view returns (Horse memory) {
        return horse[tokenId];
    }

    function setName(uint256 tokenId, string memory _name) external {
        horse[tokenId].name = _name;
    }

    function setLevel(uint256 tokenId, uint16 _level) external {
        horse[tokenId].level = _level;
    }

    function setAge(uint256 tokenId, uint16 _age) external {
        horse[tokenId].age = _age;
    }

    function setGender(uint256 tokenId, uint16 _gender) external {
        horse[tokenId].gender = _gender;
    }

    function setRarity(uint256 tokenId, uint16 _rarity) external {
        horse[tokenId].rarity = _rarity;
    }

    function setMotivation(uint256 tokenId, uint16 _motivation) external {
        horse[tokenId].motivation = _motivation;
    }

    function setSpeed(uint256 tokenId, uint16 _speed) external {
        horse[tokenId].speed = _speed;
    }

    function setStamina(uint256 tokenId, uint16 _stamina) external {
        horse[tokenId].stamina = _stamina;
    }

    function setTenacity(uint256 tokenId, uint16 _tenacity) external {
        horse[tokenId].tenacity = _tenacity;
    }

    function setGrassFieldBonus(uint256 tokenId, uint16 _grassFieldBonus)
        external
    {
        horse[tokenId].grassFieldBonus = _grassFieldBonus;
    }

    function setMuddyFieldBonus(uint256 tokenId, uint16 _muddyFieldBonus)
        external
    {
        horse[tokenId].muddyFieldBonus = _muddyFieldBonus;
    }

    function setEquipmentId(uint256 tokenId, uint16 _equipmentId) external {
        horse[tokenId].equipmentId = _equipmentId;
    }

    function setWinCount(uint256 tokenId, uint16 _winCount) external {
        horse[tokenId].winCount = _winCount;
    }

    function setLossCount(uint256 tokenId, uint16 _lossCount) external {
        horse[tokenId].lossCount = _lossCount;
    }

    function setGeneration(uint256 tokenId, uint16 _generation) external {
        horse[tokenId].generation = _generation;
    }

    function setBreedCount(uint256 tokenId, uint16 _breedCount) external {
        horse[tokenId].breedCount = _breedCount;
    }

    function setBredLimit(uint256 tokenId, uint16 _bredLimit) external {
        horse[tokenId].bredLimit = _bredLimit;
    }

    function setBreedingMateId(uint256 tokenId, uint32 _breedingMateId)
        external
    {
        horse[tokenId].breedingMateId = _breedingMateId;
    }

    function setSireTokenId(uint256 tokenId, uint32 _sireTokenId) external {
        horse[tokenId].sireTokenId = _sireTokenId;
    }

    function setMatronTokenId(uint256 tokenId, uint32 _matronTokenId) external {
        horse[tokenId].matronTokenId = _matronTokenId;
    }

    function setRaceId(uint256 tokenId, uint32 _raceId) external {
        horse[tokenId].raceId = _raceId;
    }

    function setRaceCooldownEndTime(
        uint256 tokenId,
        uint64 _raceCooldownEndTime
    ) external {
        horse[tokenId].raceCooldownEndTime = _raceCooldownEndTime;
    }

    function setBreedCooldownEndTime(
        uint256 tokenId,
        uint64 _breedCooldownEndTime
    ) external {
        horse[tokenId].breedCooldownEndTime = _breedCooldownEndTime;
    }

    function setFeedingCooldownEndTime(
        uint256 tokenId,
        uint64 _feedingCooldownEndTime
    ) external {
        horse[tokenId].feedingCooldownEndTime = _feedingCooldownEndTime;
    }

    function setTrainingCooldownEndTime(
        uint256 tokenId,
        uint64 _trainingCooldownEndTime
    ) external {
        horse[tokenId].trainingCooldownEndTime = _trainingCooldownEndTime;
    }

    function setBirthTime(uint256 tokenId, uint64 _birthTime) external {
        horse[tokenId].birthTime = _birthTime;
    }
}
