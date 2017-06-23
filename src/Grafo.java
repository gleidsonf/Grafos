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
	int[] ciclo, cicloInicial; // vetor de ciclo
	int tamanhoCiclo;
	double custoCiclo;
	int TAMANHO;
	private BufferedReader read; // Leitura e escrita em arquivos

	/**
	 * Inicializa o array de vertices nao visitados (vnv) e demais variaveis.
	 * 
	 * @param n
	 *            diz respeito ao tamanho do ciclo e serve para montagem da
	 *            matriz de distancia (D)
	 */
	public Grafo(int n) {
		vnv = new ArrayList<>();
		ciclo = new int[n];
		cicloInicial = new int[3];
		D = new double[n][n]; // matriz de distancia
		TAMANHO = n;

		for (int i = 1; i <= TAMANHO; i++) {
			vnv.add(i); // popular os vertices nao visitados
		}

		custoCiclo = 0.0;
		tamanhoCiclo = 0;
	}

	/**
	 * Gera a matriz a partir do arquivo tipo 1
	 * 
	 * @param filename
	 * @throws IOException
	 */

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
						// simetrica
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
		 * Escreve no arquivo matriz_distancia_nome_do_arquivo_.csv
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

	/**
	 * Gera a matriz a partir do arquivo tipo 2
	 * 
	 * @param filename
	 * @throws IOException
	 */
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
		 * Escreve no arquivo matriz_distancia_nome_do_arquivo_.csv
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

	/**
	 * Gera a matriz a partir do arquivo tipo 3, nao simetrica
	 */
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
				}

				line = read.readLine();
				i++;

			} while (line != null);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/**
		 * Escreve no arquivo matriz_distancia_nome_do_arquivo_.csv
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

	/**
	 * Impressao em console
	 */
	public void printCiclo() {
		System.out.print("\n\n( ");
		for (int i = 0; i < tamanhoCiclo;) {
			System.out.print(ciclo[i] + 1);
			if (++i < tamanhoCiclo)
				System.out.print(", ");
		}
		System.out.println(" )\n\n TAMANHO = " + tamanhoCiclo + "\t" + "CUSTO = " + custoCiclo);
	}

	/**
	 * Usado no metodo de insercao do ciclo, atualiza o custo a cada novo ciclo
	 */
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

	/**
	 * Metodo de insercao no vetor de ciclo, recebendo a posicao e seu
	 * respectivo valor
	 */
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

	/**
	 * Gera ciclo incial com 3 vertices aleatorios.
	 */
	public void gerarCicloInicial() {
		Random random = new Random();

		int numero;
		for (int i = 0; i < 3; i++) {
			numero = random.nextInt(vnv.size());
			inserirCiclo(i, vnv.get(numero));
			cicloInicial[i] = vnv.get(numero);
			vnv.remove(numero);
		}
	}

	/**
	 * Gera o conjunto de ciclos
	 */
	public void gerarCiclo() {
		Random random = new Random();

		int numero;
		gerarCicloInicial();

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

			// apos inserir no vetor de ciclo eu retiro o do array de vertices
			// nao visitados
			inserirCiclo(minPosicao, vToAdd);
			vnv.remove(numero);

		}

	}

	/**
	 * Metodo utilizado para obtencao dos resultados, com base em k execucoes
	 */
	public void limparCiclo() {
		ciclo = new int[TAMANHO];
	}

	public static void main(String[] args) throws IOException {

		Grafo grafo;
		double res = 999999, max = 0, med = 0;
		int[] init = new int[3];
		int k = 200;
		long tempo;

		try {
			String filename = "tsp280t2.txt"; // Chamar o arquivo.
			BufferedReader read = new BufferedReader(new FileReader(filename));
			int n, tipo;

			String line = read.readLine();
			String[] temp = line.split("  ");
			n = Integer.parseInt(temp[0].split("=")[1]);
			tipo = Integer.parseInt(temp[1].split("=")[1]);
			System.out.println("N: " + n + " - tipo: " + tipo);
			grafo = new Grafo(n);

			read.close();

			/**
			 * Redireciona o arquivo para seu respectivo metodo de construcao da
			 * matriz
			 */
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

			/**
			 * Gera os resultados pedidos em questao resultado, media, maximo e
			 * o tempo de execucao para obtencao de tais resultados
			 */
			tempo = System.currentTimeMillis(); // Tempo inicial
			for (int i = 0; i < k; i++) {
				grafo.gerarCiclo();

				if (grafo.custoCiclo < res) {
					res = grafo.custoCiclo;
					init = grafo.cicloInicial;
				}
				if (grafo.custoCiclo > max) {
					max = grafo.custoCiclo;
				}

				med += grafo.custoCiclo;

				/**
				 * Operacoes para validar a proxima geracao de ciclo
				 */
				grafo.limparCiclo();
				grafo.vnv.clear();
				for (int j = 1; j <= grafo.TAMANHO; j++) {
					grafo.vnv.add(j);
				}
				grafo.custoCiclo = 0.0;
				grafo.tamanhoCiclo = 0;
			}
			med /= k; // Valor medio
			tempo = System.currentTimeMillis() - tempo; // Tempo final da execucao

			System.out.println("\n\nInicial: " + init[0] + '-' + init[1] + '-' + init[2]);
			System.out.println("Maximo: " + max);
			System.out.println("Media: " + med);
			System.out.println("Resultado: " + res);
			System.out.println("Tempo: " + tempo);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

