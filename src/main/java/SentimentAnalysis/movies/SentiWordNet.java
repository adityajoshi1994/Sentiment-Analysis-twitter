package SentimentAnalysis.movies;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Set;
import java.util.Iterator;
/**
 * Created by adityajoshi on 12/29/16.
 */
public class SentiWordNet {
    private String pathToSWN = "/Users/adityajoshi/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
    private HashMap<String,Double> dict = new HashMap<String, Double>();

    public SentiWordNet() throws IOException {
        HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
        BufferedReader br = new BufferedReader(new FileReader(pathToSWN));
        String line = "";
        //System.out.println(br.readLine());
        while ((line = br.readLine()) != null){
            //System.out.println(line);
            String[] data = line.split("\t");
            Double score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
            //System.out.println(score);
            String[] words = data[4].split(" ");
            for(String w:words)
            {
                //System.out.println(w);
                String[] w_n = w.split("#");
                w_n[0] += "#"+data[0];
                int index = Integer.parseInt(w_n[1])-1;
                if(_temp.containsKey(w_n[0]))
                {
                    Vector<Double> v = _temp.get(w_n[0]);
                    if(index>v.size())
                        for(int i = v.size();i<index; i++)
                            v.add(0.0);
                    v.add(index, score);
                    _temp.put(w_n[0], v);
                }
                else
                {
                    Vector<Double> v = new Vector<Double>();
                    for(int i = 0;i<index; i++)
                        v.add(0.0);
                    v.add(index, score);
                    _temp.put(w_n[0], v);
                }
            }
        }
        Set<String> temp = _temp.keySet();
        for (Iterator<String> iterator = temp.iterator(); iterator.hasNext();) {
            String word = (String) iterator.next();
            Vector<Double> v = _temp.get(word);
            double score = 0.0;
            double sum = 0.0;
            for(int i = 0; i < v.size(); i++)
                score += ((double)1/(double)(i+1))*v.get(i);
            for(int i = 1; i<=v.size(); i++)
                sum += (double)1/(double)i;
            score /= sum;
            String sent = "";

            dict.put(word, score);
        }
    }
    public Double extract(String word)
    {
        Double total = new Double(0);
        if(dict.get(word+"#n") != null)
            total = dict.get(word+"#n") + total;
        if(dict.get(word+"#a") != null)
            total = dict.get(word+"#a") + total;
        if(dict.get(word+"#r") != null)
            total = dict.get(word+"#r") + total;
        if(dict.get(word+"#v") != null)
            total = dict.get(word+"#v") + total;
        return total;
    }
    public static void main(String[] args) throws IOException {
        SentiWordNet swn = new SentiWordNet();
        String sent = "Dangal is a awesome movie!!";
        String[] words = sent.split(" ");
        Double totScore = 0.0;
        for(String w:words){
            //w = w.replaceAll("([^a-zA-Z\\\\s])","");
            if(swn.extract(w) == null)
                continue;
            totScore += swn.extract(w);
        }
        String out = "";
        if(totScore>=0.75)
            out = "strong_positive";
        else
        if(totScore > 0.25 && totScore>=0.5)
            out = "positive";
        else
        if(totScore > 0 && totScore>=0.25)
            out = "weak_positive";
        else
        if(totScore < 0 && totScore>=-0.25)
            out = "weak_negative";
        else
        if(totScore < -0.25 && totScore>=-0.5)
            out = "negative";
        else
        if(totScore<=-0.75)
            out = "strong_negative";
        System.out.println(totScore + " " + out);
    }

}
