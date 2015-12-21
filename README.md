# Calculation Test (EMC Exam 1)
### Thoughts
Open Declaration com.emc.test.NumCountThread


The thought for this testing is commented as following ASCII document: 
```

 +---------------------------------+                                          +--------+
 |         File                    |             +-----------------------+    |        |
 |                                 |             |                       |    |        |
 |  +-------------------------+    |             |    Thread-1           |    |        |
 |  |                         +------------------>                       +---->count   |
 |  |                         |    |             |                       |    |        |
 |  |  MAX Line (Default 1000)|    |             +-----------------------+    |sum     |
 |  |                         |    |                                          |        |
 |  |                         |    |                     .                    |        |
 |  +-------------------------+    |                     .                    |        |
 |            ...                  |                     .                    |        |
 |                                 |                                          |        |
 |  +-------------------------+    |             +------------------------+   |        |
 |  |                         |    |             |                        |   |        |
 |  |                         |    |             |                        |   |        |
 |  |  Next Block for Max Line+------------------>    Thread-N            +--->        |
 |  |                         |    |             |                        |   |        |
 |  |                         |    |             +------------------------+   |        |
 |  |                         |    |                                          |        |
 |  +-------------------------+    |                                          |        |
 |                                 |                                          |        |
 |                                 |                                          |        |
 +---------------------------------+                                          +--------+
```

 
> Notice

   - Suppose the file is very very big, then we can also consider to split these files to several small files. 
   - Suppose the file is in HDFS, and then we can split (we can call init time) to several files, and then the each node could fetch different block so that performance will be increaed in distributed system. Then for this case, the thread model could be considered to replace to node machine. 
   - We can also use Hadoop to run and calculate, but this may not the examination point. So I simplify the class to use multi-thread to enhance the performance.

### Author:
Jason Wang

### Main Entry:

* [NumCountThread](https://github.com/iamtangram/CalculationTest/blob/master/src/com/emc/test/NumCountThread.java)

### Unit test:
* [CalculationTest](https://github.com/iamtangram/CalculationTest/blob/master/test/com/emc/test/CalculationTest.java)

### How to Run in Commandline:
* Compile
```sh
mkdir target
javac src\com\emc\test\*.java -d target/
cd target
jar -cf CalulationTest.jar com/emc/test/*.class
```
* Run 
Keep in the target folder.
```java
java -Djava.ext.dirs=. com.emc.test.NumCountThread <file you need to calculate> [#Num of max lines] [#Num of Threads]
```
* Example
```
d:\workspace\CalculationTest\target>java -Djava.ext.dirs=. com.emc.test.NumCountThread ../test/resources/1.txt 3000 10
Total execution time 63 ms.
Total number: 15
Total sum: 507.0
```
