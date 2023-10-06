import java.util.*;
import java.io.*;

class textEditor{
    //Settings variables;
    public static Boolean read_on_start = true;
    public static Boolean highlight_text_edit = true;
    public static int color = 0;
    public static String[] savedContent;
    // Function to change the settings
    public static void changeConf(){
        File file = new File("settings.txt");
        try {
            Scanner reader = new Scanner(file);
            String[] out = new String[3];
            int i = 0;
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                String[] args = data.split(":");
                System.out.print(args[0] + ":");
                out[i] = args[0]+":"+globalScanner.nextLine();
                i++;
            }
            reader.close();
            FileWriter writer = new FileWriter(file);
            writer.write(join(out, "\n"));
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("No settings file found, continuing with default settings");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    //Load Up Settings
    public static void loadConf(){
        File file = new File("settings.txt");
        try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                String[] args = data.split(":");
                if(args[0].equals("read_on_start")){
                    read_on_start = args[1].equals("1");
                }
                if(args[0].equals("highlight_text_edit")){
                    highlight_text_edit = args[1].equals("1");
                }
                if(args[0].equals("color")){
                    color = Integer.parseInt(args[1]);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No settings file found, continuing with default settings");
        }
    }
    //Color variables
    public static final String[] colors = {
        "\u001B[0m",  //Reset
        "\u001B[30m", //Black
        "\u001B[31m", //Red
        "\u001B[32m", //Green
        "\u001B[33m", //Yellow
        "\u001B[34m", //Blue
        "\u001B[35m", //Purple
        "\u001B[36m", //Cyan
        "\u001B[37m"  //White
    };
    // General variables
    public static int fileLength;
    public static Scanner globalScanner = new Scanner(System.in);
    //Function to clear the screen
    public static void cls(){
        try{
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    // Function to verify user action
    public static boolean confirm(String text){
        System.out.print(text+" (Y/N)");
        return globalScanner.nextLine().toLowerCase().equals("y");
    }
    //Function to check if file already exists
    public static boolean checkFile(String path){
        File file = new File(path);
        if(!file.exists()){
            boolean ans = confirm("("+path + ")File doesn't currently exist, do you want to create the file?");
            if(ans){
                try{
                    file.createNewFile();
                }catch(IOException e){
                    System.out.println("error creating file!");
                    e.printStackTrace();
                }
            }
            return ans;
        }
        return true;
    }
    // List creator from String
    public static Character[] iterable(String text){
        Character[] out = new Character[text.length()];
        for(int i = 0; i<out.length; i++){
            out[i] = Character.valueOf(text.charAt(i));
        }
        return out;
    }
    // creating a function that counts the amount of one character in text
    public static int count(String text, Character character){
        int c = 0;
        for(Character letter:iterable(text)){
            if(letter.toString().equals(character.toString())){
                c++;
            }
        }
        return c;
    }
    // Function to keep previous data
    public static void saveData(String path){
        File file = new File(path);
        try{
            Scanner reader = new Scanner(file);
            int i = 0;
            while(reader.hasNextLine()){
                i++;
                reader.nextLine();
            }
            fileLength = i;
            savedContent = new String[fileLength];
            reader.close();
            reader = new Scanner(file);
            int index = 0;
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                savedContent[index] = data;
                index++;
            }
            reader.close();
        }catch(FileNotFoundException e){
            System.out.println("There was an error reading the file!");
            e.printStackTrace();
        }
    }
    // Function to restore to before the last change
    public static void restoreData(String path){
        String[] oldSave = savedContent;
        saveData(path);
        File file = new File(path);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(join(oldSave, "\n"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Function that reads file
    public static void readFile(String path,Integer start, Integer end){
        // start starts from 0
        // end is length of file minus one
        File file = new File(path);
        try{
            if (start == null){
                start = 0;
            }
            Scanner reader = new Scanner(file);
            int i = 0;
            while(reader.hasNextLine()){
                i++;
                reader.nextLine();
            }
            fileLength = i;
            reader.close();
            if (end == null){
                end = fileLength-1;
            }
            reader = new Scanner(file);
            int index = 0;
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                if(index>= start && index<=end){
                    System.out.print((index+1) + "| ");
                    System.out.println(data);
                }
                index++;
            }
            reader.close();
        }catch(FileNotFoundException e){
            System.out.println("There was an error reading the file!");
            e.printStackTrace();
        }
    }
    
    // Function to check if list contains string
    public static boolean contains(String[] list, String search){
        for(String elem:list){
            if(elem.equals(search)){
                return true;
            }
        }
        return false;
    }

    // Function to join array
    public static String join(String[] list, String joiner){
        String out = "";
        int index = 0;
        for(String elem:list){
            elem = elem!=null?elem:"";
            out = out+elem;
            if(index != list.length){
                out = out+joiner;
            }
            index++;
        }
        return out;
    }

    // Function to edit a line of a file
    public static Integer edit(String path, int line){
        cls();
        String[] lines = new String[fileLength];
        File file = new File(path);
        try{
            Scanner reader = new Scanner(file);
            int i = 0;
            while(reader.hasNextLine()){
                lines[i] = reader.nextLine();
                i++;
            }
            reader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        int start = 0;
        int end = fileLength-1;
        if(!(line-4<0)){
            start = line-4;
        }
        if(!(line+4>fileLength-1)){
            end = line + 4;
        }
        readFile(path, start, line-2);
        if(highlight_text_edit){
            System.out.println(colors[color] + line + "| " + lines[line-1] + colors[0]);
        }else{
            System.out.println(line + "| " + lines[line-1]);
        }
        readFile(path, line, end);
        System.out.print(line+"| ");
        String change = globalScanner.nextLine();
        if (change.equals("%exit%")){
            return 0;
        }
        lines[line-1] = change;
        try{
            FileWriter writer = new FileWriter(file);
            writer.write(join(lines, "\n"));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        cls();
        readFile(path, null, null);
        return 0;
    }
    // Function to append to list
    public static String[] append(String[] list, String value){
        String[] out = new String[list.length+1];
        for(int i=0; i<list.length; i++){
            out[i] = list[i];
        }
        out[list.length] = value;
        return out;
    }
    // Function to write from 0 the file
    public static int write(String path){
        int blankCounter = 0;
        String[] content = {};
        while(blankCounter < 2){
            System.out.print(content.length+1 + "| ");
            String data = globalScanner.nextLine();
            if(data.equals("")){
                blankCounter++;
            }
            if(data.equals("%exit%")){
                return 0;
            }
            content = append(content, data);
        }
        fileLength = content.length;
        try{
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            writer.write(join(content, "\n"));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return 0;
    }
    // Function to pop last element from list
    public static String[] pop(String[] list){
        String[] out = new String[list.length-1];
        for(int i = 0; i<out.length; i++){
            out[i] = list[i];
        }
        return out;
    }
    // Function to write from end of file
    public static int writeEnd(String path){
        String[] lines = new String[fileLength];
        File file = new File(path);
        try{
            Scanner reader = new Scanner(file);
            int i = 0;
            while(reader.hasNextLine()){
                lines[i] = reader.nextLine();
                i++;
            }
            reader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        int blankCounter = 0;
        int maxBlanks = 2;
        String[] content = lines;
        readFile(path, null, null);
        while(blankCounter != 2){
            System.out.print(content.length+1 + "| ");
            String data = globalScanner.nextLine();
            if(data.equals("")){
                blankCounter++;
            }
            if(data.equals("%exit%")){
                return 0;
            }
            content = append(content, data);
        }
        for(int i=0; i<maxBlanks; i++){
            content = pop(content);
        }
        try{
            FileWriter writer = new FileWriter(file);
            writer.write(join(content, "\n"));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return 0;
    }
    // Function that starts writing from a specific line
    public static int writeFrom(String path, int from, Integer to){
        String next = "y";
        while(next.toLowerCase().equals("y")){
            if(from == fileLength+1){
                return 0;
            }
            edit(path, from);
            from++;
            if(to == null){
                System.out.print("Do you want to edit next line? (Y/N)");
                next = globalScanner.nextLine();
            }else{
                if(from==to+1){
                    next = "n";
                }
            }
        }
        return 0;
    }
    // Function to add an extra line to the text
    public static void addLine(String path, int line, Integer number_of_lines){
        number_of_lines = number_of_lines==null?1:number_of_lines;
        String[] content = new String[fileLength+number_of_lines];
        fileLength = fileLength+number_of_lines;
        fileLength = content.length;
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            int index = 0;
            while(reader.hasNextLine()){
                if(index >= line-1 && index <= line+number_of_lines-2){
                    content[index] = null;
                }else{
                    content[index] = reader.nextLine();
                }
                index++;
            }
            reader.close();
            FileWriter writer = new FileWriter(file);
            String out = join(content, "\n");
            writer.write(out);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    // Function to delete one line from the end of the code
    public static void delLines(String path,int lines){
        File file = new File(path);
        String[] content = new String[fileLength];
        fileLength = fileLength-1;
        try {
            Scanner reader = new Scanner(file);
            int i = 0;
            while(reader.hasNextLine()){
                content[i] = reader.nextLine();
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<lines; i++){
            if (content.length > 0){
                content = pop(content);
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(join(content, "\n"));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    // Function to delete a specific line
    public static void delLine(String path, int line, Integer endLine){
        if (endLine == null){
            endLine = line;
        }
        String[] content = new String[fileLength-(endLine-line + 1)];
        File file = new File(path);
        try {
            Scanner reader = new Scanner(file);
            int curr_line = 1;
            int index = 0;
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                if(!(curr_line>=line && curr_line <= endLine)){
                    content[index] = data;
                    index++;
                }
                curr_line++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try{
            FileWriter writer = new FileWriter(file);
            writer.write(join(content, "\n"));
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    // Just help command
    public static void help(){
        String[] out = {
            "Help Menu",
            "help : opens this menu",
            "quit, exit, xt, qu, x, ex : command to exit current file",
            "read : reads the current file",
            "",
            "zz : undo last command done",
            "%exit% : only allowed in write, and edit commands to cancel command",
            "",
            colors[color]+"MULTIPLE SINTAX COMMANDS"+colors[0],
            "write : writes from the last line",
            "write 0 : write from the first line (erases all current data of file)",
            "write <line> : write from the line specified, have to confirm for each line",
            "write <from> <to> : write from the line specified to the second line specified",
            "",
            "del : deletes file, has confirmation",
            "del y : deletes file has no confirmation",
            "",
            "delline : deletes the last line of file",
            "dellines <lines> : deletes the last number of lines specified",
            "delline <line> : deletes specified line",
            "delline <line> <endline> : deletes the lines from the first line specified to the second one",
            "",
            "add <line> : adds one line before the specified line",
            "add -1 : adds one line to the end",
            "add <line> <lines> : adds the specified number of lines before the specified line",
            "add edit <line> : adds a line before the specified line and makes you edit that line",
            "add edit <line> <lines>: same as above but for the amount of lines specified"
        };
        System.out.println(join(out, "\n"));
    }
    // Function to process all commands
    public static Integer cmd(String path){
        String[] quitCommands = {"quit", "exit", "xt", "qu", "x", "ex"};
        String command = "";
        System.out.print(path + "> ");
        command = globalScanner.nextLine();
        if(contains(quitCommands, command)){
            return 0;
        }
        String[] args = command.split(" ");
        if(args.length==1){
            if(args[0].equals("help")){
                help();
            }
            if (args[0].equals("read")){
                cls();
                readFile(path, null, null);
            }
            if(args[0].equals("write")){
                cls();
                saveData(path);
                writeEnd(path);
            }
            if(args[0].equals("del")){
                if(confirm("Do you really want to delete the file?")){
                    File file = new File(path);
                    file.delete();
                    return 0;
                }
            }
            if(args[0].equals("delline")){
                saveData(path);
                delLines(path, 1);
            }
            if(args[0].equals("zz")){
                restoreData(path);
            }
        }
        if(args.length>1){
            if(args[0].equals("edit")){
                saveData(path);
                edit(path, Integer.parseInt(args[1]));
            }
            if(args[0].equals("write")){
                saveData(path);
                if(args[1].equals("0")){
                    write(path);
                }else{
                    writeFrom(path, Integer.parseInt(args[1]), args.length<3 ? null:Integer.parseInt(args[2]));
                }
            }
            if(args[0].equals("add") && !args[1].equals("edit")){
                saveData(path);
                addLine(path, Integer.parseInt(args[1]), args.length<3 ? null:Integer.parseInt(args[2]));
            }
            if(args[0].equals("del") && args[1].equals("y")){
                File file = new File(path);
                file.delete();
                return 0;
            }
            if(args[0].equals("add") && args[1].equals("edit")){
                saveData(path);
                addLine(path, args.length>2?Integer.parseInt(args[2]):1, args.length>3?Integer.parseInt(args[3]):null);
                writeFrom(path, args.length>2?(Integer.parseInt(args[2])<0?fileLength:Integer.parseInt(args[2])):1, args.length>3?(Integer.parseInt(args[3])<0?fileLength:Integer.parseInt(args[3])):args.length>2?(Integer.parseInt(args[2])<0?fileLength:Integer.parseInt(args[2])):1);
            }
            if(args[0].equals("dellines")){
                saveData(path);
                delLines(path, Integer.parseInt(args[1]));
            }
            if(args[0].equals("delline")){
                saveData(path);
                delLine(path, Integer.parseInt(args[1]), args.length>2?Integer.parseInt(args[2]):null);
            }
        }
        cmd(path);
        return 1;
    }

    // Function to remove element from list
    public static String[] remove(String[] list, int index){
        String[] out = new String[list.length-1];
        int temp_index = 0;
        int temp_index2 = 0;
        for(String _elem:list){
            if(temp_index != index){
                out[temp_index2] = _elem;
                temp_index2++;
            }
            temp_index++;
        }
        return out;
    }

    public static void main(String[] args){
        loadConf();
        if(args.length>100){
            System.out.println("max amount of initially loaded files is 100");
            System.exit(0);
        }
        if(true){
            String[] temp = new String[100];
            String[] file_paths;
            int file_number = 0;
            for(String path:args){
                if(checkFile(path)){
                    temp[file_number] = path;
                    file_number++;
                }
            }
            file_paths = new String[file_number];
            file_number = 0;
            for(String temp_:temp){
                if(temp_ == null){break;}
                file_paths[file_number] = temp_;
                file_number++;
            }
            if(true){
                String in = "";
                while(!in.equals("exit")){
                    System.out.print(System.getProperty("user.dir")+">>>");
                    in = globalScanner.nextLine();
                    String[] splitcmd = in.split(" ");
                    if(splitcmd[0].equals("open")){
                        try{
                            cmd(file_paths[Integer.parseInt(splitcmd[1])-1]);
                        }catch(NumberFormatException e){
                            int elem_number = 0;
                            boolean found = false;
                            for(String _elem:file_paths){
                                if(splitcmd[1].equals(_elem)){
                                    found = true;
                                    break;
                                }
                                elem_number++;
                            }
                            if(found){cmd(file_paths[elem_number]);}else{System.out.println("File name not found in added file lists");}
                        }
                    }
                    if(splitcmd[0].equals("settings")){
                        changeConf();
                    }
                    if(splitcmd[0].equals("del")){
                        try{
                            String path = file_paths[Integer.parseInt(splitcmd[1])-1];
                            if(confirm("Do you really want to delete the file?")){
                                File file = new File(path);
                                file.delete();
                                file_paths = remove(file_paths,Integer.parseInt(splitcmd[1])-1);
                            }
                        }catch(NumberFormatException e){
                            int elem_number = 0;
                            boolean found = false;
                            for(String _elem:file_paths){
                                if(splitcmd[1].equals(_elem)){
                                    found = true;
                                    break;
                                }
                                elem_number++;
                            }
                            String path = file_paths[elem_number];
                            if(found){
                                if(confirm("Do you really want to delete the file?")){
                                    File file = new File(path);
                                    file.delete();
                                    file_paths = remove(file_paths, elem_number);
                                }
                            }else{
                                System.out.println(path + " not found");
                            }
                        }
                        System.out.println(colors[color] + "Currently opened files:" + colors[0]);
                        int i = 1;
                        for(String _elem:file_paths){
                            System.out.println(i + "[" + _elem + "]");
                            i++;
                        }
                    }
                    if(splitcmd[0].equals("files")){
                        int i = 1;
                        for(String _elem:file_paths){
                            System.out.println(i + "[" + _elem + "]");
                            i++;
                        }
                    }
                    if(splitcmd[0].equals("rm")){
                        if(splitcmd.length > 1){
                            try {
                                file_paths = remove(file_paths, Integer.parseInt(splitcmd[1])-1);
                            } catch (NumberFormatException e) {
                                if(contains(file_paths, splitcmd[1])){
                                    int temp_index = 0;
                                    for(String _elem:file_paths){
                                        if(_elem.equals(splitcmd[1])){
                                            break;
                                        }
                                        temp_index++;
                                    }
                                    file_paths = remove(file_paths, temp_index);
                                }
                            }
                        }
                    }
                    if(splitcmd[0].equals("add")){
                        if(splitcmd.length>1){
                            if(count(splitcmd[0], '.')==0){
                                if(splitcmd[1].equals("*")){
                                    String currentPath = System.getProperty("user.dir");
                                    File folder = new File(currentPath);
                                    File[] listOfFiles = folder.listFiles();
                                    for(int i = 0; i<listOfFiles.length; i++){
                                        if(listOfFiles[i].isFile()){
                                            file_paths = append(file_paths, listOfFiles[i].getName());
                                        }
                                    }
                                }
                                File possible_dir = new File(splitcmd[1]);
                                if(possible_dir.isDirectory()){
                                    File[] listOfFiles = possible_dir.listFiles();
                                    for(int i = 0; i<listOfFiles.length; i++){
                                        if(listOfFiles[i].isFile()){
                                            file_paths = append(file_paths, listOfFiles[i].getName());
                                        }
                                    }
                                }
                            }else{
                                if(checkFile(splitcmd[1])){
                                    file_paths = append(file_paths, splitcmd[1]);
                                }
                            }
                        }
                    }
                }
            }
        }
        globalScanner.close();
    }
}