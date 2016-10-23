package test.controller;

import com.nsw.wx.api.util.JSONHelper;
import com.nsw.wx.common.docmodel.MassMessage;
import net.sf.json.JSONObject;

/**
 * Created by liuzp on 2016/8/6.
 */
public class Test {
    public  static  void  main(String []args){

        MassMessage msg = new MassMessage();
        msg.setAppId("1111");

        String  j  =  JSONHelper.bean2json(msg);
        System.out.print(j);

    }
}
