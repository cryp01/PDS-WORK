
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;


public class AverageTemperature1 {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: AverageTemperature1 <input path> <output path>");
      System.exit(-1);
    }
    
    Job job = new Job();
    job.setJarByClass(AverageTemperature1.class);
    job.setJobName("Average Temperature");

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    job.setMapperClass(AverageTemperature1Mapper.class);
    job.setReducerClass(AverageTemperature1Reducer.class);

    job.setOutputValueClass(FloatWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

  public static class AverageTemperature1Mapper
    extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final int MISSING = 9999;
    
    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
      
      String line = value.toString();
      String year = line.substring(15, 19);
      int airTemperature;
      if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
        airTemperature = Integer.parseInt(line.substring(88, 92));
      } else {
        airTemperature = Integer.parseInt(line.substring(87, 92));
      }
      String quality = line.substring(92, 93);
      if (airTemperature != MISSING && quality.matches("[01459]")) {
        context.write(new Text(year), new IntWritable(airTemperature));
      }
    }
  }

  public static class AverageTemperature1Reducer
    extends Reducer<Text, IntWritable, Text, FloatWritable> {
    private FloatWritable result = new FloatWritable();
      Float average = 0f;
      Float count = 0f;
      int sum = 0;    

    @Override
    public void reduce(Text key, Iterable<IntWritable> values,
        Context context)
        throws IOException, InterruptedException {
      Text sumText = new Text("average");
      for (IntWritable value : values) {
        sum += value.get();
      }
          count += 1;
          average = sum/count;
          result.set(average);
          context.write(sumText, result);
    }
  }


}
