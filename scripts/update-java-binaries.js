/**
 * Update BINARY constants in generated web3j wrapper classes with current bytecodes
 */

const fs = require("fs");
const path = require("path");

function updateBinaries() {
  const artifactsDir = "src/main/resources/artifacts/contracts";
  const generatedSourcesDir = "target/generated-sources/contracts/com/julius/spring/boot/ethweb3";

  console.log("=== Updating Java Wrapper Bytecodes ===\n");

  // Ensure generated sources directory exists
  if (!fs.existsSync(generatedSourcesDir)) {
    console.log(`⚠ Generated sources directory not found: ${generatedSourcesDir}`);
    console.log("   Run 'mvn generate-resources' first");
    return;
  }

  function getContractBytecode(contractName) {
    const searchPath = path.join(artifactsDir, "**", `${contractName}.json`);

    function findArtifact(dir) {
      try {
        const files = fs.readdirSync(dir);
        for (const file of files) {
          const filePath = path.join(dir, file);
          const stat = fs.statSync(filePath);

          if (stat.isDirectory()) {
            const result = findArtifact(filePath);
            if (result) return result;
          } else if (file === `${contractName}.json`) {
            const content = fs.readFileSync(filePath, "utf8");
            const artifact = JSON.parse(content);
            return artifact.bytecode || null;
          }
        }
      } catch (error) {
        // Skip
      }
      return null;
    }

    return findArtifact(artifactsDir);
  }

  // Find all generated Java files
  try {
    const javaFiles = fs.readdirSync(generatedSourcesDir)
      .filter(f => f.endsWith(".java"));

    for (const javaFile of javaFiles) {
      const contractName = javaFile.replace(".java", "");
      const javaFilePath = path.join(generatedSourcesDir, javaFile);
      let javaContent = fs.readFileSync(javaFilePath, "utf8");

      const bytecode = getContractBytecode(contractName);

      if (!bytecode) {
        console.log(`⚠ ${contractName}: No bytecode found in artifacts`);
        continue;
      }

      // Update BINARY constant
      const oldBinaryPattern = /public static final String BINARY = "([^"]*)"/;
      const newBinaryLine = `public static final String BINARY = "${bytecode}"`;

      if (oldBinaryPattern.test(javaContent)) {
        javaContent = javaContent.replace(oldBinaryPattern, newBinaryLine);
        fs.writeFileSync(javaFilePath, javaContent);
        console.log(`✓ Updated ${contractName}: bytecode length ${bytecode.length / 2} bytes`);
      } else {
        console.log(`⚠ ${contractName}: BINARY constant not found`);
      }
    }

    console.log("\n✓ Java wrapper bytecodes updated");
  } catch (error) {
    console.error("✗ Error updating binaries:", error.message);
    process.exit(1);
  }
}

updateBinaries();

