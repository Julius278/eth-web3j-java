require("@nomiclabs/hardhat-ethers");
require("@nomicfoundation/hardhat-chai-matchers");
require("solidity-coverage");
require("hardhat-contract-sizer");

module.exports = {
  defaultNetwork: "hardhat",
  networks: {
    hardhat: {
      chainId: 31337,
      allowUnlimitedContractSize: true,
      hardfork: "london", // Ensure EVM compatibility
      accounts: {
        mnemonic: "test test test test test test test test test test test junk",
        path: "m/44'/60'/0'/0",
        initialIndex: 0,
        count: 20,
      },
    },
  },
  solidity: {
    version: "0.8.15",
    settings: {
      optimizer: {
        enabled: true,
        runs: 200,
      },
      evmVersion: "london", // Explicitly set EVM version
    },
  },
  paths: {
    sources: "./src/main/resources/contracts",
    tests: "./src/main/resources/test",
    cache: "./src/main/resources/cache",
    artifacts: "./src/main/resources/artifacts",
  },
  mocha: {
    timeout: 3000000,
  },
};




