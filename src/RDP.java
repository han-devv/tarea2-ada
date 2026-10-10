import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
            System.out.println("-- Menu --");
            System.out.printf("1. %s\n2. %s\n3. %s\n4. %s\n","Cargar datos de .txt", "Asignar valor de ε", "Iniciar el algoritmo de Ramer–Douglas–Peucker", "Salir" );
            System.out.print("-- Ingrese la opción: ");
            Scanner sc = new Scanner(System.in);
            op = sc.nextShort();
            System.out.println();
            switch(op){
                case 1-> {
                    try {
                        boolean flag = true;
                        Path path = Paths.get("Files");
                        File file = new File(path.toAbsolutePath().toString());

                        do {
                            System.out.println();

                            if (file.exists() && file.isDirectory()) {
                                int i = 0;
                                int opt;
                                System.out.println("-- Archivos en el sistema. --");
                                File[] dir = file.listFiles();

                                for (File f : dir) {
                                    i++;
                                    System.out.println(i + ". " + f.getName());
                                }

                                System.out.printf("%s: ", "Ingresa una opcion");
                                opt = sc.nextInt() - 1;

                                if (opt < 0 || opt >= dir.length) {
                                    System.out.println("Ingrese una opción valida...");
                                } else {
                                    data = fc.readData(dir[opt]);

                                    System.out.println("Datos cargados correctamente!");
                                    System.out.println();
                                    flag = false;
                                }
                            } else {
                                System.out.println("Error al buscar la carpeta 'Files'");
                                return;
                            }
                        } while (flag);
                    } catch (IOException e) {
                        System.out.println("Error al leer los datos...");
                        System.out.println();
                    } catch (InputMismatchException e) {
                        System.out.println("OH HELL NAH ");
                        System.out.println();
                        sc.nextLine();
                    } catch (NullPointerException e) {
                        System.out.println("No se encontró el directorio o No existe el archivo seleccionado");
                        System.out.println();
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
                            System.out.println();
                        }else {
                            List<point> input = Arrays.asList(data);
                            List<point> output = alg(input, epsilon);
                            System.out.println("El algoritmo se ejecuto correctamente");
                            System.out.println("Puntos originales: " + data.length);
                            System.out.println("Puntos despues de optimizar: " + output.size());
                        }
                    }catch(Exception e){
                        System.out.println("Error, no se que paso");
                    }
                }

                case 4 -> {}
                default -> System.out.println("Opción invalida");
            }
        }while(op!= 4);
    }

    public static List<point> alg(List<point> points, double epsilon){
        if(points==null) return new ArrayList<>();
        if(points.size()<=2) return new ArrayList<>(points);

        double maxDist = 0.0F;
        int maxI = -1;

        for(int i=0 ; i<points.size()-1 ; i++){
            double dist = distPerpendicular(points.getFirst(), points.getLast(), points.get(i)) ;
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
            res.add(points.getFirst());
            res.add(points.getLast());
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