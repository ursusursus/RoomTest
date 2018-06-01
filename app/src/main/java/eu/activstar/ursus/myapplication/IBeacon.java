package eu.activstar.ursus.myapplication;

import java.util.UUID;

import static eu.activstar.ursus.myapplication.Preconditions.checkNotNull;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 20-Apr-18.
 */
public class IBeacon {
    private final UUID uuid;
    private final int major;
    private final int minor;

    public IBeacon(UUID uuid, int major, int minor) {
        this.uuid = checkNotNull(uuid);
        this.major = major;
        this.minor = minor;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IBeacon iBeacon = (IBeacon) o;

        if (major != iBeacon.major) return false;
        if (minor != iBeacon.minor) return false;
        return uuid.equals(iBeacon.uuid);
    }

    @Override public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + major;
        result = 31 * result + minor;
        return result;
    }

    @Override public String toString() {
        return "IBeacon{" +
                "uuid=" + uuid +
                ", major=" + major +
                ", minor=" + minor +
                '}';
    }
}
