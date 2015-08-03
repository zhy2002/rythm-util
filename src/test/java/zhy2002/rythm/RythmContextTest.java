package zhy2002.rythm;


import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test RythmUtilContext.
 */
public class RythmContextTest {

    @Test
    public void shouldReturnAtomicValue(){
        Object stringValue = "test";

        RythmContext context = new RythmContext(stringValue);
        assertThat(context.getValue(), equalTo(stringValue));

        Object integerValue = 9998;
        context = new RythmContext(integerValue);
        assertThat(context.getValue(), equalTo(integerValue));
    }

    @Test
    public void shouldReturnArrayElement(){
       String[] values = {"Value1", "Value2 +++", " ? ? value3 ---"};

        RythmContext context = new RythmContext(values);

        assertThat(values.length, equalTo(context.length()));
        for(int i=0; i<values.length; i++){
            assertThat(values[i], equalTo(context.get(i).getValue()));
        }
    }

    @Test
    public void shouldReturnListItem(){
        List<Integer> list = Arrays.asList(1,2,3,4,5);

        RythmContext context = new RythmContext(list);

        assertThat(list.size(), equalTo(context.length()));
        for(int i=0; i<list.size(); i++){
            assertThat(list.get(i), equalTo(context.get(i).getValue()));
        }
    }


    @Test
    public void shouldReturnObjectProperty(){
        HashMap<String, Object> jsonObject =  new HashMap<>();
        jsonObject.put("name", "Rambo");
        jsonObject.put("age", 70);
        jsonObject.put("retired", true);

        RythmContext context = new RythmContext(jsonObject);

        assertThat(jsonObject.get("name"), equalTo(context.get("name").getValue()));
        assertThat(jsonObject.get("age"), equalTo(context.get("age").getValue()));
        assertThat(jsonObject.get("retired"), equalTo(context.get("retired").getValue()));

    }

    @Test
    public void shouldReturnNestedListItem(){
        HashMap<String, Object> jsonObject =  new HashMap<>();
        jsonObject.put("weapons", Arrays.asList("Machine Gun", "Knife", "Grenade"));

        RythmContext context = new RythmContext(jsonObject);

        assertThat(((List)jsonObject.get("weapons")).size(), equalTo(context.get("weapons").length()));
        for(int i=0; i<((List)jsonObject.get("weapons")).size(); i++){
            assertThat(((List)jsonObject.get("weapons")).get(i), equalTo(context.get("weapons").get(i).getValue()));
        }
    }

    @Test
    public void shouldReturnNestedObject(){
        HashMap<String, Object> rambo =  new HashMap<>();
        rambo.put("name", "Rambo");
        HashMap<String, Object> gump =  new HashMap<>();
        gump.put("name", "Forest Gump");
        rambo.put("friend", gump);

        RythmContext context = new RythmContext(rambo);

        assertThat(gump.get("name"), equalTo(context.get("friend").get("name").getValue()));
    }

}
