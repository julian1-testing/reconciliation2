/*******************************************************************************
 * Copyright (c) 2016 The University of Reading
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the University of Reading, nor the names of the
 *    authors or contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

// package uk.ac.rdg.resc.edal.examples;
package com.example;


import java.io.PrintStream;

import java.io.IOException;
//import java.net.URL;
import java.util.Arrays;

import uk.ac.rdg.resc.edal.dataset.GriddedDataset;
import uk.ac.rdg.resc.edal.dataset.cdm.CdmGridDatasetFactory;
import uk.ac.rdg.resc.edal.domain.Extent;
import uk.ac.rdg.resc.edal.exceptions.EdalException;
import uk.ac.rdg.resc.edal.feature.GridFeature;
import uk.ac.rdg.resc.edal.grid.GridCell2D;
import uk.ac.rdg.resc.edal.grid.HorizontalGrid;
import uk.ac.rdg.resc.edal.grid.RectilinearGrid;
import uk.ac.rdg.resc.edal.grid.ReferenceableAxis;
import uk.ac.rdg.resc.edal.metadata.GridVariableMetadata;
import uk.ac.rdg.resc.edal.util.Array2D;
import uk.ac.rdg.resc.edal.util.Array4D;
// import uk.ac.rdg.resc.edal.util.Array3D;




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






/**
 * This example shows how to use EDAL to open a gridded NetCDF dataset, access
 * the metadata of the variables within it, and read a feature from it.
 *
 * @author Guy Griffiths
 */
public class ExploreDataset {

    private static void printTableLine(PrintStream ps, String title, String value) {
        // ps.printf("<tr><TD><B>%s:</b></td><td>%s</td></tr>%n", title, value);
        ps.printf("  - %s: %s\n", title, value);
    }




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

/*
  Included...
        if (variableMetadata.getTemporalDomain() != null) {
            if (variableMetadata.getTemporalDomain() instanceof TimeAxis) {
                printTableLine(ps, "Time axis ("
                        + variableMetadata.getTemporalDomain().getChronology() + ")",
                        String.format("%d values",
                                ((TimeAxis) variableMetadata.getTemporalDomain()).size()));
            }
        }
*/
        // ps.println("</tbody>");
        // ps.println("</table>");

