package eu.activstar.ursus.myapplication;

import java.util.UUID;

import static eu.activstar.ursus.myapplication.Preconditions.checkNotNull;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 20-Apr-18.
 */
public class IBeaconMeasurement {

    public static final IBeaconMeasurement NULL = new IBeaconMeasurement(new IBeacon(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), -1, -1), 0);

    private final IBeacon iBeacon;
    private final int rssi;

    public IBeaconMeasurement(IBeacon iBeacon, int rssi) {
        this.iBeacon = checkNotNull(iBeacon);
        this.rssi = rssi;
    }

    public IBeacon getIBeacon() {
        return iBeacon;
    }

    public int getRssi() {
        return rssi;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IBeaconMeasurement that = (IBeaconMeasurement) o;

        if (rssi != that.rssi) return false;
        return iBeacon.equals(that.iBeacon);
    }

    @Override public int hashCode() {
        int result = iBeacon.hashCode();
        result = 31 * result + rssi;
        return result;
    }

    @Override public String toString() {
        return "IBeaconMeasurement{" +
                "iBeacon=" + iBeacon +
                ", rssi=" + rssi +
                '}';
    }
}
