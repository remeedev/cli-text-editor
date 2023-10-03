import java.util.*;
import java.io.*;
import java.lang.*;

class textEditor{
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
    //Function to check if file already exists
    public static boolean checkFile(String path){
        File file = new File(path);
        if(!file.exists()){
            System.out.print("File doesn't currently exists, do you want to create it? (Y/N)");
            boolean ans = globalScanner.nextLine().toLowerCase().equals("y");
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
    // Function that reads file
    public static void readFile(String path,Integer start, Integer end){
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
                end = i;
            }
            reader = new Scanner(file);
            int index = 0;
            while(reader.hasNextLine() && index>= start && index<=end){
                String data = reader.nextLine();
                System.out.print((index+1) + "| ");
                System.out.println(data);
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
        int index = 1;
        for(String elem:list){
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
        System.out.println(line+"| "+lines[line-1]);
        System.out.print(line+"| ");
        String change = globalScanner.nextLine();
        lines[line-1] = change;
        try{
            FileWriter writer = new FileWriter(file);
            writer.write(join(lines, "\n"));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
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
    public static void write(String path){
        int blankCounter = 0;
        String[] content = {};
        while(blankCounter < 2){
            System.out.print(content.length+1 + "| ");
            String data = globalScanner.nextLine();
            if(data.equals("")){
                blankCounter++;
            }
            content = append(content, data);
        }
        try{
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            writer.write(join(content, "\n"));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    // Function to process all commands
    public static Integer cmd(String path){
        String[] quitCommands = {"quit", "exit", "xt", "qu"};
        String command = "";
        System.out.print("> ");
        command = globalScanner.nextLine();
        if(contains(quitCommands, command)){
            return 0;
        }
        String[] args = command.split(" ");
        if(args.length>0){
            if (args[0].equals("read")){
                cls();
                readFile(path, null, null);
            }
            if(args[0].equals("write")){
                write(path);
            }
        }
        if(args.length>1){
            if(args[0].equals("edit")){
                cls();
                edit(path, Integer.parseInt(args[1]));
            }
        }
        cmd(path);
        return 1;
    }

    public static void main(String[] args){
        if(args.length>0){
            if(args[0].contains(".")){
                if(count(args[0], '.')==1){
                    if(checkFile(args[0])){
                        readFile(args[0], null, null);
                        cmd(args[0]);
                    }
                }
            }
        }
        globalScanner.close();
    }
}