# Sentiment-Analysis-twitter

This project uses Apache Storm to analyse real time data such as live twitter feeds to get a sentiment score.

<h2> What is Apache Storm? </h2>
Storm is a real time data processing platform. It works through a topology of spouts which are the i/o sources and bolts 
which are the processing units. You can incorporate various fallback mechanisms necessary while processing streams of real-time
data. You can also split the data to be processed across multiple bolts based on its contents. To know more visit <a href="http://storm.apache.org/releases/1.0.6/Concepts.html">here</a>

<h2> Stanford core-nlp</h2>
Here's a really cool library by Stanford for doing all sorts of work in the field of NLP(Natural Language Processing). The project
uses it to do sentiment analysis of twitter feeds. To know more about Stanford core-nlp visit <a href="https://stanfordnlp.github.io/CoreNLP/">
here</a>

