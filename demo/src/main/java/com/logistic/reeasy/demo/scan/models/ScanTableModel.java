package com.logistic.reeasy.demo.scan.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class ScanTableModel {
    public Long user_id;
    public int bottle_id;
    public int amount;
    public byte[] image;
    public Timestamp timeStamp;
}
