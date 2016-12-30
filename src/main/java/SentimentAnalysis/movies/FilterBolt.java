package SentimentAnalysis.movies;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

/**
 * Created by adityajoshi on 12/28/16.
 */
public class FilterBolt extends BaseRichBolt {
    OutputCollector collector;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String tweet = tuple.getString(0);
        JSONParser parser = new JSONParser();
        String text = "";
        try {
            JSONObject object = (JSONObject) parser.parse(tweet);
            JSONObject retweetedStatus;
            if((retweetedStatus = (JSONObject) object.get("retweeted_status")) != null){
                text += retweetedStatus.get("text");
            }else{
                text += (String) object.get("text");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println("Now just the text: " + text);
        collector.emit(new Values(text));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("text"));
    }
}
