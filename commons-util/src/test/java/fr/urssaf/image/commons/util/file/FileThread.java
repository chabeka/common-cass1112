package fr.urssaf.image.commons.util.file;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
final class FileThread extends Thread {

	private final int numero;

	private static final Logger LOGGER = Logger.getLogger(FileThread.class);

	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss", Locale.getDefault());

	private static final String FILE = "src/test/resources/file/file_lock.txt";

	private final LockFile file;

	private static final int THREAD = 3;

	private static final int CYCLE = 5;

	private static final int PAUSE = 100;


	public static void main(String args[]) throws IOException {
	   LockFile file = null;
	   
		file = new LockFile(FILE);
		 LOGGER.info(SystemUtils.getJavaIoTmpDir());

		for (int i = 0; i < THREAD; i++) {
			FileThread thread = new FileThread(file, i);
			thread.start();
		}
	}

	public FileThread(LockFile file, int numero) {
		super();
		this.file = file;
		this.numero = numero;
	}

	@Override
	public void run() {
		try {
			this.executer();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void executer() throws IOException {
		LOGGER.info("DEBUT DE N°" + numero);
		
		final int randomBound = 10;
		
		for (int i = 0; i < CYCLE; i++) {

			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(randomBound);
			try {

				Thread.sleep(PAUSE * randomInt);

			} catch (InterruptedException ie) {
			}

			LOGGER.debug("n°" + numero + " attend pour écrire");
			file.write(dateFormat.format(new Date()) + " thread n°" + numero
					+ " écrit la ligne n°" + i + "\n");
			LOGGER.debug("n°" + numero + " a fini d'écrire");

			randomGenerator = new Random();
			randomInt = randomGenerator.nextInt(randomBound);
			try {

				Thread.sleep(PAUSE * randomInt);

			} catch (InterruptedException ie) {
			}

			LOGGER.debug("n°" + numero + " attend pour lire");
			file.read();
			LOGGER.debug("n°" + numero + " a fini de lire");

		}

		LOGGER.info("FIN DE N°" + numero);

	}

}
