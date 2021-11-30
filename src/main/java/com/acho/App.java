package com.acho;

import com.acho.service.*;

public class App {

    private static ZookeeperService zookeeperService;

    public static void main(String[] args) throws Exception {
        zookeeperService = new ZookeeperServiceImpl();
        zookeeperService.connect("localhost");
        zookeeperService.create("/ZNODE", "AAAA");
        zookeeperService.disconnect();
        System.out.println("bla");
    }
}
