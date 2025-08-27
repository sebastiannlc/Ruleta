import java.util.Random;
import java.util.Scanner;

public class Ruleta {

    public static final int MAX_HISTORIAL = 100;
    public static int[] historialNumeros = new int[MAX_HISTORIAL];
    public static int[] historialApuestas = new int[MAX_HISTORIAL];
    public static boolean[] historialAciertos = new boolean[MAX_HISTORIAL];
    public static int historialSize = 0;
    public static Random rng = new Random();
    public static int[] numerosRojos =
            {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};

    /**
     * Metodo principal: inicia el programa llamando al menú.
     */
    public static void main(String[] args) {
        menu();
    }

    /**
     * Controla el flujo principal del programa mostrando un menú en consola.
     */
    public static void menu() {
        Scanner in = new Scanner(System.in);
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion(in);
            ejecutarOpcion(opcion, in);
        } while (opcion != 3);
        System.out.println("Saliendo del programa. ¡Hasta la próxima!");
    }

    /**
     * Muestra en consola las opciones disponibles del menú.
     */
    public static void mostrarMenu() {
        System.out.println("\n-- Menu de Ruleta --");
        System.out.println("1. Iniciar ronda.");
        System.out.println("2. Ver estadistica.");
        System.out.println("3. Salir");
    }

    /**
     * Lee la opción elegida por el usuario desde teclado.
     *
     * @param in Scanner para entrada por consola.
     * @return número de opción ingresado.
     */
    public static int leerOpcion(Scanner in) {
        System.out.print("Ingrese una opcion: ");
        while (!in.hasNextInt()) {
            System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            in.next(); // limpiar el buffer del scanner
            System.out.print("Ingrese una opcion: ");
        }
        return in.nextInt();
    }

    /**
     * Ejecuta la acción correspondiente a la opción del menú.
     *
     * @param opcion opción elegida por el usuario.
     * @param in     Scanner para entrada por consola.
     */
    public static void ejecutarOpcion(int opcion, Scanner in) {
        switch (opcion) {
            case 1:
                iniciarRonda(in);
                break;
            case 2:
                mostrarEstadisticas();
                break;
            case 3:
                break;
            default:
                System.out.println("Opcion no valida. Por favor, intente de nuevo.");
                break;
        }
    }

    /**
     * Inicia una ronda de la ruleta: leer apuesta, girar, evaluar y mostrar resultado.
     *
     * @param in Scanner para entrada por consola.
     */
    public static void iniciarRonda(Scanner in) {
        char tipoApuesta = leerTipoApuesta(in);
        System.out.print("Ingrese el monto a apostar: ");
        int monto = in.nextInt();
        int numeroRuleta = girarRuleta();
        boolean acierto = evaluarResultado(numeroRuleta, tipoApuesta);
        registrarResultado(numeroRuleta, monto, acierto);
        mostrarResultado(numeroRuleta, monto, acierto);
    }

    /**
     * Permite al usuario seleccionar el tipo de apuesta (R/N/P/I).
     *
     * @param in Scanner para entrada por consola.
     * @return el tipo de apuesta elegido.
     */
    public static char leerTipoApuesta(Scanner in) {
        char tipo;
        do {
            System.out.print("Ingrese el tipo de apuesta (R/N/P/I): ");
            String input = in.next().toUpperCase();
            tipo = input.charAt(0);
        } while (tipo != 'R' && tipo != 'N' && tipo != 'P' && tipo != 'I');
        return tipo;
    }

    /**
     * Simula el giro de la ruleta generando un número aleatorio de 0 a 36.
     *
     * @return número de la ruleta.
     */
    public static int girarRuleta() {
        return rng.nextInt(37); // Genera un numero entre 0 y 36
    }

    /**
     * Evalúa si la apuesta realizada por el jugador fue acertada.
     *
     * @param numero número obtenido en la ruleta.
     * @param tipo   tipo de apuesta elegida.
     * @return true si acertó, false si perdió.
     */
    public static boolean evaluarResultado(int numero, char tipo) {
        if (numero == 0) {
            return false;
        }
        boolean esPar = numero % 2 == 0;
        boolean esRojo = esRojo(numero);
        return switch (tipo) {
            case 'R' -> esRojo;
            case 'N' -> !esRojo;
            case 'P' -> esPar;
            case 'I' -> !esPar;
            default -> false;
        };
    }

    /**
     * Determina si un número corresponde a color rojo.
     *
     * @param n número de la ruleta.
     * @return true si es rojo, false en caso contrario.
     */
    public static boolean esRojo(int n) {
        for (int i : numerosRojos) {
            if (i == n) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registra los resultados de la ronda en los arreglos de historial.
     *
     * @param numero  número obtenido en la ruleta.
     * @param apuesta monto apostado.
     * @param acierto si el jugador acertó o no.
     */
    public static void registrarResultado(int numero, int apuesta, boolean acierto) {
        if (historialSize < MAX_HISTORIAL) {
            historialNumeros[historialSize] = numero;
            historialApuestas[historialSize] = apuesta;
            historialAciertos[historialSize] = acierto;
            historialSize++;
        } else {
            System.out.println("Historial de apuestas lleno.");
        }
    }

    /**
     * Muestra en consola el resultado de la ronda.
     *
     * @param numero  número obtenido en la ruleta.
     * @param monto   monto apostado.
     * @param acierto si el jugador ganó o perdió.
     */
    public static void mostrarResultado(int numero, int monto, boolean acierto) {
        System.out.println("La ruleta se detuvo en el numero: " + numero);
        if (acierto) {
            System.out.println("¡Felicidades! Ganaste " + monto + ".");
        } else {
            System.out.println("Lo siento, perdiste " + monto + ".");
        }
    }

    /**
     * Muestra estadísticas generales de todas las rondas jugadas.
     */
    public static void mostrarEstadisticas() {
        if (historialSize == 0) {
            System.out.println("No se han jugado rondas todavia.");
            return;
        }
        int totalApostado = 0;
        int totalAciertos = 0;
        int gananciaPerdidaNeta = 0;
        for (int i = 0; i < historialSize; i++) {
            totalApostado += historialApuestas[i];
            if (historialAciertos[i]) {
                totalAciertos++;
                gananciaPerdidaNeta += historialApuestas[i];
            } else {
                gananciaPerdidaNeta -= historialApuestas[i];
            }
        }
        double porcentajeAcierto = (double) totalAciertos / historialSize * 100;
        System.out.println("\n--- Estadisticas ---");
        System.out.println("Cantidad de rondas jugadas: " + historialSize);
        System.out.println("Total apostado: " + totalApostado);
        System.out.println("Total de aciertos: " + totalAciertos);
        System.out.printf("%% de acierto: %.2f%%\n", porcentajeAcierto);
        System.out.println("Ganancia/Perdida neta: " + gananciaPerdidaNeta);
    }
}