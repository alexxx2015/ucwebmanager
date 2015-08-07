package com.example.mysampleapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StaticAnalyser {
	String Projectlocation=new File("").getAbsolutePath();


	public void  analyseFile(String fileName)
	{
		try {
		Projectlocation=Projectlocation.replace('\\','/');
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", Projectlocation+"/flowanalyzer.jar", fileName);
		Process p = pb.start();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String s = "";
		while((s = in.readLine()) != null)
		{
			System.out.println(s);
		}
		int status = p.waitFor();
		System.out.println("Exited status: " + status);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
