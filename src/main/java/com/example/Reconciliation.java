
package com.example;

import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;




import com.example.S3Browser;
// import com.example.S3ToFileAdaptor;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.ListObjectsRequest;


import org.apache.commons.cli.*;

/*

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.sql.*;


import java.util.Map;
import java.util.HashMap;

import java.lang.RuntimeException;
*/






public class Reconciliation {

    // private static int id = 0;

    // TODO this function should be in the pool,
    // Pattern isFilePattern;
    // use a context for all this?  or use this class as a context,

    // static string buf = "";



    /**
     * Prints information about the given variable.
     */







    public static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Reconciliation", options);
        System.exit(1);
    }

    public static void main(String[] args)
        throws Exception {

        /* The first parameter is a java.lang.String that represents the option. The
        second parameter is a boolean that specifies whether the option requires an
        argument or not. In the case of a boolean option (sometimes referred to as a
        flag) an argument value is not present so false is passed. The third parameter
        is the description of the option. This description will be used in the usage
        text of the application.
        */

        Options options = new Options();

        // db credentials
        options.addOption("u", "username", true, "Database user.");
        options.addOption("p", "password", true, "Database password.");
        options.addOption("d", "db",       true, "Database connection string.");
        options.addOption("D", "driver",   true, "Database driver class.");  // TODO make optional

        // schema,
        options.addOption("schema",  "schema",   true, "schema");
        options.addOption("dbRegex", "dbRegex",  true, "db regex");

        // s3
        options.addOption("bucket",  "bucket",   true, "s3 bucket");
        options.addOption("prefix",  "prefix",   true, "s3 prefix");    // Change name to prefix...
        options.addOption("s3Regex", "s3Regex",  true, "s3 regex");

        System.out.println("here");



        String filename = "/home/meteo/imos/edal-java/in/IMOS_ACORN_V_20140103T063000Z_ROT_FV01_1-hour-avg.nc";




        new ExploreDataset().explore( filename );





/*
        // should probably only parse once...
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        }
        catch (ParseException e) {
            usage(options);
        }

        // try to get connection as first thing we do, to avoid delaying errors in authentication
        // until after the s3 bucket has been scanned
        Connection conn = Helpers.getConnection(cmd, options);

        // AmazonS3 s3 = new S3Authenticate().doit("./aws_credentials" , "default");
        AmazonS3 s3 = new AmazonS3Client();

        String schema = cmd.getOptionValue("schema");
        String bucket = cmd.getOptionValue("bucket");
        String prefix = cmd.getOptionValue("prefix");
        String s3Regex = cmd.getOptionValue("s3Regex");
        String dbRegex = cmd.getOptionValue("dbRegex");

        if (schema == null
            || bucket == null
            || prefix == null
          ) {
            Reconciliation.usage(options);
        }

        // -r 1
        ReportBuilder report = new ReportBuilder1();

        if(s3Regex != null) {
            report.event(new PropertyEvent("s3Regex", s3Regex));
        }
        if(dbRegex != null) {
            report.event(new PropertyEvent("dbRegex", dbRegex));
        }


        S3Browser browser = new S3Browser( s3, bucket  );

        // Helpers.processS3Objects( browser, prefix, report);

        Helpers.processS3Objects2(s3, bucket, prefix, report);

        Helpers.processDbEntries(conn, schema, report) ;

        report.finish();

*/
    }
}

