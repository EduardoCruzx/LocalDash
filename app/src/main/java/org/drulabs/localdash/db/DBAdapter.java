package org.drulabs.localdash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.drulabs.localdash.model.CardModel;
import org.drulabs.localdash.model.DeviceDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Authored by KaushalD on 8/27/2016.
 */
public class DBAdapter {

    //TODO Cursors have not been closed. Please close it.

    private static DBAdapter singleInstance;
    private static Object lockObject = new Object();
    private Context context;

    private SQLiteDatabase db = null;

    private DBAdapter(Context context) {
        this.context = context;
        System.out.println("CONTEXT");
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static DBAdapter getInstance(Context context) {
        if (singleInstance == null) {
            synchronized (DBAdapter.class) {
                if (singleInstance == null) {
                    singleInstance = new DBAdapter(context);
                }
            }
        }
        return singleInstance;
    }

    public long addDevice(DeviceDTO device) {
        if (device == null || device.getIp() == null || device.getPort() == 0) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_DEV_MODEL, device.getDeviceName());
        values.put(DBHelper.COL_DEV_IP, device.getIp());
        values.put(DBHelper.COL_DEV_PLAYER, device.getPlayerName());
        values.put(DBHelper.COL_DEV_PORT, device.getPort());
        values.put(DBHelper.COL_DEV_VERSION, device.getOsVersion());

        if (!deviceExists(device.getIp())) {
            long rowId = db.insert(DBHelper.TABLE_DEVICES, null, values);
            return rowId;
        }

        return -1;

    }

    public ArrayList<CardModel> GET_ITEMS() {
        ArrayList<CardModel> items = null;
        System.out.println("GET_ITEMS");
        Cursor cursor = db.query(DBHelper.TABLE_CARDS_ITEM, null, null, null, null, null,
                DBHelper.COL_CARD_DESCRIPTION);

        if (cursor != null) {
            items = new ArrayList<>();
        } else {
            return items;
        }

        int idIndex = cursor.getColumnIndex(DBHelper.COL_CARD_ID);
        int nameIndex = cursor.getColumnIndex(DBHelper.COL_CARD_NAME);
        int descriptionIndex = cursor.getColumnIndex(DBHelper.COL_CARD_DESCRIPTION);
        int bonusIndex = cursor.getColumnIndex(DBHelper.COL_CARD_BONUS);
        int availableIndex = cursor.getColumnIndex(DBHelper.COL_CARD_AVAILABLE);

        while (cursor.moveToNext()) {
            CardModel item = new CardModel();
            item.setId(cursor.getInt(idIndex));
            item.setName(cursor.getString(nameIndex));
            item.setDescription(cursor.getString(descriptionIndex));
            item.setBonus(cursor.getInt(bonusIndex));
            item.setAvailable(cursor.getInt(availableIndex));

            items.add(item);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return items;
    }

    public ArrayList<CardModel> GET_ENEMIES() {
        ArrayList<CardModel> enemies = null;

        Cursor cursor = db.query(DBHelper.TABLE_CARDS_ENEMY, null, null, null, null, null,
                DBHelper.COL_CARD_DESCRIPTION);

        if (cursor != null) {
            enemies = new ArrayList<>();
        } else {
            return enemies;
        }

        int idIndex = cursor.getColumnIndex(DBHelper.COL_CARD_ID);
        int nameIndex = cursor.getColumnIndex(DBHelper.COL_CARD_NAME);
        int descriptionIndex = cursor.getColumnIndex(DBHelper.COL_CARD_DESCRIPTION);
        int powerIndex = cursor.getColumnIndex(DBHelper.COL_CARD_POWER);
        int availableIndex = cursor.getColumnIndex(DBHelper.COL_CARD_AVAILABLE);

        while (cursor.moveToNext()) {
            CardModel enemy = new CardModel();
            enemy.setId(cursor.getInt(idIndex));
            enemy.setName(cursor.getString(nameIndex));
            enemy.setDescription(cursor.getString(descriptionIndex));
            enemy.setPower(cursor.getInt(powerIndex));
            enemy.setAvailable(cursor.getInt(availableIndex));

            enemies.add(enemy);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return enemies;
    }

    public ArrayList<DeviceDTO> getDeviceList() {
        ArrayList<DeviceDTO> devices = null;

        Cursor cursor = db.query(DBHelper.TABLE_DEVICES, null, null, null, null, null,
                DBHelper.COL_DEV_PLAYER);

        if (cursor != null) {
            devices = new ArrayList<>();
        } else {
            return devices;
        }

        int modelIndex = cursor.getColumnIndex(DBHelper.COL_DEV_MODEL);
        int ipIndex = cursor.getColumnIndex(DBHelper.COL_DEV_IP);
        int playerNameIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PLAYER);
        int portIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PORT);
        int versionIndex = cursor.getColumnIndex(DBHelper.COL_DEV_VERSION);

        while (cursor.moveToNext()) {
            DeviceDTO device = new DeviceDTO();
            device.setDeviceName(cursor.getString(modelIndex));
            device.setIp(cursor.getString(ipIndex));
            device.setPlayerName(cursor.getString(playerNameIndex));
            device.setPort(cursor.getInt(portIndex));
            device.setOsVersion(cursor.getString(versionIndex));

            devices.add(device);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return devices;
    }

    public DeviceDTO getDevice(String ip) {

        DeviceDTO device = null;

        Cursor cursor = db.query(DBHelper.TABLE_DEVICES, null, DBHelper.COL_DEV_IP + "=?",
                new String[]{ip}, null, null, DBHelper.COL_DEV_PLAYER);

        if (cursor != null) {
            device = new DeviceDTO();
        } else {
            return device;
        }

        int modelIndex = cursor.getColumnIndex(DBHelper.COL_DEV_MODEL);
        int ipIndex = cursor.getColumnIndex(DBHelper.COL_DEV_IP);
        int playerNameIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PLAYER);
        int portIndex = cursor.getColumnIndex(DBHelper.COL_DEV_PORT);
        int versionIndex = cursor.getColumnIndex(DBHelper.COL_DEV_VERSION);

        if (cursor.moveToNext()) {
            device.setDeviceName(cursor.getString(modelIndex));
            device.setIp(cursor.getString(ipIndex));
            device.setPlayerName(cursor.getString(playerNameIndex));
            device.setPort(cursor.getInt(portIndex));
            device.setOsVersion(cursor.getString(versionIndex));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return device;
    }

    public boolean removeDevice(String ip) {
        int rowsAffected = db.delete(DBHelper.TABLE_DEVICES, DBHelper.COL_DEV_IP + "=?"
                , new String[]{ip});
        return (rowsAffected > 0);
    }

    public boolean deviceExists(String ip) {
        Cursor cursor = db.query(DBHelper.TABLE_DEVICES, null, DBHelper.COL_DEV_IP + "=?", new
                String[]{ip}, null, null, null);

        return (cursor.getCount() > 0);
    }

    public int clearDatabase() {
        int rowsAffected = db.delete(DBHelper.TABLE_DEVICES, null, null);
        return rowsAffected;
    }

}
