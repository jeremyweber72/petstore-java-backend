package es.zaldo.integration.utils;

import java.io.IOException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Handles an embedded MongoDB instance.
 */
public class EmbeddedMongoDbHandler {

    private static final int MONGO_PORT = 27777;

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    /**
     * Starts embedded MongoDB.
     *
     * @throws IOException
     */
    public static void start() throws IOException  {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(
                Version.V2_3_0, MONGO_PORT, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
    }

    /**
     * Stops embedded MongoDB.
     */
    public static void stop() {
        if (mongod != null) {
            mongod.stop();
            mongodExe.stop();
        }
    }

}
