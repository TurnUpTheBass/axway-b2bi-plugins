/**==============================================================================
 * Program/Module :     PluggableSCPTransport.java
 * Description :       	Pluggable transport that implements SCP
 * Supported Products :	B2Bi 2.x
 * Author :				Bas van den Berg
 * Copyright :          Axway
 *==============================================================================
 * HISTORY
 * 20180604 bvandenberg	1.0.0	Updates for publication and pickup implementation
 * 
 * http://www.jcraft.com/jsch/
*==============================================================================*/

package com.axway.gps;

import com.cyclonecommerce.collaboration.MetadataDictionary;
import com.cyclonecommerce.tradingengine.transport.UnableToConnectException;
import com.cyclonecommerce.tradingengine.transport.UnableToAuthenticateException;
import com.cyclonecommerce.tradingengine.transport.UnableToConsumeException;
import com.cyclonecommerce.tradingengine.transport.UnableToProduceException;
import com.cyclonecommerce.tradingengine.transport.FileNotFoundException;
import com.cyclonecommerce.tradingengine.transport.UnableToDeleteException;
import com.cyclonecommerce.tradingengine.transport.UnableToDisconnectException;
import com.cyclonecommerce.tradingengine.transport.TransportTestException;
import com.cyclonecommerce.tradingengine.transport.TransportInitializationException;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableClient;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableSettings;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableException;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableMessage;
import com.cyclonecommerce.util.VirtualData;
import com.cyclonecommerce.util.VirtualDataInputStream;
import com.jcraft.jsch.*;


import util.pattern.PatternKeyValidator;
import util.pattern.PatternKeyValidatorFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;



public class PluggableSCPTransport implements PluggableClient {
	
	String _PGMNAME = com.axway.gps.PluggableSCPTransport.class.getName();
	String _PGMVERSION = "1.0.0";
	
	private static final String SETTING_HOST = "Hostname";
	private static final String SETTING_PORT = "Port";
	private static final String SETTING_DIRECTORY = "Folder";
	private static final String SETTING_USERNAME = "Username";
	private static final String SETTING_PASSWORD = "Password";
	private static final String SETTING_PICKUP_PATTERN = "Filter";
	private static final String SETTING_PATTERN_TYPE = "Filter Type";	
	private static final String SETTING_HOSTKEYVERIFICATION = "HostKeyVerification";
	private static final String SETTING_HOSTKEY = "HostKey";

	// Setting to distinguish pickup and delivery mode
	private static final String SETTING_EXCHANGE_TYPE = "Exchange Type";

	
	// this is how you get the log4J logger instance for this class
	private static Logger logger = Logger.getLogger(com.axway.gps.PluggableSCPTransport.class.getName());
	
	
	// Stores the settings from the UI
	private String _exchangeType;
	private String _host;
	private int    _port;
	private String _directory;
	private String _username;
	private String _password;
	private String _filter;
	private String _filtertype;	
	private boolean _hostKeyVerification;
	private String _hostKey;
	
    
    static JSch 		jschSCP = null;
	static Session 		SCPSession;
	static int 			intTimeOut = 60000;
    static HostKey 		hostKey = null;

	// Map containing constant settings from pluggabletransport.xml
	private Map<String,String> constantProperties = null;
	

	public PluggableSCPTransport() {
		
		// Set a default logger level
		if(logger.getLevel() == null) {
			logger.setLevel(Level.INFO);
		}
		logger.info(String.format("Executing PluggableTransport: %s version: %s",_PGMNAME,_PGMVERSION));
	}

