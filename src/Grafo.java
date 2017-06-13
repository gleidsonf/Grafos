import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.opencsv.CSVWriter;

public class Grafo {
	double[][] D; // Matriz de Distancia
	ArrayList<Integer> vnv; // Lista dos vertices nao visitados
	int[] ciclo; // vetor de ciclo
	int tamanhoCiclo;
	double custoCiclo;
	int TAMANHO;
	private BufferedReader read; // Leitura e escrita em arquivos

	public Grafo(int n) {
		vnv = new ArrayList<>();
		ciclo = new int[n];
		D = new double[n][n];
		TAMANHO = n;

		for (int i = 1; i <= TAMANHO; i++) {
			vnv.add(i);
		}

		custoCiclo = 0.0;
		tamanhoCiclo = 0;
	}

	public void gerarMatrizTipo1(String filename) throws IOException {

		String[] aux = new String[TAMANHO];
		try {
			read = new BufferedReader(new FileReader(filename));

			String line = read.readLine();
			line = read.readLine();
			System.out.println(D.length);
			double d;

			int i = 0;
			do {
				aux = line.split(" ");

				for (int j = i + 1; j < TAMANHO;) {
					for (int j2 = 0; j2 < aux.length; j2++) {
						d = Double.parseDouble(aux[j2]);
						D[i][j] = d;
						D[j][i] = d;
						j++;
					}

					if ((j - i) > aux.length) {
						break;
					}

				}

				line = read.readLine();
				i++;

			} while (line != null);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/**
		 * Escreve no arquivo .csv
		 */
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("matriz_distancia_" + filename + ".csv"));

			String[] entrada = new String[TAMANHO];
			for (int i = 0; i < TAMANHO; i++) {

				for (int j = 0; j < TAMANHO; j++) {
					entrada[j] = "";
					entrada[j] += D[i][j];
				}
				writer.writeNext(entrada);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void gerarMatrizTipo2(String filename) throws IOException {

		Ponto[] pontos = new Ponto[TAMANHO];
		try {
			read = new BufferedReader(new FileReader(filename));

			String line = read.readLine();
			line = read.readLine();

			int i = 0;

			do {
				String[] s = line.split(" ");
				pontos[i] = new Ponto(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
				line = read.readLine();
				i++;

			} while (line != null);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		double d, xi, xj, yi, yj;

		for (int i = 0; i < TAMANHO; i++) {
			xi = pontos[i].getX();
			yi = pontos[i].getY();
			for (int j = 0; j < TAMANHO; j++) {
				xj = pontos[j].getX();
				yj = pontos[j].getY();

				d = ((xi - xj) * (xi - xj)) + ((yi - yj) * (yi - yj));
				d = Math.sqrt(d);
				D[i][j] = d;
				D[j][i] = d;
			}
		}

		/**
		 * Escreve no arquivo .csv
		 */
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("matriz_distancia" + filename + ".csv"));

			String[] entrada = new String[TAMANHO];
			for (int i = 0; i < TAMANHO; i++) {

				for (int j = 0; j < TAMANHO; j++) {
					entrada[j] = "";
					entrada[j] += D[i][j];
				}
				writer.writeNext(entrada);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void gerarMatrizTipo3(String filename) throws IOException {

		String[] aux = new String[TAMANHO];
		try {
			read = new BufferedReader(new FileReader(filename));

			String line = read.readLine();
			line = read.readLine();
			System.out.println(D.length);
			double d;

			int i = 0;
			do {

				aux = line.split(" ");

				for (int j = 0; j < TAMANHO; j++) {
					d = Double.parseDouble(aux[j]);
					D[i][j] = d;
					D[j][i] = d;
				}

				line = read.readLine();
				i++;

			} while (line != null);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/**
		 * Escreve no arquivo .csv
		 */
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("matriz_distancia_" + filename + ".csv"));

			String[] entrada = new String[TAMANHO];
			for (int i = 0; i < TAMANHO; i++) {

				for (int j = 0; j < TAMANHO; j++) {
					entrada[j] = "";
					entrada[j] += D[i][j];
				}
				writer.writeNext(entrada);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void printCiclo() {
		System.out.print("\n\n( ");
		for (int i = 0; i < tamanhoCiclo;) {
			System.out.print(ciclo[i] + 1);
			if (++i < tamanhoCiclo)
				System.out.print(", ");
		}
		System.out.println(" )\n\n TAMANHO = " + tamanhoCiclo + "\t" + "CUSTO = " + custoCiclo);
	}

	public void atualizarCusto() {

		custoCiclo = 0;

		if (tamanhoCiclo > 2) {
			for (int i = 0; i < tamanhoCiclo - 1; i++) {
				int c1 = ciclo[i];
				int c2 = ciclo[i + 1];
				double dzao = D[c1][c2];
				custoCiclo += dzao;
			}
			custoCiclo += D[ciclo[tamanhoCiclo - 1]][ciclo[0]];

		}

		printCiclo();
	}

	public void inserirCiclo(int posicao, int elemento) {
		int i = tamanhoCiclo - 1;
		while (i >= posicao) {
			ciclo[i + 1] = ciclo[i];
			i--;
		}

		tamanhoCiclo++;
		ciclo[posicao] = elemento - 1;
		atualizarCusto();

	}

	public void gerarCiclo() {
		Random random = new Random();

		/**
		 * Gera ciclo incial com 3 vertices aleatorios.
		 */
		int numero;
		for (int i = 0; i < 3; i++) {
			numero = random.nextInt(vnv.size());
			inserirCiclo(i, vnv.get(numero));
			vnv.remove(numero);
			printCiclo();

		}

		/**
		 * Gera o restante do ciclo
		 */
		int vToAdd;
		while (vnv.size() > 0) {
			numero = random.nextInt(vnv.size());
			vToAdd = vnv.get(numero);
			double minCiclo = 99999999, auxCiclo;
			int minPosicao = 0;

			for (int j = 0; j < tamanhoCiclo - 1; j++) {
				auxCiclo = custoCiclo - D[ciclo[j]][ciclo[j + 1]] + D[ciclo[j]][vToAdd - 1]
						+ D[vToAdd - 1][ciclo[j + 1]];
				if (auxCiclo < minCiclo) {
					minCiclo = auxCiclo;
					minPosicao = j;
				}
			}

			inserirCiclo(minPosicao, vToAdd);

			vnv.remove(numero);

		}

	}

	public static void main(String[] args) throws IOException {

		Grafo grafo;

		try {
			String filename = "tsp10t3.txt"; // Chamar o arquivo.
			BufferedReader read = new BufferedReader(new FileReader(filename));
			int n, tipo;

			String line = read.readLine();
			String[] temp = line.split("  ");
			n = Integer.parseInt(temp[0].split("=")[1]);
			tipo = Integer.parseInt(temp[1].split("=")[1]);
			System.out.println("N: " + n + " - tipo: " + tipo);
			grafo = new Grafo(n);

			read.close();
			switch (tipo) {
			case 1:
				grafo.gerarMatrizTipo1(filename);
				break;

			case 2:
				grafo.gerarMatrizTipo2(filename);
				break;
			case 3:
				grafo.gerarMatrizTipo3(filename);
				break;

			default:
				break;
			}

			grafo.gerarCiclo();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
