/**
 * Setup script to fund the keyfile account with ETH
 * Usage: npx hardhat run scripts/setup-hardhat.js
 */

const hre = require("hardhat");

async function main() {
  // The address from keyfile.json
  const targetAddress = "0x9f162b41fa8e44f885bedae410418145a4e8ed06";

  // Get one of the hardhat default signers (has plenty of ETH)
  const [signer] = await hre.ethers.getSigners();

  console.log("Funding address:", targetAddress);
  console.log("From signer:", signer.address);

  // Send 1000 ETH to the target address
  const tx = await signer.sendTransaction({
    to: targetAddress,
    value: hre.ethers.utils.parseEther("1000"),
  });

  console.log("Transaction sent:", tx.hash);
  await tx.wait();

  // Verify balance
  const balance = await hre.ethers.provider.getBalance(targetAddress);
  console.log("Account balance:", hre.ethers.utils.formatEther(balance), "ETH");

  console.log("✓ Setup complete!");
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });

