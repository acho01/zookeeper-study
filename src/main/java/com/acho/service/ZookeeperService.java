package com.acho.service;

public interface ZookeeperService {

    void connect(String connection) throws Exception;

    void disconnect() throws Exception;

    void create(String path, String data) throws Exception;
}
