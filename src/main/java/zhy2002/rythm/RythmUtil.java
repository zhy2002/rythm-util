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

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args)  {

        if(args.length == 0 || "/?".equals(args[0])) {
            System.out.println("Please specify the template file path.");
            System.out.println("Usage: java -jar rythm-util.jar <rythm_template_path> [a_json_object_to_be_injected_into_template]");
            System.out.println("More details: https://github.com/zhy2002/rythm-util");
            System.out.println("Visit http://rythmengine.org/doc/template_guide.md to find out how to write a Rythm template.");
            return;
        }

        String path = args[0];
        String jsonString = getJsonStringFromCommandLineArgs(args);

        try{
            mergeIntoTemplateFile(path, jsonString);
            System.out.println("Successful.");
        }   catch (RuntimeException | IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static boolean isJsonArray(String jsonString) {
        boolean jsonArray = false;
        for(int i=0; i<jsonString.length(); i++){
            if(Character.isWhitespace(jsonString.charAt(i)))
                continue;

            if(jsonString.charAt(i) == '{'){
                jsonArray = false;
            } else if(jsonString.charAt(i) == '['){
                jsonArray = true;
            } else {
                throw new RuntimeException("Please pass a JSON object or array.");
            }
            break;
        }

        return jsonArray;
    }

    /**
     * Read the json string from command line arguments.
     * @param args command line arguments as passed to the main function.
     * @return the json string.
     */
    private static String getJsonStringFromCommandLineArgs(String[] args) {
        StringBuilder json = new StringBuilder();
        for(int i=1; i<args.length; i++){
            json.append(args[i]);
        }
        String jsonString = json.toString();
        if(StringUtils.isEmpty(jsonString)){
            jsonString = "{}";
        }
        return jsonString;
    }

    /**
     * Evaluate the template file against an json object and overwrite the template file with the merge result.
     * @param path path to the template file, which will be overwritten by the merge result.
     * @param jsonString json object as a string.
     * @throws IOException
     */
    public static void mergeIntoTemplateFile(String path, String jsonString) throws IOException {

        String mergeResult = mergeWithTemplateFile(path, jsonString);

        File file = new File(path);
        if(!file.delete()){
            throw new RuntimeException("Cannot delete the template.");
        }
        Files.write(file.toPath(), mergeResult.getBytes(), StandardOpenOption.CREATE_NEW);
    }

    /**
     * Merge the template specified in the path argument with a json object string.
     * @param path path to the template file.
     * @param jsonString json object as a string.
     * @return merge result.
     * @throws IOException
     */
    public static String mergeWithTemplateFile(String path, String jsonString) throws IOException {

        File file = new File(path);
        if(!file.exists()){
            throw new RuntimeException("Cannot find template file at:\n" + file.getAbsolutePath());
        }

        Object arg = jsonToListOrHashMap(jsonString);

        return Rythm.render(file, new RythmContext(arg));
    }


    /**
     * Convert a json object or array string into a hash map.
     * @param jsonString json object or array as a string.
     * @return a nesting hash map created from the json object structure.
     * @throws IOException
     */
    public static Object jsonToListOrHashMap(String jsonString) throws IOException {
        boolean jsonArray = isJsonArray(jsonString);
        return jsonArray  ? mapper.readValue(jsonString, new TypeReference<List<Object>>(){})
                : mapper.readValue(jsonString, new TypeReference<HashMap<String,Object>>(){});
    }

    /**
     * Merge the json object string with the template string.
     * @param template template in a string.
     * @param jsonString json object as a string.
     * @return merge result.
     * @throws IOException
     */
    public static String mergeWithTemplateString(String template, String jsonString) throws IOException{

        Object arg = jsonToListOrHashMap(jsonString);
        return Rythm.render(template, new RythmContext(arg));

    }

}
