package services;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

import model.Audio;
import utils.Constant;
import utils.LogUtil;

/**
 * Created by muvi on 18/5/17.
 */

public class AudioDownLoadManager {
    private static DownloadManager downloadManager =null;
    private static AudioDownLoadManager _instance;

    public String getSdCardPath() {
        return sdCardPath;
    }

    public void setSdCardPath(String sdCardPath) {
        this.sdCardPath = sdCardPath;
    }

    public String sdCardPath;
    private AudioDownLoadManager(Context mContext){
        if(downloadManager == null){
            downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }
    }

    public static AudioDownLoadManager getInstance(Context mContext){

        if(_instance==null){
            return new AudioDownLoadManager(mContext);
        }
        else{
            return _instance;
        }
    }


    public long startDownloadManager(String downloadURL, String title) {

        LogUtil.createLog("startDownloadManager ::",downloadURL);
        File filesDir = new File(Constant.AUDIO_DOWNLOAD_PATH);
        if(!filesDir.exists()){
            filesDir.mkdir();
        }

        Uri downloadUri = Uri.parse(downloadURL);
        Uri destinationUri = Uri.parse(filesDir+"/"+title.replaceAll("\\s+","")+".mp3");


        File destinationUriFile = new File(destinationUri.toString());
        if(destinationUriFile.exists()){
            destinationUriFile.delete();
            LogUtil.createLog("File delete before download",destinationUri.toString());
        }


        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setTitle(title);
       // request.addRequestHeader(ApiConstant.AUTHORIZATION, ApiConstant.BEARER+" "+accessToken);
        request.setDestinationUri(Uri.fromFile(destinationUriFile));
        setSdCardPath(destinationUriFile.getAbsolutePath());
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        //   if(!checkDownloadSuceesStatus(downloadID)) {
        long downloadID = downloadManager.enqueue(request);
        //   }
        return downloadID;
    }






    public Audio checkDownLoadStatusFromDownloadManager(Audio model){
        long downloadId = model.getDownloadId();
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if(cursor!=null&&cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    model.setDownloadStatus(Constant.ACTION_DOWNLOAD_COMPLETED);

                } else if (status == DownloadManager.STATUS_FAILED) {
                    // 1. process for download fail.
                    model.setDownloadStatus(Constant.ACTION_DOWNLOAD_FAILED);

                } else if ((status == DownloadManager.STATUS_PAUSED) ||
                        (status == DownloadManager.STATUS_RUNNING)) {
                    model.setDownloadStatus(Constant.ACTION_DOWNLOAD_RUNNING);

                } else if (status == DownloadManager.STATUS_PENDING) {
                    //Not handling now
                }
                int sizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                long size = cursor.getInt(sizeIndex);
                long downloaded = cursor.getInt(downloadedIndex);
                double progress = 0.0;
                if (size != -1) progress = downloaded*100.0/size;
                // At this point you have the progress as a percentage.
                model.setDownloadProgress((int) progress);
            }
        }else{
            model.setDownloadStatus(Constant.ACTION_NOT_DOWNLOAD_YET);
        }

        return model;

    }

}
