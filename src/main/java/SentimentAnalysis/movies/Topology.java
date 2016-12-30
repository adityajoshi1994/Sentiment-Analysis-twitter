package SentimentAnalysis.movies;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * Created by adityajoshi on 12/28/16.
 */
public class Topology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("tweet-spout",new TweetSpout());
        builder.setBolt("filter-bolt",new FilterBolt(),10).shuffleGrouping("tweet-spout");
        builder.setBolt("sentiment-bolt",new SentimentBolt(),10).shuffleGrouping("filter-bolt");
        Config conf = new Config();

        // set the config in debugging mode
        conf.setDebug(true);

        if (args != null && args.length > 0) {

            // run it in a live cluster

            // set the number of workers for running all spout and bolt tasks
            conf.setNumWorkers(3);

            // create the topology and submit with config
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());

        } else {

            // run it in a simulated local cluster

            // set the number of threads to run - similar to setting number of workers in live cluster
            conf.setMaxTaskParallelism(3);

            // create the local cluster instance
            LocalCluster cluster = new LocalCluster();

            // submit the topology to the local cluster
            cluster.submitTopology("tweet-word-count", conf, builder.createTopology());

            // let the topology run for 30 seconds. note topologies never terminate!
            Utils.sleep(30000);

            // now kill the topology
            //cluster.killTopology("tweet-word-count");

            // we are done, so shutdown the local cluster
            //cluster.shutdown();
        }
    }
}
