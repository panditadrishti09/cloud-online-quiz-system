// =============================================================
// QuizLogAnalyzer.java
// MapReduce Program for Quiz Log Analysis
//
// Cloud Computing Concept:
//   - IaaS: Hadoop cluster runs on AWS EC2 virtual machines
//   - The NameNode coordinates across multiple DataNodes (VMs)
//   - Each DataNode processes a chunk of the log file in parallel
//
// MapReduce Concept:
//   MAP phase:
//     - Reads each line of the log file
//     - Extracts the event type (LOGIN, QUIZ_ATTEMPT, REGISTER, etc.)
//     - Emits (event_type, 1) key-value pair
//
//   REDUCE phase:
//     - Receives all (event_type, [1,1,1,...]) pairs
//     - Sums the counts for each event type
//     - Outputs (event_type, total_count)
//
// INPUT  (HDFS): /quiz/logs/sample.log
// OUTPUT (HDFS): /quiz/output/part-r-00000
// =============================================================

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class QuizLogAnalyzer {

    // ===========================================================
    // MAPPER CLASS
    // Input:  Each line of the log file
    // Output: (event_type, 1)
    //
    // Log line format:
    //   2024-01-15T09:01:45.123Z | LOGIN | user=student1 | status=SUCCESS
    // ===========================================================
    public static class QuizLogMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable ONE = new IntWritable(1);
        private Text eventKey = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

            String line = value.toString().trim();

            // Skip empty lines
            if (line.isEmpty()) return;

            // Split log line by " | "
            // Format: timestamp | EVENT_TYPE | field1 | field2 ...
            String[] parts = line.split("\\|");

            if (parts.length < 2) return;

            // Extract the event type (2nd field), trim whitespace
            String eventType = parts[1].trim();

            // Categorize events for meaningful analysis
            if (eventType.contains("LOGIN")) {
                // Check if it was a successful or failed login
                String logLine = line.toUpperCase();
                if (logLine.contains("STATUS=SUCCESS")) {
                    eventKey.set("LOGIN_SUCCESS");
                } else {
                    eventKey.set("LOGIN_FAILURE");
                }
            } else if (eventType.contains("REGISTER")) {
                eventKey.set("REGISTER");
            } else if (eventType.contains("QUIZ_ATTEMPT")) {
                eventKey.set("QUIZ_ATTEMPT");

                // Also emit score range for distribution analysis
                if (line.contains("percentage=")) {
                    String[] fields = line.split("percentage=");
                    if (fields.length > 1) {
                        String pctStr = fields[1].replace("%", "").split(" ")[0].trim();
                        try {
                            int pct = Integer.parseInt(pctStr);
                            if (pct >= 90)      context.write(new Text("SCORE_A_90_PLUS"), ONE);
                            else if (pct >= 70)  context.write(new Text("SCORE_B_70_89"),  ONE);
                            else if (pct >= 50)  context.write(new Text("SCORE_C_50_69"),  ONE);
                            else                 context.write(new Text("SCORE_D_BELOW_50"),ONE);
                        } catch (NumberFormatException e) {
                            // skip
                        }
                    }
                }
            } else if (eventType.contains("LOGOUT")) {
                eventKey.set("LOGOUT");
            } else if (eventType.contains("QUESTIONS_FETCHED")) {
                eventKey.set("QUESTIONS_FETCHED");
            } else if (eventType.contains("SYSTEM")) {
                eventKey.set("SYSTEM_EVENT");
            } else {
                eventKey.set("OTHER_EVENT");
            }

            // Emit (eventType, 1) — the REDUCE phase will sum these
            context.write(eventKey, ONE);
        }
    }

    // ===========================================================
    // REDUCER CLASS
    // Input:  (event_type, [1, 1, 1, ...])
    // Output: (event_type, total_count)
    // ===========================================================
    public static class QuizLogReducer
        extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {

            int sum = 0;

            // Sum all 1s for this event type
            for (IntWritable val : values) {
                sum += val.get();
            }

            result.set(sum);
            context.write(key, result);

            // Output example:
            //   LOGIN_SUCCESS     5
            //   LOGIN_FAILURE     2
            //   QUIZ_ATTEMPT      4
            //   SCORE_A_90_PLUS   2
            //   REGISTER          3
        }
    }

    // ===========================================================
    // DRIVER (Main)
    // Configures and submits the MapReduce job to Hadoop cluster
    // ===========================================================
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: QuizLogAnalyzer <input_path> <output_path>");
            System.err.println("Example: hadoop jar quiz-log-analyzer.jar QuizLogAnalyzer");
            System.err.println("         /quiz/logs/sample.log /quiz/output");
            System.exit(1);
        }

        // Create Hadoop configuration
        Configuration conf = new Configuration();

        // Create MapReduce job
        Job job = Job.getInstance(conf, "Quiz Log Analyzer");
        job.setJarByClass(QuizLogAnalyzer.class);

        // Set Mapper and Reducer classes
        job.setMapperClass(QuizLogMapper.class);
        job.setReducerClass(QuizLogReducer.class);

        // Set output types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // Set input and output paths (from HDFS)
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Submit job and wait for completion
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}
