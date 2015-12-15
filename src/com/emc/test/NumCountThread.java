package com.emc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The thought for this testing is commented as following ASCII document:
 *
 * <pre>
 * +---------------------------------+                                          +--------+
 * |         File                    |             +-----------------------+    |        |
 * |                                 |             |                       |    |        |
 * |  +-------------------------+    |             |    Thread-1           |    |        |
 * |  |                         +------------------>                       +---->count   |
 * |  |                         |    |             |                       |    |        |
 * |  |  MAX Line (Default 1000)|    |             +-----------------------+    |sum     |
 * |  |                         |    |                                          |        |
 * |  |                         |    |                     .                    |        |
 * |  +-------------------------+    |                     .                    |        |
 * |            ...                  |                     .                    |        |
 * |                                 |                                          |        |
 * |  +-------------------------+    |             +------------------------+   |        |
 * |  |                         |    |             |                        |   |        |
 * |  |                         |    |             |                        |   |        |
 * |  |  Next Block for Max Line+------------------>    Thread-N            +--->        |
 * |  |                         |    |             |                        |   |        |
 * |  |                         |    |             +------------------------+   |        |
 * |  |                         |    |                                          |        |
 * |  +-------------------------+    |                                          |        |
 * |                                 |                                          |        |
 * |                                 |                                          |        |
 * +---------------------------------+                                          +--------+
 *
 *
 * </pre>
 * 
 * Notice</p> 
 * <li>Suppose the file is very very big, then we can also consider to split these files to several small
 * files. 
 * <li>Suppose the file is in HDFS, and then we can split (we can call init time) to several files, and then the
 * each node could fetch different block so that performance will be increaed in distributed system. Then for this case,
 * the thread model could be considered to replace to node machine. 
 * <li>We can also use Hadoop to run and calculate, but
 * this may not the examination point. So I simplify the class to use multi-thread to enhance the performance.
 * 
 * @author Jason.Wang
 */
public class NumCountThread implements Runnable {
    private final String str;
    /**
     * count number
     */
    private final static AtomicLong count = new AtomicLong();
    /**
     * sum the float
     */
    private final static AtomicFloat sum = new AtomicFloat();

    public NumCountThread(String buff) {
        this.str = buff;
    }

    public static String readFileAsLine(BufferedReader reader, int size) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(size);

        while (size > 0) {
            String readData = null;
            if ((readData = reader.readLine()) != null) {
                fileData.append(readData).append(" ");
                size--;
            } else {
                break;
            }
        }
        return fileData.toString();
    }

    /**
     * Calculate String
     * 
     * @param q
     */
    private void tokenCalulate(String q) {
        count.getAndIncrement();
        sum.getAndAdd(Float.valueOf(q));
    }

    
    public void run() {
        execute();
    }
    
    /**
     * Execute and parsing. This is a core function.
     */
    @SuppressWarnings("resource")
    public void execute(){
        StringTokenizer st = new StringTokenizer(str, " ");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            Scanner scan = new Scanner(token);
            if (scan.hasNextFloat()) 
                tokenCalulate(token);
        }
    }

    /**
     * print to console. We can also use log4j to persist to file.
     */
    public static void printResult() {
        System.out.println(String.format("Total number: %s", count));
        System.out.println(String.format("Total sum: %s", sum));
    }

    public static void main(String args[]) throws java.io.IOException {
        long startTime = System.currentTimeMillis();
        // use apache-cli.jar as dependency could be good choice to format the command line coding.
        if (args.length < 1) {
            System.out.println("Usage: <file path> [#Num of max lines] [#Num of Threads]");
            System.exit(1);
        }
        int numThreads = 4;
        // default max line is 1000;
        int maxLine = 1000;
        String path = args[0];
        File file = new File(path);
        if (!file.exists()) {
            System.err.println(String.format("%s not existed!", path));
        }
        long numFiles = 1;
        if (args.length >= 2)
            maxLine = Integer.valueOf(args[1]);
        if (args.length >= 3)
            numThreads = Integer.valueOf(args[2]);

        for (long i = 0; i < numFiles; i++) {
            ExecutorService pool = Executors.newFixedThreadPool(numThreads);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (true) {
                String line = readFileAsLine(reader, maxLine);
                if (line.equals(""))
                    break;
                // starts to thread.
                pool.execute(new NumCountThread(line));
            }
            pool.shutdown();
            try {
                pool.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                System.out.println("Pool interrupted!");
                System.exit(1);
            }
        }

        long endTime = System.currentTimeMillis();
        //add duration.
        System.out.println(String.format("Total execution time %d ms.", (endTime - startTime)));
        printResult();
    }

    /**
     * @return the count
     */
    public static long getCount() {
        return count.get();
    }

    /**
     * @return the sum
     */
    public static float getSum() {
        return sum.get();
    }
    
    
}