        if (variableMetadata.isScalar()) {

            System.out.println("is scalar");

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


    public static void explore(String filename ) throws EdalException, IOException {
        /*
         * Get the data path
         */
        // URL dataResource = ExploreDataset.class.getResource("/synthetic_data.nc");
        // JA
//        URL dataResource = ExploreDataset.class.getResource(filename );

        /*
         * Create a DatasetFactory. This can be used to create Datasets
         */
        CdmGridDatasetFactory factory = new CdmGridDatasetFactory();

        /*
         * Here we create a Dataset with the ID "example_dataset" from the
         * synthetic_data.nc file.
         * 
         * CdmGridDatasetFactory returns two possible general classes -
         * GriddedDataset and HorizontalMesh4dDataset (specifically either a
         * CdmGridDataset or a CdmSgridDataset for the gridded case, and
         * CdmUgridDataset for the mesh case)
         * 
         * Because we know that the underlying data is gridded, we can cast to a
         * GriddedDataset. Doing so means that we get more useful metadata and
         * exposes methods specific to grids
         */
        GriddedDataset dataset = (GriddedDataset) factory.createDataset("example_dataset",
//                filename dataResource.getFile());
                filename );

        /*
         * dataset.getVariableIds() will return a Set of the IDs of the
         * variables available in this dataset.
         */
        System.out.println("The following variables are defined in this dataset:");
        for (String variableId : dataset.getVariableIds()) {
            System.out.println(variableId);
        }

        /*
         * dataset.getFeatureIds() will return a Set of the IDs of the features
         * available in this dataset. By default there is one feature for each
         * variable, however the data model is flexible enough for this to
         * change in specific implementations.
         */
        System.out.println("The following features are defined in this dataset:");
        for (String featureId : dataset.getFeatureIds()) {
            System.out.println(featureId);
        }

        /*
         * dataset.getVariableMetadata() can be used to get metadata about a
         * particular variable
         */
        GridVariableMetadata variableMetadata = dataset.getVariableMetadata("UCUR");
        System.out.println("The ID of the variable: " + variableMetadata.getId());



        /*HorizontalGrid horizontalGrid = variableMetadata.getTemporalDomain(); */
        // TemporalDomain temporalDomain = variableMetadata.getTemporalDomain();

        System.out.println("TEMPORAL : " );
        if (variableMetadata.getTemporalDomain() != null) {
            if (variableMetadata.getTemporalDomain() instanceof TimeAxis) {
                printTableLine(System.out, "Time axis ("
                        + variableMetadata.getTemporalDomain().getChronology() + ")",
                        String.format("%d values",
                                ((TimeAxis) variableMetadata.getTemporalDomain()).size()));
            }
        }



        /*
         * The horizontal domain of the variable. This gives the grid on which
         * the temperature variable is measured.
         */
        System.out.println("SPATIAL:");
        HorizontalGrid horizontalGrid = variableMetadata.getHorizontalDomain();
        System.out.println("CRS: " + horizontalGrid.getCoordinateReferenceSystem());

        System.out.println("BoundingBox: " + horizontalGrid.getBoundingBox());
        System.out.println("Grid x-size: " + horizontalGrid.getXSize());
        System.out.println("Grid y-size: " + horizontalGrid.getYSize());

        GeographicBoundingBox bbox = horizontalGrid.getGeographicBoundingBox();

        printTableLine(
                System.out,
                "Geographic Bounding box",
                String.format("%f,%f,%f,%f", bbox.getWestBoundLongitude(),
                        bbox.getSouthBoundLatitude(), bbox.getEastBoundLongitude(),
                        bbox.getNorthBoundLatitude()));




        /*
         * Although we know that the grid has an x/y size, it is not necessarily
         * the case that the axes are separable in the CRS of the grid (e.g.
         * each axis could be 2d).
         * 
         * However, if the axes are separable, the grid will implement
         * RectilinearGrid, and we can see the specific axis values.
         */
        if (horizontalGrid instanceof RectilinearGrid) {

            RectilinearGrid rectilinearGrid = (RectilinearGrid) horizontalGrid;
            ReferenceableAxis<Double> xAxis = rectilinearGrid.getXAxis();
            ReferenceableAxis<Double> yAxis = rectilinearGrid.getYAxis();
            System.out.println("X axis values (range of validity):");
/*            for (int x = 0; x < xAxis.size(); x++) {
                Double xVal = xAxis.getCoordinateValue(x);
                Extent<Double> xCoordRange = xAxis.getCoordinateBounds(x);
                System.out.println(xVal + " (" + xCoordRange + ")");
            }
*/
            System.out.println("Y axis values (range of validity):");
 /*           for (int y = 0; y < yAxis.size(); y++) {
                Double yVal = yAxis.getCoordinateValue(y);
                Extent<Double> yCoordRange = yAxis.getCoordinateBounds(y);
                System.out.println(yVal + " (" + yCoordRange + ")");
            }
*/
        }
        /*
         * For any general HorizontalGrid, we can find the domain objects (i.e.
         * the grid cells of the grid).
         */
        Array2D<GridCell2D> domainObjects = horizontalGrid.getDomainObjects();
        /*
         * NOTE: Objects in the Array classes are ALWAYS indexed in the order:
         * 
         * t,z,y,x
         * 
         * or however many of these dimensions are present.
         */
        System.out.println("The index of the x-dimension is: " + domainObjects.getXIndex());
        System.out.println("The index of the y-dimension is: " + domainObjects.getYIndex());
        // JA
//        System.out.println("The index of the t-dimension is: " + domainObjects.getTIndex());



        /*
         * An example of getting the first element along the x axis and the 11th
         * element along the y axis (i.e. x=0, y=10)
         */
        System.out.println("The grid cell at xindex=0, yindex=10: " + domainObjects.get(10, 0));
        /*
         * Although the toString() method just prints the grid cell centre, more
         * information is available...
         */
        GridCell2D gridCell = domainObjects.get(10, 0);
        /*
         * The parent grid containing this cell
         */
        System.out.println("The cell's parent grid is the HorizontalGrid we extracted it from: "
                + (gridCell.getParentDomain().equals(horizontalGrid)));
        /*
         * The footprint of the grid cell - in this case it is a rectangle
         */
        System.out.println("The footprint of the grid cell: " + gridCell.getFootprint());
        /*
         * The grid-based coordinates of the cell
         */
        System.out.println("The coordinates in the parent grid: " + gridCell.getGridCoordinates());




        /*
         * We can read any of the features to get the full data for the associated variable(s).
         */
        GridFeature readFeature = dataset.readFeature("UCUR");
        
        /*
         * If we read a parent variable, all of its children will also be included
         */
        System.out.println("The following parameters are available in the temp uncert group:");
        for (String param : readFeature.getVariableIds()) {
            System.out.println(param);
        }
        
        /*
         * Now we can read the data
         */
        Array4D<Number> values = readFeature.getValues("UCUR");

        System.out.println("Shape of data: "+Arrays.toString(values.getShape()));

        /*
         * We can extract individual values
         */
        // values.get(0,0,1,1);
        System.out.println(values.get(0,1,1,1));

        /*
         * Missing data (e.g. land in ocean datasets) is represented as null
         */
//       System.out.println(values.get(0,0,0,0));
    }
}
