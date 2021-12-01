package com.acho.distributedlock;

import com.acho.service.ZookeeperService;
import com.acho.service.ZookeeperServiceImpl;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class App {

    private static ZookeeperService zookeeperService;

    private static CountDownLatch connectionLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        zookeeperService = new ZookeeperServiceImpl();
        zookeeperService.connect("localhost");
        String counterPath = "/counter";

        for (int i = 0; i < 10000; i++) {
            increment(counterPath);
        }
    }

    private static void increment(String counterPath) throws Exception {
        Stat stat = zookeeperService.exists(counterPath);
        if (stat == null) {
            zookeeperService.create(counterPath, "0", CreateMode.PERSISTENT);
        }

        byte[] data = zookeeperService.getZookeeper().getData(counterPath, false, null);
        int counter = Integer.parseInt(new String(data, StandardCharsets.UTF_8));


        String updatedValue = String.valueOf(counter + 1);
        zookeeperService.getZookeeper()
                .setData(counterPath,
                        updatedValue.getBytes(),
                        zookeeperService.exists(counterPath).getVersion());
    }
}
