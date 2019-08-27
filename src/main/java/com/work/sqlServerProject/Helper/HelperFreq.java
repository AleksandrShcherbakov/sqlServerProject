package com.work.sqlServerProject.Helper;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by a.shcherbakov on 21.08.2019.
 */
public class HelperFreq {
    @Value("${ltecarriers}")
    String [] LTEcarrierBand;
    @Value("${umtscarriers}")
    String [] UMTScarriers;

    public String[] getLTEcarrierBand() {
        return LTEcarrierBand;
    }

    public void setLTEcarrierBand(String[] LTEcarrierBand) {
        this.LTEcarrierBand = LTEcarrierBand;
    }

    public String[] getUMTScarriers() {
        return UMTScarriers;
    }

    public void setUMTScarriers(String[] UMTScarriers) {
        this.UMTScarriers = UMTScarriers;
    }
}
