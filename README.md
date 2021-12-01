# Zookeeper Study

Aim of this repo is to get better understanding in zookeeper, implementing multiple distributed examples.



## Run Examples
### Distributed Lock
First make sure to create /ROOT_LOCK  zNode inside zookeeper using zkCli.
Delete /counter zNode before running distributed lock example.
Create multiple processes using dist_lock_run.sh. 
Finally check counter value using zkCli get /counter.