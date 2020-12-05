package cn.edu.xmu.goods.utility;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.micrometer.core.instrument.util.StringUtils;

import java.io.IOException;

public class MyDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        System.out.println(jsonParser.getText());
        if(jsonParser != null && StringUtils.isNotEmpty(jsonParser.getText())){
            return jsonParser.getText();
        }else {
            return null;
        }
    }

    MyDeserializer(){

    }
}
