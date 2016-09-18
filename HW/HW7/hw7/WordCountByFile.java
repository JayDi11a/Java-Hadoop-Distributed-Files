import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class WordCountByFile extends Configured implements Tool {

    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new WordCountByFile(), args);
	System.exit(res);
    }

    public int run(String[] args) throws Exception {
	Path inputPath = new Path(args[0]);
	Path outputPath = new Path(args[1]);

	Configuration conf = getConf();
	Job job = new Job(conf, this.getClass().toString());

	FileInputFormat.setInputPaths(job, inputPath);
	FileOutputFormat.setOutputPath(job, outputPath);

	job.setJobName("WordCountByFile");
	job.setJarByClass(WordCountByFile.class);
	job.setInputFormatClass(TextInputFormat.class);
	job.setOutputFormatClass(TextOutputFormat.class);
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(Text.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(Text.class);

	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);

	return job.waitForCompletion(true) ? 0 : 1;
    }

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
	//private final static IntWritable one = new IntWritable(1);

	private Text word = new Text();
	private String pattern = "^[a-z][a-z0-9]*$";

	@Override
	public void map(LongWritable key, Text value,
			Mapper.Context context) throws IOException, InterruptedException {
	InputSplit inputSplit = context.getInputSplit();
	String fileName = ((FileSplit) inputSplit).getPath().getName();
	String line = value.toString();
	StringTokenizer tokenizer = new StringTokenizer(line);
		while (tokenizer.hasMoreTokens()) {
			word.set(tokenizer.nextToken());
			String stringWord = word.toString().toLowerCase();
			if (stringWord.matches(pattern)) {
				context.write(new Text(stringWord), new Text(fileName));
			}
		}
    	}
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		boolean check = false;
		String fileName = "";
		String textString = "";

		for (Text val : values) {
	    		if (!check) {
				fileName = val.toString();
				check = true;
			}
			if (fileName.equals(val.toString())) {
				sum = sum + 1;
			} else {
				textString+=fileName + ": " +sum+", ";
				fileName = val.toString();
				sum = 1;
			}
		}
		textString+= fileName +": "+sum+"\n";
		context.write(key, new Text(textString));

		}
	}	

}

