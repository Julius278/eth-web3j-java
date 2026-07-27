/**
 * Setup script to fund the keyfile account with ETH
 * This connects to the running Hardhat node and funds the account
 */

const { ethers } = require("ethers");

async function main() {
  // The address from keyfile.json
  const targetAddress = "0x9f162b41fa8e44f885bedae410418145a4e8ed06";

  console.log("=== Hardhat Account Funding Script ===");
  console.log("Target address:", targetAddress);

  // Connect to the Hardhat node (localhost:8545)
  const provider = new ethers.providers.JsonRpcProvider("http://localhost:8545");

  try {
    // Get the current network
    const network = await provider.getNetwork();
    console.log("✓ Connected to network, chainId:", network.chainId);

    // Get the first default account from Hardhat (has plenty of ETH)
    // Use private key of first hardhat account from mnemonic
    const mnemonic = "test test test test test test test test test test test junk";
    const hdWallet = ethers.utils.HDNode.fromMnemonic(mnemonic);
    const firstAccountKey = hdWallet.derivePath("m/44'/60'/0'/0/0").privateKey;
    const signer = new ethers.Wallet(firstAccountKey, provider);

    console.log("✓ Using source account:", signer.address);

    // Get signer balance
    const signerBalance = await provider.getBalance(signer.address);
    console.log("✓ Source account balance:", ethers.utils.formatEther(signerBalance), "ETH");

    // Check current balance of target
    const currentBalance = await provider.getBalance(targetAddress);
    console.log("✓ Target account current balance:", ethers.utils.formatEther(currentBalance), "ETH");

    if (currentBalance.gt(ethers.utils.parseEther("100"))) {
      console.log("✓ Account already has sufficient funds! No transfer needed.");
      return;
    }

    // Send 1000 ETH to the target address
    console.log("\n→ Sending 1000 ETH to target account...");
    const tx = await signer.sendTransaction({
      to: targetAddress,
      value: ethers.utils.parseEther("1000"),
      gasPrice: ethers.utils.parseUnits("1", "gwei"),
      gasLimit: 21000,
    });

    console.log("✓ Transaction sent:", tx.hash);
    console.log("  Waiting for confirmation...");
    const receipt = await tx.wait();
    console.log("✓ Transaction confirmed at block:", receipt.blockNumber);

    // Verify final balance
    const finalBalance = await provider.getBalance(targetAddress);
    console.log("✓ Target account final balance:", ethers.utils.formatEther(finalBalance), "ETH");

    console.log("\n✓ Setup complete! Account is now funded.");
    process.exit(0);
  } catch (error) {
    console.error("\n✗ Setup failed:", error.message);
    if (error.response) {
      console.error("Response:", error.response);
    }
    process.exit(1);
  }
}

main();


