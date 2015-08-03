package zhy2002.rythm;

import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test RythmUtil.
 */
public class RythmUtilTest {


    @Test
    public void mergeWithTemplateStringTest() throws IOException{

        String template = "@args zhy2002.rythm.RythmContext context\nMy name is @context.get(\"name\").\nMy values are: @context.get(\"list\").get(0),@context.get(\"list\").get(1),@context.get(\"list\").get(2)";
        String jsonString = "{\"name\":\"test1\", \"list\":[3,2,1]}";

        String result = RythmUtil.mergeWithTemplateString(template, jsonString);

        assertThat(result, equalTo("My name is test1.\nMy values are: 3,2,1"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void jsonToHashMapTest() throws IOException{
        String jsonString = "{\"name\":\"test1\", \"list\":[3,2,1], \"child\":{\"key\":99887}}";

        HashMap<String, Object> result = (HashMap<String, Object>)RythmUtil.jsonToListOrHashMap(jsonString);

        assertThat((String)result.get("name"), equalTo("test1"));
        assertThat((List<Integer>)result.get("list"), equalTo(Arrays.asList(3,2,1)));
        assertThat((Integer)((HashMap<String, Object>)result.get("child")).get("key"), equalTo(99887));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void jsonToListTest() throws IOException{

        String jsonString = " [\"Name1\",\"Value2\",3333]";

        List<Object> list = (List<Object>)RythmUtil.jsonToListOrHashMap(jsonString);

        assertThat(list, equalTo((Object)Arrays.asList("Name1", "Value2", 3333)));
    }

    @Test
    public void testMergeWithTemplateFile() throws IOException{

       String path = getClass().getResource("template1.txt").getPath();
        String jsonString = "{\"name\":\"tester\", \"skills\":[\"report bug\", \"write documentation\"]}";

        String result = RythmUtil.mergeWithTemplateFile(path, jsonString);

        assertThat(result, equalTo(""));
    }
}
