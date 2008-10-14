package de.st.cs.unisb.javalanche.analyze.tools;

import java.util.Set;

import de.st.cs.unisb.javalanche.results.Mutation;
import de.st.cs.unisb.javalanche.results.persistence.QueryManager;

/**
 * Displays all unit tests that cover a specified mutation.
 *
 * @author David Schuler
 *
 */
public class ShowTestsForMutation {

	public static void main(String[] args) {
		if (args.length > 0) {
			Long l = Long.parseLong(args[0]);
			Mutation mutation = QueryManager.getMutationByID(l);
			Set<String> testsCollectedData = QueryManager
					.getTestsCollectedData(mutation);
			for (String string : testsCollectedData) {
				System.out.println(string);
			}
		}
	}

}