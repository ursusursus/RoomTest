package eu.activstar.ursus.myapplication;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 10-May-18.
 */
public class BeaconEntity {

    public static final String NAME = "beacons";

    public static final class Columns {

        public static final String ID = "_id";
        public static final String UUID = "uuid";
        public static final String MAJOR = "major";
        public static final String MINOR = "minor";
        public static final String TIMESTAMP = "timestamp";

        private Columns() {
        }
    }
}
