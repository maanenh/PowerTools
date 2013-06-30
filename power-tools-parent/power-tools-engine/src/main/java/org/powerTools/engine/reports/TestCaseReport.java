package org.powerTools.engine.reports;

import java.io.PrintWriter;
import java.util.Date;


final class TestCaseReport implements TestCaseSubscriber, TestResultSubscriber {
	private class Counters {
		int mNrOfStackTraces;
		int mNrOfErrors;
		int mNrOfWarnings;
		
		void reset () {
			mNrOfStackTraces	= 0;
			mNrOfErrors			= 0;
			mNrOfWarnings		= 0;
		}
	}
	
	private final PrintWriter mWriter;

	private int mCounter;
	private boolean mInTestCase;
	private String mId;
	private Counters mTestCaseCounters;
	private Counters mTestCounters;


	TestCaseReport (PrintWriter writer) {
		mCounter			= 0;
		mInTestCase			= false;
		mTestCaseCounters	= new Counters ();
		mTestCounters		= new Counters ();

		mWriter = writer;
		mWriter.println ("<HTML>");
		mWriter.println ("<HEAD><TITLE>Test case report</TITLE>");
		mWriter.println ("<STYLE type=\"text/css\">");
		mWriter.println ("table { border:1px solid black; border-collapse:collapse; }");
		mWriter.println ("td { border:1px solid black; padding:3px; text-align:right; }");
		mWriter.println ("</STYLE>");
		mWriter.println ("</HEAD>");
		mWriter.println ("<BODY>");
		mWriter.flush ();
	}


	@Override
	public void start (Date dateTime) {
		mWriter.println ("<TABLE>");
		mWriter.println ("<TR><TD>Test case nr</TD><TD>Test case ID</TD><TD>nr of errors</TD><TD>nr of exceptions</TD><TD>nr of warnings</TD></TR>");
		mWriter.flush ();
	}

	@Override
	public void processBegin (String id, String description) {
		mId = id;
		++mCounter;
		mInTestCase = true;
		mTestCaseCounters.reset ();
	}

	@Override
	public boolean processEnd () {
		writeCounters (mCounter, mId, mTestCaseCounters);
		mInTestCase = false;
		return true;
	}

	@Override
	public void processStackTrace (String[] stackTraceLines) {
		if (mInTestCase) {
			++mTestCaseCounters.mNrOfStackTraces;
		} else {
			++mTestCounters.mNrOfStackTraces;
		}
	}

	@Override
	public void processError (String message) {
		if (mInTestCase) {
			++mTestCaseCounters.mNrOfErrors;
		} else {
			++mTestCounters.mNrOfErrors;
		}
	}

	@Override
	public void processWarning (String message) {
		if (mInTestCase) {
			++mTestCaseCounters.mNrOfWarnings;
		} else {
			++mTestCounters.mNrOfWarnings;
		}
	}

	@Override
	public void finish (Date dateTime) {
		writeCounters (0, "outside test cases", mTestCounters);
		mWriter.println ("</TABLE>");
		mWriter.println ("</BODY>");
		mWriter.println ("</HTML>");
		mWriter.close ();
	}

	private void writeCounters (int nr, String id, Counters counters) {
		mWriter.format ("<TR><TD>%d</TD><TD>%s</TD><TD>%d</TD><TD>%d</TD><TD>%d</TD></TR>",
						nr, id, counters.mNrOfErrors, counters.mNrOfStackTraces, counters.mNrOfWarnings).println ();
		mWriter.flush ();
	}


	@Override
	public void processInfo (String message) { }

	@Override
	public void processLink (String message) { }

	@Override
	public void processEndOfTestLine () { }

	@Override
	public void processIncreaseLevel () { }

	@Override
	public void processDecreaseLevel () { }
}