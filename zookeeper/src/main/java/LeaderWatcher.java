import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Created by yangxy on 2016/11/2.
 */
public class LeaderWatcher implements Watcher {

    private LeaderElection leader;

    private String path;

    public String getPath() {
        return path;
    }

    public LeaderWatcher(String host) throws IOException, InterruptedException, KeeperException {
        leader = new LeaderElection();
        leader.connect(host);
        path = leader.join();

    }

    public void election(String path) {
        String lder = leader.election(this);
        System.out.println("Final Leader is:" + lder);
    }

    public void election() {
        election(path);
    }

    public void process(WatchedEvent event) {
        System.out.println(event.getType() + ": " + event.getPath() + ": " + event.getState());
        if (event.getType() == Event.EventType.NodeDeleted) {
            election(event.getPath());
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        LeaderWatcher watcher = new LeaderWatcher(args[0]);
        watcher.election();
        Thread.sleep(Long.MAX_VALUE);
    }
}
