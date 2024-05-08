# General Web3J information
The application takes advantage of the [Web3J library](https://github.com/web3j/web3j) to communicate with the ethereum blockchain.
To be correct, you connect to a ethereum (main or test net) node via [JSON-RPC](https://www.jsonrpc.org/) connection and retrieve the information this specific node/chain provides.
```
    <dependency>
	<groupId>org.web3j</groupId>
	<artifactId>core</artifactId>
	<version>4.10.3</version>
    </dependency>
```

To also work with Solidity SmartContracts and interact with them within your code, you will have to add two build plugins.
The `web3j-maven-plugin is needed` to generate the Java wrapper classes for your Solidity contracts. 
The `build-helper-maven-plugin` is needed to include the generated classes into your /target/ folder.
More details in the pom.xml

```
    <build>
        <plugins>
            <plugin>
                <groupId>org.web3j</groupId>
                <artifactId>web3j-maven-plugin</artifactId>
                <version>${web3j-maven-plugin.version}</version>
                <executions>
                    <execution>
                        ...
                    </execution>
                </executions>
            </plugin>
            
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <executions>
                    <execution>
                        ...
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

# Usage
## keyfile
the given keyfile.json (/src/main/resources/) is a newly generated wallet with some SEPOLIA testnet eth.
If you test with this application, you can test with this wallet or create / include your own.

## api development / testing software
tools like Postman or Insomnia are great for testing REST endpoints.
Pros use cURL ;)

### Postman
example for postman or other testing tools:

![postman_test](docs/postman_test.png)

### cURL
example for the blockNumber endpoint:
```
curl --location 'http://localhost:8080/api/eth/blockNumber'
```

## different ETH node
It is also possible to use every other node by simply replace the _eth.node.address_ variable in application.properties.

You can also override the node by defining it via environment variables (`ETH_NODE_ADDRESS=http....`)


As this is just a showcase / fun project / poc / whatever, there are no special profiles for main or test net.