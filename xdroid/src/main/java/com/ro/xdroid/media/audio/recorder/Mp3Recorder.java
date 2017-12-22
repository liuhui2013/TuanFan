package com.ro.xdroid.media.audio.recorder;

import android.media.AudioRecord;

import com.czt.mp3recorder.util.LameUtil;
import com.ro.xdroid.media.audio.recorder.listener.ARecorderResponse;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roffee on 2017/9/29 11:30.
 * Contact with 460545614@qq.com
 */
public class Mp3Recorder extends ARecorder<Mp3Recorder> implements AudioRecord.OnRecordPositionUpdateListener{
    private static final int DEFAULT_LAME_MP3_QUALITY = 7;
    private static final int DEFAULT_LAME_MP3_BIT_RATE = 32; //with bit rate 32kbps

    private static final int FRAME_COUNT = 160;     //自定义 每160帧作为一个周期，通知一下需要进行编码
    private AudioRecord audioRecord;
    private int bufferSize;
    private short[] pcmBuffer;
    private byte[] mp3Buffer;
//    private List<RawData> rawDatas;
    private FileOutputStream fileOutputStream;
    private int realVolume;


    public Mp3Recorder() {
        super(AMode.AType.Mp3);
    }

    @Override
    protected boolean start() {
        if(isRecording) return true;
        isRecording = true;
        try {
            initRecoder();
        } catch (IOException e) {
            e.printStackTrace();
            if(isListenerOk()){
                aRecorderResponse.state = ARecorderResponse.StateError;
                aRecorderListener.onAudioRecorder(aRecorderResponse);
                ToastKit.show("mp3 init recorder error");
                return false;
            }
        }

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
//                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                try{
                    while (isRecording){
                        int readSize = audioRecord.read(pcmBuffer, 0, bufferSize);
                        if (readSize > 0) {
//                            rawDatas.add(new RawData(pcmBuffer, readSize));\
//                            processRawData();
                            int encodedSize = LameUtil.encode(pcmBuffer, pcmBuffer, readSize, mp3Buffer);
                            fileOutputStream.write(mp3Buffer, 0, encodedSize);
                        }
                    }
                    // release and finalize audioRecord
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                    stopTimer();

                    int flush = LameUtil.flush(mp3Buffer);
                    if (flush > 0) {
                        fileOutputStream.write(mp3Buffer, 0, flush);
                    }

                    emitter.onNext(true);
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(new Throwable("mp3 record error"));
                }finally {
                    if(fileOutputStream != null){
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                    LameUtil.close();
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if(isListenerOk()){
                            aRecorderResponse.state = ARecorderResponse.StateStop;
                            if(!isTempFile) aRecorderResponse.filePath = filePath;
                            aRecorderListener.onAudioRecorder(aRecorderResponse);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(isListenerOk()){
                            aRecorderResponse.state = ARecorderResponse.StateError;
                            aRecorderListener.onAudioRecorder(aRecorderResponse);
                            ToastKit.show("mp3 record error");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        startTimer();
        if(isListenerOk()){
            aRecorderResponse.state = ARecorderResponse.StateRecording;
            aRecorderResponse.durationChange = 0;
            aRecorderListener.onAudioRecorder(aRecorderResponse);
        }
        return true;
    }

    @Override
    protected boolean stop() {
        isRecording = false;
        return isRecording;
    }

    @Override
    protected int durationChange(int curDuration) {
        if(isListenerOk()){
//            aRecorderResponse.state = ARecorderResponse.StateRecording;
            aRecorderResponse.durationChange = curDuration;
            aRecorderListener.onAudioRecorder(aRecorderResponse);
        }
        return curDuration;
    }

    private boolean initRecoder() throws IOException {
        bufferSize = AudioRecord.getMinBufferSize(aSampleRate.getSampleRate(),
                aChannel.getChannel(), aPcmFormat.getPcmFormat().getAudioFormat());
        int bytesPerFrame = aPcmFormat.getPcmFormat().getBytesPerFrame();
		/* Get number of samples. Calculate the buffer size
		 * (round up to the factor of given frame size)
		 * 使能被整除，方便下面的周期性通知
		 * */
        int frameSize = bufferSize / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            bufferSize = frameSize * bytesPerFrame;
        }

        /* Setup audio recorder */
        audioRecord = new AudioRecord(aSource.getSource(), aSampleRate.getSampleRate(), aChannel.getChannel(),
                aPcmFormat.getPcmFormat().getAudioFormat(), bufferSize);

        pcmBuffer = new short[bufferSize];
        mp3Buffer = new byte[(int) (7200 + (bufferSize * 2 * 1.25))];
//        if(rawDatas == null){
//            rawDatas = Collections.synchronizedList(new ArrayList<RawData>());
//        }else{
//            rawDatas.clear();
//        }
        if(!isTempFile) getRealFilePath();
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        fileOutputStream = new FileOutputStream(file);

        /*
		 * Initialize lame buffer
		 * mp3 sampling rate is the same as the recorded pcm sampling rate
		 */
        LameUtil.init(aSampleRate.getSampleRate(), aChannel.getChannel(), aSampleRate.getSampleRate(), DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);

        // Create and run thread used to encode data
        // The thread will
//        audioRecord.setRecordPositionUpdateListener(this, XApp.getApp().getHandler());
        audioRecord.setPositionNotificationPeriod(FRAME_COUNT);

        audioRecord.startRecording();
        return true;
    }

//    private int processRawData() {
//        if(ToolsKit.isEmpty(rawDatas)) return 0;
//        RawData rawData = rawDatas.remove(0);
//        int encodedSize = LameUtil.encode(rawData.data, rawData.data, rawData.size, mp3Buffer);
//        if (encodedSize > 0){
//            try {
//                fileOutputStream.write(mp3Buffer, 0, encodedSize);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return encodedSize;
//    }

    @Override
    public void onMarkerReached(AudioRecord audioRecord) {

    }

    @Override
    public void onPeriodicNotification(AudioRecord audioRecord) {
//        processRawData();
    }
}
