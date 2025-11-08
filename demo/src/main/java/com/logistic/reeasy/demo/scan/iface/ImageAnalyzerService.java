package com.logistic.reeasy.demo.scan.iface;

import java.util.List;

import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;

public interface ImageAnalyzerService {
    List<ScanBottleDetail> scanImage(String image);
}

// interfaz para implementar fachade, ya que la idea es que al Service principal no le importe como se realiza la consulta a la IA, llocal o no