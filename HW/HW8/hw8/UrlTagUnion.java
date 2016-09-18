
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class UrlTagUnion extends Configured implements Tool {

	public static void main(String args[]) throws Exception {
		int res = ToolRunner.run(new UrlTagUnion(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		Path inputPath = new Path(args[0]);
		Path outputPath = new Path(args[1]);

		Configuration conf = getConf();
		Job job = new Job(conf, this.getClass().toString());

		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.setJobName("UrlTagUnion");
		job.setJarByClass(UrlTagUnion.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		//job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class Map extends Mapper<LongWritable, Text, Text, Text> { 

		
		
   		 @Override
		public void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {
			String line = value.toString();
			Text url;
			Text tags;
			
	
			Link link = Link.parse(line);
			url = new Text(link.url());
			tags = new Text(link.tagString());
			context.write(new Text(url), new Text(tags));
			//StringTokenizer tokenizer = new StringTokenizer(line);
			/*while (tokenizer.hasMoreTokens()) {
				url.set(tokenizer.nextToken());	
				context.write(url, tags);	
    			}*/
    		}
	}
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		private Text result = new Text();
		
    		//@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String translations = "";

			for (Text val : values) {
				translations += " " +  val.toString();	
			}
			
			result.set(translations);
			context.write(key, result);
		}

	}
}