	public void init(PluggableSettings pluggableSettings) throws TransportInitializationException {
		
		try {

			// Get all constant settings from the pluggabletransport.xml file and store them in the local map for later use
			constantProperties = new HashMap<String,String>(pluggableSettings.getConstantSettings());
			if (constantProperties != null && !constantProperties.isEmpty()) {
				Iterator<String> i = constantProperties.keySet().iterator();
				while (i.hasNext()) {
					String key = (String) i.next();
					logger.debug("Constant setting " + key + "=" + constantProperties.get(key));
				}
			}

			_exchangeType = pluggableSettings.getConstantSetting(SETTING_EXCHANGE_TYPE);
			
			// Get all settings defined in the GUI for each pluggable transport defined
			
			_host = pluggableSettings.getSetting(SETTING_HOST);
			_port = Integer.parseInt(pluggableSettings.getSetting(SETTING_PORT));
			_directory = pluggableSettings.getSetting(SETTING_DIRECTORY);
			_username = pluggableSettings.getSetting(SETTING_USERNAME);
			_password = pluggableSettings.getSetting(SETTING_PASSWORD);
			_hostKey = pluggableSettings.getSetting(SETTING_HOSTKEY);
			
			if (_exchangeType.equals("pickup")) {
				_filtertype = pluggableSettings.getSetting(SETTING_PATTERN_TYPE);
				_filter = pluggableSettings.getSetting(SETTING_PICKUP_PATTERN);
				
			}
			
			
			if(pluggableSettings.getSetting(SETTING_HOSTKEYVERIFICATION).equalsIgnoreCase("True")) {
				_hostKeyVerification = true;
				// parse the key
				byte [] key = Base64.getDecoder().decode ( _hostKey ); // Java 8 Base64 - or any other
				hostKey = new HostKey ( _host , key );
			} else {
				_hostKeyVerification = false;
			}
			
		} catch (PluggableException | JSchException e) {
			throw new TransportInitializationException("Error getting settings", e);
		}
	}


	public void connect() throws UnableToConnectException {
		logger.debug("Connecting to SCP server at: " + _host + ":" + _port + " using SSH2");
			
		try {

			jschSCP = new JSch();
			jschSCP.setKnownHosts("");
			SCPSession = jschSCP.getSession(_username, _host, _port);
			SCPSession.setPassword(_password);
			if (_hostKeyVerification) {
				  SCPSession.getHostKeyRepository().add ( hostKey, null );		   
			} else {
				SCPSession.setConfig("StrictHostKeyChecking", "no");
			}
			
			SCPSession.connect(intTimeOut);
			
		} catch  (JSchException e) {
			throw new UnableToConnectException("Unable to connect to SCP server at: " + _host + ":" + _port, e);
		}
		
		logger.debug("Connected to SCP server at: " + _host + ":" + _port + " using SSH2");

	}

	public void authenticate() throws UnableToAuthenticateException {
		
		try {
		
			logger.debug("Authenticating user " + _username + "/" + _password  + " with SCP server at: " + _host + ":" + _port);
		
		} catch (Exception e) {
			throw new UnableToAuthenticateException("Failed to authenticate", e);
		}
  
	}

	public boolean isPollable() {
		boolean isPollable = true;
		logger.debug("isPollable returning: " + isPollable);
		return isPollable;
	}


