/*
 * Copyright (C) 2011 Saarland University
 * 
 * This file is part of Javalanche.
 * 
 * Javalanche is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Javalanche is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License
 * along with Javalanche.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.unisb.cs.st.javalanche.mutation.runtime.testDriver.junit;

//import java.lang.adabu2.Tracer;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import de.unisb.cs.st.ds.util.Util;
import de.unisb.cs.st.javalanche.mutation.properties.ConfigurationLocator;
import de.unisb.cs.st.javalanche.mutation.properties.JavalancheConfiguration;
import de.unisb.cs.st.javalanche.mutation.properties.PropertyUtil;
import de.unisb.cs.st.javalanche.mutation.properties.RunMode;
import de.unisb.cs.st.javalanche.mutation.runtime.testDriver.MutationTestDriver;

@RunWith(JavalancheWrapperTestSuite.class)
public class JavalancheWrapperTestSuite extends Runner {

	private Runner r;

	public JavalancheWrapperTestSuite(Class<?> c) {
	}

	@Test
	public void testMethod() {
		System.out.println("JavalancheWrapperTestSuite.testMethod()");
	}

	@Override
	public Description getDescription() {
		return getRunner().getDescription();
	}

	private Runner getRunner() {
		if (r == null) {
			try {
				r = Junit4Util.getRunner();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InitializationError e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	@Override
	public void run(RunNotifier notifier) {
		JavalancheConfiguration config = ConfigurationLocator
				.getJavalancheConfiguration();
		RunMode runMode = config.getRunMode();
		if (runMode == RunMode.OFF) {
			Runner runner = getRunner();
			Result r = new Result();
			RunListener listener = r.createListener();
			notifier.addListener(listener);
			runner.run(notifier);
			System.out.println("Tests run: " + r.getRunCount()
					+ "\nFailing Tests: " + r.getFailureCount());

		} else {
			MutationTestDriver driver = null;
			if (config.useJunit3Runner()) {
				System.out.println("Using Junit3 Mutation Test Driver");
				driver = new Junit3MutationTestDriver();
			} else {
				driver = new Junit4MutationTestDriver();
			}
			// addTraceListener(notifier);
			driver.run();
			// Runner runner = getRunner();
			// runner.run(notifier);

		}
	}

	private void addTraceListener(RunNotifier notifier) {
		String property = System.getProperty("javalanche.trace");
		if (property != null && property.equals("true")) {
			notifier.addListener(new RunListener() {
				@Override
				public void testStarted(Description description)
						throws Exception {
					// Tracer.setNewMethod(true);
					super.testStarted(description);

				}

				@Override
				public void testRunStarted(Description description)
						throws Exception {
					super.testRunStarted(description);
				}

				@Override
				public void testRunFinished(Result result) throws Exception {
					// Tracer.setTestEnd(true);
					super.testRunFinished(result);
				}
			});
		}
	}
}
