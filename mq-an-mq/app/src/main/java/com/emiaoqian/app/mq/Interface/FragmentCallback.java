package com.emiaoqian.app.mq.Interface;

/**
 * Created by xiong on 2018/1/26.
 */


public class FragmentCallback {

    public interface ChildFragmentWebCallback {

        boolean childfragmentwebcallback();
        void childfragmentwebcancallback();

    }




   public interface  getrealurlCallback{
       void getrealurlcallback(String url);
   }
}