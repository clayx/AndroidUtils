package com.boshijj.utils;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2018/3/28 0028.
 * <p>
 * 获取配置文件工具，在assets文件夹下创建，config文件，然后在编写相关配置
 * </p>
 */

public class ProperTies {

    /**
     * 获取配置信息，
     *
     * @param c          当年上下文
     * @param configName 配置文件名
     * @return 配置文件
     */
    public static Properties getProperties(Context c, String configName) {
        Properties signProps;
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context获取setting.properties的FileInputStream
            //注意这地方的参数appConfig在eclipse中应该是appConfig.properties才对,但在studio中不用写后缀
            //InputStream in = c.getAssets().open(configName);
            InputStream in = c.getAssets().open(configName);
            //方法二：通过class获取setting.properties的FileInputStream
            //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
            props.load(in);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        signProps = props;
        return signProps;
    }
}
