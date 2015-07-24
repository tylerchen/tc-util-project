LifeCycle
=========

socket -> protocol -> compress -> compress-cache -> header -> mine-type -> 
subfix -> servlet-context -> servlet-path -> servlet-path-cache -> 

## 1. Connector

### 1.1 





XML Life Cycle
==============

### 1. Introduce

XML Life Cycle is basic xml structure parent-child node style, to analyze the xml structure 
from parent to child will treat as the life cycle for every xml node.

Every xml node will contains the "processor" property, can be specify the current node processor name.
The xml life cycle engine or parent node will load the processor by name and do the process.

		<server processor1>
			<containers processor2>
				<container processor3>
				</container>
			</containers>
		</server>

The xml life cycle will load the server node and execute processor1 and then load containers node and execute processor2 
and then load container node and execute processor3


#### 2. Processor

Every processor will do those steps:

0. process()

the life cycle entrance.

1. load_cfg()

loading configuration of the current node.

2. load_groovy()

loading groovy files of the current node.

3. start()

starting the life cycle. this step will start all children life cycle by calling "process()" method.

4. stop()

stopping the life cycle. this step will stop all children life cycle.


