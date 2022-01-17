package com.example.myble.NordicBLE.Manager2;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.ble.LegacyBleManager;
import no.nordicsemi.android.log.ILogSession;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.Logger;

public class LoggableBleManager2<T extends BleManagerCallbacks> extends LegacyBleManager<T> {

    private ILogSession logSession;

    /**
     * The manager constructor.
     * <p>
     * After constructing the manager, the callbacks object must be set with
     * {@link #setGattCallbacks(BleManagerCallbacks)}.
     *
     * @param context the context.
     */
    public LoggableBleManager2(@NonNull final Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return null;
    }

    /**
     * Sets the log session to log into.
     *
     * @param session nRF Logger log session to log inti, or null, if nRF Logger is not installed.
     */
    public void setLogger(@Nullable final ILogSession session) {
        logSession = session;
    }

    @Override
    public void log(final int priority, @NonNull final String message) {
        Logger.log(logSession, LogContract.Log.Level.fromPriority(priority), message);
        Log.println(priority, "BleManager", message);
    }
}
