
<p>This is a simple Java command line tool which wraps the RythmTemplateEngine:</p>
http://rythmengine.org/doc/template_guide.md

<h3>Usage</h3>
<code>java -jar rythm-util <template-file-name> [json string]</code>
<br>
rythm-util will parse the [json string] into a hash map, which is wrpped in a zhy2002.rythm.RythmContext object. 
RythmContext provides <code>get(String)</code> and <code>get(int)</code> accessors to object property and array element. It also provides a <code>getValue()</code> for accessing the underlying data object.

<div>
<h3>This is a sample template:</h3>
<pre>
@args zhy2002.rythm.RythmContext context
Template test line1
My name is @context.get("name").
My age is @context.get("age").

I said:
"@context.get("quotes").get(0)"
"@context.get("quotes").get(1)"
</pre>

<p>If you type in this command:
<code>java -jar rythm-util.jar template.txt "{\"name\":\"zhy2002\",\"age\":\"na\",\"quotes\":[\"Github is fun.\",\"All hail Windows 10.\"]}"</code>
</p>
Note you need to escape double qoute as <em>\"</em>.
<p>The template file will be updated to:</p>
<pre>
Template test line1
My name is zhy2002.
My age is na.
I said:
"Github is fun."
"All hail Windows 10."
</pre>
</div>

<div>
<p>See the unit tests for more details.</p>
</div>