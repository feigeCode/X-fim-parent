package com.feige.im.parser;

import com.feige.im.parser.impl.JsonParser;
import com.feige.im.parser.impl.ProtoParser;

import java.util.HashMap;
import java.util.Map;

public class ParserManager {

    public static final Map<Integer, IParser> PARSER_CONTAINS = new HashMap<>();

    static {
        add(new JsonParser());
        add(new ProtoParser());
    }

    public static void add(IParser parser){
        PARSER_CONTAINS.put(parser.getType(), parser);
    }


    public static IParser getParser(int type){
        return PARSER_CONTAINS.get(type);
    }
}
