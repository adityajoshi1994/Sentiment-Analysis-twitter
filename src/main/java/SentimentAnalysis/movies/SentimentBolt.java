package SentimentAnalysis.movies;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;

/**
 * Created by adityajoshi on 12/29/16.
 */
public class SentimentBolt extends BaseRichBolt{
    SentiWordNet sentiWordNet;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        try {
            sentiWordNet = new SentiWordNet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Tuple tuple) {
        String tweet = tuple.getString(0);
        String[] words = tweet.split(" ");
        Double totScore = 0.0;
        for(String word: words){
            if(sentiWordNet.extract(word) == null){
                continue;
            }
            totScore += sentiWordNet.extract(word);
        }
        String out = "";
        if(totScore>=0.75)
            out += "strong_positive";
        else if(totScore > 0.25 && totScore>=0.5)
            out += "positive";
        else if(totScore > 0 && totScore<=0.25)
            out += "weak_positive";
        else if(totScore < 0 && totScore>=-0.25)
            out = "weak_negative";
        else if(totScore < -0.25 && totScore>=-0.5)
            out += "negative";
        else if(totScore<=-0.75)
            out += "strong_negative";
        System.out.println(tweet + " " + totScore + " " + out);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
