# Calculation Test (EMC Exam 1)
### Thoughts
Open Declaration com.emc.test.NumCountThread


The thought for this testing is commented as following ASCII document: 

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


 
> Notice

   - Suppose the file is very very big, then we can also consider to split these files to several small files. 
   - Suppose the file is in HDFS, and then we can split (we can call init time) to several files, and then the each node could fetch different block so that performance will be increaed in distributed system. Then for this case, the thread model could be considered to replace to node machine. 
   - We can also use Hadoop to run and calculate, but this may not the examination point. So I simplify the class to use multi-thread to enhance the performance.

### Author:
Jason Wang

