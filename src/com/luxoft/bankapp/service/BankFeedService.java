package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.FeedException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.validator.Validator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankFeedService {

	private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + BankFeedService.class.getName());
	private final Bank activeBank;

	public BankFeedService(Bank activeBank) {
		this.activeBank = activeBank;
	}

	public void loadFeed() {
		File dir = new File("src/resources/files/feed");
		for (File file : dir.listFiles()) {
			try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
				proceedFile(file, reader);
			} catch (IOException e) {
				EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private void proceedFile(File file, LineNumberReader reader) throws IOException {
		String currentLine;
		while ((currentLine = reader.readLine()) != null) {
			proceedLine(currentLine, file, reader);
		}
	}

	private void proceedLine(String currentLine, File file, LineNumberReader reader) {
		List<String> attributes = new ArrayList<>(Arrays.asList(currentLine.split(";")));

		Map<String, String> splittedAttributes = splitAttributes(attributes);

		try {
			putLine(file, reader, splittedAttributes);
		} catch (FeedException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Map<String, String> splitAttributes(List<String> attributes) {
		Map<String, String> splittedAttributes = new HashMap<>();
		for (String string : attributes) {
			List<String> attribute = new ArrayList<>(Arrays.asList(string.split("=")));
			if (attribute.size() == 2) {
				splittedAttributes.put(attribute.get(0), attribute.get(1));
			}
		}
		return splittedAttributes;
	}

	private void putLine(File file, LineNumberReader reader, Map<String, String> splittedAttributes) {
		if (Validator.feedValidator(splittedAttributes)) {
			activeBank.parseFeed(splittedAttributes);
		} else {
			throw new FeedException("Incorrect entry in feed file: " + file
					+ " in line " + reader.getLineNumber() + ".");
		}
	}
}