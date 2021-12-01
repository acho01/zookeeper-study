package com.acho.service;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public interface ZookeeperService {

    void connect(String connection) throws Exception;

    void disconnect() throws Exception;

    String create(String path, String data, CreateMode mode) throws Exception;

    Stat exists(String path) throws Exception;

    void delete(String path) throws Exception;

    ZooKeeper getZookeeper();
}
