package com.leo.zookeeper.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeper_Constructor_with_SID_PASSID implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	@Override
	public void process(WatchedEvent event) {
		System.out.println("Recieved watched event: " + event);
		if(KeeperState.SyncConnected == event.getState()){
			connectedSemaphore.countDown();
		}
	}
	
	public static void main(String[] args) {
		try {
			ZooKeeper zooKeeper = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183", 5000,new ZooKeeper_Constructor_with_SID_PASSID());
			connectedSemaphore.await();
			long sessionID = zooKeeper.getSessionId();
			byte [] passwd = zooKeeper.getSessionPasswd();
			zooKeeper = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183", 5000,new ZooKeeper_Constructor_with_SID_PASSID(),1l,"test".getBytes());
			zooKeeper = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183", 5000,new ZooKeeper_Constructor_with_SID_PASSID(),sessionID,passwd);
			TimeUnit.MINUTES.sleep(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
