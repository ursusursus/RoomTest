package eu.activstar.ursus.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static eu.activstar.ursus.myapplication.Preconditions.checkNotNull;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 10-May-18.
 */
public class BeaconBuffer {

    private static final long BUFFER_BEACON_MAX_AGE = TimeUnit.MINUTES.toMillis(20);

    private final SQLiteDatabase database;
    private final ContentValues beaconContentValues = new ContentValues();

    public BeaconBuffer(SQLiteDatabase database) {
        this.database = checkNotNull(database);
    }

    public void add(IBeaconMeasurement beaconMeasurement) {
        // Caching that model, so synchronize
        update(beaconContentValues, beaconMeasurement);
        database.insert(BeaconEntity.NAME, null, beaconContentValues);
    }

    public ArrayList<IBeacon> findWhereMaxRecords(int limitCount, long maxAge) {
        ArrayList<IBeacon> cacheNearestBeacons = new ArrayList<>();
        long start = System.currentTimeMillis();
        database.delete(BeaconEntity.NAME, "timestamp <= " + ago(BUFFER_BEACON_MAX_AGE), null);
//        debug2();

        // From beacons younger than X, pick N most latest.
        // Group that by major and count number of entries for given major.
        // From that pick beacon (row) with most counts (max).
        // That is nearest beacon.
        Cursor cursor = database.rawQuery(
                "SELECT " + BeaconEntity.Columns.UUID + "," + BeaconEntity.Columns.MAJOR + "," + BeaconEntity.Columns.MINOR + ", max(cnt)" +
                        "FROM (" +
                        "SELECT " + BeaconEntity.Columns.UUID + "," + BeaconEntity.Columns.MAJOR + "," + BeaconEntity.Columns.MINOR + ", count(*) as cnt " +
                        "FROM (" +
                        "SELECT " + BeaconEntity.Columns.UUID + "," + BeaconEntity.Columns.MAJOR + "," + BeaconEntity.Columns.MINOR + " " +
                        "FROM " + BeaconEntity.NAME + " " +
                        whereBeacons(limitCount, maxAge) +
                        ") "
                        + "GROUP BY " + BeaconEntity.Columns.MAJOR +
                        ")", null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // If db is empty, max will still return row, just with null values
                    int maxCount = cursor.getInt(3);
                    if (maxCount == 0) continue;

                    UUID uuid = UUID.fromString(cursor.getString(0));
                    int major = cursor.getInt(1);
                    int minor = cursor.getInt(2);
                    cacheNearestBeacons.add(new IBeacon(uuid, major, minor));
                }
            } finally {
                cursor.close();
            }
        }

//        debug(limitCount, maxAge);
        long end = System.currentTimeMillis();
        return cacheNearestBeacons;
    }

    private void debug(int limitCount, long maxAge) {
        Cursor cursor = database.rawQuery(
                "SELECT " + BeaconEntity.Columns.UUID + "," + BeaconEntity.Columns.MAJOR + "," + BeaconEntity.Columns.MINOR + ", count(*) as cnt " +
                        "FROM (" +
                        "SELECT " + BeaconEntity.Columns.UUID + "," + BeaconEntity.Columns.MAJOR + "," + BeaconEntity.Columns.MINOR + " " +
                        "FROM " + BeaconEntity.NAME + " " +
                        whereBeacons(limitCount, maxAge) +
                        ") "
                        + "GROUP BY " + BeaconEntity.Columns.MAJOR, null);

        if (cursor != null) {
            try {
//                LOG.dumpCursor(cursor);
            } finally {
                cursor.close();
            }
        }
    }

    private void debug2() {
        Cursor cursor = database.rawQuery(
                "SELECT count(*) as cnt " +
                        "FROM " + BeaconEntity.NAME, null);

        if (cursor != null) {
            try {
//                LOG.dumpCursor(cursor);
            } finally {
                cursor.close();
            }
        }
    }

    private String whereBeacons(int limitCount, long maxAge) {
        return "WHERE " + BeaconEntity.Columns.TIMESTAMP + " > " + ago(maxAge) + " "
                + "ORDER BY " + BeaconEntity.Columns.TIMESTAMP + " DESC"
                + (limitCount != -1 ? (" LIMIT " + limitCount + "") : "");
    }

    private long ago(long millis) {
        return System.currentTimeMillis() - millis;
    }

    private void update(ContentValues beaconContentValues, IBeaconMeasurement beaconMeasurement) {
        IBeacon iBeacon = beaconMeasurement.getIBeacon();
        beaconContentValues.put(BeaconEntity.Columns.UUID, iBeacon.getUuid().toString());
        beaconContentValues.put(BeaconEntity.Columns.MAJOR, iBeacon.getMajor());
        beaconContentValues.put(BeaconEntity.Columns.MINOR, iBeacon.getMinor());
        beaconContentValues.put(BeaconEntity.Columns.TIMESTAMP, System.currentTimeMillis());
    }
}
