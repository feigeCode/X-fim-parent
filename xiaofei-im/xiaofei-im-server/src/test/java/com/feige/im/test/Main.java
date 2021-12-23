package com.feige.im.test;

import com.feige.im.parser.Parser;
import com.feige.im.server.ImServer;


import java.io.File;



/**
 * @author feige<br />
 * @ClassName: Main <br/>
 * @Description: <br/>
 * @date: 2021/10/10 15:10<br/>
 */
public class Main {
    public static void main(String[] args) {
        Parser.registerDefaultParsing();
        ImServer.start(new File("E:\\project\\im\\xiaofei-im-parent\\conf\\xiaofei-im.properties"));
    }
}
