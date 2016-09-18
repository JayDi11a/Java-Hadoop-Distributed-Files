import java.io.IOException;
import java.util.*;
import java.text.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class UrlByDate extends Configured implements Tool {

	public static void main(String args[]) throws Exception {
		int res = ToolRunner.run(new UrlByDate(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		Path inputPath = new Path(args[0]);
		Path outputPath = new Path(args[1]);


		Configuration conf = getConf();
		conf.set("start", args[2]);
		conf.set("end", args[3]);
		Job job = new Job(conf, this.getClass().toString());


		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.setJobName("UrlByDate");
		job.setJarByClass(UrlByDate.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		//job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> { 
		
	
   		 @Override
		public void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {
			String line = value.toString();
			Link link = Link.parse(line);
			IntWritable one = new IntWritable(1);
			Text url = new Text(link.url());
			String date = Long.toString(link.timestamp());
			String d = date.substring(0,2);
			
			Configuration conf= context.getConfiguration();
			String param1 = conf.get("start");
			String param2 = conf.get("end");

			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
				Date startDate = simpleDateFormat.parse(param1);
				Date endDate = simpleDateFormat.parse(param2);
				Long startmilliseconds = startDate.getTime()/1000;
				Long endmilliseconds = endDate.getTime()/1000;
				Long datemilliseconds = link.timestamp();
				String inputDate = Long.toString(startmilliseconds);
				String outputDate = Long.toString(endmilliseconds);
				String i = inputDate.substring(0,2);
				String o = outputDate.substring(0,2);
				if (datemilliseconds > startmilliseconds && datemilliseconds < endmilliseconds) {
					//context.write(new Text(url), new Text(date));
					context.write(new Text(url), one);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}	
    		}
	}
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		private Text result = new Text();
		
    		//@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			/*String translation = "";
		
			for (Text val :values) {
				translation += "" + val.toString();
			}
			result.set(translation);
			context.write(key,result);*/
			
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();	
			}
			
			context.write(key, new IntWritable(sum));
		}

	}
}


