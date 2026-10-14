import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class RDP{
    double epsilon = 0.0F;
    point[] data;
    FileControler fc = new FileControler();
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args){
      new RDP().menu();
    }

    void menu(){
        short op;
        do{
            System.out.println("-- Menu --");
            System.out.printf("1. %s\n2. %s\n3. %s\n4. %s\n5. %s\n","Cargar datos de archivo", "Asignar valor de ε", "Iniciar el algoritmo de Ramer–Douglas–Peucker","Realizar Simulación", "Salir" );
            System.out.print("-- Ingrese la opción: ");
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
                    do {
                        System.out.printf("-- %s: ", "Ingrese el nuevo valor de ε");
                        try {
                            epsilon = sc.nextDouble();
                            System.out.println();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Entrada invalida... *Ejemplo de entrada valida: 0,01");
                            sc.nextLine();
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

                case 4 -> {
                    menuSim();
                }
                case 5 -> {}
                default -> System.out.println("Opción invalida");
            }
        }while(op!= 4);
    }

    void menuSim(){
        int op = 0;
        File[] archivos = null;
        double[] epsilons = null;

        do{
            System.out.println("-- Menu simulacion --");
            System.out.printf("1. %s\n2. %s\n3. %s\n4. %s\n", "Selección de archivos",
                    "Seleccionar valores de ε", "Iniciar simulación", "Salir");
            System.out.print("-- Ingrese la opción: ");
            try {
                op = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ingrese un número válido...");
                sc.nextLine();
                op = 0;
                continue;
            }
            System.out.println();

            switch(op){
                case 1 -> {
                    File folder = new File(Paths.get("Files").toAbsolutePath().toString());
                    File[] dir = folder.listFiles();

                    if (!folder.isDirectory() || dir == null || dir.length == 0) {
                        System.out.println("Error al buscar la carpeta 'Files' o está vacía");
                    } else {
                        Arrays.sort(dir);
                        System.out.println("-- Archivos en el sistema. --");
                        for (int i = 0; i < dir.length; i++) {
                            System.out.println((i + 1) + ". " + dir[i].getName());
                        }

                        System.out.print("Formato: 1-2-3-...-n\nIngrese los archivos para simular: ");
                        String[] temp = sc.next().split("-");

                        List<File> sel = new ArrayList<>();
                        for (String t : temp) {
                            try {
                                int ind = Integer.parseInt(t.trim()) - 1;
                                if (ind < 0 || ind >= dir.length) {
                                    System.out.println("Opción inválida ignorada: " + t);
                                } else if (!sel.contains(dir[ind])) {
                                    sel.add(dir[ind]);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Valor inválido ignorado: " + t);
                            }
                        }
                        archivos = sel.toArray(new File[0]);
                        System.out.println("Archivos seleccionados: " + archivos.length);
                    }
                    System.out.println();
                }

                case 2 -> {
                    System.out.println("Formato: 0,01-0,1-1-2-...-n");
                    System.out.print("Ingrese los valores de ε para simular: ");
                    String[] temp = sc.next().split("-");

                    List<Double> sel = new ArrayList<>();
                    for (String t : temp) {
                        try {
                            double e = Double.parseDouble(t.trim().replace(',', '.'));
                            if (e < 0) System.out.println("ε debe ser positivo, se ignora: " + t);
                            else sel.add(e);
                        } catch (NumberFormatException ex) {
                            System.out.println("Valor inválido ignorado: " + t);
                        }
                    }
                    epsilons = sel.stream().mapToDouble(Double::doubleValue).toArray();
                    System.out.println("Valores de ε seleccionados: " + epsilons.length);
                    System.out.println();
                }

                case 3 -> {
                    if (archivos == null || archivos.length == 0) {
                        System.out.println("Primero seleccione archivos (opción 1)");
                    } else if (epsilons == null || epsilons.length == 0) {
                        System.out.println("Primero seleccione valores de ε (opción 2)");
                    } else {
                        System.out.printf("%-20s %8s %12s %13s %12s %14s%n",
                                "Archivo", "ε", "Originales", "Optimizados", "Eliminados", "Tiempo (ms)");

                        for (File f : archivos) {
                            try {
                                point[] datos = fc.readData(f);
                                if (datos == null || datos.length == 0) {
                                    System.out.println(f.getName() + ": sin datos");
                                    continue;
                                }
                                List<point> input = Arrays.asList(datos);

                                for (double eps : epsilons) {
                                    long ini = System.nanoTime();
                                    List<point> out = alg(input, eps);
                                    long fin = System.nanoTime();

                                    int eliminados = datos.length - out.size();
                                    double ms = (fin - ini) / 1_000_000.0;

                                    System.out.printf("%-20s %8.4f %12d %13d %12d %14.3f%n",
                                            f.getName(), eps, datos.length, out.size(), eliminados, ms);
                                }
                            } catch (IOException e) {
                                System.out.println("Error al leer el archivo " + f.getName());
                            }
                        }
                    }
                    System.out.println();
                }

                case 4 -> {}
                default -> System.out.println("Opción inválida");
            }
        } while (op != 4);
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