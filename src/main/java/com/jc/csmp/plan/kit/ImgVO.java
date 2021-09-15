package com.jc.csmp.plan.kit;

public class ImgVO {
    private byte[] data;
    private int format;

    public ImgVO(byte[] data,int format) {
        this.data = data;
        this.format = format;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }
}
