package eu.activstar.ursus.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 20-May-18.
 */
@RunWith(AndroidJUnit4.class)
public class DbTest {

    private SQLiteDatabase db;

    @Before public void before() {
        SQLiteOpenHelper helper = new SQLiteOpenHelper(InstrumentationRegistry.getTargetContext(), null, null, 1) {
            @Override public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `beacons` (" +
                        "`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`uuid` TEXT NOT NULL, " +
                        "`major` INTEGER NOT NULL, " +
                        "`minor` INTEGER NOT NULL, " +
                        "`timestamp` INTEGER NOT NULL)");
            }

            @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        db = helper.getWritableDatabase();

//        db = SQLiteDatabase.createInMemory(new SQLiteDatabase.OpenParams.Builder().build());
////        db.execSQL("CREATE TABLE IF NOT EXISTS `article` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `desc` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `url` TEXT NOT NULL, `addedAt` INTEGER NOT NULL)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS `beacons` (" +
//                "`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                "`uuid` TEXT NOT NULL, " +
//                "`major` INTEGER NOT NULL, " +
//                "`minor` INTEGER NOT NULL, " +
//                "`timestamp` INTEGER NOT NULL)");
    }

    @Test public void foo() {

        BeaconBuffer beaconBuffer = new BeaconBuffer(db);
        beaconBuffer.add(new IBeaconMeasurement(new IBeacon(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), 120, 1), 50));
        beaconBuffer.add(new IBeaconMeasurement(new IBeacon(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"), 130, 1), 50));
        beaconBuffer.add(new IBeaconMeasurement(new IBeacon(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), 120, 1), 50));
//        db.execSQL("INSERT INTO article VALUES (1, 'some title1', 'some desc1', 'some image url1', 'some url1', 1)");
//        db.execSQL("INSERT INTO article VALUES (2, `some title2`, `some desc2`, `some image url2`, `some url2`, 2)");
//        db.execSQL("INSERT INTO article VALUES (3, `some title3`, `some desc3`, `some image url3`, `some url3`, 3)");
//        Assert.assertNotNull(cursor);
//        Assert.assertEquals(1, cursor.getCount());
        ArrayList<IBeacon> beacons = beaconBuffer.findWhereMaxRecords(10, TimeUnit.MINUTES.toMillis(20));
        assertEquals(1, beacons.size());
        assertEquals(new IBeacon(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), 120, 1), beacons.get(0));
    }

    @After public void after() {
        db.close();
    }
}
