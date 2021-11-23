// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;

import "./libs/ERC20/ERC20.sol";
import "./libs/ERC20/extensions/ERC20Burnable.sol";
import "./libs/access/Ownable.sol";

contract FingerToken is ERC20, ERC20Burnable, Ownable {
    string private constant _name = "Finger Token";
    string private constant _symbol = "FINGER";

    constructor () ERC20(_name, _symbol) {
      _mint(msg.sender, 10 ** 27);
    }

}

