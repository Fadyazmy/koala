package com.tree.koala.Models;

/**
 * Created by Jashan Shewakramani
 * Description: Base class for representing vault files
 */
public class VaultFile {
    private String mFileName;
    private byte[] mFileData;

    public VaultFile(String fileName, byte[] fileData) {
        mFileName = fileName;
        mFileData = fileData;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public byte[] getFileData() {
        return mFileData;
    }

    public void setFileData(byte[] fileData) {
        mFileData = fileData;
    }
}
