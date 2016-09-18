import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class DocWordIndex extends Configured implements Tool {

    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new DocWordIndex(), args);
	System.exit(res);
    }

    public int run(String[] args) throws Exception {
	Path inputPath = new Path(args[0]);
	Path outputPath = new Path(args[1]);

	Configuration conf = getConf();
	Job job = new Job(conf, this.getClass().toString());

	FileInputFormat.setInputPaths(job, inputPath);
	FileOutputFormat.setOutputPath(job, outputPath);

	job.setJobName("DocWordIndex");
	job.setJarByClass(DocWordIndex.class);
	job.setInputFormatClass(TextInputFormat.class);
	job.setOutputFormatClass(TextOutputFormat.class);
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(IntWritable.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);

	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);

	return job.waitForCompletion(true) ? 0 : 1;
    }

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	//private final static IntWritable one = new IntWritable(1);

	private Text word = new Text();
	private String pattern = "^[a-z][a-z0-9]*$";

	@Override
	public void map(LongWritable key, Text value,
			Mapper.Context context) throws IOException, InterruptedException {
	InputSplit inputSplit = context.getInputSplit();
	String fileName = ((FileSplit) inputSplit).getPath().getName();
	String str = value.toString();
	String[] tokens = str.split(" "); //split into words
	
	//create hashmap for unique word
	HashMap<String, Integer> uniqueString = new HashMap<String,Integer>();
	for(int i = 0;i<tokens.length;i++){
		uniqueString.put(tokens[i],1);
	}
	
	//for sorting create TreeMap from above hash map
	TreeMap<String, Integer> map = new TreeMap<String, Integer>(uniqueString);

	Configuration conf = context.getConfiguration();
	int strIndex = 0;
	for (Entry<String, Integer> entry : map.entrySet()) {
		strIndex = conf.getInt("index", 0);
		int index = str.indexOf((String)entry.getKey());
		while (index >= 0) {
			index += strIndex;
			context.write(new Text((String)entry.getKey().concat("   ").concat(fileName)), new IntWritable(index));
			index = str.indexOf((String)entry.getKey(), index + 1);
		}
	}
	//I know I need to use this to write the filename the index (which I suppose I could just cast the int to a string then modify the setMapOutput/setOutput calls accordingly
	/*StringTokenizer tokenizer = new StringTokenizer(line);
		while (tokenizer.hasMoreTokens()) {
			word.set(tokenizer.nextToken());
			String stringWord = word.toString().toLowerCase();
			if (stringWord.matches(pattern)) {
				context.write(new Text(stringWord), new Text(fileName));
			}
		}*/
	conf.setInt("index", strIndex + str.length());
    	}
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		/*int index = 0;
		boolean check = false;
		String fileName = "";
		String textString = "";

		for (Text val : values) {
	    		if (!check) {
				fileName = val.toString();
				check = true;
			}
			if (fileName.equals(val.toString())) {
				index = index + 1;
			} else {
				textString+=fileName + ": " +index+", ";
				fileName = val.toString();
				index = 1;
			}
		}
		textString+= fileName +": "+index+"\n";
		context.write(key, new Text(textString));*/
		
		for (IntWritable val : values) {
			context.write(key, new IntWritable(val.get()));
		}
		}
	}	

}


