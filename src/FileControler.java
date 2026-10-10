import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileControler {

    point[] readData(File file) throws IOException {
        ArrayList<point> out = new ArrayList<point>();
        Scanner sc = new Scanner(file).useDelimiter("\n");

        while(sc.hasNextLine()){
            String line = sc.nextLine().trim();
            String[] data = line.split(",");
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            out.add(new point(x, y));
        }
        return out.toArray(new point[out.size()]);
    }
}