	@Override
	public PluggableMessage consume(PluggableMessage message, String idFromList) throws UnableToConsumeException, FileNotFoundException {

		logger.debug("Now consuming "+ idFromList);
		
		try {
		    InputStream cIS = copyRemoteToB2Bi(SCPSession, _directory, idFromList);
			VirtualData data = new VirtualData();
			data.readFrom(cIS);
			message.setData(data);
			message.setFilename(idFromList);
		 	message.setMetadata("SCP Host", _host);
		 	message.setMetadata("SCP Port", String.valueOf(_port));
		 	message.setMetadata("SCP user", _username);
		 	message.setMetadata("SCP Pickup Folder", _directory);
		 	message.setMetadata("SCP File Name", idFromList);
		 	message.setMetadata("SCP user", _username);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return message;
	}
	
	
	public PluggableMessage produce(PluggableMessage message, PluggableMessage returnMessage) throws UnableToProduceException {
		
		try {
	          String fileName = message.getMetadata(MetadataDictionary.CONSUMPTION_FILENAME);
	     	  copyB2BiToRemote(SCPSession, message, _directory, fileName);
		} catch(Exception e) {
			throw new UnableToProduceException("Failed to produce message",e);
		} 
			
		return null;
	}


	public String test() throws TransportTestException {
		
		logger.debug("Testing connection to SCP server at: " + _host + ":" + _port);

		try {
			
			JSch 	testJSch = null;
			Session 	testSession;
			int 		testTimeOut = 60000;
			
			
			testJSch = new JSch();
			testJSch.setKnownHosts("");
			testSession = testJSch.getSession(_username, _host, _port);
			testSession.setPassword(_password);
			
			if (_hostKeyVerification) {
				testSession.getHostKeyRepository().add ( hostKey, null );		   
			} else {
				testSession.setConfig("StrictHostKeyChecking", "no");
			}
			
			testSession.connect(testTimeOut);
			testSession.disconnect();
			
			
			
		} catch(Exception e) {
			return "Failed to connect to SCP server " + _host + ":" + _port + "\n" + e.getMessage();
		}

		logger.debug("Finished testing connection to SCP server at: " + _host + ":" + _port);

		return "Success, socket connect to SCP server " + _host + ":" + _port;
	}

	public void disconnect() throws UnableToDisconnectException {
		logger.debug("Disconnecting from SCP server at: " + _host + ":" + _port);
		try {
			
			SCPSession.disconnect();
			
		} catch (Exception e) {
			logger.error("Failed to disconnect SCP client",e);
		}	
		logger.debug("Disconnected from SCP server at: " + _host + ":" + _port);
		
	}



	@Override
	public void delete(String nameFromList) throws UnableToDeleteException,
			FileNotFoundException {		
		
			Deletefile(SCPSession, _directory, nameFromList);
	}

	@Override
	public String getUrl() throws PluggableException {
		return _username + "@" + _host + ":" + _port + ":" + "SSH2" + "(" + _directory + ")";
	}

	@Override
	public String[] list() throws UnableToConsumeException {
		
        logger.debug("Listing the files from folder: " + _directory);
        
        
    	String[] list = null;
    	String[] files = null;
        ArrayList<String> result = new ArrayList<String>();
		
        files = listFiles(SCPSession, _directory);

        if (files.length == 0) {
            logger.debug("No files in: " + _directory);
        } else {
 	        
 	        for (int i = 0; i < files.length; i++) {
 	        	
        		logger.debug("FileName: " + files[i]); 
	        	
			    if (files[i].startsWith(".")) {

			    	// Directory
	        		logger.debug("Entry is a directory - ignore"); 
			    	
			    } else {
	 	        	
			    	PatternKeyValidator validator = PatternKeyValidatorFactory.createPatternValidator(_filtertype);
	 	        	if (validator.isValid(files[i], _filter)) {
	 	        		result.add(files[i]);
	 	        	}
	 	        	else {
	 	        		logger.debug(files[i] + " does not match the defined filter (" + _filter +") and /or filter type (" + _filtertype + ")"); 
	 	        	}
			    }
 			}
 	
         }     
        

		list = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			list[i] = result.get(i);
			logger.debug("Adding Item [" + i + "]: " + list[i]);
		}
    
	    
		logger.debug("Finished listing the files from folder: " + _directory);
	       		
        return list;
	}

	
	// SCP Commmands 
	
	public static String[] listFiles(Session lsession, String WorkingDir) {

		String splitresult[] = null;
		String result = "";
	    String command = "ls " + WorkingDir;

	    logger.debug("Command: " + command);
	    
		try {
			result = executeExecCommand(lsession, command);
		} catch (Exception e) {
			logger.error("failed to list files from: " + WorkingDir);
			return null;
		}
	    splitresult = result.split("\\r?\\n");
	    return splitresult;
	}

	public static void Deletefile(Session lsession, String WorkingDir, String FileName) {

		String result = "";

		if (!WorkingDir.endsWith(File.separator)) { WorkingDir = WorkingDir + File.separator; }
	    String command = "rm " + WorkingDir + FileName;

		try {
			  executeShellCommand(lsession, command);
		} catch (Exception e) {
			System.err.println("Failed to delete file" + e.toString());
			// TODO: handle exception
		}
  
		System.out.println("Delete Result:" + result);
	}
	
