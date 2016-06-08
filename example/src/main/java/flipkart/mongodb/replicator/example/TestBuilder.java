package flipkart.mongodb.replicator.example;

import flipkart.mongo.replicator.core.model.Node;
import org.apache.commons.cli.*;

/**
 * Created by kishan.gajjar on 08/06/16.
 */
public class TestBuilder {
    private static Options opts = new Options()
            .addOption(OptionBuilder
                    .isRequired(true)
                    .hasArgs(1)
                    .withLongOpt("hostname")
                    .withDescription("Mongos hostname")
                    .create('h'))
            .addOption(OptionBuilder
                    .isRequired(true)
                    .hasArgs(1)
                    .withLongOpt("port")
                    .withDescription("Mongos port")
                    .create('p'));

    public Node getMongosNodeFromArgs(String[] args) {
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(opts, args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        String hostname = cmd.getOptionValue('h');
        int port = Integer.parseInt(cmd.getOptionValue('p'));
        return new Node(hostname, port);
    }
}
