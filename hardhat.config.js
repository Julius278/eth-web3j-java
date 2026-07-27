require("@nomiclabs/hardhat-ethers");
require("@nomicfoundation/hardhat-chai-matchers");
require("solidity-coverage");
require("hardhat-contract-sizer");

module.exports = {
  defaultNetwork: "hardhat",
  networks: {
    hardhat: {
      // Allow the network to handle accounts properly
      chainId: 31337,
      allowUnlimitedContractSize: true,
      // Initialize accounts with large balances
      accounts: {
        mnemonic: "test test test test test test test test test test test junk",
        path: "m/44'/60'/0'/0",
        initialIndex: 0,
        count: 20,
      },
      // Custom genesis state to fund the keyfile account
      genesisAccounts: {
        "0x9f162b41fa8e44f885bedae410418145a4e8ed06": {
          balance: "1000000000000000000000" // 1000 ETH in wei
        }
      }
    },
  },
  solidity: {
    compilers: [
      {
        version: "0.8.15",
        settings: {
          optimizer: {
            enabled: true,
            runs: 200,
          },
        },
      },
    ],
  },
  paths: {
    sources: "./src/main/resources/contracts",
    tests: "./src/main/resources/test",
    cache: "./src/main/resources/cache",
    artifacts: "./src/main/resources/artifacts",
  },
  mocha: {
    // Maybe necessary when testing on-chain.
    timeout: 3000000,
  },
};



