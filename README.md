# RuntimeCompilation
On the fly compile and use java code.

If you downloaded it zip make sure to rename Project Root to RuntimeCompile if it has any other name.

![image](https://github.com/user-attachments/assets/3115b07a-de9a-4cc1-a951-a0ec5ab17657)

Use dibugger on OnTheFlyCompiler class and evaluate below expression in debugmode to see loaded classes.
classLoader.findLoadedClass("umrao.compile.OnTheFlyCompiler");

classLoader.findLoadedClass("umrao.generated.MessageImpl");

classLoader.findLoadedClass("umrao.std.Message");

classLoader.findLoadedClass("umrao.std.Print");

classLoader.findLoadedClass("umrao.std.Log");
