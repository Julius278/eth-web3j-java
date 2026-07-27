/**
 * Debug script to verify contract bytecodes
 */

const fs = require("fs");
const path = require("path");

function checkBytecodes() {
  const artifactsDir = "src/main/resources/artifacts";

  console.log("=== Contract Bytecode Verification ===\n");

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
      // Skip
    }
    return results;
  }

  const artifactFiles = walkDir(artifactsDir).filter((artifactPath) => {
    const normalized = artifactPath.replace(/\\/g, "/");
    return !normalized.includes("/build-info/") && !normalized.endsWith(".dbg.json");
  });

  for (const artifactPath of artifactFiles) {
    try {
      const content = fs.readFileSync(artifactPath, "utf8");
      const artifact = JSON.parse(content);
      const contractName = path.basename(artifactPath, ".json");

      if (!artifact.bytecode) {
        console.log(`⚠ ${contractName}: No bytecode found`);
        continue;
      }

      const bytecode = artifact.bytecode;
      const bytecodeLength = bytecode.length / 2; // Each byte is 2 hex chars

      console.log(`✓ ${contractName}`);
      console.log(`  Bytecode length: ${bytecodeLength} bytes`);
      console.log(`  Has constructor: ${artifact.abi.some(f => f.type === "constructor") ? "yes" : "no"}`);

      // Check if bytecode looks valid
      if (!bytecode.startsWith("0x")) {
        console.log(`  ⚠ Bytecode doesn't start with 0x`);
      }
      if (bytecode.length < 4) {
        console.log(`  ✗ Bytecode is too short (${bytecode.length} chars)`);
      }
      console.log("");
    } catch (error) {
      console.error(`✗ Error reading ${path.basename(artifactPath)}:`, error.message);
    }
  }
}

checkBytecodes();

