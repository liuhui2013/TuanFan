package com.ro.xdroid.test;

import android.content.Context;

import com.ro.xdroid.media.image.viewer.ImageOverlayView;
import com.ro.xdroid.media.image.viewer.ImagesViewer;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roffee on 2017/9/13 18:27.
 * Contact with 460545614@qq.com
 */
public class TheApiTestUseSimeple {
    /**********************************ImagesViewer***************************************/
    private static final String POSTERS_PATH = "https://raw.githubusercontent.com/stfalcon-studio/FrescoImageViewer/v.0.5.0/images/posters";

    public static String[] getImages() {
        return new String[]{
                "http://vod.doushow.com/1504770731914_20170904_104753/Android_a/4608X2592.jpg",
                "https://images-cn.ssl-images-amazon.com/images/I/81ghN3jk6AL._SL1000_.jpg",
                "https://images-cn.ssl-images-amazon.com/images/I/61YK4KgVWLL._SL1000_.jpg",
                POSTERS_PATH + "/Vincent.jpg",
                POSTERS_PATH + "/Jules.jpg",
                POSTERS_PATH + "/Korben.jpg",
                POSTERS_PATH + "/Toretto.jpg",
                POSTERS_PATH + "/Marty.jpg",
                POSTERS_PATH + "/Driver.jpg",
                POSTERS_PATH + "/Frank.jpg",
                POSTERS_PATH + "/Max.jpg"
//                POSTERS_PATH + "/Daniel.jpg"
        };
    }

    public static String[] getDescriptions() {
        return new String[]{
                "Vincent Vega is a hitman and associate of Marsellus Wallace.",
                "Vincent Vega is a hitman and associate of Marsellus Wallace.",
                "Vincent Vega is a hitman and associate of Marsellus Wallace. He had a brother named Vic Vega who was shot and killed by an undercover cop while on a job. He worked in Amsterdam for over three years and recently returned to Los Angeles, where he has been partnered with Jules Winnfield.",
                "Jules Winnfield - initially he is a Hitman working alongside Vincent Vega but after revelation, or as he refers to it \"a moment of clarity\" he decides to leave to \"Walk the Earth.\" During the film he is stated to be from Inglewood, California",
                "Korben Dallas. A post-America taxi driver in New York City with a grand military background simply lives his life day to day, that is, before he meets Leeloo. Leeloo captures his heart soon after crashing into his taxi cab one day after escaping from a government-run laboratory. Korben soon finds himself running from the authorities in order to protect Leeloo, as well as becoming the center of a desperate ploy to save the world from an unknown evil.",
                "Dominic \"Dom\" Toretto is the brother of Mia Toretto, uncle to Jack and husband to Letty Ortiz. The protagonist in The Fast and the Furious franchise, Dominic is an elite street racer and auto mechanic.",
                "Martin Seamus \"Marty\" McFly Sr. - he is the world's second time traveler, the first to travel backwards in time and the first human to travel though time. He was also a high school student at Hill Valley High School in 1985. He is best friends with Dr. Emmett Brown, who unveiled his first working invention to him.",
                "The Driver - real name unknown - is a quiet man who has made a career out of stealing fast cars and using them as getaway vehicles in big-time robberies all over Los Angeles. Hot on the Driver's trail is the Detective (Bruce Dern), a conceited (and similarly nameless) cop who refers to the Driver as \"Cowboy\".",
                "Frank Martin (Transporter) - he initially serves as a reluctant hero. He is portrayed as a former Special Forces operative who was a team leader of a search and destroy unit. His military background includes operations \"in and out of\" Lebanon, Syria and Sudan. He retires from this after becoming fatigued and disenchanted with his superior officers.",
                "Maximillian \"Max\" Rockatansky started his apocalyptic adventure as a Main Force Patrol officer who fought for peace on the decaying roads of Australian civilization. Max served as the last line of defense against the reckless marauders terrorizing the roadways, driving a V8 Interceptor.",
                "Daniel Morales - the fastest delivery man for the local pizza parlor Pizza Joe in Marseille, France. On the last day of work, he sets a new speed record, then leaves the job to pursue a new career as a taxi driver with the blessings of his boss and co-workers. Daniel's vehicle is a white 1997 Peugeot 406..."
        };
    }
    public static class ImageModel {
        public String url;
        public String description;
    }
    private static List<ImageModel> getImagesList(){
        List<ImageModel> imageModels = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.url = getImages()[i];
            imageModel.description = getDescriptions()[i];
            imageModels.add(imageModel);
        }
        return imageModels;
    }
    public static void testImagesViewer(Context context){
        try {
            ImagesViewer.create(context, getImages())
                    .setOnDismissListener(new ImageViewer.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // do someting
                        }
                    })
                    .show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    private static boolean isSetOverlayViewAttri;
    public static void testImagesViewer1(Context context){
        try {
            ImagesViewer.create(context, getImagesList(), new ImageViewer.Formatter<ImageModel>() {
                @Override
                public String format(ImageModel imageModel) {
                    return imageModel.url;
                }
            })
                    .isHideStatusBar(false)
                    .isImageCircle(true)
                    .useRandomBackgroundColor()
                    .useOverlayView(new ImagesViewer.OnImageChangeListener() {
                        @Override
                        public void onImageChange(int position, ImageOverlayView overlayView) {
                            String url = getImages()[position];
                            overlayView.setShareText(url);
                            overlayView.setDescription(getDescriptions()[position]);
//                            if(!isSetOverlayViewAttri){
//                            isSetOverlayViewAttri = true;
//                                overlayView.setDescriptionColor();
//                                overlayView.setDescriptionSize();
//                            }
                        }
                    })
                    .setOnDismissListener(new ImageViewer.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // do someting
                        }
                    })
                    .show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
