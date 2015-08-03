package zhy2002.rythm;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

        assertThat(result, equalTo("Hi My name is tester.\nI have 2 skills:\n report bug\n write documentation\n"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionIfTemplateFileNotFound() throws IOException{

        RythmUtil.mergeIntoTemplateFile("invalid file path", "");
    }

    @Test
    public void mergeIntoTemplateFileTest() throws IOException, URISyntaxException{
        File tempFile = File.createTempFile("template", "2");
        Path sourcePath = Paths.get(getClass().getResource("template2.txt").toURI());
        Files.copy(sourcePath, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String jsonString = "{\"content\":\"Windows 10 is the best OS ever.\", \"from\":{\"name\":\"Steve\",\"email\":\"steveb@microsoft.com\"}}";

        RythmUtil.mergeIntoTemplateFile(tempFile.getPath(), jsonString);
        String result = new String(Files.readAllBytes(Paths.get(tempFile.toURI())));

        assertThat(result, equalTo("=============BEGIN============\n" +
                "Message content:\n" +
                "Windows 10 is the best OS ever.\n" +
                " From: Steve | <steveb@microsoft.com>\n" +
                "==============END=============="));
    }
}
