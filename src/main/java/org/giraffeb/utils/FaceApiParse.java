package org.giraffeb.utils;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class FaceApiParse {

    public JSONArray parseFaceApi(String plainJsonArray){
        return  new JSONArray(plainJsonArray);
    }

}
