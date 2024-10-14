package umrao.compile;

import umrao.std.Log;
import umrao.std.Message;
import umrao.std.Print;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnTheFlyCompiler extends Log {

    public void compile() {
        StringBuilder javaCode = getJavaCodeFromFile();

        //File messageImplJava = new File("src/umrao/generated/MessageImpl.java");
        File messageImplJava = new File("out/production/RuntimeCompile/umrao/generated/MessageImpl.java");
        if (messageImplJava.getParentFile().exists() || messageImplJava.getParentFile().mkdirs()) {

            try {
                try (Writer writer = new FileWriter(messageImplJava)) {
                    writer.write(javaCode.toString());
                    writer.flush();
                }

                //********** Start of Compilation Requirements **********
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                //This sets up the class path that the compiler will use. I've added the .jar file that contains the DoStuff interface within in it.
                List<String> optionList = new ArrayList<>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + File.pathSeparator + "out/OnTheFlyCompiler.jar");

                //Set MessageImpl.class file destination
                optionList.add("-d");
                optionList.add("out/production/RuntimeCompile");

                Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(List.of(messageImplJava));
                JavaCompiler.CompilationTask compileTask = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);
                //********** End of Compilation Requirements **********

                //Start compilation and capture compileTask status
                boolean result = compileTask.call();

                if (result) {
                    //********** Start Loading and executing compiled class **********
                    System.out.println("MessageImpl class compilation Success.");

                    ClassLoader classLoader = OnTheFlyCompiler.class.getClassLoader();

                    Print print = null;
                    print = new Print();
                    print.printMessage();
                    printLog();

                    //Try loading compiled class using same ClassLoader that loaded OnTheFlyCompiler class.
                    Class<?> messageImpl = classLoader.loadClass("umrao.generated.MessageImpl");

                    //classLoader.findLoadedClass("umrao.compile.OnTheFlyCompiler");
                    //classLoader.findLoadedClass("umrao.generated.MessageImpl");
                    //classLoader.findLoadedClass("umrao.std.Message");
                    //classLoader.findLoadedClass("umrao.std.Print");
                    //classLoader.findLoadedClass("umrao.std.Log");

                    //Create a new instance of same loaded class
                    Message msg = (Message) messageImpl.getDeclaredConstructor().newInstance();

                    //Run overridden showMessage method from MessageImpl class
                    msg.showMessage();

                    //********** End Loading and executing compiled class **********

                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(), diagnostic.getSource().toUri());
                    }
                }
                fileManager.close();
            } catch (IOException | ClassNotFoundException
                     | InstantiationException | IllegalAccessException
                     | InvocationTargetException | NoSuchMethodException exp) {
                System.out.println("Error:  " + exp);
            }
        }
    }

    private static StringBuilder getJavaCodeFromFile() {
        StringBuilder builder = new StringBuilder();
        try {
            File myObj = new File("src/JavaCode.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                builder.append(data).append("\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred." + e);
        }
        return builder;
    }

    private static StringBuilder getJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package umrao.generated;\n\n");
        builder.append("import umrao.std.Message;\n\n");
        builder.append("public class MessageImpl implements Message {\n");
        builder.append("    public void showMessage() {\n");
        builder.append("        System.out.println(\"Message from MessageImpl class that is runtime generated, compiled and class loaded.\");\n");
        builder.append("    }\n");
        builder.append("}\n");
        return builder;
    }
}