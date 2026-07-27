/**
 * Extract ABIs from Hardhat artifacts and prepare them for web3j
 */

const fs = require("fs");
const path = require("path");

function extractAbis() {
  const artifactsDir = "src/main/resources/artifacts/contracts";
  const abiOutDir = "src/main/resources/abi";

  // Create ABI directory if it doesn't exist
  if (!fs.existsSync(abiOutDir)) {
    fs.mkdirSync(abiOutDir, { recursive: true });
    console.log(`✓ Created directory: ${abiOutDir}`);
  }

  // Recursively find all artifact JSON files
  function walkDir(dir) {
    let results = [];

    try {
      const files = fs.readdirSync(dir);

      for (const file of files) {
        const filePath = path.join(dir, file);
        const stat = fs.statSync(filePath);

        if (stat.isDirectory()) {
          results = results.concat(walkDir(filePath));
        } else if (file.endsWith(".json") && !file.endsWith(".dbg.json")) {
          results.push(filePath);
        }
      }
    } catch (error) {
      // Directory doesn't exist, skip
    }

    return results;
  }

  const artifactFiles = walkDir(artifactsDir);

  if (artifactFiles.length === 0) {
    console.log("⚠ No artifacts found. Make sure to run: npx hardhat compile");
    return;
  }

  console.log(`Found ${artifactFiles.length} artifact(s)`);

  for (const artifactPath of artifactFiles) {
    try {
      const content = fs.readFileSync(artifactPath, "utf8");
      const artifact = JSON.parse(content);

      if (!artifact.abi) {
        console.log(`⚠ No ABI found in ${path.basename(artifactPath)}`);
        continue;
      }

      const contractName = path.basename(artifactPath, ".json");
      const abiPath = path.join(abiOutDir, `${contractName}.json`);

      fs.writeFileSync(abiPath, JSON.stringify(artifact.abi, null, 2));
      console.log(`  ✓ Extracted ABI: ${contractName}`);
    } catch (error) {
      console.error(`✗ Error processing ${artifactPath}:`, error.message);
    }
  }

  console.log(`\n✓ All ABIs extracted to ${abiOutDir}`);
}

try {
  extractAbis();
  process.exit(0);
} catch (error) {
  console.error("✗ Failed to extract ABIs:", error);
  process.exit(1);
}

