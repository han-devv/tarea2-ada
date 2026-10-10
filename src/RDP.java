import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

class RDP{
    double epsilon = 0.0F;
    point[] data;
    FileControler fc = new FileControler();

    public static void main(String[] args){
      new RDP().menu();
    }

    void menu(){
        short op;
        do{
            System.out.println("-- Welcome to the menu --");
            System.out.printf("1. %s\n2. %s\n3. %s\n4. %s\n","Load Data form .txt", "Set ε", "Start the  Ramer–Douglas–Peucker algorithm", "Exit" );
            System.out.print("-- Select an option: ");
            Scanner sc = new Scanner(System.in);
            op = sc.nextShort();
            System.out.println();
            switch(op){
                case 1->{
                    try{
                        boolean flag = true;
                        do {
                            int index;
                            System.out.println();
                            System.out.println("-- Amount of points. --");
                            System.out.printf("1. %s\n 2. %s\n 3. %s\n 4. %s\n 5. %s\n 6. %s\n", "1k points", "5k points", "10k points", "20k points0", "30k points", "50k points");
                            System.out.printf("%s: ", "-- Select an option: ");
                            index = sc.nextInt();

                            switch (index) {
                                case 1 -> {
                                    data = fc.readData(Paths.get("1kdata.txt").toAbsolutePath().toString());
                                    flag = false;
                                }
                                case 2 -> {
                                    data = fc.readData(Paths.get("5kdata.txt").toAbsolutePath().toString());
                                    flag = false;
                                }
                                case 3 -> {
                                    data = fc.readData(Paths.get("10kdata.txt").toAbsolutePath().toString());
                                    flag = false;
                                }
                                case 4 -> {
                                    data = fc.readData(Paths.get("20kdata.txt").toAbsolutePath().toString());
                                    flag = false;
                                }
                                case 5 -> {
                                    data = fc.readData(Paths.get("30kdata.txt").toAbsolutePath().toString());
                                    flag = false;
                                }
                                case 6 -> {
                                    data = fc.readData(Paths.get("50kdata.txt").toAbsolutePath().toString());
                                    flag = false;
                                }
                                default -> {
                                    System.out.println("Please select a valid option..");
                                }
                            }
                        }while(flag);

                        System.out.println("Data loaded successfully!");
                        System.out.println();
                    }catch( IOException e){
                        System.out.println("Error while reading data...");
                        System.out.println();
                    }catch (InputMismatchException e){
                        System.out.println("OH HELL NAH");
                    }
                }

                case 2->{
                    System.out.printf("-- %s: ", "Ingrese el nuevo valor de ε");
                    do {
                        try {
                            epsilon = sc.nextDouble();
                            System.out.println();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Prueba con coma... (0,0)");
                        }
                    }while(true);
                }

                case 3 -> {
                    try{
                        if (data==null || data.length==0){
                            System.out.println("No hay datos");
                            break;
                        }
                        List<point> input = Arrays.asList(data);
                        List<point> output = alg(input, epsilon);
                        System.out.println("El algoritmo se ejecuto correctamente");
                        System.out.println("Puntos originales: "+ data.length);
                        System.out.println("Puntos despues de optimizar: "+ output.size());
                    }catch(Exception e){
                        System.out.println("Error, no se que paso");
                    }
                }

                case 4 -> {continue;}
                default -> System.out.println("Invalid option");
            }
        }while(op!= 4);
    }

    public static List<point> alg(List<point> points, double epsilon){
        if(points==null) return new ArrayList<>();
        if(points.size()<=2) return new ArrayList<>(points);

        double maxDist = 0.0F;
        int maxI = -1;

        for(int i=0 ; i<points.size()-1 ; i++){
            double dist = distPerpendicular(points.get(0), points.get(points.size()-1), points.get(i)) ;
            if(dist>maxDist){
                maxDist=dist;
                maxI = i;
            }
        }
        if(maxDist>epsilon){
            List<point> izq = alg(points.subList(0, maxI+1), epsilon);
            List<point> der = alg(points.subList(maxI, points.size()), epsilon);

            List<point> res = new ArrayList<>(izq);
            res.addAll(der.subList(1,der.size()));
            return res;
        }else{
            List<point> res = new ArrayList<>();
            res.add(points.get(0));
            res.add(points.get(points.size()-1));
            return res;
        }
    }

    private static double distPerpendicular(point p0, point pn, point p ){
        double dx = pn.getX() - p0.getX();
        double dy = pn.getY() - p0.getY();
        double num = Math.abs(dx*(p0.getY()-p.getY()) - dy*(p0.getX()-p.getX()));
        double den = Math.sqrt(dx*dx + dy*dy);
        if(den==0) return p.getDistance(p0);
        return num/den;
    }

}