	public static void executeShellCommand(Session eCsession, String command)
	{

	   try {	   
			Channel eCchannel = eCsession.openChannel("shell");
			
			OutputStream inputstream_for_the_channel = eCchannel.getOutputStream();
			PrintStream commander = new PrintStream(inputstream_for_the_channel, true);
			
			eCchannel.connect();
			commander.println(command);
			commander.println("exit");
			commander.close();
			
			do {
			    Thread.sleep(1000);
			} while(!eCchannel.isEOF());
			
			eCchannel.disconnect();
	   }
	   
	   catch(IOException ioX)
	   {
	      logger.error(ioX.getMessage());
	      return;
	   }
	   catch(JSchException jschX)
	   {
		   logger.error(jschX.getMessage());
	      return;
	   }
	   catch(Exception e) {
		   logger.error(e.getMessage());
	   }
	
	}

		
	public static String executeExecCommand(Session session, String command)
	{
	   StringBuilder outputBuffer = new StringBuilder();
	   
	   try
	   {
	      Channel sCchannel = session.openChannel("exec");
	      ((ChannelExec)sCchannel).setCommand(command);

	      InputStream commandOutput = sCchannel.getInputStream();
	      sCchannel.connect();
      
	      
	      int readByte = commandOutput.read();

	      
	      while(readByte != 0xffffffff)
	      {
	         outputBuffer.append((char)readByte);
	         readByte = commandOutput.read();
	      }
	      
	      sCchannel.disconnect();
	
	   }
	   catch(IOException ioX)
	   {
	      logger.error(ioX.getMessage());
	      return null;
	   }
	   catch(JSchException jschX)
	   {
		  logger.error(jschX.getMessage());
	      return null;
	   }
	   
	   return outputBuffer.toString();
	}
	
	
    private static void copyB2BiToRemote(Session session, PluggableMessage message, String toPath, String fileName) throws JSchException, IOException {
        boolean ptimestamp = true;
        
        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + toPath;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        
        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();
        
        if (checkAck(in) != 0) {
            System.exit(0);
        } 
        
        // Create Inputstream
        
        VirtualData virtualData = message.getData();
        byte[] byteArray = new byte[(int)virtualData.length()];
        InputStream is = new ByteArrayInputStream(byteArray);
        
        long millis = System.currentTimeMillis();

        if (ptimestamp) {
            command = "T" + (millis / 1000) + " 0";
            command += (" " + (millis / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            
            if (checkAck(in) != 0) {
                System.exit(0);
            } else {
                logger.debug("Continue");         	
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = virtualData.length();
        
        logger.debug("File length: " + filesize);  
        
        command = "C0644 " + filesize + " ";
        if (fileName.lastIndexOf('/') > 0) {
            command += fileName.substring(fileName.lastIndexOf('/') + 1);
        } else {
            command += fileName;
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // Send message content
         
        byte[] buf = new byte[1024];
        while (true) {
            int len = is.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
            
            String s = new String(buf);
            logger.debug(s);
        }

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        channel.disconnect();
    }

    public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                logger.debug(sb.toString());
            }
            if (b == 2) { // fatal error
                logger.debug(sb.toString());
            }
        }
        return b;
    }
    
    private static InputStream copyRemoteToB2Bi(Session session, String fromPath,  String fileName) throws JSchException, IOException {
    
    	String from;
		if (!fromPath.endsWith(File.separator)) { from = fromPath + File.separator + fileName; } else { from = fromPath + fileName; }
    	
    	logger.debug("Source: " + from);
    	
        // exec 'scp -f rfile' remotely
        String command = "scp -f " + from;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        InputStream is = null;

        channel.connect();

        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {
            int c = checkAck(in);
            if (c != 'C') {
                break;
            }

            // read '0644 '
            in.read(buf, 0, 5);

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') break;
                filesize = filesize * 10L + (long) (buf[0] - '0');
            }

            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            logger.debug("file-size=" + filesize + ", file=" + file);

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            // read a content of lfile
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            
            int foo;
            while (true) {
                if (buf.length < filesize) foo = buf.length;
                else foo = (int) filesize;
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                buffer.write(buf, 0 , foo);
                filesize -= foo;
                if (filesize == 0L) break;
            }

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            is=new ByteArrayInputStream(buffer.toByteArray());
            
             
        }

        channel.disconnect();

        return is;

    }
	
}
