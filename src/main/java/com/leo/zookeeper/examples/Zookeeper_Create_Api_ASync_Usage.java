package com.leo.zookeeper.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Zookeeper_Create_Api_ASync_Usage implements Watcher {

	private static CountDownLatch connectedSamephore = new CountDownLatch(1);
	
	@Override
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()){
			connectedSamephore.countDown();
		}
	}
	
	public static void main(String[] args) {
		String connectString = "localhost:2181,localhost:2182,localhost:2183";
		try {
			ZooKeeper zooKeeper = new ZooKeeper(connectString, 5000, new Zookeeper_Create_Api_ASync_Usage());
			connectedSamephore.await();
			zooKeeper.create("/zk-test-async-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallback(),"I am context");
			zooKeeper.create("/zk-test-async-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallback(),"I am context");
			zooKeeper.create("/zk-test-async-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallback(),"I am context");
			TimeUnit.MINUTES.sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

class IStringCallback implements AsyncCallback.StringCallback{

	@Override
	public void processResult(int rc, String path, Object ctx, String name) {
		System.out.println("Create path result : [ " + rc + ", path " + path +", ctx " + ctx + ", real path name " + name+ " ]");
	}
	
}
