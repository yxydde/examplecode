import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.Comparator;
import java.util.List;

/**
 * Created by yangxy on 2016/11/2.
 */
public class LeaderElection extends ConnectionWatcher {

    private String PATH = "/leader";

    public String join() throws KeeperException,
            InterruptedException {
        String path = PATH + "/node";
        String createdPath = zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Joined " + createdPath);
        return createdPath;

    }

    public String election(Watcher watcher) {
        try {
            List<String> list = zk.getChildren(PATH, false);
            LeaderWatcher w = (LeaderWatcher) watcher;

            list.sort((o1, o2) -> o1.compareTo(o2));
            System.out.println(list);
            int idx = list.indexOf(w.getPath().replace(PATH + "/", ""));
            if (idx > 0) {
                String prev = list.get(idx - 1);
                System.out.println(prev);
                Stat exist = zk.exists(PATH + "/" + prev, watcher);
                System.out.println("Add Watcher to " + prev + " " + exist);
            }
            return list.get(0);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

}
