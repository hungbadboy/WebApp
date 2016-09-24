package com.siblinks.ws.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.uni_siegen.wineme.come_in.thumbnailer.Main;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException;
import de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerManager;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODExcelConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODHtmlConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODPowerpointConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.JODWordConverterThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.OpenOfficeThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.PDFBoxThumbnailer;
import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.ScratchThumbnailer;

public class Thumbnailer {
	
	protected static Logger mLog = Logger.getLogger(Main.class);
	private static ThumbnailerManager thumbnailer;
	private static Options options;
	private static File outFile;
    private static File inFile;
    private static final String LOG4J_CONFIG_FILE = "conf/javathumbnailer.log4j.properties";
    
    public String CaptureImage(String params[]) throws IOException, ThumbnailerException {
    	if (params.length == 0)
        {
            explainUsage();
            System.exit(-1);
        }
        initLogging();
        
        mLog.info("Params thumbnailer: " + params);
        
        thumbnailer = new ThumbnailerManager();

        loadExistingThumbnailers();

        // Set Default Values
        thumbnailer.setImageSize(160, 120, 0);
        thumbnailer.setThumbnailFolder("thumbs/");

        initParams();
        parseParams(params);

        if (outFile == null)
            outFile = thumbnailer.createThumbnail(inFile);
        else
            thumbnailer.generateThumbnail(inFile, outFile);

    	return null;
    }
    
    private static void initParams() {
        options = new Options();
        options.addOption(OptionBuilder.withArgName("WIDTHxHEIGHT").hasArg().withDescription("Size of the new thumbnail (default: 160x120)").create("size"));
    }
    
    private static void parseParams(String[] params) {
        CommandLineParser parser = new GnuParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, params);
        } catch ( ParseException e ) {
            System.err.println("Invalid command line: " + e.getMessage());
            explainUsage();
            System.exit(1);
        }

        if (line.hasOption("size"))
        {
            // TODO Set
        }

        String[] files = line.getArgs();
        if (files.length == 0 || files.length > 2)
        {
            explainUsage();
            System.exit(1);
        }
        inFile = new File(files[0]);
        if (files.length > 1)
            outFile = new File(files[1]);
    }

    private static void explainUsage() {
        System.out.println("JavaThumbnailer");
        System.out.println("===============");
        System.out.println("");
        System.out.println("Usage: java -jar javathumbnailer-standalone.jar [-size 160x120] inputfile [outputfile]");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar javathumbnailer-standalone.jar", options);
    }
    
    protected static void loadExistingThumbnailers() {
        if (classExists("de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.NativeImageThumbnailer"))
            thumbnailer.registerThumbnailer(new NativeImageThumbnailer());

        thumbnailer.registerThumbnailer(new OpenOfficeThumbnailer());
        thumbnailer.registerThumbnailer(new PDFBoxThumbnailer());

        try {
            thumbnailer.registerThumbnailer(new JODWordConverterThumbnailer());
            thumbnailer.registerThumbnailer(new JODExcelConverterThumbnailer());
            thumbnailer.registerThumbnailer(new JODPowerpointConverterThumbnailer());
            thumbnailer.registerThumbnailer(new JODHtmlConverterThumbnailer());
        } catch (IOException e) {
            mLog.error("Could not initialize JODConverter:", e);
        }

        thumbnailer.registerThumbnailer(new ScratchThumbnailer());
    }
    
    protected static void initLogging() throws IOException
    {
        System.setProperty("log4j.configuration", LOG4J_CONFIG_FILE);

        File logConfigFile = new File(LOG4J_CONFIG_FILE);
        if (!logConfigFile.exists())
        {
            // Extract config properties from jar
            InputStream in = Main.class.getResourceAsStream("/" + LOG4J_CONFIG_FILE);
            if (in == null)
            {
                System.err.println("Packaging error: can't find logging configuration inside jar. (Neither can I find the config file on the file system: " + logConfigFile.getAbsolutePath() + ")");
                System.exit(1);
            }

            OutputStream out = null;
            try {
                out = FileUtils.openOutputStream(logConfigFile);
                IOUtils.copy(in, out);
            } finally { try { if (in != null) in.close(); } finally { if (out != null) out.close(); } }
        }

        PropertyConfigurator.configureAndWatch(logConfigFile.getAbsolutePath(), 10 * 1000);
        mLog.info("Logging initialized");
    }
    
    public static boolean classExists(String qualifiedClassname) {
        try {
            Class.forName(qualifiedClassname);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
