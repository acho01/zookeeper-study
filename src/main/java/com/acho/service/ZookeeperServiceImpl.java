package com.acho.service;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperServiceImpl implements ZookeeperService {

    private static ZooKeeperConnector zooKeeperConnector;

    private static ZooKeeper zooKeeper;

    public ZookeeperServiceImpl() {
        zooKeeperConnector = new ZooKeeperConnector();
    }

    @Override
    public void connect(String connection) throws Exception {
        zooKeeper = zooKeeperConnector.connect(connection);
    }

    @Override
    public void disconnect() throws Exception {
        zooKeeperConnector.disConnect();
    }

    @Override
    public void create(String path, String data) throws Exception {
        zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    }
}
