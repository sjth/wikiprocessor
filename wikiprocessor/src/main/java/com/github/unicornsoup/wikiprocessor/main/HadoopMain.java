package com.github.unicornsoup.wikiprocessor.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
<<<<<<< Updated upstream
import org.apache.mahout.classifier.bayes.XmlInputFormat;

=======

/**
 * The main class. Call this with the required parameters.
 * The required libraries should be set.
 * @author sp
 *
 */
>>>>>>> Stashed changes
public class HadoopMain extends Configured implements Tool {
	static final Logger log = Logger.getLogger(HadoopMain.class);
	public static final String PAGE_FIELD_DELIMITER = "=";
	public static final String GENERAL_DELIMITER = "::";
<<<<<<< Updated upstream

	static final String PAGEIDHARDCODEDPATH = "CHANGETHIS";
	static final String INPUTPATHHARDCODED = "CHANGETHIS";
	static final String OUTPUTPATHHARDCODED = "CHANGETHIS";
	static final String PROPERTYFILE = "CHANGETHIS";

	// Not catching the exception for now
	public static void main(String args[]) throws Exception {
		// String log4jFilePath = Main.getProperty("log4jprops", true);
		String log4jFilePath = PROPERTYFILE;
		System.out.println("log4jFilePath=" + log4jFilePath);
		PropertyConfigurator.configure(log4jFilePath);
		// String pageIdsFile = Main.getProperty("pageIdsFile", true);
		String pageIdsFile = PAGEIDHARDCODEDPATH;
		String params[] = { pageIdsFile, INPUTPATHHARDCODED,
				OUTPUTPATHHARDCODED };
		Configuration conf = new Configuration();

		int returnValue = ToolRunner.run(new HadoopMain(), params);
=======
	/**
	 * args[0] - pageids.txt
	 * args[1] - path to input
	 * args[2] - path to output
	 * args[3] - path to log4j file
	 * @param args
	 * @throws Exception
	 */
	public static void main (String args[]) throws Exception{
		Configuration conf = new Configuration();	
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		System.out.println("log4jFilePath=" + otherArgs[3]);
		PropertyConfigurator.configure(otherArgs[3]);
		int returnValue = ToolRunner.run(new HadoopMain(), otherArgs);
>>>>>>> Stashed changes
		System.exit(returnValue);
	}

	/**
	 * args[0] - pageids.txt args[1] - path to input args[2] - path to output
	 */
	@Override
	public int run(String[] args) {
		try {
			log.debug("run: start");
			Configuration conf = super.getConf();
			Path path = new Path(args[0]);
			log.debug("run: got args[0]= " + args[0] + ": calling filesystem.get conf");
			FileSystem fs = FileSystem.get(conf);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fs.open(path)));
			log.debug("run: got buffered reader, starting to read pages and titles");
			String allPageIds = "";
			String allPageTitles = "";
			String line = "";
			// Yeah, it will have more than one line!
			line = br.readLine();
			allPageIds = line.split(PAGE_FIELD_DELIMITER)[0];
			allPageTitles = line.split(PAGE_FIELD_DELIMITER)[1];
			while ((line = br.readLine()) != null) {
				String pageIdAndTitle[] = line.split(PAGE_FIELD_DELIMITER);
				allPageIds = allPageIds + GENERAL_DELIMITER + pageIdAndTitle[0];
				allPageTitles = allPageTitles + GENERAL_DELIMITER
						+ pageIdAndTitle[1];
			}
			log.debug("run: finished reading all pages and titles");
			conf.set("allPageIds", allPageIds);
<<<<<<< Updated upstream
			conf.set("allPageTitles", allPageTitles); // we may not use this!
			// These tags are for the XMLInputFormat
			conf.set("xmlinput.start", "<page>");
			conf.set("xmlinput.end", "</page>");
			Job job = new Job(conf);
=======
			conf.set("allPageTitles", allPageTitles); //we may not use this!
			//These tags are for the XMLInputFormat
			conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
			conf.set(XmlInputFormat.END_TAG_KEY, "</page>");
			Job job = Job.getInstance(conf);
>>>>>>> Stashed changes
			job.setJobName("Wikipedia comment processor");
			job.setJarByClass(HadoopMain.class);
			job.setInputFormatClass(XmlInputFormat.class);
			job.setMapperClass(WikiHistoryMapper.class);
			job.setReducerClass(WikiHistoryReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			log.debug("run: setting inputpath - getting args[1]=" + args[1]);
			XmlInputFormat.setInputPaths(job, new Path(args[1]));
			//FileInputFormat.setInputPaths(job, new Path(args[1]));
			log.debug("run: setting outputpath - getting args[2]=" + args[2]);
			Path outPath = new Path(args[2]);
			FileOutputFormat.setOutputPath(job, outPath);
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
			log.debug("run: calling job");
			return job.waitForCompletion(true) ? 0 : 1;
		} catch (IllegalArgumentException e) {
			log.error("run: IllegalArgumentException:" + e.getMessage(), e);
			return -1;
		} catch (IllegalStateException e) {
			log.error("run: IllegalStateException:" + e.getMessage(), e);
			return -1;
		} catch (ClassNotFoundException e) {
			log.error("run: ClassNotFoundException:" + e.getMessage(), e);
			return -1;
		} catch (IOException e) {
			log.error("run: IOException:" + e.getMessage(), e);
			return -1;
		} catch (InterruptedException e) {
			log.error("run: InterruptedException:" + e.getMessage(), e);
			return -1;
		}

	}
}
