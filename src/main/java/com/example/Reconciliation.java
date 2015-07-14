
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




import org.opengis.metadata.extent.GeographicBoundingBox;

import uk.ac.rdg.resc.edal.dataset.DataSource;
import uk.ac.rdg.resc.edal.dataset.Dataset;
import uk.ac.rdg.resc.edal.dataset.DiscreteLayeredDataset;
import uk.ac.rdg.resc.edal.dataset.cdm.CdmGridDatasetFactory;
import uk.ac.rdg.resc.edal.domain.Extent;
// import uk.ac.rdg.resc.edal.graphics.utils.GraphicsUtils;  // different package...
import uk.ac.rdg.resc.edal.grid.TimeAxis;
import uk.ac.rdg.resc.edal.grid.VerticalAxis;
import uk.ac.rdg.resc.edal.metadata.DiscreteLayeredVariableMetadata;
import uk.ac.rdg.resc.edal.metadata.VariableMetadata;




public class Reconciliation {

    // private static int id = 0;

    // TODO this function should be in the pool,
    // Pattern isFilePattern;
    // use a context for all this?  or use this class as a context,

    // static string buf = "";


    private static void printTableLine(PrintStream ps, String title, String value) {
        // ps.printf("<tr><TD><B>%s:</b></td><td>%s</td></tr>%n", title, value);
        ps.printf("  - %s: %s\n", title, value);
    }


    /**
     * Prints information about the given variable.
     */
    private static void printVarInfo(PrintStream ps, VariableMetadata variableMetadata, Dataset dataset
    /*, String filename, String imageDir */) throws IOException {
        // ps.println("<hr />");
        // ps.printf("<h2>Variable: %s</h2>%n", variableMetadata.getId());
        ps.printf("\nVariable: %s %n", variableMetadata.getId());
        /// ps.println("<table>");
        // ps.println("<tbody>");
        printTableLine(ps, "Title", variableMetadata.getParameter().getTitle());
        printTableLine(ps, "Units", variableMetadata.getParameter().getUnits());
        printTableLine(ps, "Description", variableMetadata.getParameter().getDescription());

        GeographicBoundingBox bbox = variableMetadata.getHorizontalDomain()
                .getGeographicBoundingBox();

        printTableLine(
                ps,
                "Geographic Bounding box",
                String.format("%f,%f,%f,%f", bbox.getWestBoundLongitude(),
                        bbox.getSouthBoundLatitude(), bbox.getEastBoundLongitude(),
                        bbox.getNorthBoundLatitude()));

        if (variableMetadata.getVerticalDomain() != null) {
            if (variableMetadata.getVerticalDomain() instanceof VerticalAxis) {
                printTableLine(
                        ps,
                        "Elevation axis",
                        String.format("%d values",
                                ((VerticalAxis) variableMetadata.getVerticalDomain()).size()));
            }
        }

        if (variableMetadata.getTemporalDomain() != null) {
            if (variableMetadata.getTemporalDomain() instanceof TimeAxis) {
                printTableLine(ps, "Time axis ("
                        + variableMetadata.getTemporalDomain().getChronology() + ")",
                        String.format("%d values",
                                ((TimeAxis) variableMetadata.getTemporalDomain()).size()));
            }
        }
        // ps.println("</tbody>");
        // ps.println("</table>");

        if (variableMetadata.isScalar()) {
/*
            int width = 256;
            int height = 256;

            BufferedImage im = GraphicsUtils.plotDefaultImage(dataset, variableMetadata.getId(),
                    width, height);

            Extent<Float> dataRange = GraphicsUtils.estimateValueRange(dataset,
                    variableMetadata.getId());

            String imageFilename = imageDir + "/" + dataset.getId() + "-"
                    + variableMetadata.getId() + ".png";
            ImageIO.write(im, "png", new File(imageFilename));

            ps.printf("<p>Data min: %f, max: %f<br />", dataRange.getLow(), dataRange.getHigh());

            ps.printf("<img src=\"%s\" width=\"%d\" height=\"%d\" /></p>%n", imageFilename, width,
                    height);
*/

        } else {
            ps.println("Not a scalar field - plotting is more complex, but there is no reason to think this won't work in ncWMS2");
        }
    }




    private static void diagnoseDataset(String filename, PrintStream ps /*, String imageDir*/) {


        ps.printf("<h1>Report from %s</h1>%n", filename);
        CdmGridDatasetFactory df = new CdmGridDatasetFactory();
        DiscreteLayeredDataset<? extends DataSource, ? extends DiscreteLayeredVariableMetadata> dataset;
        try {
            dataset = df.createDataset("dataset" /*+ (id++)*/ , filename);
        } catch (Exception e) {
            ps.println("<h2>File could not be read as a dataset:</h2>");
            ps.println("<h2>" + filename + "</h2>");
            ps.println("<h2>Stack trace follows:</h2>\n");
            e.printStackTrace(ps);
            return;
        }



        // We need the dimensions
        // dataset.getDimensions();

//        new ExploreDataset().explore( dataset ) ;

/*
        System.out.println("The following features are defined in this dataset:");
        for (String featureId : dataset.getFeatureIds()) {
            System.out.println(featureId);
        }
*/


/*
        Set<String> variableIds = dataset.getVariableIds();
        for (String variableId : variableIds) {
            VariableMetadata variableMetadata = dataset.getVariableMetadata(variableId);
            try {
                // private static void printInfo(PrintStream ps, VariableMetadata variableMetadata, Dataset dataset
                printVarInfo( ps, variableMetadata, dataset );
            } catch (Exception e) {
                ps.println("<h2>Variable could not be read:</h2>");
                ps.println("<h2>" + variableId + "</h2>");
                ps.println("<h2>Stack trace follows:</h2>\n");
                e.printStackTrace(ps);
            }
        }
*/

    }


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


//        diagnoseDataset(filename, System.out );


        new ExploreDataset().explore( filename );




        /*




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

