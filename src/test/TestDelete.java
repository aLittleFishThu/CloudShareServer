package test;

import java.io.File;

public class TestDelete {
    public static void main(String args[]){
        File file=new File("C:\\Users\\yzj\\Desktop\\2ef8b1a4-21cc-4890-8a35-d3a43528010e.dat");
        System.out.println(file.delete());
    }
}
