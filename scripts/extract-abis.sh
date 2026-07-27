#!/bin/bash
# Extract ABIs from Hardhat artifacts and prepare them for web3j

echo "Extracting ABIs from Hardhat artifacts..."

# Create ABI directory if it doesn't exist
mkdir -p src/main/resources/abi

# Get all artifact files and extract ABIs
for artifact in src/main/resources/artifacts/contracts/**/*.sol/*.json; do
  if [ -f "$artifact" ]; then
    # Skip the .dbg.json files
    if [[ "$artifact" == *.dbg.json ]]; then
      continue
    fi

    contractName=$(basename "$artifact" .json)
    abiFile="src/main/resources/abi/${contractName}.json"

    echo "  Processing: $contractName"

    # Extract ABI from artifact using jq or grep+sed
    if command -v jq &> /dev/null; then
      jq '.abi' "$artifact" > "$abiFile"
    else
      # Fallback without jq - extract ABI field using grep
      grep -o '"abi":\[.*\]' "$artifact" | sed 's/"abi"://g' > "$abiFile"
    fi
  fi
done

echo "✓ ABIs extracted to src/main/resources/abi/"

