package zhy2002.rythm;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.rythmengine.Rythm;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class RythmUtil {

    public static void main(String[] args) throws IOException{

        if(args.length == 0) {
            System.out.println("Please specify the template file path.");
            System.out.println("Usage: java -jar rythm-util.jar <rythm_template_path> [a_json_object_to_be_injected_into_template]");
            System.out.println("Visit http://rythmengine.org/doc/template_guide.md to find out how to write a Rythm template.");
            return;
        }

        String path = args[0];
        File file = new File(path);
        if(!file.exists()){
            System.out.println("Cannot find template file at:");
            System.out.println(file.getAbsolutePath());
            return;
        }

        StringBuilder json = new StringBuilder();
        for(int i=1; i<args.length; i++){
            json.append(args[i]);
        }
        String jsonString = json.toString();
        if(StringUtils.isEmpty(jsonString)){
            jsonString = "{}";
        }

        boolean jsonArray = false;
        for(int i=0; i<jsonString.length(); i++){
            if(Character.isWhitespace(jsonString.charAt(i)))
                continue;

            if(jsonString.charAt(i) == '{'){
                jsonArray = false;
            } else if(jsonString.charAt(i) == '['){
                jsonArray = true;
            } else {
                System.out.println("Please pass a JSON object or array.");
                return;
            }
            break;
        }

        ObjectMapper mapper = new ObjectMapper();
        Object arg = jsonArray  ? mapper.readValue(jsonString, new TypeReference<List<Object>>(){})
                                : mapper.readValue(jsonString, new TypeReference<HashMap<String,Object>>(){});
        String newContent = Rythm.render(file, new RythmContext(arg));
        if(!file.delete()){
            System.out.println("Cannot delete the template.");
            return;
        }
        Files.write(file.toPath(), newContent.getBytes(), StandardOpenOption.CREATE_NEW);
        System.out.println("Successful.");
    }
}
