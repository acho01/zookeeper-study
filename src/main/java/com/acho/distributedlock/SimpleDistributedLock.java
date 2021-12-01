package com.acho.distributedlock;

import com.acho.service.ZookeeperService;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimpleDistributedLock {
    private final String LOCK_PATH = "/lock_";

    private final String LOCK_REGEX = "lock_";

    private ZookeeperService zookeeperService;

    private CountDownLatch lockCountDownLatch;

    private String rootPath;

    private String lockZNode;

    public SimpleDistributedLock(String rootPath, ZookeeperService zookeeperService) {
        this.rootPath = rootPath;
        this.zookeeperService = zookeeperService;
    }

    public void lock() throws Exception {
        zookeeperService.connect("localhost");
        lockCountDownLatch = new CountDownLatch(1);
        String zNodeN = rootPath + LOCK_PATH;
        lockZNode = zookeeperService.create(zNodeN, "lock", CreateMode.EPHEMERAL_SEQUENTIAL);
        for (int i = 0; i < 2; i++) {
            List<String> children = zookeeperService.getZookeeper().getChildren(rootPath, false);
            if (children.size() == 0) {
                break;
            }

            children.sort((nodeA, nodeB) -> {
                int nodeAIndex = Integer.parseInt(extractZNodeSeq(nodeA));
                int nodeBIndex = Integer.parseInt(extractZNodeSeq(nodeB));
                return Integer.compare(nodeAIndex, nodeBIndex);
            });
            if (extractZNodeSeq(lockZNode).equals(extractZNodeSeq(children.get(0)))) {
                break;
            }

            String zNodeP = getPreviousZNode(lockZNode, children);
            String zNodePFullPath = String.format("%s/%s", rootPath, zNodeP);
            Stat stat = zookeeperService.getZookeeper().exists(zNodePFullPath, event -> {
                lockCountDownLatch.countDown();
            });

            if (stat != null) {
                lockCountDownLatch.await();
            }
        }
    }

    private String extractZNodeSeq(String zNode) {
        return zNode.split(LOCK_REGEX)[1];
    }

    private String getPreviousZNode(String createdZNodeN, List<String> children) {
        int resIndex = 0;
        for (int i = 0; i < children.size() - 1; i++) {
            if (createdZNodeN.contains(children.get(i + 1))) {
                resIndex = i;
                break;
            }
        }
        return children.get(resIndex);
    }

    public void unlock() throws Exception {
        zookeeperService.delete(lockZNode);
        zookeeperService.disconnect();
    }
}
