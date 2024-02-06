The application takes advantage of the [Web3J library](https://github.com/web3j/web3j) to communicate with the ethereum blockchain.
To be correct, you connect to a ethereum (main or test net) node and get the information this specific node provides.
```
    <dependency>
	<groupId>org.web3j</groupId>
	<artifactId>core</artifactId>
	<version>4.10.3</version>
    </dependency>
```
# Usage

## api development / testing software
tools like Postman or Insomnia are

## cURL
example for the blockNumber endpoint:
```
curl --location 'http://localhost:8080/api/eth/blockNumber'
```

## different ETH node
It is also possible to use every other node by simply replace the _eth.node.address_ variable in application.properties.

As this is just a showcase / fun project / poc / whatever, there are no special profiles for main or test net.