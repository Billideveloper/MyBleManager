package com.example.myble.NordicBLE.ScanManager.Model;

import no.nordicsemi.android.support.v18.scanner.ScanResult;

public class ScanResults {
    public ScanResult device;

    public ScanResults(ScanResult scanResults){
        this.device = scanResults;
    }
}
