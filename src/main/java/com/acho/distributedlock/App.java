package com.acho.distributedlock;

import com.acho.service.ZookeeperService;
import com.acho.service.ZookeeperServiceImpl;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class App {

    private static ZookeeperService zookeeperService;

    private static SimpleDistributedLock simpleLock;

    private static CountDownLatch connectionLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        zookeeperService = new ZookeeperServiceImpl();
        zookeeperService.connect("localhost");
        String counterPath = "/counter";
        String rootLock = "/ROOT_LOCK";
        simpleLock = new SimpleDistributedLock(rootLock, zookeeperService);

        simpleLock.lock();
        Stat stat = zookeeperService.exists(counterPath);
        if (stat == null) {
            zookeeperService.create(counterPath, "0", CreateMode.PERSISTENT);
        }
        simpleLock.unlock();

        for (int i = 0; i < 10000; i++) {
            simpleLock.lock();
            increment(counterPath);
            simpleLock.unlock();
        }
    }

    private static void increment(String counterPath) throws Exception {
        byte[] data = zookeeperService.getZookeeper().getData(counterPath, false, null);
        int counter = Integer.parseInt(new String(data, StandardCharsets.UTF_8));

        String updatedValue = String.valueOf(counter + 1);
        zookeeperService.getZookeeper()
                .setData(counterPath,
                        updatedValue.getBytes(),
                        zookeeperService.exists(counterPath).getVersion());
    }
}
