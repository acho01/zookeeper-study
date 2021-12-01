package com.acho.service;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

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
    public String create(String path, String data, CreateMode mode) throws Exception {
        return zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
    }

    @Override
    public Stat exists(String path) throws Exception {
        return zooKeeper.exists(path, true);
    }

    @Override
    public void delete(String path) throws Exception {
        zooKeeper.delete(path, exists(path).getVersion());
    }

    @Override
    public ZooKeeper getZookeeper() {
        return zooKeeper;
    }
}
