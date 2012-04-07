package com.jjnford.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shell {
	
	public static enum OUTPUT {
		NONE, STDOUT, STDERR
	};
	
	private static OUTPUT sOStream = OUTPUT.STDOUT;
	
	private static String sShell;
	private static final String EOL = System.getProperty("line.separator");
	private static final String EXIT = "exit" + Shell.EOL;

	/**
	 * Used to buffer shell output off of the main thread.
	 * 
	 * @author JJ Ford
	 *
	 */
	private static class Buffer extends Thread {
		private InputStream mInputStream;
		private StringBuffer mBuffer;
		
		/**
		 * @param inputStream Data stream to get shell output from.
		 */
		public Buffer(InputStream inputStream) {
			mInputStream = inputStream;
			mBuffer = new StringBuffer();
			this.start();
		}
		
		public String getOutput() {
			return mBuffer.toString();
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));
				if((line = reader.readLine()) != null) {
					mBuffer.append(line);
					while((line = reader.readLine()) != null) {
						mBuffer.append(Shell.EOL).append(line);
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Block instantiation of this object.
	 */
	private Shell() {}
	
	/**
	 * Gets the buffer for the shell output stream that is currently set.
	 * 
	 * @param proc Process running the shell command.
	 * @return The buffer containing the shell output stream, NULL is none.
	 */
	private static Buffer getBuffer(Process proc) {
		Buffer buffer = null;
		switch(sOStream) {
			case NONE:
				new Buffer(proc.getInputStream());
				new Buffer(proc.getErrorStream());
				break;
			case STDOUT:
				buffer = new Buffer(proc.getInputStream());
				new Buffer(proc.getErrorStream());
				break;
			case STDERR:
				buffer = new Buffer(proc.getErrorStream());
				new Buffer(proc.getInputStream());
				break;
			default:
				return buffer;
		}
		return buffer;
	}
	
	/* 
	 * API
	 * 
	 * 
	 * 
	 */
	
	/**
	 * Sets the shell's {@link Shell.OUTPUT output stream}.  Default value is STDOUT.
	 * 
	 * @param ostream
	 */
	public void setOutputStream(Shell.OUTPUT ostream) {
		sOStream = ostream;
	}
	
	/**
	 * Sets the su shell to be used.
	 * 
	 * @param shell The shell to be used for sudo.
	 */
	public void setShell(String shell) {
		sShell = shell;
	}
	
	/**
	 * Gains privileges to root shell.  Device must be rooted to use.
	 * 
	 * @return True if root shell is obtained, false if not.
	 */
	public static boolean su() {
		return false;
	}
	
	/**
	 * Executes a command in the root shell.  Devices must be rooted to use.
	 * 
	 * @param cmd The command to execute in root shell.
	 * @return Output of the command, null if there is no output.
	 */
	public static String sudo(String cmd) {
		return null;
	}
	
	/**
	 * Executes a native shell command.
	 * 
	 * @param cmd The command to execute in the native shell.
	 * @return Output of the command, null if there is no output.
	 */
	public static String exec(String cmd) {
		return null;
	}
}
